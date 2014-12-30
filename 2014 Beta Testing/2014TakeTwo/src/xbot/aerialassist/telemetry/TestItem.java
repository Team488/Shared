/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.telemetry;

/**
 * Example item to demonstrate how a TelemetryItem works.
 * @author John
 */
public class TestItem extends TelemetryItem {

    /**
     *
     * @return
     */
    public String GetName() {
        return "TestItem";
    }

    /**
     *
     * @return
     */
    public String GetData() {
        return "Test";
    }

    /**
     *
     */
    public void GatherData() {
        // do nothing!
    }
    
}
