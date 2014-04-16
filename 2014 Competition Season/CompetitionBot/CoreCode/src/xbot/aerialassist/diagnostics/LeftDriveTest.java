/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * This test moves the two left drive motors forward.
 * @author John
 */
public class LeftDriveTest extends DiagnosticTest {

    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
        return "Setting left motors forward.";
    }

    /**
     *
     */
    public void executeTest() {
        robot().GetDriveCore().setDriveMotors(0.3, 0, 0.3, 0);
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
