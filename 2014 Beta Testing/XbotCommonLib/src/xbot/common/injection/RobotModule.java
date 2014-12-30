package xbot.common.injection;

import xbot.common.injection.wpi_factories.RealWPIFactory;
import xbot.common.injection.wpi_factories.WPIFactory;

import com.google.inject.AbstractModule;

public class RobotModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(WPIFactory.class).to(RealWPIFactory.class);
	}

}
