/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * Diagnostic test that runs the catapult
 * @author Maya
 */
public class CatapultMotorTest extends DiagnosticTest {

    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
       return "Testing catapult motor.";
    }
    
    /**
     *
     */
    public void executeTest() {
       robot().GetCatapultCore().fireCatapult();
    }

    /**
     *
     */
    public void cleanup() {
       robot().GetCatapultCore().stopCatapult();
    }

    public String getCurrentTestInformation() {
        return "";
    }
    
}
