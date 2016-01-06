package xbot.edubot.commands;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.edubot.subsystems.DriveSubsystem;

public class DriveToOrientationCommand extends BaseCommand{
	
	DriveSubsystem drive;
	
	@Inject
	public DriveToOrientationCommand(DriveSubsystem driveSubsystem) {
		this.drive = driveSubsystem;
	}
	
	public void setTargetHeading(double heading) {
		
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
