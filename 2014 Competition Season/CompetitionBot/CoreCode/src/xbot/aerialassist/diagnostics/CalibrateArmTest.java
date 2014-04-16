/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.diagnostics;

import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorSensorPackage;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author sterlingdorminey
 */
public abstract class CalibrateArmTest extends DiagnosticTest {
    private final Collector collector;
    
    protected CalibrateArmTest(Collector collector) {
        this.collector = collector;
    }
    
    public String getDiagnosticTestName() {
        return "CalibrateArmTest:" + collector.getName();
    }

    public String getCurrentTestInformation() {
        return "Voltage: " + trim(getCurrentVoltage());
    }

    public void executeTest() {
        
    }

    public void cleanup() {
        getVoltageProperty(collector.getSensors()).set(getCurrentVoltage());
    }
    
    /**
     * Return the property (up voltage or down voltage) to use.
     * @param sensor
     * @return 
     */
    protected abstract DoubleProperty getVoltageProperty(CollectorSensorPackage sensor);
    
    private double getCurrentVoltage() {
        return collector.getSensors().currentVoltage.get();
    }
}
