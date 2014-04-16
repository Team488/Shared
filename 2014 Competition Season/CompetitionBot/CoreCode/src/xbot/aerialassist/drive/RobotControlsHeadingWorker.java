/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.workers.AerialStateMachineWorker;

/**
 *
 * @author John
 */
public class RobotControlsHeadingWorker extends AerialStateMachineWorker {
    
    public RobotControlsHeadingWorker()
    {
        super("RobotControlsHeadingWorker");
        SetupMachine();
    }
    
    private void SetupMachine()
    {
        int waitAWhile = this.stateMachine.addWorker(new WaitAfterJoysticksWorker());
        int maintain = this.stateMachine.addWorker(new MaintainHeadingWorker());
        
        this.stateMachine.onStart(waitAWhile);
        this.stateMachine.onSuccess(waitAWhile, maintain);
        this.stateMachine.onSuccess(maintain, maintain);
    }
    
}
