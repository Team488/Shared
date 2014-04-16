/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.*;
import xbot.aerialassist.collection.*;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.*;
import xbot.common.properties.DoubleProperty;

/**
 * Collects the ball from one side of the robot.
 * 
 * Starting assumptions:
 * 1) Both collectors are in the Firing position.
 * 
 * Steps:
 * 1) Start spinning the roller.
 * 2) The collector on the side where the ball is is brought from Closed
 *    to Down.
 * 3) The collector is brought back up to Firing.
 * 4) The roller is stopped.
 * @author sterlingdorminey
 */
public class CollectFromSideWorker extends AerialStateMachineWorker {

    private DoubleProperty ballCollectTime = new DoubleProperty("BallCollectTime", 200);

    private DoubleProperty ballSettleTime = new DoubleProperty("BallSettleTime", 2000);
    /**
     * Create a CollectFromSideWorker, which uses "collector" to collect,
     * and side as the other collector.
     * @param collector
     * @param side 
     */
    public CollectFromSideWorker(Collector collector, Collector side) {
        super("SideCollect:" + collector.getName());
        
        // Step 0 :
        // Main roller:    Collecting
        // Main collector: Firing
        // Side collector: Firing
        int startRoller = this.stateMachine.addWorker(
                new ImmediateRollerWorker(
                    collector,
                    CollectorRollerState.COLLECT));
        
        // Step 1 :
        // Main roller:    Collecting
        // Main collector: Down
        // Side collector: Up
        int bringDownCollector = this.stateMachine.addWorker(
                new DeployBothCollectorsWorker(
                    collector,
                    CollectorDeployState.DOWN,
                    side,
                    CollectorDeployState.UP));
        
        // Step 2 :
        // Delay for a while to get the ball.
        int waitToCollectBall = this.stateMachine.addWorker(
                new WaitWorker(this.ballCollectTime));

        // Step 3 :
        // Main roller:    Collecting
        // Main collector: Up
        // Side collector: Up
        int bringUpCollector = this.stateMachine.addWorker(
                new CollectorDeployPIDWorker(
                    collector,
                    CollectorDeployState.UP));
        
        // Step 4 :
        // Wait for some period of time.
        int waitForBallToSettle = this.stateMachine.addWorker(
                new WaitWorker(this.ballSettleTime));
        
        // Step 5 :
        // Main roller:    Collecting
        // Main collector: Firing
        // Side collector: Firing
        int bringBothToFiring = this.stateMachine.addWorker(
                new DeployBothCollectorsWorker(
                    collector,
                    CollectorDeployState.FIRING,
                    side,
                    CollectorDeployState.FIRING));
        
        // Step 6 :
        // Main roller:    Stopped
        // Main collector: Firing
        // Side collector: Firing
        int stopRoller = this.stateMachine.addWorker(
                new ImmediateRollerWorker(
                    collector,
                    CollectorRollerState.STOP));
        
        this.stateMachine.onStart(startRoller);
        this.stateMachine.onSuccess(startRoller, bringDownCollector);
        this.stateMachine.onSuccess(bringDownCollector, waitToCollectBall);
        this.stateMachine.onSuccess(waitToCollectBall, bringUpCollector);
        this.stateMachine.onSuccess(bringUpCollector, waitForBallToSettle);
        this.stateMachine.onSuccess(waitForBallToSettle, bringBothToFiring);
        this.stateMachine.onSuccess(bringBothToFiring, stopRoller);
        this.stateMachine.onSuccess(stopRoller, StateMachineWorker.SUCCESS);
    }
}
