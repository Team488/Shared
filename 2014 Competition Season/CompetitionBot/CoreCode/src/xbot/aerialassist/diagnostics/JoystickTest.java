/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

import xbot.aerialassist.systems.OICore;

/**
 * This test reads joystick values to the operator to ensure they are behaving well.
 * @author Maya
 */
public class JoystickTest extends DiagnosticTest {
    
    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
       return "Testing the Two Driver Joysticks.";
    }
    
    /**
     *
     * @return
     */
    public String getCurrentTestInformation()
    {
        OICore joy = robot().GetOICore();
        return "Left Joy (" + trim(joy.getLeftJoyX()) + ", " + trim(joy.getLeftJoyY()) +
                ") Right Joy (" + trim(joy.getRightJoyX()) + ", " + trim(joy.getRightJoyY()) + ")";
    }

    /**
     *
     */
    public void executeTest() {
    }

    /**
     *
     */
    public void cleanup() {
    }
    
}
