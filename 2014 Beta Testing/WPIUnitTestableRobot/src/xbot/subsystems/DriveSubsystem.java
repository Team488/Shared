package xbot.subsystems;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.RobotMap;
import xbot.commands.DriveWithJoysticksCommand;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.common.wpi_extensions.BaseSubsystem;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

@Singleton
public class DriveSubsystem extends BaseSubsystem {

	public SpeedController leftDrive;
	public SpeedController rightDrive;
	
	@Inject
	public DriveSubsystem(RobotMap map) {
		this.leftDrive = map.leftDrive;
		this.rightDrive = map.rightDrive;
	}
	
	public void tankDrive(double left, double right){
		leftDrive.set(left);
		rightDrive.set(right);
	}

}
