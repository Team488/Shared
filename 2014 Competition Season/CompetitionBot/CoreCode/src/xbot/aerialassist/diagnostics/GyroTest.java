/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * This diagnostic tests the gyro - you'll need to rotate the robot physically!
 * @author Maya
 */
public class GyroTest extends DiagnosticTest {
    
    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
       return "Testing current gyro heading. Verify decreases when turned right.";
    }
    
    /**
     *
     * @return
     */
    public String getCurrentTestInformation()
    {
        return "Gyro Angle: " + trim(robot().GetSensorCore().getCurrentHeading());
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
