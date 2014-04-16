/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.systems;

import java.util.Vector;
import xbot.aerialassist.telemetry.*;
import xbot.common.CommonUtils;
import xbot.common.Loggable;
import xbot.common.logging.LogProducer;
import xbot.common.properties.BooleanProperty;

/**
 * The telemetry system contains individual TelemetryItems. Each second, the robot 
 * saves some telemetry on the machine for future analysis. Example items include
 * Gyros, limit switches, and so on.
 * @author John
 */
public class TelemetryCore extends Loggable {
    
    private LogProducer telemetryProducer;
    boolean hasSentFirstMessage;
    private Vector telemetryItems;
    
    private BooleanProperty isDisabled = new BooleanProperty("RobotIsDisabled", true);
        
    /**
     *
     */
    public TelemetryCore()
    {
        super("TelemetryCore");
        telemetryProducer = new LogProducer("Telemetry", LogProducer.TELEMETRY);
        hasSentFirstMessage = false;
        telemetryItems = new Vector();
        AddTelemetryItemsToVector();
    }
    
    public void setIsDisabled(boolean isDisabled)
    {
        this.isDisabled.set(isDisabled);
    }
    
    public boolean isRobotDisabled()
    {
        return isDisabled.get();
    }
    
    private void AddTelemetryItemsToVector()
    {
        telemetryItems.addElement(new GyroItem());
        telemetryItems.addElement(new DSItem());
        telemetryItems.addElement(new TestItem());
    }
    
    /**
     * Gathers and Posts telemetry data to a logger.
     */
    public void ReportTelemetry()
    {
        GatherTelemetryData();
        SendTelemetryData();
        hasSentFirstMessage = true;
    }
    
    private void SendTelemetryData()
    {
        StringBuffer builder = new StringBuffer();
        
        for (int i = 0; i < telemetryItems.size(); i++)
        {
            ITelemetryItem item = (ITelemetryItem)telemetryItems.elementAt(i);
            
            builder.append(item.GetName());
            builder.append(":");
            builder.append(item.GetData());
            
            // Don't want to add a delimiter if it's the last item in the series
            if (i < telemetryItems.size() - 1)
            {
                builder.append(CommonUtils.GetPropertyDelimiter());
            }
        }
        
        telemetryProducer.Log(LogProducer.DATA, builder.toString());
    }
    
    private void GatherTelemetryData()
    {
        for (int i = 0; i < telemetryItems.size(); i++)
        {
            ((ITelemetryItem)telemetryItems.elementAt(i)).GatherData();
        }
    }
}
