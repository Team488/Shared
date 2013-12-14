/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot;

import edu.wpi.first.wpilibj.Counter;

/**
 *
 * @author Alex
 */
public class RPMCounter extends Counter {
    public RPMCounter(int a, int b){
        super(a, b);
    }
    
    public double getRPM() {
        // divide period by 60.0 to get RPM
	double rpm = 60.0 / this.getPeriod();
        return rpm;
    }
    
}
