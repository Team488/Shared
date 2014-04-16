/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.telemetry;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.CommonTools;
import xbot.common.ExecState;

/**
 * Worker to flush the telemetry system to permanent storage.
 * @author John
 */
public class ForceFlushTelemetryWorker extends AerialWorkerBase {

    /**
     *
     */
    public ForceFlushTelemetryWorker()
    {
        super("ForceFlushTelemetryWorker");
    }

    /**
     *
     */
    public void exec()
    {
        CommonTools.Get().telemetryConsumer.ForceFlushToStorage();
    }

    /**
     *
     * @return
     */
    public ExecState isFinished() {
        return ExecState.SUCCESS;
    }
}
