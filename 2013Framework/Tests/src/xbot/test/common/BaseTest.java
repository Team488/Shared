/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.test.common;

import xbot.common.CommonTools;
import xbot.common.ITableProxy;
import xbot.common.PermanentStorageProxy;
import xbot.common.logging.LogWriter;
import xbot.common.TableProxy;
import xbot.common.Time;
import xbot.core.RobotContext;

/**
 *
 * @author Alex
 */
public class BaseTest {
    protected PermanentStoreTest permanentStore;
    protected ITableProxy randomAccessStore;
    protected RobotContext context;
    
    public BaseTest() {
        resetContext();
    }
    
    protected void resetContext() {
        permanentStore = new PermanentStoreTest();
        randomAccessStore = new TableProxy("TestRandomStore");
        CommonTools.CreateCommonTools(new Time(), new LogWriter(), 
                permanentStore, randomAccessStore);
        RobotContext.ResetRobotContextForTestingPurposesOnly();
        context = RobotContext.Get();
    }
    
}
