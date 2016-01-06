package xbot.edubot;

import org.junit.Test;

import xbot.edubot.commands.RaiseArmCommand;

public class MoveArmToLimitTest extends BaseArmTest {
	
	@Test
	public void RaiseArmToLimit(){
		RaiseArmCommand command = injector.getInstance(RaiseArmCommand.class);
		
		command.initialize();
		
		command.execute();
		
		//Initially, no raise button is pressed so the motors should run at 0
		assertArmSpeed(0, "Expect Motors initially 0");
		
		//Press button 0 and assert that the arm motors are running
		left.pressButton(0);
		command.execute();
		assertArmSpeed(1.0, "Expect Arm motors to move ");
		
		// Trigger the upper arm limit switch and the motors should stop
		triggerUpperLimitSwitch();
		assertArmSpeed(0, "Expect arms to stop after hitting limit switch");	
	}
	
	@Test
	public void LowerArmToLimit(){
		RaiseArmCommand command = injector.getInstance(RaiseArmCommand.class);
		
		command.initialize();
		
		command.execute();
		
		//Initially, no raise button is pressed so the motors should run at 0
		assertArmSpeed(0, "Expect Motors initially 0");
		
		//Press button 0 and assert that the arm motors are running in the opposite direction 
		left.pressButton(1);
		command.execute();
		assertArmSpeed(-1.0, "Expect Arm motors to move ");
		
		// Trigger the lower limit switch and the motors should stop
		triggerLowerLimitSwitch();
		assertArmSpeed(0, "Expect arms to stop after hitting limit switch");	
	}
	
}
