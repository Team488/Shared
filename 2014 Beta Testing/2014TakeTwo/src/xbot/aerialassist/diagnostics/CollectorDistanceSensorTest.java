/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

import xbot.aerialassist.collection.Collector;

/**
 *
 * @author John
 */
public class CollectorDistanceSensorTest extends DiagnosticTest {
        
    boolean frontCollector;
    Collector collector;
    
    public CollectorDistanceSensorTest(boolean frontCollector)
    {
        this.frontCollector = frontCollector;
    }

    public String getDiagnosticTestName() {
        EnsureCollector();        
        return "Testing " + collector.getName() + "Range Sensor";
    }

    public String getCurrentTestInformation() {
        EnsureCollector();
        return "Current Range: " + trim(collector.getSensors().GetRangeSensorTESTONLY().GetRange()) + " inches";
    }

    public void executeTest() {
    }

    public void cleanup() {
    }
    
    private void EnsureCollector()
    {
        if (collector == null)
        {
            if (frontCollector)
            {
                collector = robot().getCollectionCore().getFrontCollector();
            }
            else
            {
                collector = robot().getCollectionCore().getBackCollector();
            }
        }
    }
    
}
