/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.common.ExecState;

/**
 *
 * @author Alex
 */
public class ManualBallEjectionWorkerThatEnds extends ManualBallEjectionWorker {
    
    public ManualBallEjectionWorkerThatEnds(boolean extend) {
        super(extend);
    }
    
    public ExecState isFinished() {
        return ExecState.SUCCESS;
    }
}
