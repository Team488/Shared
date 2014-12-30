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
public class SetArmsDuressWorker extends StateMachineWorker {
    
    public SetArmsDuressWorker()
    {
        super("SetArmsDuressAndFireCatapultWorker");
        setupMachine();
    }
    
    private void setupMachine()
    {
        WorkerBase safeFront = new SafeArmForFiringWorker(
                RobotContext.Get().getCollectionCore().getFrontCollector(),true);
        
        WorkerBase safeBack = new SafeArmForFiringWorker(
                RobotContext.Get().getCollectionCore().getBackCollector(),true);
        
        WorkerBase lockForFiring = new SetFiringLockWorker();
        
        WorkerBase fire = new FireCatapultWorker();
        
 
        
            // Lower the arms, then fire once that is complete.
            WhenAllWorker bothSafeWorker = new WhenAllWorker();
            bothSafeWorker.addWorker(safeFront);
            bothSafeWorker.addWorker(safeBack);
            
            int lockArms = this.addWorker(new SetFiringLockWorker());
        
            int bothSafe = this.addWorker(bothSafeWorker);

            int fireCatapult = this.addWorker(new FireCatapultWorker());

            //this.onStart(lockArms);
            //this.onSuccess(lockArms, bothSafe);
            this.onStart(bothSafe);
            this.onSuccess(bothSafe,SUCCESS);
        }
       
  }


