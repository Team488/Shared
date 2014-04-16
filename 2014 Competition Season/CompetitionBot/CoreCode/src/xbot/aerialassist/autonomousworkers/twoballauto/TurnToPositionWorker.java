/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.aerialassist.AerialWorkerBase;

/**
 * Uses a DriveHeadingMaintainer to turn the robot to a position.
 * 
 * Returns SUCCESS when complete.
 * Returns NOT_DONE until it is complete.
 * 
 * @author sterlingdorminey
 */
public class TurnToPositionWorker extends AerialWorkerBase {

    public TurnToPositionWorker() {
        super("TurnToPositionWorker");
    }
    
}
