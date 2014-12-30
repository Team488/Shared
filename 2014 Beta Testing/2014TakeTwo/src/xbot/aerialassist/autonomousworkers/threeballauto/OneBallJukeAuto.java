/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.autonomousworkers.threeballauto;

import xbot.aerialassist.autonomousworkers.AutoDriveForwardWorker;
import xbot.aerialassist.autonomousworkers.AutoJukeToSideWorker;
import xbot.aerialassist.autonomousworkers.AutonomousWorkerFactory;
import xbot.aerialassist.autonomousworkers.CollectAndLoadBallFromSideWorker;
import xbot.aerialassist.autonomousworkers.IAutonomousWorkerFactory;
import xbot.aerialassist.autonomousworkers.WaitForBallToSettleAndFireWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.aerialassist.workers.SetArmsAndFireCatapultWorker;

/**
 *
 * @author John
 */
public class OneBallJukeAuto extends AerialStateMachineWorker {

    //BooleanProperty useFrontProperty = new BooleanProperty("AutonomousUseFront", true);
    //BooleanProperty useBackProperty = new BooleanProperty("AutonomousUseBack", false);
    
    public OneBallJukeAuto() {
        super("OneBallJukeAutonomous");
        this.create(new AutonomousWorkerFactory());
    }
    
    public OneBallJukeAuto(IAutonomousWorkerFactory factory) {
        super("OneBallJukeAutonomous");
        this.create(factory);
    }
    
    private void create(IAutonomousWorkerFactory factory) {
        Collector front = robot().getCollectionCore().getFrontCollector();
        Collector back = robot().getCollectionCore().getBackCollector();
        
        int collectFront = this.stateMachine.addWorker(
                new CollectAndLoadBallFromSideWorker(factory, front, back));
        
        int jukeFront = this.stateMachine.addWorker(new AutoJukeToSideWorker(-1));
        
        int fireFront = this.stateMachine.addWorker(
                new WaitForBallToSettleAndFireWorker(factory, front));
               
        int justDrive = this.stateMachine.addWorker(
                new AutoDriveForwardWorker());
        
        stateMachine.onStart(collectFront);
        // we'll have now collected the first ball and it is sitting in the cocked 
        // catapult
        
        // TODO: Figure out how to juke here without bumping the back side ball
        // way out of the way
        stateMachine.onSuccess(collectFront, jukeFront);
        
        stateMachine.onSuccess(jukeFront, fireFront);
        stateMachine.onSuccess(fireFront, justDrive);
        stateMachine.onSuccess(justDrive, stateMachine.SUCCESS);
    }
}
