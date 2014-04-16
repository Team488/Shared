/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.common.actualrobot;

import xbot.common.math.ITrigUtil;

/**
 *
 * @author aschokk
 */
public class TrigUtil implements ITrigUtil {

    public double acos(double d) {
        return com.sun.squawk.util.MathUtils.acos(d);
    }

    public double atan(double d) {
        return com.sun.squawk.util.MathUtils.atan(d);
    }

    public double asin(double d) {
        return com.sun.squawk.util.MathUtils.asin(d);
    }
    
    public double pow(double x, double y)
    {
        return com.sun.squawk.util.MathUtils.pow(x, y);
    }

    public double atan2(double y, double x) {
        return com.sun.squawk.util.MathUtils.atan2(y, x);
    }
    
}
