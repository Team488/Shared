package xbot.edubot.rotationTests;

import static org.junit.Assert.*;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.edubot.BaseDriveTest;
import xbot.edubot.commands.TurnLeft90DegreesCommand;
import xbot.edubot.engines.RotationEngine;


public class BaseOrientationEngineTest extends BaseDriveTest {
	
	protected void run90ClassTest(double start, double finish)
	{
		RotationEngine engine = new RotationEngine(0.1, start, 0);
		drive.gyro.setYaw(start);
		
		TurnLeft90DegreesCommand command = 
				injector.getInstance(TurnLeft90DegreesCommand.class);
		
		double targetHeading = finish;
		System.out.println("Target Heading: " + targetHeading);
		
		runRotationTest(command, engine, targetHeading);
	}
	
	protected void runRotationTest(BaseCommand command, RotationEngine engine, double targetHeading)
	{
		
		command.initialize();
		
		for (int i = 0; i < 501; i++)
		{
			engine.setPower(getRotationPower());
			engine.step();
			drive.gyro.setYaw(engine.getOrientation());
			// print output
			System.out.printf("Time:%.1f sec, TurningPower:%.2f, Velocity:%.2f, Yaw:%.2f \n",
					(double)i/10,
					getRotationPower(),
					engine.getVelocity(),
					engine.getOrientation());
			
			if(command.isFinished()) {
				break;
			}
			
			command.execute();
		}
		
		double error_threshold = 3;
		// check for desired result
		assertTrue("Verify command reports successfully finished", command.isFinished());
		command.end();
		
		assertEquals("Make sure robot is close to target position within " + error_threshold, 
				targetHeading, drive.gyro.getYaw(), error_threshold);
		assertEquals("Make sure robot has come to a stop, not just flying past the target position.", 
				0.0, engine.getVelocity(), 0.1);
		// check how long it took
	}
	
	private double getRotationPower()
	{
		// read from drive wheels, make a turning function
		//left pair
		double l1 = this.mockRobotIO.getPWM(1);
		double l2 = this.mockRobotIO.getPWM(3);
		
		//right pair
		double r1 = this.mockRobotIO.getPWM(2);
		double r2 = this.mockRobotIO.getPWM(4);
		
		//left turns are positive. So right power is positive, left power negative.
		
		double turningPower = (r1 + r2 - l1 - l2) / 4;
				
		return turningPower;
	}

}
