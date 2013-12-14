/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import java.util.Date;

/**
 *
 * @author Alex
 */
public class Time {
    double startTime;
    public Time() {
        Date date = new Date();
        startTime = date.getTime();
    }
    
    private double getMSFromStart() {
        Date date = new Date();
        return date.getTime() - startTime;
    }
    
    public double GetMarker() {
        return getMSFromStart();
    }

    public double GetElapsedMSecFromMarker(double marker) {
        return getMSFromStart() - marker;
    }
}
