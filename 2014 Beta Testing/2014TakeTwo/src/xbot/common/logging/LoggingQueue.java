/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.logging;

import java.util.Vector;

/**
 * This is a simple queue implementation that wraps a Vector (Java ME doesn't have queues)
 * @author John
 */
public class LoggingQueue {
    private Vector queue;
    
    /**
     *
     */
    public LoggingQueue()
    {
        queue = new Vector();
    }
    
    /**
     * Queue a string to be logged later
     * @param message
     */
    public void Enqueue(String message)
    {
        queue.addElement(message);
    }
    
    /**
     * Pull a log statement off of the queue
     * @return
     */
    public String Dequeue()
    {
        if (queue.size() > 0)
        {
            Object topElement = queue.elementAt(0);
            if (topElement == null)
            {
                return "";
            }        
            queue.removeElementAt(0);
            return (String)topElement;
        }
        return "";
    }
    
    /**
     * How large is the queue?
     * @return
     */
    public int Size()
    {
        return queue.size();
    }
    
    
}
