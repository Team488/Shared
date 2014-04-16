/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import java.util.Date;
import xbot.common.properties.StringProperty;

/**
 * This is the Time system for CommonTools.
 * @author Alex
 */     
public class Time {
    
    protected double startTime;
    protected StringProperty timeStamp;
    
    /**
     *
     */
    public Time() {
        Date date = new Date();
        startTime = date.getTime();
    }
    
    /**
     * Sets up the timeStamp property.
     */
    public void initialize()
    {
        timeStamp = new StringProperty("DSTimestamp", "[NoTime]");
    }
    
    /**
     * Gets a string representing real time from the Driver Station (presumably from Network Tables).
     * @return
     */
    public String getRealTime()
    {
        return timeStamp.get();
    }
    
    protected double getMSFromStart() {
        Date date = new Date();
        return date.getTime() - startTime;
        
    }
    
    /**
     * Returns the number of milliseconds since robot start. We call this a "marker" and don't typically perform
     * any operations on the number itself.
     * @return
     */
    public double GetMarker() {
        
        double msFromStart = getMSFromStart();
        
        // multiply by 100, floor the value, then divide by 100.
        
        msFromStart *= 100;
        int ims = (int)msFromStart;
        ims /= 100;
        
        return (double)ims;
    }

    /**
     * Returns the number of milliseconds that have elapsed since you created the input marker.
     * @param marker a "marker" from GetMarker().
     * @return time (in milliseconds) between now and when the marker was created.
     */
    public double GetElapsedMSecFromMarker(double marker) {
        return getMSFromStart() - marker;
    }
}
