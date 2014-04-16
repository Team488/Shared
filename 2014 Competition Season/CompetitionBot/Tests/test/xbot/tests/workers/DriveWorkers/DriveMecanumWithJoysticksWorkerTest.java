/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers.DriveWorkers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.drive.DriveMecanumWithJoysticksWorker;
import xbot.common.logging.LogConsumer;
import xbot.common.logging.LogProducer;
import xbot.test.common.BaseTest;
import xbot.test.common.StringLogWriter;

/**
 *
 * @author Alex
 */
public class DriveMecanumWithJoysticksWorkerTest extends BaseTest {
    DriveMecanumWithJoysticksWorker worker;
    
    LogConsumer logConsumer;
    LogProducer logProducer;
    StringLogWriter writer;
    
    
    public DriveMecanumWithJoysticksWorkerTest() {
    }
    
    @Before
    public void setUp() {
        worker = new DriveMecanumWithJoysticksWorker();
        worker.init();
        writer = new StringLogWriter("Log");
        logConsumer = new LogConsumer(writer, LogProducer.LOGGING);
        logProducer = new LogProducer("test", LogProducer.LOGGING);
    
    }

        
    @Test
    public void zerodOutTest() {
        context.GetOICore().setLeftJoyY(0);
        context.GetOICore().setLeftJoyX(0);
        context.GetOICore().setRightJoyX(0);
        
        worker.exec();
        AssertDriveExpected(0, 0, 0, 0);
        logConsumer.Consume();
        
        worker.exec();
        AssertDriveExpected(0, 0, 0, 0);
        logConsumer.Consume();
    }
    
    @Test
    public void maxForwardTest() {
        context.GetOICore().setLeftJoyY(1.0);
        context.GetOICore().setLeftJoyX(0);
        
        context.GetSensorCore().DepositLocationInformationFromRobot(-90, 0, 0, 0);
        
        worker.exec();
        AssertDriveExpected(1, 1, 1, 1);
        logConsumer.Consume();
    }
    
    @Test
    public void maxTranslateRightTest()
    {
        context.GetOICore().setLeftJoyY(0);
        context.GetOICore().setLeftJoyX(1);
        context.GetOICore().setRightJoyX(0);
        
        context.GetSensorCore().DepositLocationInformationFromRobot(-90, 0, 0, 0);
        
        worker.exec();
        
        
        
        AssertDriveExpected(1, -1, -1, 1);
        logConsumer.Consume();
    }
    
    @Test
    public void maxRightRotationTest() {
        context.GetOICore().setLeftJoyY(0);
        context.GetOICore().setLeftJoyX(0);
        context.GetOICore().setRightJoyX(-1.0);
        
        worker.exec();
        
        AssertDriveExpected(1, 1, -1, -1);
        logConsumer.Consume();
    }
    
    @Test
    public void fieldSenseTest_45deg() {
        context.GetSensorCore().resetHeading();
        context.GetOICore().setLeftJoyY(1.0);
        context.GetOICore().setLeftJoyX(0.0);
        context.GetOICore().setRightJoyX(0.0);
        
        context.GetSensorCore().setGyroAngles(45, false);        
        worker.exec();
        AssertDriveExpected(1,0,0,1);
        logConsumer.Consume();
        
        context.GetSensorCore().setGyroAngles(90, false);
        assertEquals ("Rotated 90 degrees", 180, context.GetSensorCore().getCurrentHeading(), 0.001);
        logConsumer.Consume();
        
        worker.exec();
        AssertDriveExpected(1,-1,-1,1);
        logConsumer.Consume();
        
        context.GetSensorCore().setGyroAngles(135, false);        
        worker.exec();
        AssertDriveExpected(0,-1,-1,0);
        logConsumer.Consume();
        
        
        context.GetSensorCore().setGyroAngles(180, false);
        worker.exec();
        AssertDriveExpected(-1,-1,-1,-1);
        logConsumer.Consume();
        
        context.GetSensorCore().setGyroAngles(225, false);        
        worker.exec();
        AssertDriveExpected(-1,0,0,-1);
        logConsumer.Consume();
        
        context.GetSensorCore().setGyroAngles(270, false);
        worker.exec();
        AssertDriveExpected(-1,1,1,-1);
        logConsumer.Consume();
        
        context.GetSensorCore().setGyroAngles(315, false);        
        worker.exec();
        AssertDriveExpected(0,1,1,0);
        logConsumer.Consume();
        
        context.GetSensorCore().setGyroAngles(360, false);
        worker.exec();
        AssertDriveExpected(1,1,1,1);
        logConsumer.Consume();
    }
    
    @Test
    public void driveShiftTest(){
        context.GetDriveCore().getIsMecanum();
        logConsumer.Consume();
       
        worker.exec();
        assertTrue("Is robot running Mecanum Drive?", true);
        assertEquals(context.GetDriveCore().getIsMecanum(), true);
        logConsumer.Consume();
    }
    
    @Test
    public void rotateRightTest()
    {
        context.GetOICore().setLeftJoyY(0);
        context.GetOICore().setLeftJoyX(0);
        context.GetOICore().setRightJoyX(-1);
        
        worker.exec();
        AssertDriveExpected(1, 1, -1, -1);
    }
    
    @Test
    public void rotateRightThenStopTest()
    {
        context.GetOICore().setLeftJoyY(0);
        context.GetOICore().setLeftJoyX(0);
        context.GetOICore().setRightJoyX(-1);
        
        worker.exec();
        AssertDriveExpected(1, 1, -1, -1);
        
        context.GetSensorCore().DepositLocationInformationFromRobot(-90, 0, 0, 0);
        assertEquals("Heading is 90", 90, context.GetSensorCore().getCurrentHeading(), 0.001);
        
        worker.exec();
        AssertDriveExpected(1, 1, -1, -1);
        
        //stop the joysticks;
        context.GetOICore().setLeftJoyY(1);
        context.GetOICore().setLeftJoyX(0);
        context.GetOICore().setRightJoyX(0);
        worker.exec();
        // wait a bit
        
        mockTime.incrementTime(100);
        worker.exec();
        AssertDriveExpected(1, 1, 1, 1);
        
        mockTime.incrementTime(2000);
        worker.exec();
        AssertDriveExpected(1, 1, 1, 1);
        
        // stop the Y joystick
        context.GetOICore().setLeftJoyY(0);
        
        //now, we force the robot to rotate back to 180 (left, positive rotation);
        // This should cause the robot to attempt to turn right (negative rotation);
        context.GetSensorCore().DepositLocationInformationFromRobot(0, 0, 0, 0);
        assertEquals("Heading is 180", 180, context.GetSensorCore().getCurrentHeading(), 0.001);
        
        worker.exec();
        
        assertTrue("Left Front should be > 0", context.GetDriveCore().getLeftMotorFront() > 0);
        assertTrue("Left Rear should be > 0", context.GetDriveCore().getLeftMotorRear() > 0);
        assertTrue("Right Front should be < 0", context.GetDriveCore().getRightMotorFront() < 0);
        assertTrue("Right rear should be < 0", context.GetDriveCore().getRightMotorRear() < 0);
        
        
    }
    
    @Test
    public void switchFieldRobotRelativeTest()
    {
        context.GetOICore().setLeftJoyY(1.0);
        context.GetOICore().setLeftJoyX(0);
        context.GetOICore().setRightJoyX(0);
        
        context.GetSensorCore().DepositLocationInformationFromRobot(0, 0, 0, 0);
        // drive a little while field-oriented
        worker.exec();
        AssertDriveExpected(1, -1, -1, 1);
        logConsumer.Consume();
        
        // switch to robot oriented
        context.GetDriveCore().mecanumFieldOriented.set(false);
        logConsumer.Consume();
        worker.exec();
        AssertDriveExpected(1, 1, 1, 1); // <-- Error
        logConsumer.Consume();
        
        // switch back
        context.GetDriveCore().mecanumFieldOriented.set(true);
        logConsumer.Consume();
        worker.exec();
        AssertDriveExpected(1, -1, -1, 1);
        logConsumer.Consume();
        
    }
    
    private void AssertDriveExpected(double LeftFront, double LeftRear, double RightFront, double RightRear)
    {
        assertEquals("Motor impetus LF", LeftFront, context.GetDriveCore().getLeftMotorFront(), 0.001);
        assertEquals("Motor impetus LR", LeftRear, context.GetDriveCore().getLeftMotorRear(), 0.001);
        assertEquals("Motor impetus RF", RightFront, context.GetDriveCore().getRightMotorFront(), 0.001);
        assertEquals("Motor impetus RR", RightRear, context.GetDriveCore().getRightMotorRear(), 0.001);
        logConsumer.Consume();
        
    }
    
    

}