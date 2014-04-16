/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot;

import edu.wpi.first.wpilibj.Timer;
import xbot.common.Time;
import xbot.common.properties.StringProperty;

/**
 *
 * @author John
 */
public class RobotTime extends Time {
    
    
    StringProperty timeStamp;
    
    public RobotTime() {
        startTime = Timer.getFPGATimestamp() * 1000;
    }
    
    protected double getMSFromStart() {
        return (Timer.getFPGATimestamp() * 1000) - startTime;
    }    
}
