/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers.DriveWorkers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.aerialassist.drive.SetDesiredHeadingWorker;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class SetDesiredHeadingWorkerTest extends BaseTest {
    
    SetDesiredHeadingWorker worker;
    
    public SetDesiredHeadingWorkerTest() {
        
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void TestBasics() {
        worker = new SetDesiredHeadingWorker(90);
        
        worker.init();
        worker.exec();
        
        assertEquals("Value should be saved", 90, context.GetDriveCore().GetDesiredHeading(), 0.001);
    }
    
    @Test
    public void TestZero() {
        worker = new SetDesiredHeadingWorker(0);
        
        worker.init();
        worker.exec();
        
        assertEquals("Value should be saved", 0, context.GetDriveCore().GetDesiredHeading(), 0.001);
    }
    
    @Test
    public void TestAbove360() {
        worker = new SetDesiredHeadingWorker(410);
        
        worker.init();
        worker.exec();
        
        assertEquals("Value should be saved", 50, context.GetDriveCore().GetDesiredHeading(), 0.001);
    }
    
    @Test
    public void TestNegative() {
        worker = new SetDesiredHeadingWorker(-60);
        
        worker.init();
        worker.exec();
        
        assertEquals("Value should be saved", 300, context.GetDriveCore().GetDesiredHeading(), 0.001);
    }
}