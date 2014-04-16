/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.common.StateMachineWorker;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class StateMachineWorkerTest extends BaseTest {
    StateMachineWorker worker;
    public StateMachineWorkerTest() {
    }
    
    @Before
    public void setUp() {
        worker = new StateMachineWorker("test worker");
    }
    
    @After
    public void tearDown() {
        
    }
    
    @Test
    public void testEmptyMachine() {
        // shouldn't throw any exceptions, just log warnings
        worker.init();
        worker.exec();
        worker.isFinished();
    }
}