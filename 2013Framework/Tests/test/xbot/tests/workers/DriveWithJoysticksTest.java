/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.core.RobotContext;
import xbot.core.workers.DriveWithJoysticksWorker;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class DriveWithJoysticksTest extends BaseTest {
    RobotContext context;
    DriveWithJoysticksWorker worker;
    
    
    @Before
    public void setUp() {
        context = RobotContext.Get();
        worker = new DriveWithJoysticksWorker();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void defaultsToZero() {
        context.GetOICore().setLeftJoyY(0);
        context.GetOICore().setRightJoyY(0);
        worker.exec();
        assertEquals("Motor impetus", 0, context.GetDriveCore().getLeftMotor1(), 0.001);
        assertEquals("Motor impetus", 0, context.GetDriveCore().getLeftMotor2(), 0.001);
        assertEquals("Motor impetus", 0, context.GetDriveCore().getRightMotor1(), 0.001);
        assertEquals("Motor impetus", 0, context.GetDriveCore().getRightMotor2(), 0.001); 
    }
    
    @Test
    public void maxForward() {
        context.GetOICore().setLeftJoyY(1.0);
        context.GetOICore().setRightJoyY(1.0);
        worker.exec();
        assertEquals("Motor impetus", 1.0, context.GetDriveCore().getLeftMotor1(), 0.001);
        assertEquals("Motor impetus", 1.0, context.GetDriveCore().getLeftMotor2(), 0.001);
        assertEquals("Motor impetus", 1.0, context.GetDriveCore().getRightMotor1(), 0.001);
        assertEquals("Motor impetus", 1.0, context.GetDriveCore().getRightMotor2(), 0.001); 
    }
    
    @Test
    public void maxLeft() {
        context.GetOICore().setLeftJoyY(1.0);
        context.GetOICore().setRightJoyY(-1.0);
        worker.exec();
        assertEquals("Motor impetus", 1.0, context.GetDriveCore().getLeftMotor1(), 0.001);
        assertEquals("Motor impetus", 1.0, context.GetDriveCore().getLeftMotor2(), 0.001);
        assertEquals("Motor impetus", -1.0, context.GetDriveCore().getRightMotor1(), 0.001);
        assertEquals("Motor impetus", -1.0, context.GetDriveCore().getRightMotor2(), 0.001); 
    }
}