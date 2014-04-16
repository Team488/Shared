/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import xbot.aerialassist.autonomousworkers.threeballauto.ThreeBallAutonomousWorker;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.StateMachineWorker;
import xbot.common.logging.LogConsumer;
import xbot.common.logging.LogProducer;
import xbot.common.properties.ITableProxy;
import xbot.test.common.BaseTest;
import xbot.test.common.StringLogWriter;
import xbot.test.workers.MockWorker;

/**
 *
 * @author sterlingdorminey
 */
@Ignore 
public  class AutonomousTest extends BaseTest {
    private MockWorker worker;
    private ThreeBallAutonomousWorker autonomous;
    private MockAutonomousWorkerFactory factory;
    
    public AutonomousTest() {
        this.worker = new MockWorker();
        this.factory = new MockAutonomousWorkerFactory(this.worker);
    }
  
    @Before
    public void setUp() {
        this.autonomous = new ThreeBallAutonomousWorker(this.factory);
        this.autonomous.init();
    }
    
    @Test
    public void test3BallAutoSuccess() {
        this.setEnabled(true, true);
        
        this.worker.setExecState(ExecState.SUCCESS);
        
        for (int i = 1; i <= 5; i++) {
            this.execAndAssert(i);
        }
        
        this.execAndAssert(5);
        this.execAndAssert(5);
        this.execAndAssert(5);
        this.mockTime.incrementTime(3000);
        this.execAndAssert(StateMachineWorker.SUCCESS);
    }
    
    @Test
    public void test2BallAutoBackSuccess() {
        this.setEnabled(false, true);
        
        this.worker.setExecState(ExecState.SUCCESS);
        
        this.execAndAssert(1);
        this.execAndAssert(2);
        this.execAndAssert(4);
        this.execAndAssert(5);
        this.execAndAssert(5);
        this.execAndAssert(5);
        this.execAndAssert(5);
        this.mockTime.incrementTime(3000);
        this.execAndAssert(StateMachineWorker.SUCCESS);
    }
    
    @Test
    public void test2BallAutoFrontSuccess() {
        this.setEnabled(true, false);
        
        this.worker.setExecState(ExecState.SUCCESS);
        
        this.execAndAssert(1);
        this.execAndAssert(2);
        this.execAndAssert(3);
        this.execAndAssert(4);
        this.execAndAssert(StateMachineWorker.SUCCESS);
    }
    
    private void setEnabled(boolean front, boolean back) {
        ITableProxy properties = CommonTools.Get().propertyManager.randomAccessStore;
        properties.setBoolean("AutonomousUseFront", front);
        properties.setBoolean("AutonomousUseBack", back);
    }
    
    private void execAndAssert(int state) {
        this.autonomous.exec();
        assertEquals(state, this.autonomous.getCurrentState());
    }
}
