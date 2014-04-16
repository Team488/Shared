/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.systems;

import xbot.aerialassist.RobotContext;
import xbot.common.Loggable;

/**
 *
 * @author Alex
 */
public class LightsCore extends Loggable {
    OICore oi;

        private boolean isAskingForBall;

    /**
     * Get the value of isAskingForBall
     *
     * @return the value of isAskingForBall
     */
    public boolean isIsAskingForBall() {
        return isAskingForBall;
    }

    /**
     * Set the value of isAskingForBall
     *
     * @param isAskingForBall new value of isAskingForBall
     */
    public void setIsAskingForBall(boolean isAskingForBall) {
        this.isAskingForBall = isAskingForBall;
    }

    
    private boolean arduino1;

    /**
     * Get the value of arduino1
     *
     * @return the value of arduino1
     */
    public boolean isArduino1() {
        return arduino1;
    }

    /**
     * Set the value of arduino1
     *
     * @param arduino1 new value of arduino1
     */
    public void setArduino1(boolean arduino1) {
        this.arduino1 = arduino1;
    }

    private boolean arduino2;

    /**
     * Get the value of arduino2
     *
     * @return the value of arduino2
     */
    public boolean isArduino2() {
        return arduino2;
    }

    /**
     * Set the value of arduino2
     *
     * @param arduino2 new value of arduino2
     */
    public void setArduino2(boolean arduino2) {
        this.arduino2 = arduino2;
    }

    private boolean arduino3;

    /**
     * Get the value of arduino3
     *
     * @return the value of arduino3
     */
    public boolean isArduino3() {
        return arduino3;
    }

    /**
     * Set the value of arduino3
     *
     * @param arduino3 new value of arduino3
     */
    public void setArduino3(boolean arduino3) {
        this.arduino3 = arduino3;
    }

    
    
    public LightsCore() {
        super("LightsCore");
    }
    
    public void updateLights() {
        oi = RobotContext.Get().GetOICore();
        
        setArduino1(oi.isRedAlliance());
        
        int state = 0; // normal
        if(RobotContext.Get().GetTelemetryCore().isRobotDisabled()) {
            state = 1;
        } else if(this.isAskingForBall) {
            state = 2;
        } else if(RobotContext.Get().getCollectionCore().getFrontCollector().getIsManual() ||
                RobotContext.Get().getCollectionCore().getBackCollector().getIsManual()) {
            state = 3;
        }
        
        // 3 mod 2  = 1 ... 1 mod 2 = 1 ... 2 mod 2 = 0
        
        // 3/2 mod 2 = 1.5  ... 1/2 mod 2 = .5 ... 2/2 mod 2 = 1
        
        setArduino2(state % 2 == 1); // arm manual and robot disabled
        setArduino3((state / 2) % 2 == 1); // asking for ball
    }
}
