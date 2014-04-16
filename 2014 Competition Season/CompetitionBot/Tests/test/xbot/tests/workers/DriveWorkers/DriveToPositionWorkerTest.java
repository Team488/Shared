/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers.DriveWorkers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.aerialassist.drive.DriveToPositionWorker;
import xbot.common.ExecState;
import xbot.common.math.XYPair;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class DriveToPositionWorkerTest extends BaseTest {
    
    DriveToPositionWorker worker;
    
    public DriveToPositionWorkerTest() {
    }
    
    @Before
    public void setUp() {
        worker = new DriveToPositionWorker();
    }
    
    @Test
    public void testNotMoving() {
        assertDriveExpected(0,0,0,0);
        worker.init();
        worker.exec();
        assertDriveExpected(0,0,0,0);
    }
    
    @Test
    public void testMaxForward() {
        worker.setSetpoint(new XYPair(0, 100));
        
        context.GetSensorCore().DepositLocationInformationFromRobot(-90, 0, 0, 0);
        
        worker.init();
        worker.exec();
        assertDriveExpected(1, 1, 1, 1);
    }
    
    @Test
    public void testNotAtGoal() {
        XYPair goal = new XYPair(-10, 100);
        worker.setSetpoint(goal);
        worker.init();
        worker.exec();
        assertTrue(worker.isFinished() == ExecState.NOT_DONE);
    }
    
    @Test
    public void testAtGoal() throws InterruptedException {
        XYPair goal = new XYPair(-10, 100);
        worker.setSetpoint(goal);
        worker.init();
        worker.exec();
        
        // simulate the robot has now moved successfully to the final position
        context.GetSensorCore().odometry.TESTONLY_setManualPosition(goal.X, goal.Y);
        
        worker.exec();
        
        assertDriveExpected(0,0,0,0);
        
        assertTrue(worker.isFinished() == ExecState.NOT_DONE);
        
        // wait for on target logic to kick in
        mockTime.incrementTime(501);
        
        assertTrue(worker.isFinished() == ExecState.SUCCESS);
        
    }
    
    private void assertDriveExpected(double LeftFront, double LeftRear, double RightFront, double RightRear)
    {
        assertEquals("Motor impetus LF", LeftFront, context.GetDriveCore().getLeftMotorFront(), 0.001);
        assertEquals("Motor impetus LR", LeftRear, context.GetDriveCore().getLeftMotorRear(), 0.001);
        assertEquals("Motor impetus RF", RightFront, context.GetDriveCore().getRightMotorFront(), 0.001);
        assertEquals("Motor impetus RR", RightRear, context.GetDriveCore().getRightMotorRear(), 0.001);
    }
}