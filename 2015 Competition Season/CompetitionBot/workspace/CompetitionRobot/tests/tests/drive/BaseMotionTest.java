package tests.drive;

import org.junit.*;

import static org.junit.Assert.*;
import tests.xbot.BaseTestCase;
import xbot.RobotMap;
import xbot.common.math.XYPair;
import xbot.subsystems.DriveSubsystem;
import xbot.subsystems.RotationalSubsystem;
import xbot.subsystems.TranslationalMeasurementSubsystem;
import xbot.subsystems.TranslationalSubsystem;

public class BaseMotionTest extends BaseTestCase {

    protected RotationalSubsystem rotSystem;
    protected TranslationalSubsystem tranSystem;
    protected TranslationalMeasurementSubsystem tranMeasSystem;
    protected DriveSubsystem driveSystem;

    protected RobotMap map;

    double leftFrontPower;
    double leftBackPower;
    double rightFrontPower;
    double rightBackPower;

    @Before
    public void setupTests() {
        map = injector.getInstance(RobotMap.class);
        rotSystem = injector.getInstance(RotationalSubsystem.class);
        tranSystem = injector.getInstance(TranslationalSubsystem.class);
        tranMeasSystem = injector
                .getInstance(TranslationalMeasurementSubsystem.class);
        driveSystem = injector.getInstance(DriveSubsystem.class);
        
        setTilt(0, 0);
        setHeading(0);
    }

    public enum RobotDirection {
        Stopped, Forward, Backward, TurningLeft, TurningRight, TranslatingLeft, TranslatingRight, FightingSelf, MovingButNotRotating, Unknown
    }

    // Helper methods to evaluate speed, rotation, etc
    // First is Translation, second is Rotation
    protected RobotDirection[] getRobotRelativeMotion() {
        return new RobotDirection[] { getRobotTranslation(false),
                getRobotRotation() };
    }

    protected RobotDirection[] getFieldRelativeMotion() {
        return new RobotDirection[] { getRobotTranslation(true),
                getRobotRotation() };
    }

    private void refreshWheelPowers() {
        // * (map.leftFrontDrive.getInverted()? -1d:1d);
        leftFrontPower = mockRobotIO
                .getPWM(this.driveSystem.leftFrontDrive.getChannel());
        // * (map.leftRearDrive.getInverted()? -1d:1d);
        leftBackPower = mockRobotIO.getPWM(
                this.driveSystem.leftRearDrive.getChannel());

        // Note - right motors are inverted, so re-inverting them here
        // * (map.rightFrontDrive.getInverted()? -1d:1d);
        rightFrontPower = mockRobotIO
                .getPWM(this.driveSystem.rightFrontDrive.getChannel());
        // * (map.rightRearDrive.getInverted()? -1d:1d);
        rightBackPower = mockRobotIO
                .getPWM(this.driveSystem.rightRearDrive.getChannel());
    }

    protected RobotDirection getRobotRotation() {
        refreshWheelPowers();

        double leftPower = leftFrontPower + leftBackPower;
        double rightPower = rightFrontPower + rightBackPower;

        double turnPower = rightPower - leftPower;

        double deadband = 0.01;
        if (Math.abs(turnPower) < deadband) {
            // We're not turning. Either going forward, backward, or stopped.

            if ((Math.abs(leftPower) > 0) && (Math.abs(rightPower) > 0)) {
                return RobotDirection.MovingButNotRotating;
            }

            return RobotDirection.Stopped;
        }

        if (turnPower > 0) {
            return RobotDirection.TurningLeft;
        }
        return RobotDirection.TurningRight;
    }

    private XYPair getRobotMotionVector() {
        refreshWheelPowers();

        double factor = Math.sqrt(2) / 2;
        XYPair leftFrontVector = new XYPair(factor * leftFrontPower, factor
                * leftFrontPower);
        XYPair leftBackVector = new XYPair(-factor * leftBackPower, factor
                * leftBackPower);
        XYPair rightFrontVector = new XYPair(-factor * rightFrontPower, factor
                * rightFrontPower);
        XYPair rightBackVector = new XYPair(factor * rightBackPower, factor
                * rightBackPower);

        XYPair summedVectors = leftFrontVector.add(leftBackVector
                .add(rightFrontVector.add(rightBackVector)));

        return summedVectors;
    }

    private XYPair getFieldOrientedMotionVector() {
        XYPair robotRelativeVector = getRobotMotionVector();
        return robotRelativeVector.rotate(getHeading());
    }

    protected RobotDirection analyzeMotionVector(XYPair summedVectors) {
        // check for dangerous conditions
        if ((leftFrontPower > 0) && (leftBackPower < 0)
                && (rightFrontPower > 0) && (rightBackPower < 0)) {
            // Robot is fighting itself!
            return RobotDirection.FightingSelf;
        }

        if ((leftFrontPower < 0) && (leftBackPower > 0)
                && (rightFrontPower < 0) && (rightBackPower > 0)) {
            // Robot is fighting itself!
            return RobotDirection.FightingSelf;
        }

        // Return the dominant robot motion
        double deadband = 0.01;
        if ((Math.abs(summedVectors.x) < deadband)
                && (Math.abs(summedVectors.y) < deadband)) {
            return RobotDirection.Stopped;
        }

        if (Math.abs(summedVectors.x) > Math.abs(summedVectors.y)) {
            if (summedVectors.x > 0) {
                return RobotDirection.TranslatingRight;
            }
            return RobotDirection.TranslatingLeft;
        } else {
            if (summedVectors.y > 0) {
                return RobotDirection.Forward;
            }
            return RobotDirection.Backward;
        }
    }

    protected RobotDirection getRobotTranslation(boolean fieldOriented) {
        XYPair summedVectors = new XYPair();

        if (fieldOriented) {
            summedVectors = getFieldOrientedMotionVector();
        } else {
            summedVectors = getRobotMotionVector();
        }

        return analyzeMotionVector(summedVectors);
    }

    protected double getHeading() {
        return rotSystem.getGyroYaw().getValue();
    }

    protected void setHeading(double heading) {
        mockRobotIO.setGyroHeading(heading);
    }
    
    protected XYPair getTilt() {
        return tranMeasSystem.getTilt();
    }
    
    protected void setTilt(double xTilt, double yTilt) {
        // Since gyro not set straight, we have to do the same in test code
        mockRobotIO.setGyroRoll(-yTilt);
        mockRobotIO.setGyroPitch(xTilt);
    }

    protected void assertXYPairEquals(XYPair first, XYPair second,
            double tolerance) {
        assertEquals(first.x, second.x, tolerance);
        assertEquals(first.y, second.y, tolerance);
    }

    protected void assertXYPairEquals(XYPair first, XYPair second) {
        assertEquals(first.x, second.x, 0.00001);
        assertEquals(first.y, second.y, 0.00001);
    }
}
