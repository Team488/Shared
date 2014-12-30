/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.WhenAllWorker;

/**
 *
 * @author sterlingdorminey
 */
public class ConsumeBallAndFinishCockingWorker extends WhenAllWorker {
    public ConsumeBallAndFinishCockingWorker(IAutonomousWorkerFactory factory, Collector main) {
        super("ConsumeBallAndFinishCockingWorker");
        
        // Simultaneously:
        // 0: Finish cocking the catapult.
        this.addWorker(factory.createCockCatapultWorker());
        // 1: Raise the main arm to "up" to consume the ball.
        this.addWorker(factory.createDeployWorker(main, CollectorDeployState.UP));
    }
}
