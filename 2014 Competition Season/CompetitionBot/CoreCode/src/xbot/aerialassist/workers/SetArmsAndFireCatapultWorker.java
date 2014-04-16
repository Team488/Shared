/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.workers;

import xbot.aerialassist.RobotContext;
import xbot.aerialassist.autonomousworkers.SetFiringLockWorker;
import xbot.aerialassist.collection.SafeArmForFiringWorker;
import xbot.common.StateMachineWorker;
import xbot.common.WhenAllWorker;
import xbot.common.WorkerBase;


/**
 *
 * @author Sterling
 */
public class SetArmsAndFireCatapultWorker extends StateMachineWorker {
    
    public SetArmsAndFireCatapultWorker()
    {
        super("SetArmsAndFireCatapultWorker");
        setupMachine(false);
    }
    
    public SetArmsAndFireCatapultWorker(boolean simultaneous)
    {
        super("SetArmsAndFireCatapultWorker" + (simultaneous ? "Simultaneous" : "Serial"));
        setupMachine(simultaneous);
    }
    
    private void setupMachine(boolean simultaneous)
    {
        WorkerBase safeFront = new SafeArmForFiringWorker(
                RobotContext.Get().getCollectionCore().getFrontCollector(),false);
        
        WorkerBase safeBack = new SafeArmForFiringWorker(
                RobotContext.Get().getCollectionCore().getBackCollector(),false);
        
        WorkerBase lockForFiring = new SetFiringLockWorker();
        
        WorkerBase fire = new FireCatapultWorker();
        
        if (simultaneous)
        {
            // Lower the arms & fire at the same time
            WhenAllWorker everything = new WhenAllWorker();
            everything.addWorker(safeFront);
            everything.addWorker(safeBack);
            everything.addWorker(fire);
            
            int lockArms = this.addWorker(lockForFiring);
            int all = this.addWorker(everything);
            
            this.onStart(lockArms);
            this.onSuccess(lockArms, all);
            this.onSuccess(all, StateMachineWorker.SUCCESS);
        }
        else
        {
            // Lower the arms, then fire once that is complete.
            WhenAllWorker bothSafeWorker = new WhenAllWorker();
            bothSafeWorker.addWorker(safeFront);
            bothSafeWorker.addWorker(safeBack);
            
            int lockArms = this.addWorker(new SetFiringLockWorker());
        
            int bothSafe = this.addWorker(bothSafeWorker);

            int fireCatapult = this.addWorker(new FireCatapultWorker());

            this.onStart(lockArms);
            this.onSuccess(lockArms, bothSafe);
            this.onSuccess(bothSafe, fireCatapult);
            this.onSuccess(fireCatapult, SUCCESS);
        }
       
        
        
    }
}
