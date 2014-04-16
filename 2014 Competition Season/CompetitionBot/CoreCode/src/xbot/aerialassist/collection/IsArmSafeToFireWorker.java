/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.collection;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.ExecState;

/**
 *
 * @author Alex
 */
public class IsArmSafeToFireWorker extends AerialWorkerBase {
    Collector collector;
    public IsArmSafeToFireWorker(Collector collector) {
        super("IsArmSafeToFireWorker");
        this.collector = collector;
    }
    
    public ExecState isFinished() {
        if(collector.getIsManual() ||
                collector.getSensors().GetIsSafeToFireUnderDuress()) {
            return ExecState.SUCCESS;
        } else {
            return ExecState.FAILURE;
        }
    }
    
}
