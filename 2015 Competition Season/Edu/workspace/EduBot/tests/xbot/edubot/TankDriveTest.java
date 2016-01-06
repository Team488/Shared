package xbot.edubot;

import org.junit.Test;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.edubot.commands.TankDriveWithJoysticksCommand;

public class TankDriveTest extends BaseDriveTest {

	@Test
	public void test() {
		BaseCommand command = injector.getInstance(TankDriveWithJoysticksCommand.class);
		
		command.initialize();
		
		command.execute();
		assertDrive(0, 0, "Expect Motors initially 0");
		
		left.setY(1.0);
		right.setY(1.0);
		command.execute();
		assertDrive(1.0,1.0, "Expect Motors are all forward when both joysticks are completely forward");
	}
}
