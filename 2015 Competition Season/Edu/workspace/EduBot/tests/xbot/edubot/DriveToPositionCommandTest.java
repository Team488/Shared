package xbot.edubot;

import static org.junit.Assert.*;

import org.junit.Test;

import xbot.edubot.commands.DriveToPositionCommand;

public class DriveToPositionCommandTest extends BaseDriveTest {

	double target_distance = 5;
	
	@Test
	public void test() {
		drive.distanceSensor.setDistance(0);
		
		DriveToPositionCommand command = 
				injector.getInstance(DriveToPositionCommand.class);
		command.setTargetPosition(target_distance);
		
		command.initialize();
		
		double velocity = 0;
		
		double friction = 0.005;
		double power_factor = 0.1;
		
		double error_threshold = 0.2;
		
		boolean isFinished = false;
		
		for(int i = 0; i < 300; i++) {
			System.out.println("Loop: " + i + 
					"  Velocity: " + velocity + 
					"  Distance: " + drive.distanceSensor.getDistance() + 
					"  Power: " + getForwardPower());
			// apply motor power
			velocity += getForwardPower() * power_factor;
			
			// apply friction model to velocity
			if(velocity > 0) {
				velocity -= friction;
				velocity = Math.max(0, velocity);
			} else {
				velocity += friction;
				velocity = Math.min(0, velocity);
			}
			
			// model change in position based on motor power
			drive.distanceSensor.incrementDistance(velocity);
			
			if(command.isFinished()) {
				isFinished = true;
				break;
			}
			
			command.execute();
			
		}
		assertTrue("Verify command reports successfully finished", isFinished);
		command.end();
		assertEquals("Make sure robot is close to target position within " + error_threshold, 
				target_distance, drive.distanceSensor.getDistance(), error_threshold);
		assertEquals("Make sure robot has come to a stop, not just flying past the target position.", 
				0.0, velocity, 0.1);
		
		
	}
	
	double getForwardPower() {
		return this.mockRobotIO.getPWM(1);
	}

}
