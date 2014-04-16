/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * This test runs the front two motors forward.
 * @author John
 */
public class FrontDriveTest extends DiagnosticTest {

    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
        return "Setting front motors forward.";
    }

    /**
     *
     */
    public void executeTest() {
        robot().GetDriveCore().setDriveMotors(0.3, 0.3, 0, 0);
    }

    /**
     *
     */
    public void cleanup() {
        robot().GetDriveCore().setDriveMotors(0, 0, 0, 0);
    }

    public String getCurrentTestInformation() {
        return "";
    }
    
}
