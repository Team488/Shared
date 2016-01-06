package tests.drive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tests.drive.BaseMotionTest.RobotDirection;
import xbot.common.math.XYPair;

public class TiltTest extends BaseMotionTest {

    @Test
    public void tiltForward() {
        
        // Drive backwards
        tranSystem.translatePowerRobotRelative(new XYPair(0, -1));
        assertEquals(RobotDirection.Backward, getRobotRelativeMotion()[0]);
        
        // Set tilt forward
        setTilt(0, -30);
        
        // try and keep driving backward (would cause more tilt maybe)
        tranSystem.translatePowerRobotRelative(new XYPair(0, -1));
        
        // make sure we are in fact driving forward (to stop tilting)
        assertEquals(RobotDirection.Forward, getRobotRelativeMotion()[0]);
    }
    
    @Test
    public void tiltBackward() {
        
        // Drive forward
        tranSystem.translatePowerRobotRelative(new XYPair(0, 1));
        assertEquals(RobotDirection.Forward, getRobotRelativeMotion()[0]);
        
        // Set tilt backward
        setTilt(0, 30);
        
        // try and keep driving forward (would cause more tilt maybe)
        tranSystem.translatePowerRobotRelative(new XYPair(0, 1));
        
        // make sure we are in fact driving backward (to stop tilting)
        assertEquals(RobotDirection.Backward, getRobotRelativeMotion()[0]);
    }
    
    @Test
    public void tiltLeft() {
        
        // Drive right
        tranSystem.translatePowerRobotRelative(new XYPair(1, 0));
        assertEquals(RobotDirection.TranslatingRight, getRobotRelativeMotion()[0]);
        
        // Set tilt left
        setTilt(30, 0);
        
        // try and keep driving right (would cause more tilt maybe)
        tranSystem.translatePowerRobotRelative(new XYPair(1, 0));
        
        // make sure we are in fact driving left (to stop tilting)
        assertEquals(RobotDirection.TranslatingLeft, getRobotRelativeMotion()[0]);
    }
    
    @Test
    public void tiltRight() {
        
        // Drive right
        tranSystem.translatePowerRobotRelative(new XYPair(-1, 0));
        assertEquals(RobotDirection.TranslatingLeft, getRobotRelativeMotion()[0]);
        
        // Set tilt right
        setTilt(-30, 0);
        
        // try and keep driving right (would cause more tilt maybe)
        tranSystem.translatePowerRobotRelative(new XYPair(-1, 0));
        
        // make sure we are in fact driving left (to stop tilting)
        assertEquals(RobotDirection.TranslatingRight, getRobotRelativeMotion()[0]);
    }
}
