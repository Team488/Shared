package xbot;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Test;

import edu.wpi.first.wpilibj.MockJoystick;
import xbot.commands.DriveWithJoysticksCommand;
import xbot.common.injection.BaseWPITest;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.DriveSubsystem;

public class DriveWithJoysticksCommandTest extends BaseWPITest {

	private static Logger log = Logger.getLogger(DriveWithJoysticksCommandTest.class);
	
	@Test
	public void test() {
		DriveSubsystem drive = this.injector.getInstance(DriveSubsystem.class);
		OI oi = this.injector.getInstance(OI.class);
		
		/*log.info("This is the starting message.");
		for (int i = 0; i < 400; i++)
		{
			log.info("THIS IS A DANGEROUS WARNING MESSAGE!!!");
		}
		log.info("This is the last message.");
		*/
		BaseCommand command = new DriveWithJoysticksCommand(oi, drive);
		
		
		MockJoystick left = (MockJoystick)oi.leftJoystick;
		MockJoystick right = (MockJoystick)oi.rightJoystick;
		
		command.initialize();
		
		command.execute();
		assertEquals(0, drive.leftDrive.get(), 0.001);
		assertEquals(0, drive.rightDrive.get(), 0.001);
		
		left.setY(1.0);
		right.setY(1.0);
		command.execute();
		assertEquals(1.0, drive.leftDrive.get(), 0.001);
		assertEquals(1.0, drive.rightDrive.get(), 0.001);
		
	}

}
