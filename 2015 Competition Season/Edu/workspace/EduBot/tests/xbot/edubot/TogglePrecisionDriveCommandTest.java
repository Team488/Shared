package xbot.edubot;

import org.junit.Test;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.edubot.commands.TankDriveWithJoysticksCommand;
import xbot.edubot.commands.TogglePrecisionDriveCommand;
import xbot.edubot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.MockJoystick;

public class TogglePrecisionDriveCommandTest extends BaseDriveTest {

	@Test
	public void test() {
		DriveSubsystem drive = this.injector.getInstance(DriveSubsystem.class);
		OI oi = this.injector.getInstance(OI.class);
		
		BaseCommand driveCommand = new TankDriveWithJoysticksCommand(drive, oi);
		BaseCommand togglePrecisionCommand = new TogglePrecisionDriveCommand(drive);
		
		MockJoystick left = (MockJoystick)oi.leftJoystick;
		MockJoystick right = (MockJoystick)oi.rightJoystick;
		
		driveCommand.initialize();
		
		togglePrecisionCommand.initialize();
		togglePrecisionCommand.execute();
		
		left.setY(1.0);
		right.setY(1.0);
		driveCommand.execute();
		this.assertDrive(0.5, 0.5);
		
		left.setY(-1.0);
		right.setY(-1.0);
		driveCommand.execute();
		this.assertDrive(-0.5, -0.5);
		
		togglePrecisionCommand.initialize();
		togglePrecisionCommand.execute();
		
		left.setY(1.0);
		right.setY(1.0);
		driveCommand.execute();
		this.assertDrive(1, 1);
		
	}
}
