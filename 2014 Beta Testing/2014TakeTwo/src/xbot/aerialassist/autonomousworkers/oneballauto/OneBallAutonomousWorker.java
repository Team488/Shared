/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.autonomousworkers.oneballauto;

import xbot.aerialassist.autonomousworkers.AutoDriveForwardWorker;
import xbot.aerialassist.autonomousworkers.AutonomousWorkerFactory;
import xbot.aerialassist.autonomousworkers.twoballauto.CheckGoalHotWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.drive.DriveForTimeWorker;
import xbot.aerialassist.systems.AutonomousCore;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.aerialassist.workers.SetArmsAndFireCatapultWorker;
import xbot.common.BooleanPropertyWorker;
import xbot.common.StateMachineWorker;
import xbot.common.WaitWorker;
import xbot.common.WhenAllWorker;

/**
 *
 * @author John
 */
public class OneBallAutonomousWorker extends AerialStateMachineWorker {
    
    public OneBallAutonomousWorker()
    {
        super("OneBallAutonomousWorker");
        
        setupMachine();
    }
    
    private void setupMachine()
    {        
        int determineIfShouldDriveForwardFirst = this.stateMachine.addWorker(
                new BooleanPropertyWorker(AutonomousCore.autoShouldDriveForwardFirstProperty));
        
        WhenAllWorker driveForwardAndLookForHot = new WhenAllWorker();
        driveForwardAndLookForHot.addWorker(new AutoDriveForwardWorker());
        driveForwardAndLookForHot.addWorker(new CheckGoalHotWorker());
        int driveAndDetect = this.stateMachine.addWorker(driveForwardAndLookForHot);
         
        int justDetectHot = this.stateMachine.addWorker(new CheckGoalHotWorker());
        
        int stopDriveHot = this.stateMachine.addWorker(new DriveForTimeWorker(0, 0, 50,true));
        int stopDriveNotHot = this.stateMachine.addWorker(new DriveForTimeWorker(0, 0, 50,true));
        
        //int determineIfCocked = this.stateMachine.addWorker(new BooleanPropertyWorker(AutonomousCore.autoShouldDriveForwardFirstProperty));
                
        int waitAfterHot = this.stateMachine.addWorker(new WaitWorker(AutonomousCore.afterHotWaitTime));
        
        //int fireFromFront = this.stateMachine.addWorker(
        //        factory.createCollectAndFireBallFromSideWorker(front, back));
        
        int fireCatapult = this.stateMachine.addWorker(new SetArmsAndFireCatapultWorker());
        
        int driveForwardAtEnd = this.stateMachine.addWorker(new AutoDriveForwardWorker());
        
        this.stateMachine.onStart(determineIfShouldDriveForwardFirst);
            this.stateMachine.onSuccess(determineIfShouldDriveForwardFirst, driveAndDetect);
            this.stateMachine.onFailure(determineIfShouldDriveForwardFirst, justDetectHot);

            this.stateMachine.onSuccess(justDetectHot, fireCatapult);
            this.stateMachine.onFailure(justDetectHot, waitAfterHot);
        
        this.stateMachine.onSuccess(driveAndDetect, stopDriveHot);
        this.stateMachine.onSuccess(stopDriveHot, fireCatapult);
        this.stateMachine.onFailure(driveAndDetect, stopDriveNotHot);
        this.stateMachine.onSuccess(stopDriveNotHot, waitAfterHot);
        this.stateMachine.onSuccess(waitAfterHot, fireCatapult);
        this.stateMachine.onSuccess(fireCatapult, driveForwardAtEnd);
        this.stateMachine.onSuccess(driveForwardAtEnd, StateMachineWorker.SUCCESS);
        
        // If we started pointing to a dead goal - we need to check if we started cocked.
        // This is because cocking & loading takes a few seconds, and if we waited the full 5+ seconds,
        // we might miss our opportunity to shoot.
    }
}
