package xbot.common.injection.wpi_factories;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SpeedController;

public abstract class WPIFactory {
	
	public abstract SpeedController getSpeedController(int channel);
	
	public abstract GenericHID getJoystick(int number);
}


