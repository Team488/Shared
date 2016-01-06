package xbot.edubot.subsystems;


import xbot.common.wpi_extensions.mechanism_wrappers.XDigitalInput;
import xbot.common.wpi_extensions.mechanism_wrappers.XSpeedController;
import xbot.edubot.RobotMap;

import com.google.inject.Inject;

import edu.wpi.first.wpilibj.SpeedController;

public class ArmSubsystem {
	
	public XSpeedController armMotor;
	public XDigitalInput upperLimitSwitch, lowerLimitSwitch;
	
	@Inject
	public ArmSubsystem(RobotMap robotMap) {
		upperLimitSwitch = robotMap.upperLimitSwitch;
		lowerLimitSwitch = robotMap.lowerLimitSwitch;
		
		armMotor = robotMap.arm;
	}

}
