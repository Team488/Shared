/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.oneballauto;

import xbot.aerialassist.drive.DriveForTimeWorker;
import xbot.aerialassist.drive.SetDesiredHeadingWorker;
import xbot.aerialassist.drive.SetDesiredHeadingWorkerAndWaitUntilMet;
import xbot.aerialassist.workers.ResetGyroWorker;
import xbot.common.StateMachineWorker;

/**
 *
 * @author test
 */
public class AutoDriveForwardInTankAndRotate extends StateMachineWorker{
    public AutoDriveForwardInTankAndRotate() {
        super("OneBallTankWorker");
        
        int resetHeading = addWorker(new ResetGyroWorker());
        
        int driveForward = addWorker(new DriveForTimeWorker(0, 1.0, null,false));
        
        int rotateToGoal = addWorker(new SetDesiredHeadingWorkerAndWaitUntilMet(180.0));
        
        this.onStart(resetHeading);
        this.onSuccess(resetHeading,driveForward);
        this.onSuccess(driveForward,rotateToGoal);
        this.onSuccess(rotateToGoal,SUCCESS);
    }
}
