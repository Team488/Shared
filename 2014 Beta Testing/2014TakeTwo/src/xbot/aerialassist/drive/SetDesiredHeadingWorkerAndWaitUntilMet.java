/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.drive;

import xbot.common.ExecState;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author Alex
 */
public class SetDesiredHeadingWorkerAndWaitUntilMet extends SetDesiredHeadingWorker {
    
    private DoubleProperty marginOfErrorForTurning = new DoubleProperty("TurningMOE",4.0);
    
    public SetDesiredHeadingWorkerAndWaitUntilMet(double heading) {
        super(heading);
    }
    
    public ExecState isFinished() {
        double currentHeading = robot().GetSensorCore().getCurrentHeading();
        double desiredHeading = robot().GetDriveCore().GetDesiredHeading();
        if(Math.abs(currentHeading - desiredHeading) < marginOfErrorForTurning.get()) {
            return ExecState.SUCCESS;
        }
        else {
            return ExecState.NOT_DONE;
        }
    }
}
