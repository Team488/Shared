/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.common.ExecState;
import xbot.common.TimeOutWorker;
import xbot.common.WorkerBase;
import xbot.test.common.BaseTest;
import xbot.test.common.TestableGenericWorker;

/**
 *
 * @author Alex
 */
public class TimeOutWorkerTest extends BaseTest {
    TestableGenericWorker child;
    TimeOutWorker worker;
    
    
    
    public TimeOutWorkerTest() {
    }
    
    @Before
    public void setUp() {
        child = new TestableGenericWorker();
        worker = new TimeOutWorker(child, 200, true);
        worker.init();
    }
    
    @Test
    public void callsChildMethods() {
        assertEquals(child.initCallCount, 1);
        worker.exec();
        assertEquals(child.execCallCount, 1);
    }
    
    @Test
    public void testNotTimedOutYet() {
        assertTrue("verify timed out meant success", worker.isFinished() == ExecState.NOT_DONE);
    }
    
    @Test
    public void testNotImpedingChildState() {
        child.setFinishedState(ExecState.SUCCESS);
        assertTrue("verify not impeding child state", worker.isFinished() == ExecState.SUCCESS);
    }
    
    @Test
    public void testTimingOutSuccess() throws InterruptedException {
        mockTime.incrementTime(201);
        assertTrue("verify timed out meant success", worker.isFinished() == ExecState.SUCCESS);
    }
    
    @Test
    public void testTimingOutFailure() throws InterruptedException {
        worker = new TimeOutWorker(child, 200, false);
        worker.init();
        mockTime.incrementTime(201);
        assertTrue("verify timed out meant success", worker.isFinished() == ExecState.FAILURE);
    }
}
