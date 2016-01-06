package xbot.subsystems;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.RobotMap;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseSubsystem;
import xbot.common.wpi_extensions.mechanism_wrappers.XAnalogInput;
import xbot.common.wpi_extensions.mechanism_wrappers.XEncoder;
import xbot.common.wpi_extensions.mechanism_wrappers.XSolenoid;
import xbot.common.wpi_extensions.mechanism_wrappers.XSpeedController;

@Singleton
public class ElevatorSubsystem extends BaseSubsystem implements Observer {

    private static Logger log = Logger.getLogger(ElevatorSubsystem.class);
    
	private XSpeedController elevatorMotorA;
	private XSpeedController elevatorMotorB;
	private XSolenoid elevatorLock;
	
    private DoubleProperty elevatorCurrentVoltage;
    private DoubleProperty elevatorLowVoltage;
    private DoubleProperty elevatorLowHeight;
    private DoubleProperty elevatorHighVoltage;
    private DoubleProperty elevatorHighHeight;
    private DoubleProperty elevatorPower;

    
    public static final String lowHeightPropname = "Elevator lower position reference height (in)";
    public static final String highHeightPropname = "Elevator upper position reference height (in)";

    
    public static final String currentVoltagePropname = "Elevator current pot voltage";
    public static final String lowVoltagePropname = "Elevator lower position reference voltage";
    public static final String highVoltagePropname = "Elevator upper position reference voltage";
    public static final String encoderDistancePerPulse = "Elevator inches per pulse";
    public static final String potPositionPropname = "Elevator pot height";
    
    private static final String encoderDefaultCalibrationHeightPropname = "Encoder Default height after calibration";
    private DoubleProperty encoderDefaultCalibrationHeight;
    
    
    private DoubleProperty elevatorPotPosition;
    private DoubleProperty elevatorEncoderPosition;
    
    private DoubleProperty stallOperationDeadzone;
    private DoubleProperty stallSpeedThreshold;
    private DoubleProperty stallHistPercent;
    private DoubleProperty stallHistLength;
    
    private DoubleProperty elevatorInchesPerPulse;

    private DoubleProperty manualPowerDown;
    private DoubleProperty manualPowerUp;
    
    private DoubleProperty elevatorPotentiometerDangerRange;
	
	private DoubleProperty elevatorMaxSafeHeight;
	private DoubleProperty elevatorMinSafeHeight;
    
    private BooleanProperty isElevatorStalled;
    
    private BooleanProperty safetyPositionChecksEnabled;
    private BooleanProperty stallDetectionEnabled;
    private BooleanProperty elevatorPidEnabled;
    private BooleanProperty isEncoderCalibrated;
    
    private XAnalogInput elevatorHeightPot;
    private XEncoder elevatorHeightEncoder;
    
    private double elevatorHeightEncoderOffset = 0;
    
    private ArrayList<Boolean> stallHistory;
	
	private Latch stallLatch;
	private Latch potentiometerDangerLatch;
		
	private BooleanProperty manualPowerUsedMostRecently;
	
	private ElevatorSensorSubsystem sensors;
	
	@Inject
	public ElevatorSubsystem(RobotMap map, PropertyManager propMan, ElevatorSensorSubsystem sensors)  {
		
		log.info("Creating ElevatorSubsystem");
		
		this.sensors = sensors;
		
		stallLatch = new Latch(false, EdgeType.Both);
		potentiometerDangerLatch = new Latch(false, EdgeType.Both);
		
		potentiometerDangerLatch.addObserver(this);
		stallLatch.addObserver(this);
		
		this.elevatorMotorA = map.elevatorMotorA;
		this.elevatorMotorB = map.elevatorMotorB;
		this.elevatorLock = map.elevatorLock;
		this.elevatorHeightPot = map.elevatorHeightPot;
		this.elevatorHeightEncoder = map.elevatorEncoder;
		
		// The robot starts with the brake engaged.
		setLocked(true);
		
		encoderDefaultCalibrationHeight  = propMan.createProperty(encoderDefaultCalibrationHeightPropname, 7d);
		
		//TODO: Fix naming strings of properties
        elevatorCurrentVoltage = propMan.createProperty(currentVoltagePropname, 0d);

        elevatorLowVoltage = propMan.createProperty(lowVoltagePropname, 3.778);
        elevatorLowHeight = propMan.createProperty(lowHeightPropname, 0d);
        
        elevatorPotentiometerDangerRange = propMan.createProperty("Elevator pot danger threshold", 0.2);
        
        elevatorHighVoltage = propMan.createProperty(highVoltagePropname, 0.36d);
        elevatorHighHeight = propMan.createProperty(highHeightPropname, 60d);
        
        elevatorPotPosition = propMan.createProperty(potPositionPropname, 0d);
        elevatorEncoderPosition = propMan.createProperty("Elevator encoder position (in)", 0d);
        
        isElevatorStalled = propMan.createProperty("Is elevator stalled?", false);
        stallOperationDeadzone = propMan.createProperty("Operational stall deadzone", 0.1);
        stallSpeedThreshold = propMan.createProperty("Stall rate threshold", 1d);
        stallHistLength = propMan.createProperty("Stall history length", 10d);
        stallHistPercent = propMan.createProperty("Stall history percentage threshold", 0.10d);
        
        elevatorInchesPerPulse = propMan.createProperty(encoderDistancePerPulse, 0.072d);

        manualPowerDown = propMan.createProperty("Elevator manual power down", 0.5);
        manualPowerUp = propMan.createProperty("Elevator manual power up", 0.8);
        
        safetyPositionChecksEnabled = propMan.createProperty("Enable elevator safety checks", true);
        stallDetectionEnabled = propMan.createProperty("Elevator stall detection enabled", false);        
		
		elevatorMaxSafeHeight = propMan.createProperty("ElevatorMaxSafeHeight", 50.0);
        
        elevatorPower= propMan.createProperty("ElevatorPower", 1d);
        
        manualPowerUsedMostRecently = propMan.createProperty("Elevator Manual used recently", false);
        
        elevatorPidEnabled = propMan.createProperty("Elevator PID enabled", true);
		
        stallHistory = new ArrayList<Boolean>();
        
        isEncoderCalibrated = propMan.createProperty("Is encoder calibrated", false);
        isEncoderCalibrated.set(false);
        
        // Calibration sets the in/tick ratio so that the PID won't freak out if enabled
        // Will cause undesirable operation if not calibrated again with a proper current position
        calibrateEncoder();
        //start with safeties initially engaged.
        setSafetyPositionChecksEnabled(true);
        // start with PID initially on
        setElevatorPidEnabled(true);
	}
	/*
	public boolean isElevatorInUpperDangerousRange()
	{
		if (areHighAndLowPotVoltagesInverted())
		{
			// Reverse case - Low voltages mean high elevator
			// So, we check if the voltage is too low (i.e. elevator is too high)
			// High voltages mean high elevator
			return isPotCloseToZeroVolts();
		}
		else
		{
			// Normal case - high voltages mean high elevator
			// So, we check if the voltage is too high (i.e. elevator is too high)
			return isPotCloseToFiveVolts();
		}
	}
	
	public boolean isElevatorInLowerDangerousRange()
	{
		if (areHighAndLowPotVoltagesInverted())
		{
			// Reverse case - high voltages mean low elevator
			// So, we check if the voltage is too high (i.e. elevator is too low)
			// High voltages mean high elevator
			return isPotCloseToFiveVolts();
		}
		else
		{
			// Normal case - low voltages mean low elevator
			// So, we check if the voltage is too low (i.e. elevator is too low)
			return isPotCloseToZeroVolts();
		}
	}
	
	private boolean areHighAndLowPotVoltagesInverted()
	{
		boolean invert = false;
		if (elevatorLowVoltage.get() > elevatorHighVoltage.get())
		{
			// Voltage positions are inverted.
			invert = true;
		}
		
		return invert;
	}
	
	private boolean isPotCloseToFiveVolts()
	{
		//updatePotPosition();
		double maxSafeVoltage = 5 - elevatorPotentiometerDangerRange.get();
		
		return (elevatorCurrentVoltage.get() > maxSafeVoltage);
	}
	
	private boolean isPotCloseToZeroVolts()
	{
		//updatePotPosition();
		double minSafeVoltage = elevatorPotentiometerDangerRange.get();
		
		return (elevatorCurrentVoltage.get() < minSafeVoltage);
	}*/
	
	public void calibrateEncoder()
	{
	    calibrateEncoder(encoderDefaultCalibrationHeight.get());
	}
	
	public void clearEncoderCalibration()
	{
	    isEncoderCalibrated.set(false);
	}
	
	public void setEncoderInchesPerPulse()
	{
	    if(elevatorInchesPerPulse.get() == 0)
        {
            log.error("Elevator inches per encoder pulse is set to 0! This could render the elevator uncontrollable.");
        }
        
        elevatorHeightEncoder.setDistancePerPulse(elevatorInchesPerPulse.get());
	}
	
	public void calibrateEncoder(double currentPosition)
	{
	    /*
	    log.info("Elevator encoder height was " + elevatorHeightEncoder.getDistance());
	    log.info("Elevator encoder offset was " + elevatorHeightEncoderOffset);
	    */
	    setEncoderInchesPerPulse();
	    
	    elevatorHeightEncoderOffset = currentPosition - elevatorHeightEncoder.getDistance();
	    //log.info("Encoder encoder offset is now: " + elevatorHeightEncoderOffset);
	    isEncoderCalibrated.set(true);
	}
	
    public void updatePotPosition()
    {
        elevatorCurrentVoltage.set(elevatorHeightPot.getAverageVoltage());
        
        double potMeasuredHeight = MathUtils.scaleDouble(
                elevatorCurrentVoltage.get(),
                elevatorLowVoltage.get(), elevatorHighVoltage.get(),
                elevatorLowHeight.get(), elevatorHighHeight.get());
        
        elevatorPotPosition.set(potMeasuredHeight);
        
        // Elevator danger latch
        //boolean isInDangerZone =  isElevatorInLowerDangerousRange() || isElevatorInUpperDangerousRange();
        //potentiometerDangerLatch.setValue(isInDangerZone);
    }
    
    public void updateEncoderPosition()
    {
        elevatorEncoderPosition.set(elevatorHeightEncoder.getDistance() + elevatorHeightEncoderOffset);
    }
    
    private double getHeight()
    {
        return this.elevatorEncoderPosition.get();
    }
    
    public void updateStallTracking()
    {
        double currentPower = (elevatorMotorA.get() + elevatorMotorB.get()) / 2d;
        
        boolean currentlyStalledInstant = false;
        
        if(Math.abs(currentPower) <= stallOperationDeadzone.get())
        {
            currentlyStalledInstant = false;
        }
        else
        {
	        double currentVelocity = elevatorHeightEncoder.getRate();
	        currentlyStalledInstant = currentVelocity < stallSpeedThreshold.get();
        }
        
        stallHistory.add(currentlyStalledInstant);
        if(stallHistory.size() > stallHistLength.get())
        {
            stallHistory.remove(0);
        }
        
        double numStalledHist = 0;
        for(boolean b : stallHistory)
        {
            if(b)
            {
                numStalledHist++;
            }
        }
        
        double percentStalled = 0;
        if(stallHistory.size() > 0)
        {
            percentStalled = numStalledHist / stallHistory.size();
        }
        
        boolean elevatorConsideredToBeStalling = percentStalled >= stallHistPercent.get();
        isElevatorStalled.set(elevatorConsideredToBeStalling);
        
        stallLatch.setValue(elevatorConsideredToBeStalling);
    }
    
    public boolean isStalled()
    {
        return stallDetectionEnabled.get() && isElevatorStalled.get();
    }

	public void setLocked(boolean locked)
	{
		elevatorLock.set(locked);
	}
	
	private void setElevatorPower(double power)
	{
	    // check if we are at our minimum point. If so, calibrate first.
        if (sensors.isAtHomePosition())
        {
            calibrateEncoder();
            isEncoderCalibrated.set(true);
        }
        
        // Get our height
	    double currHeight = updateAndGetHeight();
	    
	    // See if we need to limit our power output for some good reason
	    power= checkSafetyAndCalculateElevatorPower(power, currHeight);
	    
	    // Actually set motor powers
	    elevatorPower.set(power);
	    elevatorMotorA.set(elevatorPower.get());
	    elevatorMotorB.set(elevatorPower.get());
	}   
	
	public double checkSafetyAndCalculateElevatorPower(double power, double currentHeight)
	{    
	    if (safetyPositionChecksEnabled.get() && sensors.isAtHomePosition())
	    {
	        // We're at the bottom, and we care about it. No power!
	        return Math.max(0, power);
	    }
	    
		if (safetyPositionChecksEnabled.get() && getIsEncoderCalibrated())
	    {
    	    // Basic idea:
            // Run safety checks. Modify motor power as necessary to meet them.
            
            // First tier of safety checks - according to our sensors, have we exceeded
            // our desired height range?
            if (currentHeight > getMaxHeight())
            {
                power = Math.min(power, 0);
            }
            else if (currentHeight < getMinHeight())
            {
                power = Math.max(power, 0);
            }
            
            
            // Second tier of safety checks - are we about to break our potentiometer?
            // (On the actual robot, we could conceivably just bypass this - we may break the pot
            // during the match, but the encoder should keep working, and it's probably already calibrated.)
            /*
            if (isElevatorInLowerDangerousRange())
            {
                // TODO: alert somehow?
                // We can only go up.
                power = Math.max(power, 0);
            }
            if (isElevatorInUpperDangerousRange())
            {
                // We can only go down.
                power = Math.min(power, 0);
            }

            // This is where we test for potentiometer destruction, since we want to catch manual
		    // as well as PID violations.
		    power =	modifyPowerToAvoidBreakingPotentiometer(power);*/
        }
	    
		return power;
	}
	/*
	private double modifyPowerToAvoidBreakingPotentiometer(double power)
	{
		if (isElevatorInLowerDangerousRange())
		{
			// TODO: alert somehow?
			// We can only go up.
			power = Math.max(power, 0);
		}
		if (isElevatorInUpperDangerousRange())
		{
			// We can only go down.
			power = Math.min(power, 0);
		}
		
		return power;
	}*/
	
	public double getMaxHeight()
	{
	    return elevatorMaxSafeHeight.get();
	}
	
	public double getMinHeight()
    {
        return encoderDefaultCalibrationHeight.get();
    }
	
	public double getDefaultCalibrationHeight()
	{
	    return encoderDefaultCalibrationHeight.get();
	}

    public double updateAndGetHeight()
    {
        updatePotPosition();
        updateEncoderPosition();
        return getHeight();
    }
    
    public void goDownManually() {
        // Note - manual power down is positive for readability on the dashboard, which is why we invert it here.
        setManualPower(-manualPowerDown.get());
    }
    
    public void goUpManually() {
        setManualPower(manualPowerUp.get());
    }
    
    public void setManualPower(double power) {
        manualPowerUsedMostRecently.set(true);
        setElevatorPower(power);
    }
    
    public void setAutomaticPower(double power) {
        manualPowerUsedMostRecently.set(false);
        setElevatorPower(power);
    }
    
    public boolean wasManualUsedMostRecently() {
        return manualPowerUsedMostRecently.get();
    }

	@Override
	public void update(Observable o, Object arg) {
		
		if (o.equals(stallLatch))
		{
			// Cast arg out as edgetype
			EdgeType edge = (EdgeType)arg;
			
			if (edge == EdgeType.RisingEdge)
			{
				//log.info("Elevator has stalled!");
			}
			if (edge == EdgeType.FallingEdge)
			{
				//log.info("Elevator is no longer stalling.");
			}
		}
		
		if (o.equals(potentiometerDangerLatch))
		{
			// Cast arg out as edgetype
			EdgeType edge = (EdgeType)arg;
			
			if (edge == EdgeType.RisingEdge)
			{
				//log.error("Elevator has entered voltage danger zone! Voltage: " + this.elevatorCurrentVoltage.get());
			}
			if (edge == EdgeType.FallingEdge)
			{
				//log.info("Elevator has exited voltage danger zone.");
			}
		}
	}
	public boolean getElevatorPidEnabled() {
		return elevatorPidEnabled.get();
	}
	public void setElevatorPidEnabled(boolean enabled) {
	    log.info("Setting elevator PID enabled: " + enabled);
		this.elevatorPidEnabled.set(enabled);
	}

	public void setSafetyPositionChecksEnabled(boolean sensorsEnabled) {
		this.safetyPositionChecksEnabled.set(sensorsEnabled);
	}
	
	public boolean getIsEncoderCalibrated() {
	    return isEncoderCalibrated.get();
	}
	
	public double getCurrentOffset() {
	    return elevatorHeightEncoderOffset;
	}
}
