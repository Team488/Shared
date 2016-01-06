package xbot.subsystems;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.RobotMap;
import xbot.common.wpi_extensions.BaseSubsystem;
import xbot.common.controls.XTimer;
import xbot.common.math.*;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;

@Singleton
public class TranslationalSubsystem extends BaseSubsystem
{
    private static Logger log = Logger.getLogger(TranslationalSubsystem.class);

    private DriveSubsystem drive;
    private RotationalSubsystem rotSubsystem;
    private TranslationalMeasurementSubsystem translationalMeasurement;
    
    private PIDManager xVelocityPID;
    private PIDManager yVelocityPID;
    private PIDManager xPositionPID;
    private PIDManager yPositionPID;
    
    private XYPair velocityThrottle;
    
    private DoubleProperty targetVelocityX;
    private DoubleProperty targetVelocityY;
    
    private BooleanProperty enableTipPrevention;
    
    private PIDManager xTiltPID;
    private PIDManager yTiltPID;
    
    @Inject
    public TranslationalSubsystem(RobotMap map,
            DriveSubsystem drive,
            RotationalSubsystem rotSubsystem,
            TranslationalMeasurementSubsystem translationalMeasurement,
            PropertyManager propMan)
    {
    	log.info("Creating TranslationalSubsystem");
    	
        this.drive = drive;
        this.translationalMeasurement = translationalMeasurement;
        this.rotSubsystem = rotSubsystem;
        
        this.xVelocityPID = new PIDManager("Velocity", propMan, 0.02d, 0.0001d, 0.001d);
        this.yVelocityPID = new PIDManager("Velocity", propMan, 0.02d, 0.0001d, 0.001d);
        
        this.xPositionPID = new PIDManager("Position", propMan, 0.1d, 0.001d, 1);
        this.yPositionPID = new PIDManager("Position", propMan, 0.1d, 0.001d, 1);
        
        this.targetVelocityX = propMan.createProperty("Target X velocity", 0d);
        this.targetVelocityY = propMan.createProperty("Target Y velocity", 0d);
        
        this.xTiltPID = new PIDManager("Tilt prevention PID", propMan, 0.1, 0, 0);
        this.yTiltPID = new PIDManager("Tilt prevention PID", propMan, 0.1, 0, 0);
        
        this.enableTipPrevention = propMan.createProperty("Enable tip prevention", true);
        
        velocityThrottle = new XYPair();
    }
    
    /**
     * Sets the motor powers to translate in the specified robot-relative
     * direction with the specified power. Powers are in the scale [-1, 1].
     * Anything out of those bounds will be constrained.
     * 
     * @param translateVector
     */
    public void translatePowerRobotRelative(XYPair powerVector)
    {
        XYPair correctedTargetPower = powerVector.clone();
        correctTargetPowerForTilt(correctedTargetPower);
        drive.setTranslatonalVector(correctedTargetPower);
    }
    
    private void correctTargetPowerForTilt(XYPair correctedTargetPower) {
        translationalMeasurement.updateTrackingState();
        
        XYPair tilt = translationalMeasurement.getTilt();
        XYPair tiltMax = translationalMeasurement.getMaxTilt();
        
        if(!enableTipPrevention.get()
                || (Math.abs(tilt.x) < tiltMax.x
                && Math.abs(tilt.y) < tiltMax.y))
        {
            // If we are in the safe zone, we shouldn't try to correct for tilt
            return;
        }
        
        if (Math.abs(tilt.x) > 60
                || Math.abs(tilt.y) > 60)
        {
            // We are tilting a huge amount. Time to give up, so we're not spinnign wheels while knocked over.
            return;
        }
        
        double xPower = xTiltPID.calculate(0,  tilt.x);
        double yPower = yTiltPID.calculate(0,  tilt.y);
        
        correctedTargetPower.x = MathUtils.constrainDoubleToRobotScale(xPower);
        correctedTargetPower.y = MathUtils.constrainDoubleToRobotScale(yPower);
    }

    /**
     * Sets the motor powers to translate in the specified field-relative
     * direction with the specified power. All powers are in the scale [-1, 1].
     * Anything out of those bounds will be constrained.
     * 
     * @param translateVector The field-relative vector
     */
    public void translatePowerFieldRelative(XYPair powerVector)
    {
        XYPair fieldRelativeVector = powerVector.clone();
        fieldRelativeVector.rotate(RobotMap.forwardAngle - rotSubsystem.getGyroYaw().getValue());
        translatePowerRobotRelative(fieldRelativeVector);
    }
    
    /**
     * Attempts to maintain the given robot-relative velocity using PID
     * and the ground-truth sensors.
     * 
     * This method should be called repeatedly, even if the target isn't changing.
     * 
     * @param velocityVector The robot-relative target velocity
     */
    public void translateVelocityRobotRelative(XYPair velocityTargetVector)
    {
        XYPair currVelocity = this.translationalMeasurement.getVelocityRobotRelative();
        double xError = velocityTargetVector.x - currVelocity.x;
        double yError = velocityTargetVector.y - currVelocity.y;
        
        XYPair newPower = new XYPair();
        newPower.x = xVelocityPID.calculate(0, xError);
        newPower.y = yVelocityPID.calculate(0, yError);
        
        translatePowerRobotRelative(newPower);
        
    }
    
    /**
     * Attempts to maintain the given field-relative velocity using PID
     * and the ground-truth sensors.
     * 
     * This method should be called repeatedly, even if the target isn't changing.
     * 
     * @param velocityVector The field-relative target velocity
     */
    public void translateVelocityFieldRelative(XYPair velocityTargetVector)
    {
        targetVelocityX.set(velocityTargetVector.x);
        targetVelocityY.set(velocityTargetVector.y);
        
        XYPair currVelocity = this.translationalMeasurement.getVelocityFieldRelative();
        double xError = currVelocity.x - velocityTargetVector.x;
        double yError = currVelocity.y - velocityTargetVector.y;
        
        velocityThrottle.x +=  xVelocityPID.calculate(0, xError);
        velocityThrottle.y +=  yVelocityPID.calculate(0, yError);
        
        translatePowerFieldRelative(velocityThrottle);        
    }
    
    /**
     * Helper method to allow fallback to "dumb" operation if we can't use PID
     * @param target
     * @param velPIDEnabled
     */
    @Deprecated
    public void translateDualDegradableRobotRelative(XYPair target, boolean velPIDEnabled)
    {
        if(velPIDEnabled) {
            translateVelocityRobotRelative(target);
        }
        else
        {
            //TODO: Be smart about resetting state
            this.resetState();
            translatePowerRobotRelative(target);
        }
    }
    
    /**
     * Attempts to translate to the specified absolute field-relative coordinates.
     * 
     * This method should be called repeatedly, even if the target isn't changing.
     * 
     * @param targetPosition
     */
    public void goToFieldPositionAbsolute(XYPair targetPosition)
    {
        //TODO: Decide on the things above in translatePositionRobotRelative
        
        XYPair currentPosition = translationalMeasurement.getPosition();
        XYPair distanceToTarget = new XYPair(currentPosition.x - targetPosition.x, currentPosition.y - targetPosition.y);
        
        XYPair power = new XYPair();
        power.x = xPositionPID.calculate(0, distanceToTarget.x);
        power.y = yPositionPID.calculate(0, distanceToTarget.y);
        
        // cap maximum motor power
        power.x = MathUtils.constrainDouble(power.x, -.5, .5);
        power.y = MathUtils.constrainDouble(power.y, -.5, .5);
        translatePowerFieldRelative(power);
    }
    
    /**
     * Resets all history.
     * This should be called every time a new command takes over this subsystem or the purpose changes.
     * 
     * TODO: Auto-reset instead
     */
    public void resetState()
    {
    	log.info("Resetting VelocityPID and PositionalPID");
        xVelocityPID.reset();
        yVelocityPID.reset();
        
        xPositionPID.reset();
        yPositionPID.reset();
        
        xTiltPID.reset();
        yTiltPID.reset();
        
        velocityThrottle = new XYPair();
    }


    public boolean isAtPositionTarget(double posTolerance, double velTolerance)
    {
        return xPositionPID.isOnTarget(posTolerance)
                && yPositionPID.isOnTarget(posTolerance)
                && translationalMeasurement.isStopped(velTolerance);
    }

    public void zeroPower()
    {
        translatePowerFieldRelative(XYPair.ZERO);
    }
}
