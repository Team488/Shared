/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

import xbot.aerialassist.RobotContext;
import xbot.common.Formatter;

/**
 * This is a base class for a Diagnostic Test. It's mostly a helper class
 * so that diagnostics will have easy access to the RobotContext via the
 * robot() method.
 * @author John
 */
public abstract class DiagnosticTest implements IDiagnosticTest {
    
    /**
     * 
     * @return  the 2014 RobotContext
     */
    protected RobotContext robot()
    {
        return RobotContext.Get();
    }
    
    protected String trim(double value) {
        return Formatter.format(value, 3);
    }
}
