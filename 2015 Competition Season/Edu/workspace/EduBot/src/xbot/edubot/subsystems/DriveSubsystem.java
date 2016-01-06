package xbot.edubot.subsystems;

import xbot.common.controls.*;
import xbot.common.wpi_extensions.mechanism_wrappers.XSpeedController;
import xbot.edubot.RobotMap;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.wpi.first.wpilibj.SpeedController;

@Singleton
public class DriveSubsystem {

	public DistanceSensor distanceSensor = new MockDistanceSensor();
	
	public HeadingSensor gyro = new HeadingSensor();
	
	XSpeedController frontLeft;
	XSpeedController frontRight;
	XSpeedController rearLeft;
	XSpeedController rearRight;
		
	@Inject
	public DriveSubsystem(RobotMap robotMap) {
		frontLeft = robotMap.frontLeft;
		frontRight = robotMap.frontRight;
		rearLeft = robotMap.rearLeft;
		rearRight = robotMap.rearRight;
	}
	
	// Add methods below that commands will call into to manipulate the drive motors
	// Eg: public void tankDrive(double leftPower, double rightPower) {
}
