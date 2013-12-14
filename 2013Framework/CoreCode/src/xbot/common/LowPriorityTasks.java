/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import xbot.common.logging.LogConsumer;
import java.util.Vector;

/**
 *
 * @author John
 */
public class LowPriorityTasks {
    
    public Vector LoggingQueue;
    private LogConsumer logConsumer;
    private int iteration;
    
    public LowPriorityTasks()
    {
        LoggingQueue = new Vector();
    }
    
    public void Feed()
    {
        iteration++;
        CommonTools.Get().logConsumer.Consume();
        
        if (iteration % 100 == 0)
        {
            CommonTools.Get().propertyManager.saveOutAllProperties();
        }
    }
}
