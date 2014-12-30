/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot.wpilibextensions;

import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author John
 */
public class InvertableTalon extends Talon {
    
    private boolean invert;
    
    public InvertableTalon(final int channel, boolean invertMotor)
    {
        super(channel);
        invert = invertMotor;
    }
    
    public void set(double speed) {
        if (invert)
        {
            speed = -speed;
        }
        super.set(speed);
    }
    
}
