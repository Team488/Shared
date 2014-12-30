package xbot;

import static org.junit.Assert.*;

import org.junit.Test;

import xbot.common.injection.BaseWPITest;
import xbot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.MockJoystick;
import edu.wpi.first.wpilibj.command.Scheduler;

public class DefaultCommandMapTest extends BaseWPITest {

	@Test
	public void testDefaultCommandRuns() {
		OI oi = injector.getInstance(OI.class);
		DriveSubsystem drive = injector.getInstance(DriveSubsystem.class);
		DefaultCommandMap map = injector.getInstance(DefaultCommandMap.class);
		// run the scheduler
		Scheduler.getInstance().run();
		((MockJoystick) oi.leftJoystick).setY(1.0);
		Scheduler.getInstance().run();
		assertEquals(1.0, drive.leftDrive.get(), 0.001);
	}
}
