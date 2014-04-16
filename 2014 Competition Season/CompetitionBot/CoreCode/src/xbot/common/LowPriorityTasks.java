/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

/**
 * These tasks need to be Fed occasionally by the Robot.
 * @author John
 */
public class LowPriorityTasks {
    
    private int iteration;
    
    /**
     *
     */
    public LowPriorityTasks()
    {
        iteration = 1;
    }
    
    /**
     * This method needs to be called every (or nearly every) robot cycle. It consumes
     * logs and telemetry, and periodically saves properties to permanent storage.
     */
    public void Feed()
    {
        iteration++;
        CommonTools.Get().logConsumer.Consume();
        CommonTools.Get().telemetryConsumer.Consume();
    }
}
