/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.test.common;

import xbot.common.Time;

/**
 *
 * @author Alex
 */
public class MockTime extends Time {
    private long msFromStart;
    public void setMSFromStart(long msFromStart) {
        this.msFromStart = msFromStart;
    }
    
    public void incrementTime(long deltaInMS) {
        this.msFromStart += deltaInMS;
    }
    
    protected double getMSFromStart() {
        return msFromStart;
    }
}
