package tests.elevator;

import org.junit.Test;

import xbot.commands.elevator.ElevatorMaintainTargetHeightCommand;


public class ElevatorLockTest extends BaseElevatorTest {
	
	@Test
	public void test() 
	{
		ElevatorMaintainTargetHeightCommand maintainTargetHeight = 
		        injector.getInstance(ElevatorMaintainTargetHeightCommand.class);
		
		// We should probably set some values here.
		
		heightChooser.setTargetHeight(elevator.updateAndGetHeight());
		maintainTargetHeight.initialize();
		maintainTargetHeight.execute();
		
		checkBrake(true);		
		}
}

