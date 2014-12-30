/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * This test sets the rear drive motors forward.
 * @author John
 */
public class RearDriveTest extends DiagnosticTest {

    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
        return "Setting rear motors forward.";
    }

    /**
     *
     */
    public void executeTest() {
        robot().GetDriveCore().setDriveMotors(0, 0, 0.3, 0.3);
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
