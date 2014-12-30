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
public class RotationForceProviderWorker extends AerialStateMachineWorker {
    
    public RotationForceProviderWorker()
    {
        super("RotationForceProviderWorker");
        buildMachine();
    }
    
    private void buildMachine()
    {
        int humanControl = this.stateMachine.addWorker(new HumanRotationWorker());
        int robotControl = this.stateMachine.addWorker(new RobotControlsHeadingWorker());
        
        this.stateMachine.onStart(humanControl);
        this.stateMachine.onFailure(humanControl, robotControl);
        this.stateMachine.onFailure(robotControl, humanControl);
        
        this.stateMachine.onSuccess(humanControl, humanControl);
        this.stateMachine.onSuccess(robotControl, robotControl);
    }
    
}
