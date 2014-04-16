/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.test.common;

import xbot.aerialassist.RobotContext;
import xbot.common.CommonTools;
import xbot.common.logging.LogWriter;
import xbot.common.Time;
import xbot.common.properties.ITableProxy;
import xbot.common.properties.TableProxy;

/**
 *
 * @author Alex
 */
public class BaseTest {
    protected PermanentStoreTest permanentStore;
    protected ITableProxy randomAccessStore;
    protected RobotContext context;
    protected MockTime mockTime;
    
    public BaseTest() {
        resetContext();
    }
    
    protected void resetContext() {
        permanentStore = new PermanentStoreTest();
        randomAccessStore = new TableProxy("TestRandomStore");
        mockTime = new MockTime();
        CommonTools.CreateCommonTools(mockTime, new LogWriter("Log"), new LogWriter("Telemetry"),
                permanentStore, randomAccessStore, new TrigUtil());
        RobotContext.ResetRobotContextForTestingPurposesOnly();
        context = RobotContext.Get();
    }
    
}
