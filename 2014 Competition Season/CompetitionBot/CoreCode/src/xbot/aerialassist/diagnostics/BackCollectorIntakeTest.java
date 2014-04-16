/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * Diagnostic test that rolls the back collector rollers to intake balls.
 * @author Maya
 */
public class BackCollectorIntakeTest extends DiagnosticTest {

    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
        return "Running back collector intake!";
    }

    /**
     *
     */
    public void executeTest() {
        robot().getCollectionCore().getBackCollector().intakeBalls();
    }

    /**
     *
     */
    public void cleanup() {
        robot().getCollectionCore().getBackCollector().stopRoller();
    }

    public String getCurrentTestInformation() {
        return "";
    }
    
}
