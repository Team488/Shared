package xbot;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.wpi_extensions.mechanism_wrappers.XAnalogInput;
import xbot.common.wpi_extensions.mechanism_wrappers.XDigitalOutput;
import xbot.common.wpi_extensions.mechanism_wrappers.XPowerDistributionPanel;
import xbot.common.wpi_extensions.mechanism_wrappers.XSolenoid;
import xbot.common.wpi_extensions.mechanism_wrappers.XEncoder;
import xbot.common.wpi_extensions.mechanism_wrappers.XSpeedController;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import xbot.common.controls.*;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
@Singleton
public class RobotMap {
    
    private static Logger log = Logger.getLogger(RobotMap.class);
    
    public XAnalogInput elevatorHeightPot;
	public XSolenoid intakeArmLeftSolenoid;
	public XSolenoid intakeArmRightSolenoid;
	public XSolenoid elevatorLock;
	public XSpeedController intakeArmLeftWheel;
	public XSpeedController intakeArmRightWheel;
	public XSpeedController elevatorMotorA;
	public XSpeedController elevatorMotorB;
	
	// Extra magic
	public XPowerDistributionPanel pdp;

    /* Motor mappings in this drive system (for visual identification
     *   and a love for ASCII art):
     *    _______
     * 0 |_|   |_| 1
     *    _|   |_
     * 2 |_|___|_| 3
     * 
     */
	
	// Motors
	
     // Arm Motors
    public static final int leftArmWheelChannel = 5;
    public static final int rightArmWheelChannel = 4;
    // Elevator Motors
    public static final int elevatorMotorAchannel = 6;
    public static final int elevatorMotorBchannel = 7;

    // Solenoids
    // Arms
    public static final int leftPistonArmSolenoidChannel = 1;    
    // Elevator
    public static final int elevatorLockSolenoidChannel = 3;

    // Digital IO
    // Elevator
    public static final int elevatorEncoderChannelA = 0;
    public static final int elevatorEncoderChannelB = 1;
    
    // Follow Wheels
    public static final int followWheelVerticalEncoder1ChannelA = 2;
    public static final int followWheelVerticalEncoder1ChannelB = 3;
    
    public static final int followWheelVerticalEncoder2ChannelA = 4;
    public static final int followWheelVerticalEncoder2ChannelB = 5;
    
    public static final int followWheelHorizontalEncoderChannelA = 6;
    public static final int followWheelHorizontalEncoderChannelB = 7;
    
    // Signal LEDs
    public static final int ledChannel1 = 8;
    public static final int ledChannel2 = 9;
    
    // Analog Inputs
    // Elevator
    public static final int elevatorAbsoluteHeightAnalogChannel = 0;
    public static final int elevatorHomePositionAnalogChannel = 3;
    
    public static final int leftDistanceSensorChannel = 1;
    public static final int rightDistanceSensorChannel = 2;
        
    public XSpeedController leftFrontDrive; // Motor 0
    public XSpeedController leftRearDrive; //  Motor 1
    public XSpeedController rightFrontDrive; //Motor 2
    public XSpeedController rightRearDrive; // Motor 3
    
    public XGyro gyro;
    public DistanceSensor frontLeftDistanceSensor;
    public DistanceSensor frontRightDistanceSensor;
    
    public XEncoder verticalEncoder1;
    public XEncoder verticalEncoder2;
    public XEncoder horizontalEncoder;
    
    public XAnalogInput homePositionSensor;
    
    public static final double forwardAngle = 90;
    
    public XEncoder elevatorEncoder;
    
    public XDigitalOutput[] ledOutputs;
    
    @Inject
    public RobotMap (WPIFactory factory) {
        log.info("Initializing RobotMap");
		
        intakeArmLeftSolenoid = factory.getSolenoid(leftPistonArmSolenoidChannel);
		intakeArmLeftSolenoid.setInverted(true);
		intakeArmLeftWheel = factory.getSpeedController(leftArmWheelChannel);
		intakeArmLeftWheel.setInverted(true);
		intakeArmRightWheel = factory.getSpeedController(rightArmWheelChannel);
		
		elevatorHeightPot = factory.getAnalogInput(elevatorAbsoluteHeightAnalogChannel);
		elevatorHeightPot.setAverageBits(2);
		
		elevatorEncoder = factory.getEncoder(elevatorEncoderChannelA, elevatorEncoderChannelB);
		elevatorEncoder.setInverted(true);
		
		elevatorMotorA = factory.getSpeedController(elevatorMotorAchannel);
		elevatorMotorA.setInverted(true);
		elevatorMotorB = factory.getSpeedController(elevatorMotorBchannel);
		elevatorMotorB.setInverted(true);
		
		elevatorLock = factory.getSolenoid(elevatorLockSolenoidChannel);
		elevatorLock.setInverted(false);
        
        
        gyro = factory.getGyro();
        
        verticalEncoder1 = factory.getEncoder(followWheelVerticalEncoder1ChannelA, followWheelVerticalEncoder1ChannelB);
        verticalEncoder2 = factory.getEncoder(followWheelVerticalEncoder2ChannelA, followWheelVerticalEncoder2ChannelB);
        verticalEncoder2.setInverted(true);
        horizontalEncoder = factory.getEncoder(followWheelHorizontalEncoderChannelA, followWheelHorizontalEncoderChannelB);
        
        verticalEncoder1.setSamplesToAverage(5);
        verticalEncoder2.setSamplesToAverage(5);
        horizontalEncoder.setSamplesToAverage(5);
        
        frontLeftDistanceSensor = factory.getAnalogDistanceSensor(
                leftDistanceSensorChannel,
                AnalogDistanceSensor.VoltageMaps::sharp0A51SK);
        frontRightDistanceSensor = factory.getAnalogDistanceSensor(
                rightDistanceSensorChannel,
                AnalogDistanceSensor.VoltageMaps::sharp0A51SK);
        
        if(frontRightDistanceSensor instanceof AnalogDistanceSensor) {
            ((AnalogDistanceSensor)frontRightDistanceSensor).setDistanceOffset(-2.8d);
        }
        
        frontLeftDistanceSensor.setAveraging(true);
        frontRightDistanceSensor.setAveraging(true);
        
        ledOutputs = new XDigitalOutput[] {
                factory.getDigitalOutput(ledChannel1),
                factory.getDigitalOutput(ledChannel2)
            };
        
        homePositionSensor = factory.getAnalogInput(elevatorHomePositionAnalogChannel);
        
        pdp = factory.getPDP();
    }
}
