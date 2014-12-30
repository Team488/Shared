/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.diagnostics;

import xbot.aerialassist.RobotContext;
import xbot.aerialassist.systems.CatapultCore;

/**
 *
 * @author sterlingdorminey
 */
public class CalibrateCatapultTest extends DiagnosticTest {
    private final CatapultCore catapultCore = RobotContext.Get().GetCatapultCore();
    
    public String getDiagnosticTestName() {
        return "CalibrateCatapultTest";
    }

    public String getCurrentTestInformation() {
        return "Offset: " + this.catapultCore.getCatapultScaledPosition();
    }

    public void executeTest() {
        // We want to set the catapult offset to 0, to get an
        // absolute sense of the sensor value.
        // Note that the voltage returned will be scale * voltage in range [0, 1].
        catapultCore.catapultPositionOffset.set(0);
        
        // Set the catapult motor to the left joystick's Y axis.
        // This way, you can move the catapult while calibrating it.
        double joyY = RobotContext.Get().GetOICore().getLeftJoyY();
        this.catapultCore.setCatapultMotorSpeed(joyY);
    }

    public void cleanup() {
        this.catapultCore.stopCatapult();

        // Use offset = 1.001 - scale * voltage.
        // This is because:
        // pos = (scale * voltage + offset) mod 1.001
        // Solving for pos = 0 (since 0 means "cocked.")
        // 0 = (scale * voltage + (1.001 - scale * voltage)) mod 1.001
        //   = 1.001 mod 1.001 = 0.
        double offset = 1.001 - this.catapultCore.getCatapultScaledPosition();
        this.catapultCore.catapultPositionOffset.set(offset);
    }
}
