/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * This test sets the right drive motors forward.
 * @author John
 */
public class RightDriveTest extends DiagnosticTest {

    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
        return "Setting right motors forward.";
    }

    /**
     *
     */
    public void executeTest() {
        robot().GetDriveCore().setDriveMotors(0, 0.3, 0, 0.3);
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
