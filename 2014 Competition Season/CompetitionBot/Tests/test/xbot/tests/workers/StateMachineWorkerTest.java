/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.tests.workers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.StateMachineWorker;
import xbot.test.common.BaseTest;
import xbot.test.workers.MockWorker;

/**
 *
 * @author jogilber
 */
public class StateMachineWorkerTest extends BaseTest {
    private MockWorker w1;
    private MockWorker w2;
    
    public StateMachineWorkerTest() {
        w1 = new MockWorker();
        w2 = new MockWorker();
    }
    
    @Before
    public void setUp() {
        this.w1.reset();
        this.w2.reset();
    }
    
    @After
    public void tearDown() {
        CommonTools.Get().GetLowPriorityTasks().Feed();
    }
    
    @Test
    public void testValidation() {
        StateMachineWorker machine;
    
        machine = buildMachine();
        machine.onFailure(0, -99);
        machine.onSuccess(0, 1);
        machine.onFailure(1, StateMachineWorker.FAILURE);
        machine.onSuccess(1, StateMachineWorker.SUCCESS);
        
        machine.init();
        machine.exec();
        Assert.assertEquals(StateMachineWorker.FAILURE, machine.getCurrentState());
        
        machine = buildMachine();
        machine.init();
        Assert.assertEquals(StateMachineWorker.FAILURE, machine.getCurrentState());
        
        
        machine = buildMachine();
        machine.onFailure(0, StateMachineWorker.FAILURE);
        machine.onSuccess(0, 1);
        machine.onFailure(1, StateMachineWorker.FAILURE);
        machine.onSuccess(1, StateMachineWorker.SUCCESS);
        
        machine.init();
        Assert.assertEquals(StateMachineWorker.FAILURE, machine.getCurrentState());
        
        machine = buildMachine();
        machine.onFailure(0, StateMachineWorker.FAILURE);
        machine.onSuccess(0, 1);
        machine.onFailure(1, StateMachineWorker.FAILURE);
        machine.onSuccess(1, StateMachineWorker.SUCCESS);
        machine.onStart(0);
        
        machine.init();
    }

    @Test
    public void testExecution() {
        StateMachineWorker machine = buildMachine();
        machine.onFailure(0, 0);
        machine.onSuccess(0, 1);
        machine.onSuccess(1, StateMachineWorker.SUCCESS);
        machine.onFailure(1, StateMachineWorker.FAILURE);
        machine.onStart(0);
        
        machine.init();
        // start in state w1.
        machine.exec();
        Assert.assertEquals(1, w1.getTimesExecuted());
        // (w1, NOT_DONE) -> w1
        machine.exec();
        Assert.assertEquals(2, w1.getTimesExecuted());
        // (w1, FAILURE) -> w1
        w1.setExecState(ExecState.FAILURE);
        machine.exec();
        Assert.assertEquals(3, w1.getTimesExecuted());
        // (w1, SUCCESS) -> w2
        w1.setExecState(ExecState.SUCCESS);
        machine.exec();
        // (w2, NOT_DONE) -> w2
        machine.exec();
        Assert.assertEquals(1, w2.getTimesExecuted());
        // (w2, FAILURE) -> FAILURE
        w2.setExecState(ExecState.FAILURE);
        machine.exec();
        Assert.assertEquals(2, w2.getTimesExecuted());
        // nothing executed in FAILURE
        machine.exec();
        Assert.assertEquals(2, w2.getTimesExecuted());
    }
    
    /**
     * Tests constructing state machines where names are specified,
     * and selecting/verifying states by names.
     */
    @Test
    public void testNames() {
        StateMachineWorker machine = new StateMachineWorker("NameTest");
        w1.setExecState(ExecState.SUCCESS);
        int i1 = machine.addWorker("w1", w1);
        int i2 = machine.addWorker("w2", w1);
        int i3 = machine.addWorker(w1);
        machine.onStart(i1);
        machine.onSuccess(i1, i2);
        machine.onSuccess(i2, i3);
        machine.onSuccess(i3, StateMachineWorker.SUCCESS);
        
        machine.init();
        Assert.assertEquals(i1, machine.getCurrentState());
        Assert.assertEquals("w1", machine.getCurrentStateName());
        machine.exec();
        Assert.assertEquals(i2, machine.getCurrentState());
        Assert.assertEquals("w2", machine.getCurrentStateName());
        machine.exec();
        Assert.assertEquals(i3, machine.getCurrentState());
        Assert.assertEquals("Unknown", machine.getCurrentStateName());
        machine.exec();
        Assert.assertEquals(StateMachineWorker.SUCCESS, machine.getCurrentState());
        Assert.assertEquals("Success", machine.getCurrentStateName());
    }
    
    /**
     * Tests skipping to a certain state.
     */
    @Test
    public void skipToState() {
        StateMachineWorker machine = new StateMachineWorker("SkipTest");
        w1.setExecState(ExecState.NOT_DONE);
        int i1 = machine.addWorker("w1", w1);
        int i2 = machine.addWorker("w2", w2);
        machine.onStart(i1);
        machine.onSuccess(i1, i2);
        machine.onSuccess(i2, StateMachineWorker.SUCCESS);
        
        machine.init();
        machine.setStateTestOnly("w2");
        machine.exec();
        Assert.assertEquals("w2", machine.getCurrentStateName());
    }
    
    private StateMachineWorker buildMachine() {
        StateMachineWorker machine = new StateMachineWorker("Test");
        int i1 = machine.addWorker(w1);
        int i2 = machine.addWorker(w2);
        
        Assert.assertEquals(0, i1);
        Assert.assertEquals(1, i2);
        
        return machine;
    }
}
