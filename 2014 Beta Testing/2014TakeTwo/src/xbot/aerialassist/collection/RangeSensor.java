/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.collection;

import xbot.common.CommonTools;
import xbot.common.CommonUtils;
import xbot.common.Loggable;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author John
 */
public class RangeSensor extends Loggable {
    
    private double voltage;
    
    private DoubleProperty distanceProp;
    
    public RangeSensor(String position)
    {
        super(position+"RangeSensor");
        distanceProp = new DoubleProperty(position+"Distance", 0.0);
    }
    
    double lastTime = -10000;
    
    public void SetVoltage(double voltage)
    {
        this.voltage = voltage;
        
        if (CommonTools.Get().time.GetElapsedMSecFromMarker(lastTime) > 5000)
        {
            distanceProp.set(GetRange());
            lastTime = CommonTools.Get().time.GetMarker();
        }        
    }
    
    /**
     * If the voltage is too small (near 0) then it will return 500 distance as a safety measure.
     * @return Range in inches
     */
    public double GetRange()
    { 
        if (!CommonUtils.InRange(voltage, 0.1, -0.1))
        {
            double cmDistance = 25.75 * Math.pow(voltage, -1.289);
            return cmDistance * 0.393701;
        }
        return 500;
    }
}
