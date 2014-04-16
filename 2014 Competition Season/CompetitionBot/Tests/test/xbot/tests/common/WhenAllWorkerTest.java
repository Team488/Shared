/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.tests.common;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import xbot.common.ExecState;
import xbot.common.WhenAllWorker;
import xbot.test.workers.MockWorker;

/**
 *
 * @author sterlingdorminey
 */
public class WhenAllWorkerTest {
    private WhenAllWorker whenAll;
    private MockWorker worker1;
    private MockWorker worker2;
    
    public WhenAllWorkerTest() {
        this.whenAll = new WhenAllWorker();
        this.worker1 = new MockWorker();
        this.worker2 = new MockWorker();
        this.whenAll.addWorker(this.worker1);
        this.whenAll.addWorker(this.worker2);
    }
    
    @Before
    public void setUp() {
        this.worker1.reset();
        this.worker2.reset();
        this.worker1.setExecState(ExecState.NOT_DONE);
        this.worker2.setExecState(ExecState.NOT_DONE);
    }
    
    @Test
    public void testWorkerIsFinished() {
        
        this.whenAll.init();
        this.whenAll.exec();
        
        assertEquals(ExecState.NOT_DONE, this.whenAll.isFinished());
        
        this.worker1.setExecState(ExecState.FAILURE);
        assertEquals(ExecState.FAILURE, this.whenAll.isFinished());
        
        this.worker2.setExecState(ExecState.SUCCESS);
        assertEquals(ExecState.FAILURE, this.whenAll.isFinished());
        
        this.worker1.setExecState(ExecState.SUCCESS);
        assertEquals(ExecState.SUCCESS, this.whenAll.isFinished());
    }
    
    @Test
    public void testWorkDoneAfterSuccess() {
        this.worker1.reset();
        this.worker2.reset();
        
        this.whenAll.init();
        this.whenAll.exec();
        
        
        assertEquals(1, this.worker1.getTimesExecuted());
        assertEquals(1, this.worker2.getTimesExecuted());
        
        this.worker2.setExecState(ExecState.SUCCESS);

        this.whenAll.exec();
        
        assertEquals(2, this.worker1.getTimesExecuted());
        assertEquals(1, this.worker2.getTimesExecuted());
        
        this.worker1.setExecState(ExecState.SUCCESS);
        
        this.whenAll.exec();
        
        assertEquals(2, this.worker1.getTimesExecuted());
        assertEquals(1, this.worker2.getTimesExecuted());
    }
    
    @Test
    public void testFailureStopsWorker() {
        this.whenAll.init();
        this.whenAll.exec();
        
        assertEquals(1, this.worker1.getTimesExecuted());
        assertEquals(1, this.worker2.getTimesExecuted());
        
        this.worker1.setExecState(ExecState.FAILURE);
        
        assertEquals(1, this.worker1.getTimesExecuted());
        assertEquals(1, this.worker2.getTimesExecuted());        
    }
}
