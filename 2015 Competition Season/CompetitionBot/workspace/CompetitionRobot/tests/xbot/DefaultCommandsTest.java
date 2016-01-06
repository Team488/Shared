package xbot;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tests.xbot.BaseTestCase;
import xbot.common.wpi_extensions.XScheduler;

public class DefaultCommandsTest extends BaseTestCase {

    @Test
    public void defaultsAreClean() {
        
        XScheduler sched = injector.getInstance(XScheduler.class);
        
        sched.run();
        sched.run();
        sched.run();
        sched.run();
        
        assertTrue("No crashes when running default commands", sched.getNumberOfCrashes() == 0);
    }
}
