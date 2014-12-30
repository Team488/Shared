/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.aerialassist.autonomousworkers.DeployBothCollectorsWorker;
import xbot.aerialassist.autonomousworkers.ImmediateRollerWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.collection.CollectorRollerState;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.StateMachineWorker;
import xbot.common.WhenAllWorker;

/**
 *
 * @author sterlingdorminey
 */
public class BringArmsUpAndStopRollersWorker extends AerialStateMachineWorker {

    public BringArmsUpAndStopRollersWorker() {
        super("BringArmsUpAndStopRollersWorker");
        
        Collector front = robot().getCollectionCore().getFrontCollector();
        Collector back = robot().getCollectionCore().getBackCollector();
        
        WhenAllWorker startRollersWorker = new WhenAllWorker();
        startRollersWorker.addWorker(new ImmediateRollerWorker(front, CollectorRollerState.COLLECT));
        startRollersWorker.addWorker(new ImmediateRollerWorker(back, CollectorRollerState.COLLECT));
        
        int startRollers = this.stateMachine.addWorker(startRollersWorker);
        
        int armsUp = this.stateMachine.addWorker(new DeployBothCollectorsWorker(CollectorDeployState.UP, CollectorDeployState.UP));
        
        WhenAllWorker stopRollersWorker = new WhenAllWorker();
        stopRollersWorker.addWorker(new ImmediateRollerWorker(front, CollectorRollerState.STOP));
        stopRollersWorker.addWorker(new ImmediateRollerWorker(back, CollectorRollerState.STOP));
        
        int stopRollers = this.stateMachine.addWorker(stopRollersWorker);
        
        this.stateMachine.onStart(startRollers);
        this.stateMachine.onSuccess(startRollers, armsUp);
        this.stateMachine.onSuccess(armsUp, stopRollers);
        this.stateMachine.onSuccess(stopRollers, StateMachineWorker.SUCCESS);
    }
    
}
