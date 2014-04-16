/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.diagnostics;

import xbot.aerialassist.RobotContext;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorSensorPackage;
import xbot.aerialassist.systems.CollectionCore;
import xbot.common.Formatter;

/**
 *
 * @author sterlingdorminey
 */
public class CollectorDeploySensorTest extends DiagnosticTest {

    private boolean frontCollector;
    
    private Collector collector;
    
    public CollectorDeploySensorTest(boolean frontCollector) {
        this.frontCollector = frontCollector;
    }
    
    public String getDiagnosticTestName() {
        EnsureCollector();
        return "CollectorDeployTest:" + this.collector.getName();
    }

    public String getCurrentTestInformation() {
        EnsureCollector();
        CollectorSensorPackage sensors = this.collector.getSensors();
        
        return "Voltage: " + trim(sensors.currentVoltage.get()) + " "
                + "Up: " + trim(sensors.GetUpVoltageTESTONLY()) + " "
                + "Down: " + trim(sensors.GetDownVoltageTESTONLY()) + " "
                + "Position: " + trim(sensors.GetCollectorPosition());
    }
    
    public void executeTest() {
        
    }

    public void cleanup() {
    }
    
    private void EnsureCollector()
    {
        if (this.collector != null) {
            return;
        }        
        CollectionCore collection = RobotContext.Get().getCollectionCore();
        if (frontCollector) {
            this.collector = collection.getFrontCollector();
        } else {
            this.collector = collection.getBackCollector();
        }
    }
    
}
