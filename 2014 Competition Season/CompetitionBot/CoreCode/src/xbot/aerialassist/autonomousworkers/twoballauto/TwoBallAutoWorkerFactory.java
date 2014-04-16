/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.aerialassist.autonomousworkers.AutoDriveForwardWorker;
import xbot.common.WorkerBase;

/**
 *
 * @author sterlingdorminey
 */
public class TwoBallAutoWorkerFactory implements ITwoBallAutoWorkerFactory {
    
    public WorkerBase createLoadBallAndWaitForGoalHotWorker() {
        return new LoadBallAndWaitForGoalHotWorker();
    }

    public WorkerBase createGoalHotWorker() {
        return new AutoGoalHotWorker();
    }

    public WorkerBase createGoalColdWorker() {
        return new AutoGoalColdWorker();
    }

    public WorkerBase createDriveWorker() {
        return new AutoDriveForwardWorker();
    }
    
}
