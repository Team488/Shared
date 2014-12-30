package xbot.common.injection;

import xbot.common.injection.wpi_factories.MockWPIFactory;
import xbot.common.injection.wpi_factories.WPIFactory;

import com.google.inject.AbstractModule;

import edu.wpi.first.wpilibj.MockTimer;
import edu.wpi.first.wpilibj.Timer;

public class UnitTestModule extends AbstractModule {
	@Override
	protected void configure() {
		this.bind(Timer.StaticInterface.class).to(MockTimer.class);
		
		this.bind(WPIFactory.class).to(MockWPIFactory.class);
	}
}