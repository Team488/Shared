/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.telemetry;

import xbot.aerialassist.systems.OICore;

/**
 *
 * @author John
 */
public class DSItem extends TelemetryItem {

    public String GetName() {
        return "DS-Data";
    }

    public String GetData() {
        OICore core = robot().GetOICore();
        double leftX = core.getLeftJoyX();
        double leftY = core.getLeftJoyY();
        double rightX = core.getRightJoyX();
        double rightY = core.getRightJoyY();
        
        return "Left-X"+leftX+"-Y"+leftY+" Right-X"+rightX+"-Y"+rightY;
    }

    public void GatherData() {
        //
    }
    
}
