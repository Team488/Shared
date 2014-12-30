package xbot;

import xbot.commands.DriveWithJoysticksCommand;
import xbot.subsystems.DriveSubsystem;

import com.google.inject.Inject;

public class DefaultCommandMap {
	
	@Inject
	public DefaultCommandMap(DriveSubsystem drive, DriveWithJoysticksCommand driveWithJoysticksCommand) {
		drive.setDefaultCommand(driveWithJoysticksCommand);
	}
}
