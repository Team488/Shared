/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.drive.DriveForTimeWorker;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author sterlingdorminey
 */
public class AutoDriveForwardWorker extends DriveForTimeWorker {

    private static DoubleProperty autoDriveForwardTime = new DoubleProperty("autoDriveForwardTime", 1750);
    
    public AutoDriveForwardWorker() {
        super(0, 1, autoDriveForwardTime.get());
    }
    
}
