package tests.drive;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wpi.first.wpilibj.MockJoystick;
import tests.xbot.BaseTestCase;
import xbot.OI;
import xbot.commands.DriveWithJoysticksCommand;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.DriveSubsystem;

public class DriveWithJoysticksCommandTest extends BaseTestCase {
	
	@Test
	public void test() {
		BaseCommand command = injector.getInstance(DriveWithJoysticksCommand.class);
		DriveSubsystem driveSub = injector.getInstance(DriveSubsystem.class);
		
		MockJoystick left = (MockJoystick)(injector.getInstance(OI.class).leftJoystick);
		
		command.initialize();
		
		command.execute();
		assertEquals(0, driveSub.leftFrontDrive.get(), 0.001);
		assertEquals(0, driveSub.rightFrontDrive.get(), 0.001);
		
		left.setY(1.0);
		//right.setY(1.0);
		command.execute();
		assertEquals(1.0, driveSub.leftFrontDrive.get(), 0.001);
		assertEquals(1.0, driveSub.rightFrontDrive.get(), 0.001);
		
	}

}
