/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.aerialassist.autonomousworkers.DeployBothCollectorsWorker;
import xbot.aerialassist.autonomousworkers.ImmediateRollerWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.collection.CollectorRollerState;
import xbot.aerialassist.collection.CollectorRollerWorker;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.StateMachineWorker;
import xbot.common.WaitWorker;
import xbot.common.WhenAllWorker;
import xbot.common.properties.DoubleProperty;

/**
 * Gets a ball (into the "safe" position) so that it can be fired.
 * @author sterlingdorminey
 */
public class LoadBallWorker extends AerialStateMachineWorker {
    private DoubleProperty ballCollectTime = new DoubleProperty("BallCollectTime", 200);

    public LoadBallWorker() {
        super("LoadBallWorker");
        
        Collector front = robot().getCollectionCore().getFrontCollector();
        Collector back = robot().getCollectionCore().getBackCollector();
                
        // 0. Bring both arms to down.
        int armsDown = this.stateMachine.addWorker(
                new DeployBothCollectorsWorker(CollectorDeployState.DOWN, CollectorDeployState.DOWN));
        
        // 1. Load a ball.
        int loadBall = this.stateMachine.addWorker(new WaitWorker(this.ballCollectTime));
        
        // 2. Bring both arms to safe.
        int armsSafe = this.stateMachine.addWorker(
                new DeployBothCollectorsWorker(CollectorDeployState.SAFE, CollectorDeployState.SAFE));
        
        this.stateMachine.onStart(armsDown);
        this.stateMachine.onSuccess(armsDown, loadBall);        
        this.stateMachine.onSuccess(loadBall, armsSafe);
        this.stateMachine.onSuccess(armsSafe, StateMachineWorker.SUCCESS);
        
    }
    
}
