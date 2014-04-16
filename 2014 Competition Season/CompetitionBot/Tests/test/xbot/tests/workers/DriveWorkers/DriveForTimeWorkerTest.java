/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.tests.workers.DriveWorkers;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.drive.DriveForTimeWorker;
import xbot.test.common.BaseTest;
import xbot.common.ExecState;

/**
 *
 * @author X-Bot(Noobies)
 */
public class DriveForTimeWorkerTest extends BaseTest {
    DriveForTimeWorker worker;
    
    public DriveForTimeWorkerTest() {
        double x;
        double y;
        double timeOutInMs;
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
         RobotContext.ResetRobotContextForTestingPurposesOnly();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void testGoingForward() throws InterruptedException
     {
        // Create a DriveForTimeWorker
        context.GetSensorCore().DepositLocationInformationFromRobot(-90, 0, 0, 0);
         
        DriveForTimeWorker w = new DriveForTimeWorker(0, 1, 1000);
  
        // Check that the drive is doing nothing
        assertEquals("Drive is stopped", 0, RobotContext.Get().GetDriveCore().getLeftMotorFront(), 0.001);
        
        // Run our worker
        w.init();

        w.exec();
        
        // Check that the drive is doing something
        assertEquals("Drive is going", 1, RobotContext.Get().GetDriveCore().getLeftMotorFront(), 0.001);
        
        // Wait for a little bit 
        mockTime.incrementTime(2000);
        w.exec();
        // Check that the drive is not doing anything
        assertEquals("Drive is stopped", ExecState.SUCCESS, w.isFinished());
    }
}
