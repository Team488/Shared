/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.logging;

import xbot.common.CommonTools;
import xbot.common.Loggable;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author Alex
 */
public class Profiler extends Loggable {
    
    Double previousUpdate = null;
    DoubleProperty currentDelay = null;
    
    public Profiler(String name, double timeWarningLimitMS) {
        super("Profiler:" + name);
        currentDelay = new DoubleProperty("Profiler:" + name + ":delay", -1);
    }
    
    public void startProfile() {
        //previousUpdate = new Double(CommonTools.Get().time.GetMarker());
    }
    
    public void stopProfile() {
//        if(previousUpdate != null) {
//            double deltaInMS = CommonTools.Get().time.GetElapsedMSecFromMarker(previousUpdate.doubleValue());
//            currentDelay.set(deltaInMS);
//            // robot normally should be calling this every 20-50ms, warn if longer
//            if(deltaInMS > 75) {
//                Warning("Time between update calls was " + deltaInMS + "ms");
//            } else {
//            }
//        }
    }
}
