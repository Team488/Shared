package xbot.commands;

import com.google.inject.Inject;

import xbot.OI;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.DriveSubsystem;

public class DriveWithJoysticksCommand extends BaseCommand {
	OI oi;
	DriveSubsystem drive;

	@Inject
	public DriveWithJoysticksCommand(OI oi, DriveSubsystem drive) {
		this.oi = oi;
		this.drive = drive;
		this.requires(drive);
	}
	
	@Override
	public void initialize() {

	}

	@Override
	public void execute() {
		drive.tankDrive(oi.leftJoystick.getY(), oi.rightJoystick.getY());
	}

	
}
