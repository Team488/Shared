package xbot.edubot;

import xbot.edubot.commands.TankDriveWithJoysticksCommand;
import xbot.edubot.subsystems.DriveSubsystem;

import com.google.inject.Inject;

public class DefaultCommandMap {
	
	@Inject
	public DefaultCommandMap(DriveSubsystem drive, TankDriveWithJoysticksCommand driveWithJoysticksCommand) {

	}
}
