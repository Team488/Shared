/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.threeballauto;

import xbot.aerialassist.autonomousworkers.AutoDriveForwardWorker;
import xbot.aerialassist.autonomousworkers.AutonomousWorkerFactory;
import xbot.aerialassist.autonomousworkers.IAutonomousWorkerFactory;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.systems.AutonomousCore;
import xbot.aerialassist.workers.*;
import xbot.common.BooleanPropertyWorker;
import xbot.common.StateMachineWorker;
import xbot.common.WaitWorker;

/**
 *
 * @author sterlingdorminey
 */
public class ThreeBallAutonomousWorker extends AerialStateMachineWorker {

    //BooleanProperty useFrontProperty = new BooleanProperty("AutonomousUseFront", true);
    //BooleanProperty useBackProperty = new BooleanProperty("AutonomousUseBack", false);
    
    public ThreeBallAutonomousWorker() {
        super("ThreeBallAutonomous");
        this.create(new AutonomousWorkerFactory());
    }
    
    public ThreeBallAutonomousWorker(IAutonomousWorkerFactory factory) {
        super("ThreeBallAutonomous");
        this.create(factory);
    }
    
    private void create(IAutonomousWorkerFactory factory) {
        
        
        
        Collector front = robot().getCollectionCore().getFrontCollector();
        Collector back = robot().getCollectionCore().getBackCollector();
        
        // Stage -Infinity (How the robot starts the match)
        // Both arms are Up
        
        // State -1:
        // Determine if robot starts cocked
        int determineIfShouldDriveForwardFirst = this.stateMachine.addWorker(
                new BooleanPropertyWorker(AutonomousCore.autoShouldDriveForwardFirstProperty));
        
        // Step 0: Set arms to down.
        int setArmsToDown = this.stateMachine.addWorker(
                factory.createDeployBothWorker(
                CollectorDeployState.DOWN,
                CollectorDeployState.DOWN));
        
        // State 0:
        int setArmsToDownAndDriveForward = this.stateMachine.addWorker(
                new ArmsDownAndMoveForwardWorker(factory));
        
        // State 0a:
        // Wait for a bit
        int waitForBallToStopRocking = this.stateMachine.addWorker(
                new WaitWorker(AutonomousCore.timeToWaitForBallToSettleInitially));
        
        // State 1:
        // We fire the ball that we came with.
        int fireFirst = this.stateMachine.addWorker(
                factory.createFireCatapultWorker());
        
        // State 2:
        // We check whether we should collect from the front.
        int useFront = this.stateMachine.addWorker(
                new BooleanPropertyWorker(AutonomousCore.useFrontProperty));
        
        // State 3:
        int fireFromFront = this.stateMachine.addWorker(
                factory.createCollectAndFireBallFromSideWorker(front, back));
        
        // State 4:
        // We check whether we should collect from the back.
        int useBack = this.stateMachine.addWorker(
                new BooleanPropertyWorker(AutonomousCore.useBackProperty));
        
        // State 5:
        // We check what we should do with that ball
        int shouldFireLastBall = this.stateMachine.addWorker(
                new BooleanPropertyWorker(AutonomousCore.fireThirdBallProperty));
        
        // States 5+:
        // Fire last ball
        int fireFromBack = this.stateMachine.addWorker(
                factory.createCollectAndFireBallFromSideWorker(back, front));
        
        
        int loadback = this.stateMachine.addWorker(
                factory.createPrepareToCollectBallWorker(back, front));
        
        int putBackBallInCatapult = this.stateMachine.addWorker(
                factory.createDeployWorker(back, CollectorDeployState.UP));
        
        int justDrive = this.stateMachine.addWorker(
                new AutoDriveForwardWorker());
        
        this.stateMachine.onStart(determineIfShouldDriveForwardFirst);
            
            this.stateMachine.onSuccess(determineIfShouldDriveForwardFirst, setArmsToDownAndDriveForward);
            this.stateMachine.onSuccess(setArmsToDownAndDriveForward, waitForBallToStopRocking);
            
            this.stateMachine.onFailure(determineIfShouldDriveForwardFirst, setArmsToDown);
            this.stateMachine.onSuccess(setArmsToDown, waitForBallToStopRocking);
        
        this.stateMachine.onSuccess(waitForBallToStopRocking, fireFirst);
        this.stateMachine.onSuccess(fireFirst, useFront);

        // If we should use the front, collect & fire from there
        this.stateMachine.onSuccess(useFront, fireFromFront);
        this.stateMachine.onSuccess(fireFromFront, useBack);
        
        // Otherwise, just check to see if we should use the back
        this.stateMachine.onFailure(useFront, useBack);
        // If we don't use the back ball at all, just go.
        this.stateMachine.onFailure(useBack, justDrive);
        
        // If, instead, we should use the back, figure out what to do with that last ball - hold or fire?
        this.stateMachine.onSuccess(useBack, shouldFireLastBall);
        
            // If we are using the back, and should FIRE last ball
            this.stateMachine.onSuccess(shouldFireLastBall, fireFromBack);
            this.stateMachine.onSuccess(fireFromBack, justDrive);

            // If we are using the back and should HOLD the last ball
            this.stateMachine.onFailure(shouldFireLastBall, loadback);        
            this.stateMachine.onSuccess(loadback, putBackBallInCatapult);
            this.stateMachine.onSuccess(putBackBallInCatapult, justDrive);
        
        // Finally, drive and get those extra 5 points.
        this.stateMachine.onSuccess(justDrive, StateMachineWorker.SUCCESS);
    }
}
