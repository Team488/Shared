/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.drive.DriveForTimeWorker;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author John
 */
public class AutoJukeToSideWorker extends DriveForTimeWorker {

    private static DoubleProperty autoDriveForwardTime = new DoubleProperty("autoDriveJukeTime", 1000);
    
    public AutoJukeToSideWorker(double sidePower) {
        super(sidePower, 0, autoDriveForwardTime.get());
    }
    
}
