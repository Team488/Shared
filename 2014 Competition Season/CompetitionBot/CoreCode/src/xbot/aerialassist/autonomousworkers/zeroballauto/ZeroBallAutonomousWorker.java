/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.autonomousworkers.zeroballauto;

import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.aerialassist.drive.DriveForTimeWorker;

/**
 *
 * @author John
 */
public class ZeroBallAutonomousWorker extends AerialStateMachineWorker {
    
    public ZeroBallAutonomousWorker()
    {
        super("ZeroBallAutonomousWorker");
        makeMachine();
    }
    
    private void makeMachine()
    {
        int driveForward = this.stateMachine.addWorker(new DriveForTimeWorker(0, 1, 2000));
        
        this.stateMachine.onStart(driveForward);
    }
}
