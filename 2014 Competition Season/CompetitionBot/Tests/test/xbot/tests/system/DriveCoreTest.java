/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.aerialassist.systems.DriveCore;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class DriveCoreTest extends BaseTest {
    DriveCore drive;
    public DriveCoreTest() {
        drive = context.GetDriveCore();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test 
    public void arcadeStraightForward() {
        drive.arcadeDrive(1.0, 0);
        AssertDriveExpected(1, 1, 1, 1);
    }
    
    @Test 
    public void arcadeHalfForward() {
        drive.arcadeDrive(0.5, 0);
        AssertDriveExpected(0.5, 0.5, 0.5, 0.5);
    }
    
    @Test 
    public void arcadeStraightBackward() {
        drive.arcadeDrive(-1.0, 0);
        AssertDriveExpected(-1, -1, -1, -1);
    }
    
    @Test 
    public void arcadeFullTurnRight() {
        drive.arcadeDrive(0, -1.0);
        AssertDriveExpected(1, 1, -1, -1);
    }
    
    private void AssertDriveExpected(double LeftFront, double LeftRear, double RightFront, double RightRear)
    {
        assertEquals("Motor impetus LF", LeftFront, context.GetDriveCore().getLeftMotorFront(), 0.001);
        assertEquals("Motor impetus LR", LeftRear, context.GetDriveCore().getLeftMotorRear(), 0.001);
        assertEquals("Motor impetus RF", RightFront, context.GetDriveCore().getRightMotorFront(), 0.001);
        assertEquals("Motor impetus RR", RightRear, context.GetDriveCore().getRightMotorRear(), 0.001);
    }
}