/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * Diagnostic test that asks an operator to press the Catapult limit switch.
 * @author Maya
 */
public class CatapultLimitSwitchTest extends DiagnosticTest {

    private boolean isSwitchSet;
    private double catapultPosition;
    
    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
       return "Testing catapult limit switch.";
    }
    
    /**
     *
     * @return
     */
    public String getCurrentTestInformation()
    {
        return "Position: " + trim(catapultPosition) + " and CatapultCocked: " + isSwitchSet;
    }

    /**
     *
     */
    public void executeTest() {
       this.isSwitchSet = robot().GetCatapultCore().isCatapultCocked();
       this.catapultPosition = robot().GetCatapultCore().getCatapultScaledPosition();
    }

    /**
     *
     */
    public void cleanup() {
    }
    
}
