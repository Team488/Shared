/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * This diagnostic tests the Drive system in a very basic way - moving all the motors forward.
 * @author John
 */
public class DriveTest extends DiagnosticTest {

    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
        return "Setting all motors forward!";
    }

    /**
     *
     */
    public void executeTest() {
        robot().GetDriveCore().setDriveMotors(0.3, 0.3, 0.3, 0.3);
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
