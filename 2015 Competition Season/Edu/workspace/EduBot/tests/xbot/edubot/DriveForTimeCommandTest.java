package xbot.edubot;

import static org.junit.Assert.*;

import org.junit.Test;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.edubot.commands.DriveForTimeCommand;
import edu.wpi.first.wpilibj.MockTimer;

public class DriveForTimeCommandTest extends BaseDriveTest {

	@Test
	public void test() {

		BaseCommand command = injector.getInstance(DriveForTimeCommand.class);

		MockTimer timer = injector.getInstance(MockTimer.class);
		
		command.initialize();
		
		command.execute();
		this.assertDrive(0.5, 0.5);
		
		assertFalse(command.isFinished());
		
		timer.setTimeInSeconds(0.25);
		
		assertFalse(command.isFinished());
		command.execute();
		this.assertDrive(0.5, 0.5);
		
		
		timer.setTimeInSeconds(0.51);
		assertTrue(command.isFinished());
		command.end();
		this.assertDrive(0, 0);
		
	}
}
