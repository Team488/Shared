package tests.elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.wpi.first.wpilibj.MockEncoder;
import xbot.RobotMap;
import xbot.commands.elevator.ElevatorMaintainTargetHeightCommand;
import xbot.common.properties.ITableProxy;
import xbot.subsystems.ElevatorHeightChooserSubsystem;

public class ElevatorSubsystemMaintainerTest extends BaseElevatorTest {
	
	@Test
	public void testBasicDirections() 
	{		
	    ElevatorMaintainTargetHeightCommand command = 
	            injector.getInstance(ElevatorMaintainTargetHeightCommand.class);
	    
		// Set PID values
		ITableProxy randomStore = injector.getInstance(ITableProxy.class);
		randomStore.setDouble("Elevator P", 0.05);
		
		MockEncoder elevatorEncoder = (MockEncoder)robotMap.elevatorEncoder;
		elevatorEncoder.setRate(10); 
		
		//Set height by modifying the voltage of the analog sensor
		setElevatorSensorHeight(10);
		// Set goal by telling the maintainer what the goal is
		heightChooser.setTargetHeight(15);
		// Run the maintainer logic
		command.execute();
		
		// Verify motor outputs and brake state
		// Since our goal is above our current position, motors should be running positively,
		// and the brake should NOT be engaged.
		checkElevatorPower(Zone.Above, 0.05);
		checkBrake(false);
		
		setElevatorSensorHeight(elevator.getMinHeight());
		heightChooser.setTargetHeight(elevator.getMinHeight());
        
		// Since we use averaging, this needs to run several times before zeroing out completely.
		for (int i = 0; i < 11; i++) {
		    command.execute();
		}
		
		checkElevatorPower(Zone.Equals, 0.0);
		checkBrake(true);
		
		//TODO: Be smarter about test values for encoder and pot
		mockRobotIO.setAnalogVoltage(RobotMap.elevatorAbsoluteHeightAnalogChannel, 1);
		elevatorEncoder.setDistance(100);
		heightChooser.setTargetHeight(0);
        command.execute();
		
		checkElevatorPower(Zone.Below, -0.1);
		checkBrake(false);
		
		setElevatorSensorHeight(70);
        elevatorEncoder.setDistance(10000);
		heightChooser.setTargetHeight(0);
        command.execute();
		
		checkElevatorPower(Zone.Below, -.1);
		checkBrake(false);
	}
	
	@Test
	public void testBounds()
	{
        randomStore.setDouble("Elevator P", 0.05);
        ElevatorMaintainTargetHeightCommand command = injector.getInstance(ElevatorMaintainTargetHeightCommand.class);
        
		// Test somebody setting a goal that's too low
		setElevatorSensorHeight(elevator.getMinHeight());
		heightChooser.setTargetHeight(-50);
		command.execute();
		
		checkElevatorPower(Zone.Equals, 0);
		checkBrake(true);
		
		// Too high
		setElevatorSensorHeight(100);
		heightChooser.setTargetHeight(55);
		command.execute();
		checkElevatorPower(Zone.Below, 0);
		checkBrake(false);
	}
	
	@Test
	public void testSmoothUpAcceleration()
	{
	   	randomStore.setDouble("Elevator P", 0.05);
        ElevatorMaintainTargetHeightCommand command = injector.getInstance(ElevatorMaintainTargetHeightCommand.class);
        randomStore.setDouble(ElevatorMaintainTargetHeightCommand.elevatorAveragingWindowPropname, 10.0);
        command.initialize();
        
        setElevatorSensorHeight(0);
        heightChooser.setTargetHeight(50);
        command.execute();
        
        checkElevatorState(Zone.Above, 0, false);
        double firstPower = getElevatorPower();
        
        command.execute();
        
        checkElevatorState(Zone.Above, 0, false);
        double secondPower = getElevatorPower();
        
        assertTrue("Second power is higher than first, due to averaging", secondPower > firstPower);
	}
	
	@Test
	public void testAveragingPositiveToNegative()
	{
	    randomStore.setDouble("Elevator P", 0.05);
        ElevatorMaintainTargetHeightCommand command = injector.getInstance(ElevatorMaintainTargetHeightCommand.class);
        command.initialize();
        
        setElevatorSensorHeight(0);
        heightChooser.setTargetHeight(20);
        for (int i = 0; i < 10; i++) {
            command.execute();
        }
        
        checkElevatorState(Zone.Above, 0, false);
        
        setElevatorSensorHeight(45);
        command.execute();
        checkElevatorState(Zone.Below, 0, false);
	}
	
	@Test
	public void testAveragingNegativeToPositive()
	{
	    randomStore.setDouble("Elevator P", 0.05);
        ElevatorMaintainTargetHeightCommand command = injector.getInstance(ElevatorMaintainTargetHeightCommand.class);
        command.initialize();
        
        setElevatorSensorHeight(0);
        heightChooser.setTargetHeight(20);
        for (int i = 0; i < 10; i++) {
            command.execute();
        }
        
        checkElevatorState(Zone.Above, 0, false);
        
        setElevatorSensorHeight(45);
        command.execute();
        checkElevatorState(Zone.Below, 0, false);
        
        // then test averaging starts right back up
        
        heightChooser.setTargetHeight(50);
        command.execute();
        
        checkElevatorState(Zone.Above, 0, false);
	}
	
	@Test
	public void testMaintainerBrake()
	{
	    // TODO: change magic numbers 5 and 2 to actually grab properties from the map
	    ElevatorMaintainTargetHeightCommand command = 
	            injector.getInstance(ElevatorMaintainTargetHeightCommand.class);
	    
	    ITableProxy randomStore = injector.getInstance(ITableProxy.class);
        randomStore.setDouble("Elevator P", 0.05);
        
        // Set height and goal to the bottom of the range
        setElevatorSensorHeight(elevator.getMinHeight());
        heightChooser.setTargetHeight(elevator.getMinHeight());
        
        command.initialize();
        command.execute();
        
        // Verify we are in large deadband, and brake engaged
        assertEquals(
                randomStore.getDouble(ElevatorHeightChooserSubsystem.deadbandLargePropname), 
                heightChooser.getElevatorPositionDeadband(), 
                0.05);
        checkBrake(true);
        
        double goalHeight = 49;
        
        // Move goal
        heightChooser.setTargetHeight(goalHeight);
        command.execute();
        
        // should have small deadband target, and brake is free
        assertEquals(0.25, heightChooser.getElevatorPositionDeadband(), 0.1);
        checkBrake(false);
        
        // Arrive at goal
        setElevatorSensorHeight(goalHeight);
        command.execute();
        assertEquals(0, elevator.updateAndGetHeight()-goalHeight, 0.01);
        
        // should have large deadband, and brake is engaged.
        assertEquals(.35, heightChooser.getElevatorPositionDeadband(), 0.1);
        checkBrake(true);        
	}
}