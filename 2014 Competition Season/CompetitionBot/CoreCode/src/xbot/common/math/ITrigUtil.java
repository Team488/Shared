/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.common.math;

/**
 *
 * @author aschokk
 */
public interface ITrigUtil {
    /**
     *
     * @param r
     * @return
     */
    public double acos(double r);
    /**
     *
     * @param r
     * @return
     */
    public double atan(double r);
    
    public double atan2(double y, double x);
    /**
     *
     * @param r
     * @return
     */
    public double asin(double r);
    
    public double pow(double x, double y);
}
