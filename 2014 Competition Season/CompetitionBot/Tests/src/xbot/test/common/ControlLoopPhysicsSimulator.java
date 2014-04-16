/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.test.common;

/**
 *
 * @author Alex
 */
public class ControlLoopPhysicsSimulator {

    public static double update(double pidWrite, double pidGet,
            double dragFactor, double powerFactor, double elapsedMS) {
        // slowdown output by dragFactor
        pidGet *= (dragFactor); 
        
        // accelerate by currentOutput amount;
        pidGet += pidWrite * powerFactor * elapsedMS;
        
        return pidGet;
    }
}
