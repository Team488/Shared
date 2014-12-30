/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.StateMachineWorker;

/**
 *
 * @author sterlingdorminey
 */
public class TwoBallAutonomousWorker extends AerialStateMachineWorker {

    public TwoBallAutonomousWorker() {
        super("TwoBallAutonomousWorker");

        this.create(new TwoBallAutoWorkerFactory());
    }
    public TwoBallAutonomousWorker(ITwoBallAutoWorkerFactory factory) {
        super("TwoBallAutonomousWorker");
        
        this.create(factory);
    }
    
    private void create(ITwoBallAutoWorkerFactory factory) {
        // Load from the side, and wait to see which goal is hot.
        int checkHot = this.stateMachine.addWorker(
                factory.createLoadBallAndWaitForGoalHotWorker());
        
        int goalHot = this.stateMachine.addWorker(
                factory.createGoalHotWorker());
        
        int goalCold = this.stateMachine.addWorker(
                factory.createGoalColdWorker());
        
        int driveForward = this.stateMachine.addWorker(
                factory.createDriveWorker());
        
        this.stateMachine.onStart(checkHot);
        this.stateMachine.onSuccess(checkHot, goalHot);
        this.stateMachine.onFailure(checkHot, goalCold);
        this.stateMachine.onSuccess(goalHot, driveForward);
        this.stateMachine.onSuccess(goalCold, driveForward);
        this.stateMachine.onSuccess(driveForward, StateMachineWorker.SUCCESS);
    }
}
