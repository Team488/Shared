/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers.DriveWorkers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.aerialassist.drive.DriveTankWithJoysticksWorker;
import xbot.test.autonomous.Helpers;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class TankWheelsDriveFieldOrientedTest extends BaseTest {
    
    DriveTankWithJoysticksWorker worker;
    
    public TankWheelsDriveFieldOrientedTest() {
        
    }
    
    @Before
    public void setUp() {
        worker = new DriveTankWithJoysticksWorker();
        context.GetDriveCore().tankFieldOriented.set(true);
        worker.init();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testRobotRotatedDriveStraight() {
        Helpers.setRobotHeading(0);
        this.setJoysticks(1, 0);
        
        worker.exec();
        
        AssertDriveExpected(1.0, 1.0);
        
    }
    
    @Test
    public void testRobotTurnRight() {
        this.setJoysticks(0, 1);
        Helpers.setRobotHeading(179);
        
        worker.exec();
        AssertDrivePolarity(true, true, false, false);
    }
    
    @Test
    public void testRobotTurnLeft() {
        this.setJoysticks(0, 1);
        Helpers.setRobotHeading(181);
        
        worker.exec();
        AssertDrivePolarity(false, false, true, true);
    }
    
    @Test
    public void testRobotStraight() {
        this.setJoysticks(0, 1);
        Helpers.setRobotHeading(90);
        worker.exec();
        this.AssertDriveExpected(1, 1, 1, 1);
    }
    
    @Test
    public void testRotateCorrectly() {
        this.setJoysticks(1, -0.1);
        Helpers.setRobotHeading(90);
        worker.exec();
        // Should spend time turning.
        this.AssertDrivePolarity(false, false, true, true);
    }
    
    @Test
    public void testTranslateBackwards() {
        this.setJoysticks(0.1, -1);
        Helpers.setRobotHeading(90);
        worker.exec();
        // Should use all wheels to translate backwards.
        this.AssertDrivePolarity(false, false, false, false);
    }
    
    private void setJoysticks(double x, double y) {
        context.GetOICore().setLeftJoyX(x);
        context.GetOICore().setLeftJoyY(y);
    }
    
    private void AssertDrivePolarity(boolean leftFrontPos, boolean leftRearPos, boolean rightFrontPos, boolean rightRearPos) {
        assertEquals("Motor impetus LF", leftFrontPos, context.GetDriveCore().getLeftMotorFront() > 0);
        assertEquals("Motor impetus LR", leftRearPos, context.GetDriveCore().getLeftMotorRear() > 0);
        assertEquals("Motor impetus RF", rightFrontPos, context.GetDriveCore().getRightMotorFront() > 0);
        assertEquals("Motor impetus RR", rightRearPos, context.GetDriveCore().getRightMotorRear() > 0);
    }
    
    private void AssertDriveExpected(double leftFront, double leftRear, double rightFront, double rightRear) {
        assertEquals("Motor impetus LF", leftFront, context.GetDriveCore().getLeftMotorFront(), 0.01);
        assertEquals("Motor impetus LR", leftRear, context.GetDriveCore().getLeftMotorRear(), 0.01);
        assertEquals("Motor impetus RF", rightFront, context.GetDriveCore().getRightMotorFront(), 0.01);
        assertEquals("Motor impetus RR", rightRear, context.GetDriveCore().getRightMotorRear(), 0.01);
    }
    
    private void AssertDriveExpected(double Left, double Right)
    {
        this.AssertDriveExpected(Left, Left, Right, Right);
    }
}