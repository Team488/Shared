package xbot.common.injection.wpi_factories;

import xbot.common.injection.wpi_factories.WPIFactory;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.MockJoystick;
import edu.wpi.first.wpilibj.MockSpeedController;
import edu.wpi.first.wpilibj.SpeedController;

public class MockWPIFactory extends WPIFactory {

	@Override
	public SpeedController getSpeedController(int channel) {
		return new MockSpeedController(channel);
	}

	@Override
	public GenericHID getJoystick(int number) {
		return new MockJoystick();
	}

}
