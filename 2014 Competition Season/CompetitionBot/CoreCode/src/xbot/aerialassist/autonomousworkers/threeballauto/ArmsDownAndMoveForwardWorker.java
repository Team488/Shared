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
import xbot.aerialassist.drive.DriveForTimeWorker;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.StateMachineWorker;
import xbot.common.WaitWorker;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author Sterling
 */
public class ArmsDownAndMoveForwardWorker extends AerialStateMachineWorker {
    public ArmsDownAndMoveForwardWorker(IAutonomousWorkerFactory factory) {
        super("ArmsDownAndMoveForwardWorker");
        
        DoubleProperty captureBallWaitTime = new DoubleProperty("CaptureBallWaitTime", 100);
        DoubleProperty driveForwardAtBeginningTime
                = new DoubleProperty("DriveForwardAtBeginningTime", 2000);
        DoubleProperty driveForwardX =
                new DoubleProperty("DriveForwardX", 0);
        DoubleProperty driveForwardY =
                new DoubleProperty("DriveForwardY", 0.5);
        
        Collector front = robot().getCollectionCore().getFrontCollector();
        Collector back = robot().getCollectionCore().getBackCollector();
        
        // Step 0: Set arms to down.
        int setArmsToDown = this.stateMachine.addWorker(
                factory.createDeployBothWorker(
                CollectorDeployState.DOWN,
                CollectorDeployState.UP));
        
        // Step 1: Engage the ball.
        int spinFrontRoller = this.stateMachine.addWorker(
                factory.createRollerWorker(front, CollectorRollerState.COLLECT));
        int spinBackRoller = this.stateMachine.addWorker(
                factory.createRollerWorker(back, CollectorRollerState.COLLECT));
        
        int waitForBallCaptured = this.stateMachine.addWorker(
                new WaitWorker(captureBallWaitTime));
        
        int stopFrontRoller = this.stateMachine.addWorker(
                factory.createRollerWorker(front, CollectorRollerState.STOP));
        int stopBackRoller = this.stateMachine.addWorker(
                factory.createRollerWorker(back, CollectorRollerState.STOP));

        // Step 2: Move forward.
        int driveForward = this.stateMachine.addWorker(
                new DriveForTimeWorker(driveForwardX, driveForwardY, driveForwardAtBeginningTime));
        
        this.stateMachine.onStart(setArmsToDown);
        this.stateMachine.onSuccess(setArmsToDown, spinFrontRoller);
        this.stateMachine.onSuccess(spinFrontRoller, spinBackRoller);
        this.stateMachine.onSuccess(spinBackRoller, waitForBallCaptured);
        this.stateMachine.onSuccess(waitForBallCaptured, stopFrontRoller);
        this.stateMachine.onSuccess(stopFrontRoller, stopBackRoller);
        this.stateMachine.onSuccess(stopBackRoller, driveForward);
        this.stateMachine.onSuccess(driveForward, StateMachineWorker.SUCCESS);
    }
}
