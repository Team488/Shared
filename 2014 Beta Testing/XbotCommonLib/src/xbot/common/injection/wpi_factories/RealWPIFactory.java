package xbot.common.injection.wpi_factories;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class RealWPIFactory extends WPIFactory {

	@Override
	public SpeedController getSpeedController(int channel) {
		return new Talon(channel);
	}

	@Override
	public GenericHID getJoystick(int number) {
		return new Joystick(number);
	}

}
