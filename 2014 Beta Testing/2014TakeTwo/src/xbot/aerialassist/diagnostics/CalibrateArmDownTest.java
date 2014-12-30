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
public class CalibrateArmDownTest extends CalibrateArmTest {

    public CalibrateArmDownTest(Collector collector) {
        super(collector);
    }

    protected DoubleProperty getVoltageProperty(CollectorSensorPackage sensor) {
        return sensor.downVoltage;
    }
    
}
