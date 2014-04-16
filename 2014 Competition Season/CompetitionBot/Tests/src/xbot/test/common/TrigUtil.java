/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.common;

import xbot.common.math.ITrigUtil;

/**
 *
 * @author aschokk
 */
public class TrigUtil implements ITrigUtil {

    @Override
    public double acos(double r) {
        return Math.acos(r);
    }

    @Override
    public double atan(double r) {
        return Math.atan(r);
    }
    
    @Override
    public double atan2(double y, double x)
    {
        return Math.atan2(y, x);
    }

    @Override
    public double asin(double r) {
        return Math.asin(r);
        
    }
    
    @Override
    public double pow(double x, double y)
    {
        return Math.pow(x, y);
    }
    
}
