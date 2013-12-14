/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.logging;

import java.util.Vector;

/**
 *
 * @author John
 */
public class LoggingQueue {
    private static Vector queue;
    
    public LoggingQueue()
    {
        queue = new Vector();
    }
    
    public void Enqueue(String message)
    {
        queue.addElement(message);
    }
    
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
    
    public int Size()
    {
        return queue.size();
    }
    
    
}
