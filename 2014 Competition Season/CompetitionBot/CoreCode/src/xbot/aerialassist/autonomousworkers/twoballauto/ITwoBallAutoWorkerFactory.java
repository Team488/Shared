/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.common.WorkerBase;

/**
 *
 * @author sterlingdorminey
 */
public interface ITwoBallAutoWorkerFactory {
    WorkerBase createLoadBallAndWaitForGoalHotWorker();
    
    WorkerBase createGoalHotWorker();
    
    WorkerBase createGoalColdWorker();
    
    WorkerBase createDriveWorker();
}
