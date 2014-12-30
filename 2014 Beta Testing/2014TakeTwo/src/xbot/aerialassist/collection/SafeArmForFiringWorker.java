/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.collection;

import xbot.common.ImmediateWorker;
import xbot.common.StateMachineWorker;

/**
 * Get the arm ready for firing. After this worker is complete, it is safe to 
 * fire the catapult. 
 * Logic: if the arm is up, move it to the firing position. Else, return immediately
 * but set the setpoint for the arm to down so it won't move up.
 * Also needs to handle the arm's PID being disabled.
 * @author Alex
 */
public class SafeArmForFiringWorker extends StateMachineWorker {
    public SafeArmForFiringWorker(Collector collector, boolean duress) {
        super(collector.getName() + ":SafeArmForFiringWorker");
        
        int isArmSafe = this.addWorker(new IsArmSafeToFireWorker(collector));
        int setFireState = 
                this.addWorker(
                    new CollectorDeployPIDWorker(
                        collector, 
                        CollectorDeployState.FIRING));
        int immediateSetDown =
                this.addWorker(new ImmediateWorker(
                    new CollectorDeployPIDWorker(
                        collector, 
                        CollectorDeployState.DOWN)));
        
        int isArmSafeUnderDuress = this.addWorker(new IsArmSafeToFireUnderDuressWorker(collector));
        
        int setDuressFireState = 
                this.addWorker(
                    new CollectorDeployPIDWorker(
                    collector,
                    CollectorDeployState.DURESS));
        
        
        if(!duress)
        {
            this.onStart(isArmSafe);
            this.onSuccess(isArmSafe, SUCCESS);
            this.onFailure(isArmSafe, immediateSetDown);
            this.onSuccess(immediateSetDown, SUCCESS);
        }
        else
        {
            this.onStart(isArmSafeUnderDuress);
            this.onSuccess(isArmSafeUnderDuress,SUCCESS);
            this.onFailure(isArmSafeUnderDuress,setDuressFireState);
            this.onSuccess(setDuressFireState,SUCCESS);
        }
    }
}
