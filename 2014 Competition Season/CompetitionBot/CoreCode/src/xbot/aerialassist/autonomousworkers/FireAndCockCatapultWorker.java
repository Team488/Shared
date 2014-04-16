/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.workers.*;
import xbot.common.StateMachineWorker;

/**
 *
 * @author sterlingdorminey
 */
public class FireAndCockCatapultWorker extends AerialStateMachineWorker {

    public FireAndCockCatapultWorker() {
        super("Fire and cock catapult");
        
        int fireCatapult = this.stateMachine.addWorker(
                new FireCatapultWorker());
        
        int cockCatapult = this.stateMachine.addWorker(
                new CockCatapultWorker());
        
        this.stateMachine.onStart(fireCatapult);
        this.stateMachine.onSuccess(fireCatapult, cockCatapult);
        this.stateMachine.onSuccess(cockCatapult, StateMachineWorker.SUCCESS);
    }
}
