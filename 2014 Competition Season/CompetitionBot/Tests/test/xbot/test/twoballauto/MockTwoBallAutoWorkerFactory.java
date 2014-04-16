/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.twoballauto;

import xbot.aerialassist.autonomousworkers.twoballauto.ITwoBallAutoWorkerFactory;
import xbot.common.WorkerBase;

/**
 *
 * @author sterlingdorminey
 */
public class MockTwoBallAutoWorkerFactory implements ITwoBallAutoWorkerFactory {
    private final WorkerBase worker;
    
    public MockTwoBallAutoWorkerFactory(WorkerBase worker) {
        this.worker = worker;
    }

    @Override
    public WorkerBase createLoadBallAndWaitForGoalHotWorker() {
        return this.worker;
    }

    @Override
    public WorkerBase createGoalHotWorker() {
        return this.worker;
    }

    @Override
    public WorkerBase createGoalColdWorker() {
        return this.worker;
    }

    @Override
    public WorkerBase createDriveWorker() {
        return this.worker;
    }
    
}
