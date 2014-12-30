/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.collection;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;

/**
 *
 * @author Maya
 */
public class ManualDeployWorker extends AerialWorkerBase {
    
    
    private Collector collector;
    private CollectorDeployState state;
    
    public ManualDeployWorker(Collector collector, CollectorDeployState state)
    {
        super("ManualDeployWorker");
        this.collector = collector;
        this.state = state;
    }
    
    public void init()
    {
        collector.setTargetDeployState(state);
    }
}
