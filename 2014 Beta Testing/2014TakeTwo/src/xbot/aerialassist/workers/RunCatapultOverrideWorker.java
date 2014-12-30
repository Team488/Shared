/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.aerialassist.AerialWorkerBase;

/**
 * Intended to be used while held on the OI as an over-ride
 * @author Alex
 */
public class RunCatapultOverrideWorker extends AerialWorkerBase {
    public RunCatapultOverrideWorker() {
        super("RunCatapultOverrideWorker");
    }
    
    public void init() {
        robot().GetCatapultCore().cockCatapult();
        robot().getCollectionCore().getFrontCollector().DangerousSetDeployMotor(0.5);
        robot().getCollectionCore().getBackCollector().DangerousSetDeployMotor(0.5);
    }
    
    public void interrupted() {
        robot().GetCatapultCore().stopCatapult();
        robot().getCollectionCore().getFrontCollector().DangerousSetDeployMotor(0);
        robot().getCollectionCore().getBackCollector().DangerousSetDeployMotor(0);
    }

}
