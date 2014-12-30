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
public class CalibrateArmUpTest extends CalibrateArmTest {

    public CalibrateArmUpTest(Collector collector) {
        super(collector);
    }

    protected DoubleProperty getVoltageProperty(CollectorSensorPackage sensor) {
        return sensor.upVoltage;
    }
    
}
