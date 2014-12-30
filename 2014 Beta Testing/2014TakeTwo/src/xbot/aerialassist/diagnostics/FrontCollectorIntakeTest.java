/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * This diagnostic tests running the Front Collector rollers intaking balls.
 * @author Maya
 */
public class FrontCollectorIntakeTest extends DiagnosticTest {

    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
        return "Running front collector intake!";
    }

    /**
     *
     */
    public void executeTest() {
        robot().getCollectionCore().getFrontCollector().intakeBalls();
    }

    /**
     *
     */
    public void cleanup() {
        robot().getCollectionCore().getFrontCollector().stopRoller();
    }

    public String getCurrentTestInformation() {
        return "";
    }
    
}
