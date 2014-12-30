/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.threeballauto;

import xbot.aerialassist.autonomousworkers.IAutonomousWorkerFactory;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.collection.CollectorRollerState;
import xbot.aerialassist.collection.CollectorRollerWorker;
import xbot.aerialassist.drive.DriveForTimeWorker;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.StateMachineWorker;
import xbot.common.TimeOutWorker;
import xbot.common.WaitWorker;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author Sterling
 */
public class ArmsDownAndMoveForwardWorker extends AerialStateMachineWorker {
    public ArmsDownAndMoveForwardWorker(IAutonomousWorkerFactory factory) {
        super("ArmsDownAndMoveForwardWorker");
        
        DoubleProperty captureBallWaitTime = new DoubleProperty("CaptureBallWaitTime", 500);
        DoubleProperty reverseBallWaitTime = new DoubleProperty("ReverseBallWaitTime", 300);
        DoubleProperty driveForwardAtBeginningTime
                = new DoubleProperty("DriveForwardAtBeginningTime", 2000);
        DoubleProperty driveForwardX =
                new DoubleProperty("DriveForwardX", 0);
        DoubleProperty driveForwardY =
                new DoubleProperty("DriveForwardY", 0.5);
        
        Collector front = robot().getCollectionCore().getFrontCollector();
        Collector back = robot().getCollectionCore().getBackCollector();
        
        // Step 0: Set arms to down.
        int setFrontArmDownAndBackUp = this.stateMachine.addWorker(
                factory.createDeployBothWorker(
                CollectorDeployState.DOWN,
                CollectorDeployState.UP));
        
        int collectFrontRoller = stateMachine.addWorker(new TimeOutWorker(
                new CollectorRollerWorker(front, CollectorRollerState.COLLECT), captureBallWaitTime, true));
       
        int stopCollectFrontRoller = this.stateMachine.addWorker(
                factory.createRollerWorker(front, CollectorRollerState.STOP));
        
        
        int reverseFrontRoller = stateMachine.addWorker(new TimeOutWorker(
                new CollectorRollerWorker(front, CollectorRollerState.REVERSE), 
                reverseBallWaitTime, true));
       
        int stopReverseFrontRoller = this.stateMachine.addWorker(
                factory.createRollerWorker(front, CollectorRollerState.STOP));

        // Step 2: Move forward.
        int driveForward = this.stateMachine.addWorker(
                new DriveForTimeWorker(driveForwardX, driveForwardY, driveForwardAtBeginningTime,true));
        
        this.stateMachine.onStart(setFrontArmDownAndBackUp);
        this.stateMachine.onSuccess(setFrontArmDownAndBackUp, collectFrontRoller);
        this.stateMachine.onSuccess(collectFrontRoller, stopCollectFrontRoller);
        this.stateMachine.onSuccess(stopCollectFrontRoller, driveForward);
        this.stateMachine.onSuccess(driveForward, reverseFrontRoller);
        this.stateMachine.onSuccess(reverseFrontRoller, stopReverseFrontRoller);
        this.stateMachine.onSuccess(stopReverseFrontRoller, StateMachineWorker.SUCCESS);
    }
}
