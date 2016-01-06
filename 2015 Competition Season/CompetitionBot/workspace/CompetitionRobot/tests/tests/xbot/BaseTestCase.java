package tests.xbot;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;

import com.google.inject.Guice;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.MockHLUsageReporting;
import edu.wpi.first.wpilibj.MockRobotState;
import edu.wpi.first.wpilibj.MockTimer;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import xbot.RobotContext;
import xbot.common.injection.BaseWPITest;
import xbot.common.injection.MockRobotIO;
import xbot.common.injection.UnitTestModule;
import xbot.common.properties.PropertyManager;

public class BaseTestCase extends BaseWPITest {
	
	protected RobotContext context;
	
	private static final Logger log = Logger.getLogger(BaseTestCase.class);
	
	@Before
	public void setUp() {
        injector = Guice.createInjector(new CompetitionTestModule());
        
        // TODO: Alex: Refactor this otherwise common stuff back into BaseWPITest
        
        mockRobotIO = injector.getInstance(MockRobotIO.class);
        
        HLUsageReporting.SetImplementation(new MockHLUsageReporting());
        Timer.SetImplementation(injector.getInstance(MockTimer.class));
        this.mockRobotState = injector.getInstance(MockRobotState.class);
        RobotState.SetImplementation(mockRobotState);
        
        propertyManager = injector.getInstance(PropertyManager.class);
        
        DOMConfigurator.configure("../../../CompetitionBot/workspace/log4jConfig/log4j4unitTesting.xml");
		
		log.info("======SETTING UP BASE_TEST_CASE======");
		
		RobotContext.initializeRobotSystems(injector);

	}

}
