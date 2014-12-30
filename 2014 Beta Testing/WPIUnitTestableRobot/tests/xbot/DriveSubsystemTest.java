package xbot;

import static org.junit.Assert.*;

import org.junit.*;

import xbot.common.injection.BaseWPITest;
import xbot.subsystems.DriveSubsystem;

public class DriveSubsystemTest extends BaseWPITest {
	DriveSubsystem drive;
	
	@Test
	public void test() {
		drive = injector.getInstance(DriveSubsystem.class);
		drive.tankDrive(1, 1);
		
		assertEquals(1.0, drive.leftDrive.get(), 0.001);
		assertEquals(1.0, drive.rightDrive.get(), 0.001);
	}

}
