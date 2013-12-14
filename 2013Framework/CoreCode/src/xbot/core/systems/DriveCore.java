/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.core.systems;

/**
 *
 * @author Alex
 */
public class DriveCore {
    
    private double leftMotor1;

    /**
     * Get the value of LeftMotor1
     *
     * @return the value of LeftMotor1
     */
    public double getLeftMotor1() {
        return leftMotor1;
    }
    
    private double leftMotor2;

    /**
     * Get the value of leftMotor2
     *
     * @return the value of leftMotor2
     */
    public double getLeftMotor2() {
        return leftMotor2;
    }


    private double rightMotor1;

    /**
     * Get the value of rightMotor1
     *
     * @return the value of rightMotor1
     */
    public double getRightMotor1() {
        return rightMotor1;
    }

    private double rightMotor2;

    /**
     * Get the value of rightMotor2
     *
     * @return the value of rightMotor2
     */
    public double getRightMotor2() {
        return rightMotor2;
    }

    public void tankDrive(double left, double right) {
        // TODO: need to do some bounds checking and the life
        leftMotor1 = left;
        leftMotor2 = left;
        
        rightMotor1 = right;
        rightMotor2 = right;
    }

}
