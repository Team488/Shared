/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author John
 */
public class GatherTelemetryWorker extends AerialWorkerBase {
    
    private DoubleProperty millisecondsBetweenTelemetry = 
            new DoubleProperty("MSbetweenTelemetry", 1000);
    
    private double lastQueryTime;
    
    /**
     *
     */
    public GatherTelemetryWorker()
    {
        super("GatherTelemetryWorker");
        lastQueryTime = -10000;
    }
    
    /**
     *
     */
    public void exec() {
        double deltaTime = CommonTools.Get().time.GetElapsedMSecFromMarker(lastQueryTime);
        if (deltaTime > millisecondsBetweenTelemetry.get())
        {
            robot().GetTelemetryCore().ReportTelemetry();
            lastQueryTime = CommonTools.Get().time.GetMarker();
        }
    }
    
    /**
     *
     * @return
     */
    public ExecState isFinished() {
        return ExecState.NOT_DONE;
    }   
    
    
    
    
    
}
