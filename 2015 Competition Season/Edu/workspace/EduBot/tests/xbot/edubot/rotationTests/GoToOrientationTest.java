package xbot.edubot.rotationTests;

import org.junit.Test;

import xbot.edubot.commands.DriveToOrientationCommand;
import xbot.edubot.engines.RotationEngine;

public class GoToOrientationTest extends BaseOrientationEngineTest {
	
	@Test
	public void goToOrientation()
	{
		RotationEngine engine = new RotationEngine();
		
		DriveToOrientationCommand command = 
				injector.getInstance(DriveToOrientationCommand.class);
		
		double targetHeading = 150;
		System.out.println("Target Heading: " + targetHeading);
		command.setTargetHeading(targetHeading);
		
		runRotationTest(command, engine, targetHeading);		
	}
}
