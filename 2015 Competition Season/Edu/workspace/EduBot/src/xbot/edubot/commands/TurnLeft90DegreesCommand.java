package xbot.edubot.commands;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.edubot.subsystems.DriveSubsystem;

public class TurnLeft90DegreesCommand extends BaseCommand{
	
	DriveSubsystem drive;
	
	@Inject
	public TurnLeft90DegreesCommand(DriveSubsystem driveSubsystem) {
		this.drive = driveSubsystem;
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}



}
