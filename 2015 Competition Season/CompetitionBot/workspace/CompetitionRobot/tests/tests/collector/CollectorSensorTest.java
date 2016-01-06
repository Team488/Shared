package tests.collector;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tests.xbot.BaseTestCase;
import xbot.RobotMap;
import xbot.common.controls.MockDistanceSensor;
import xbot.subsystems.ArmWheelSubsystem;

public class CollectorSensorTest extends BaseTestCase {
    MockDistanceSensor leftSensor;
    MockDistanceSensor rightSensor;

    @Before
    public void init() {
        RobotMap map = injector.getInstance(RobotMap.class);
        leftSensor = (MockDistanceSensor) map.frontLeftDistanceSensor;
        rightSensor = (MockDistanceSensor) map.frontRightDistanceSensor;
    }

    @Test
    public void testAngle() {
        ArmWheelSubsystem subsystem = injector
                .getInstance(ArmWheelSubsystem.class);

        setLeft(1);
        setRight(4);
        assertTrue(subsystem.getToteAngle() > 0d);

        setLeft(3);
        setRight(2);
        assertTrue(subsystem.getToteAngle() < 0d);
        
        setRight(5);
        setLeft(1);
        assertEquals(19.9831065, subsystem.getToteAngle(), 0.001);
    }

    @Test
    public void testInPlace() {
        ArmWheelSubsystem subsystem = injector
                .getInstance(ArmWheelSubsystem.class);

        setLeft(5);
        setRight(5);
        assertFalse(subsystem.isToteInPosition());

        setLeft(1);
        setRight(1);
        assertTrue(subsystem.isToteInPosition());
    }
    
    @Test
    public void testValidation() {
        ArmWheelSubsystem subsystem = injector
                .getInstance(ArmWheelSubsystem.class);

        setLeft(5);
        setRight(5);
        assertTrue(subsystem.isToteWithinDetectionRange());
        
        setLeft(20);
        setRight(20);
        assertFalse(subsystem.isToteWithinDetectionRange());
        
        setLeft(0.5);
        setRight(0.5);
        assertFalse(subsystem.isToteWithinDetectionRange());
        
        setLeft(0.5);
        setRight(5);
        assertFalse(subsystem.isToteWithinDetectionRange());
        
        setLeft(5);
        setRight(0.5);
        assertFalse(subsystem.isToteWithinDetectionRange());
        
        setLeft(5);
        setRight(50);
        assertFalse(subsystem.isToteWithinDetectionRange());
        
        setLeft(50);
        setRight(5);
        assertFalse(subsystem.isToteWithinDetectionRange());
    }

    private void setLeft(double val) {
        leftSensor.setDistance(val);
    }

    private void setRight(double val) {
        rightSensor.setDistance(val);
    }
}
