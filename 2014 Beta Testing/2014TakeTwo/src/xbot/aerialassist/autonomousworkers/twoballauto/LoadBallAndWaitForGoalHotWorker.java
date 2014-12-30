/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.common.WhenAllWorker;

/**
 * Load a ball from the side
 * and wait for a signal as to which goal is hot.
 * @author sterlingdorminey
 */
public class LoadBallAndWaitForGoalHotWorker extends WhenAllWorker {
    public LoadBallAndWaitForGoalHotWorker() {
        super("LoadBallAndWaitForGoalHotWorker");
        
        this.addWorker(new LoadBallWorker());
        this.addWorker(new CheckGoalHotWorker());
    }
}
