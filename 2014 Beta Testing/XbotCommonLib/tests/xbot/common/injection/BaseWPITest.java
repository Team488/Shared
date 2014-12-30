package xbot.common.injection;

import org.junit.*;

import com.google.inject.Guice;
import com.google.inject.Injector;

import xbot.common.injection.wpi_factories.MockWPIFactory;
import xbot.common.injection.wpi_factories.WPIFactory;
import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.MockHLUsageReporting;
import edu.wpi.first.wpilibj.MockRobotState;
import edu.wpi.first.wpilibj.MockTimer;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;

public class BaseWPITest {
	public Injector injector;
	
	@Before
	public void setUp() {
		injector = Guice.createInjector(new UnitTestModule());
		
		HLUsageReporting.SetImplementation(new MockHLUsageReporting());
		Timer.SetImplementation(injector.getInstance(Timer.StaticInterface.class));
		RobotState.SetImplementation(new MockRobotState());
	}
}
