/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers.DriveWorkers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.aerialassist.drive.OrientForSpeedWorker;
import xbot.aerialassist.drive.SetDesiredHeadingWorker;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class OrientForSpeedWorkerTest extends BaseTest {
    
    OrientForSpeedWorker worker;
    
    public OrientForSpeedWorkerTest() {
        
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void TestBasics() {
        worker = new OrientForSpeedWorker();
        setJoystick(0, 1);
        
        worker.init();
        worker.exec();
        
        assertEquals("Value should be forward", 90, context.GetDriveCore().GetDesiredHeading(), 0.001);
        
        setJoystick(1, 0);
        
        worker.exec();
        
        assertEquals("Value should be unchanged", 180, context.GetDriveCore().GetDesiredHeading(), 0.001);
    }
    
    @Test
    public void TestInvertedDirections()
    {
        worker = new OrientForSpeedWorker();
        setJoystick(1, -1);
        
        //Robot is now in "default" configuration
        context.GetSensorCore().DepositLocationInformationFromRobot(-90, 0, 0, 0);
        
        worker.init();
        worker.exec();
        
        assertEquals("Value should be forward-left", 135, context.GetDriveCore().GetDesiredHeading(), 0.001);
        
        setJoystick(1, 0);
        
        worker.exec();
        
        assertEquals("Value should be unchanged", 0, context.GetDriveCore().GetDesiredHeading(), 0.001);
    }
    
    @Test
    public void TestNegatives()
    {
        worker = new OrientForSpeedWorker();
        setJoystick(0, -1);
        
        worker.init();
        worker.exec();
        assertEquals("Value should be down", 270, context.GetDriveCore().GetDesiredHeading(), 0.001);
        
        setJoystick(-1, 0);
        worker.exec();
        assertEquals("Value should be left", 180, context.GetDriveCore().GetDesiredHeading(), 0.001);
        
        setJoystick(-1, -1);
        worker.exec();
        assertEquals("Value should be down & left", 225, context.GetDriveCore().GetDesiredHeading(), 0.001);
    }
    
    @Test
    public void TestZero()
    {
        worker = new OrientForSpeedWorker();
        setJoystick(0, 1);
        
        worker.init();
        worker.exec();
        
        assertEquals("Value should be up", 90, context.GetDriveCore().GetDesiredHeading(), 0.001);
        
        setJoystick(0, 0);
        worker.exec();
        
        
        assertEquals("Value should be old value", 90, context.GetDriveCore().GetDesiredHeading(), 0.001);
        
    }
    
    private void setJoystick(double x, double y)
    {
        context.GetOICore().setLeftJoyX(x);
        context.GetOICore().setLeftJoyY(y);
    }
    
}