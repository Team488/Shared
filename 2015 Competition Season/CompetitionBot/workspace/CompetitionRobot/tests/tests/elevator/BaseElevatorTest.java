package tests.elevator;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;

import edu.wpi.first.wpilibj.MockEncoder;
import edu.wpi.first.wpilibj.MockJoystick;
import tests.xbot.BaseTestCase;
import xbot.OI;
import xbot.OperatorInterfaceCommandMap;
import xbot.RobotMap;
import xbot.common.properties.ITableProxy;
import xbot.subsystems.ElevatorHeightChooserSubsystem;
import xbot.subsystems.ElevatorSubsystem;

public class BaseElevatorTest extends BaseTestCase {
	
	protected ElevatorHeightChooserSubsystem heightChooser;
	protected ElevatorSubsystem elevator;
	protected RobotMap robotMap;
	protected ITableProxy randomStore;
	protected MockEncoder elevatorEncoder;
	
	private static Logger log = Logger.getLogger(BaseElevatorTest.class);
	
	protected double lowestHeight;
	protected double[] totePosHeights;
    protected double highestHeight;
	
	@Before
	public void setupTests()
	{
		log.info("======SETTING UP BASE_ELEVATOR_TEST======");
		heightChooser = injector.getInstance(ElevatorHeightChooserSubsystem.class);
		robotMap = injector.getInstance(RobotMap.class);
		elevator = injector.getInstance(ElevatorSubsystem.class);
		randomStore = injector.getInstance(ITableProxy.class);
		
		elevatorEncoder = (MockEncoder)robotMap.elevatorEncoder;
		
		setElevatorSensorHeight(0);
		
		lowestHeight = 
		        randomStore.getDouble(ElevatorHeightChooserSubsystem.forkLowestGroundHeightPropname);
		highestHeight = 
		        randomStore.getDouble(ElevatorHeightChooserSubsystem.forkMaxHeightPropname);

        OI oi = injector.getInstance(OI.class);
                randomStore.getDouble(ElevatorHeightChooserSubsystem.forkMaxHeightPropname);
        totePosHeights = new double[] {
            randomStore.getDouble(ElevatorHeightChooserSubsystem.forkHeightPrepareFirstPickupPropname),
            randomStore.getDouble(ElevatorHeightChooserSubsystem.totePositionBasePropname + 1),
            randomStore.getDouble(ElevatorHeightChooserSubsystem.totePositionBasePropname + 2),
            randomStore.getDouble(ElevatorHeightChooserSubsystem.totePositionBasePropname + 3),
            randomStore.getDouble(ElevatorHeightChooserSubsystem.totePositionBasePropname + 4)
        };
        
        //calibrate encoder
        elevator.calibrateEncoder();        
        calibrationRoutine();
	}
	
	protected void calibrationRoutine() {
	    setAtHomePosition(true);
	    elevator.setAutomaticPower(1);
	    setAtHomePosition(false);
	    elevator.setAutomaticPower(0);
	}
	
	protected void setAtHomePosition(boolean atHome)
	{
	    double voltage = 5;
	    
	    if (atHome)
	    {
	        voltage = 0;
	    }
	    
	    mockRobotIO.setAnalogVoltage(RobotMap.elevatorHomePositionAnalogChannel, voltage);
	}
	
	protected void setElevatorSensorHeight(double height)
	{
	    // tests are easier with simple units
	    setInchesPerPulse(1d);
	    elevator.calibrateEncoder();
	    
	    // Need to subtract the offset
	    elevatorEncoder.setDistance(height - elevator.getCurrentOffset());
	}
	
	protected double getLowestPossibleHeight() {
	    return randomStore.getDouble(ElevatorHeightChooserSubsystem.forkLowestGroundHeightPropname);
	}
	
	protected void setInchesPerPulse(double inchesPerPulse)
	{
	    randomStore.setDouble(ElevatorSubsystem.encoderDistancePerPulse, inchesPerPulse);
	    elevator.setEncoderInchesPerPulse();
	}
	
	protected void checkElevatorState(Zone zone, double power, boolean brakeIsEngaged)
	{
	    checkElevatorPower(zone, power);
	    checkBrake(brakeIsEngaged);
	}
	
	protected double getElevatorPower()
	{
	    double a = mockRobotIO.getPWM(RobotMap.elevatorMotorAchannel);
	    double b = mockRobotIO.getPWM(RobotMap.elevatorMotorAchannel);
	    
	    assertEquals("Elevator powers should match", a, b, 0.001);
	    
	    return a;
	}
	
	protected void checkElevatorPower(Zone zone, double power)
	{
		if (zone == Zone.Above)
		{
			assertEquals("Elevator should be moving up", zone, analyzeDirection(mockRobotIO.getPWM(RobotMap.elevatorMotorAchannel)));
			assertEquals("Elevator should be moving up", zone, analyzeDirection(mockRobotIO.getPWM(RobotMap.elevatorMotorAchannel)));
		}
		if (zone == Zone.Equals)
		{
			assertEquals(power, mockRobotIO.getPWM(RobotMap.elevatorMotorAchannel), 0.01);
			assertEquals(power, mockRobotIO.getPWM(RobotMap.elevatorMotorBchannel), 0.01);
		}
		if (zone == Zone.Below)
		{
			assertEquals("Elevator should be moving down", zone, analyzeDirection(mockRobotIO.getPWM(RobotMap.elevatorMotorAchannel)));
			assertEquals("Elevator should be moving down", zone, analyzeDirection(mockRobotIO.getPWM(RobotMap.elevatorMotorAchannel)));
		}
		
		assertNotEquals("Don't use Zone.Zero in this method.",  Zone.Zero);
	}
	
	private Zone analyzeDirection(double power)
	{
		if (power > 0)
		{
			return Zone.Above;
		}
		if (power < 0)
		{
			return Zone.Below;
		}
		return Zone.Zero;
	}
	
	protected void checkBrake(boolean brakeIsEngaged)
	{
		assertEquals(brakeIsEngaged, mockRobotIO.getSolenoid(RobotMap.elevatorLockSolenoidChannel));
	}
	
	protected enum Zone
	{
		Above,
		Below,
		Zero,
		Equals
	}
}
