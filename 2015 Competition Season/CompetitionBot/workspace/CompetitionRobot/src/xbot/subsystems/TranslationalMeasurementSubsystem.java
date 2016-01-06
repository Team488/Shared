package xbot.subsystems;

import org.apache.log4j.Logger;

import xbot.RobotMap;
import xbot.common.controls.XGyro;
import xbot.common.math.XYPair;
import xbot.common.math.XYPairManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseSubsystem;
import xbot.common.wpi_extensions.mechanism_wrappers.XEncoder;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TranslationalMeasurementSubsystem extends BaseSubsystem
{
    private static Logger log = Logger
            .getLogger(TranslationalMeasurementSubsystem.class);
    
    private RotationalSubsystem rotSubsystem;

    private XEncoder verticalEncoder1;
    private XEncoder verticalEncoder2;
    private XEncoder horizontalEncoder;
    
    //Positions are relative to the position that the robot was turned on for now.
    private XYPair currentTrackedPosition;
    
    //Velocity is in units/second (units are TBD)
    private XYPair currentTrackedVelocity;
    
    private double previousTotalVerticalDistance1;
    private double previousTotalVerticalDistance2;
    private double previousTotalHorizontalDistance;
    
    private static final double ticksPerRev = 128d;
    private static final double wheelDiameter = 3; //in
    // (PI * wheel diameter) / ticks per revolution
    private static final double inPerTick = (Math.PI * (wheelDiameter)) / ticksPerRev;

    private DoubleProperty velocityXProperty;
    private DoubleProperty velocityYProperty;
    private DoubleProperty positionXProperty;
    private DoubleProperty positionYProperty;
    
    private XYPairManager maxTilt;
    private XYPairManager currentTilt;
    
    private XGyro gyro;
    
    @Inject
    public TranslationalMeasurementSubsystem(RobotMap map, RotationalSubsystem rotSubsystem, PropertyManager propMan)
    {
    	log.info("Creating TranslationalMeasurementSubsystem");
    	
        verticalEncoder1 = map.verticalEncoder1;
        verticalEncoder2 = map.verticalEncoder2;
        horizontalEncoder = map.horizontalEncoder;
        gyro = map.gyro;
        
        verticalEncoder1.setDistancePerPulse(inPerTick);
        verticalEncoder2.setDistancePerPulse(inPerTick);
        horizontalEncoder.setDistancePerPulse(inPerTick);

        velocityXProperty = propMan.createProperty("Translational velocity X", 0d);
        velocityYProperty = propMan.createProperty("Translational velocity Y", 0d);
        
        positionXProperty = propMan.createProperty("Translational position X", 0d);
        positionYProperty = propMan.createProperty("Translational position Y", 0d);
        
        maxTilt = new XYPairManager("Tilt protection threshold", propMan, 15, 15);
        currentTilt = new XYPairManager("Current tilt", propMan, 0, 0);
        
        this.rotSubsystem = rotSubsystem;
        
        initStateVectors();
    }
    
    private void initStateVectors()
    {
        //TODO: Add origin point
        currentTrackedPosition = new XYPair(0, 0);
        currentTrackedVelocity = new XYPair(0, 0);
        
        updatePrevTotals();
    }
    
    private void updatePrevTotals()
    {
        previousTotalVerticalDistance1 = verticalEncoder1.getDistance();
        previousTotalVerticalDistance2 = verticalEncoder2.getDistance();
        previousTotalHorizontalDistance = horizontalEncoder.getDistance();
    }
    
    /**
     * Updates all position and velocity measures in the system.
     * 
     * This should be called repeatedly, and as fast as possible for
     * accurate results.
     */
    public void updateTrackingState()
    {
        double vDist1 = verticalEncoder1.getDistance() - previousTotalVerticalDistance1;
        double vDist2 = verticalEncoder2.getDistance() - previousTotalVerticalDistance2;
        double hDist = horizontalEncoder.getDistance() - previousTotalHorizontalDistance;
        
        XYPair distSinceLastTick = new XYPair(hDist, (vDist1 + vDist2) / 2);
        //Rotate vector from robot- to field-relative
        distSinceLastTick.rotate(rotSubsystem.getGyroYaw().getValue() - RobotMap.forwardAngle);
        
        currentTrackedPosition.add(distSinceLastTick);
        
        updatePrevTotals();
        
        double vVel1 = verticalEncoder1.getRate();
        double vVel2 = verticalEncoder2.getRate();
        double hVel = horizontalEncoder.getRate();
        
        currentTrackedVelocity.y = (vVel1 + vVel2) / 2;        
        currentTrackedVelocity.x = hVel;
        
        velocityXProperty.set(currentTrackedVelocity.x);
        velocityYProperty.set(currentTrackedVelocity.y);
        
        positionXProperty.set(currentTrackedPosition.x);
        positionYProperty.set(currentTrackedPosition.y);
        
        // Our gyro is set facing 0 degrees, so this changes pitch roll.
        // Tilt robot back (+Pitch) becomes tilt gyro right (-Roll)
        // Tilt robot left (+Roll) becomes tilt gyro back (+Pitch)
        
        // However, it also turns out that our whole sensor is inverted... so we need to negate as well.
        
        currentTilt.setX(gyro.getPitch());
        currentTilt.setY(-gyro.getRoll());
        
        // The below commented lines would be for a straight gyro
        //currentTilt.setX(gyro.getRoll());
        //currentTilt.setY(gyro.getPitch());
        
        
    }
    
    public void resetState()
    {
    	log.info("Resetting internal state. X and Y positions set to 0.");
        resetState(new XYPair(0, 0));
    }
    
    public void resetState(XYPair currentPosition)
    {
    	log.info(
    			String.format(
    					"Resetting internal state. X and Y positions set to %.3f and %.3f respectively.",
    					currentPosition.x,
    					currentPosition.y));
        currentTrackedPosition = currentPosition.clone();
    }
    
    public XYPair getVelocityFieldRelative()
    {
        return currentTrackedVelocity.clone();
    }
    
    public XYPair getVelocityRobotRelative()
    {
        return currentTrackedVelocity.clone().rotate(rotSubsystem.getGyroYaw().getValue() - RobotMap.forwardAngle);
    }
    
    public XYPair getPosition()
    {
        return currentTrackedPosition.clone();
    }

    public boolean isStopped(double velTolerance) {
        return getVelocityFieldRelative().getMagnitude() <= velTolerance;
    }
    
    public XYPair getTilt() {
        return currentTilt.get();
    }
    
    public XYPair getMaxTilt() {
        return maxTilt.get();
    }
}
