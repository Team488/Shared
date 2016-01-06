package xbot.edubot;

import static org.junit.Assert.*;

import org.junit.Test;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.edubot.commands.ArcadeDriveWithJoysticksCommand;
import edu.wpi.first.wpilibj.MockJoystick;

public class ArcadeDriveTest extends BaseDriveTest {

	@Test
	public void test() {
		OI oi = this.injector.getInstance(OI.class);
		
		BaseCommand command = injector.getInstance(ArcadeDriveWithJoysticksCommand.class);
			
		MockJoystick left = (MockJoystick)oi.leftJoystick;
		
		command.initialize();
		
		command.execute();
		this.assertDrive(0, 0);
		
		left.setY(1.0);
		left.setX(0.0);
		command.execute();
		this.assertDrive(1, 1);
		
		left.setY(0.0);
		left.setX(1.0);
		command.execute();
		this.assertDrive(1, -1);
		
		left.setY(0.8);
		left.setX(0.8);
		command.execute();
		assertTrue(this.mockRobotIO.getPWM(1) > this.mockRobotIO.getPWM(2));
	}
}