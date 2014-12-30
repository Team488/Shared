/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.drive;

import xbot.common.ExecState;

/**
 *
 * @author Alex
 */
public class SetOrientToGoalWorker extends SetDesiredHeadingWorker {
    public SetOrientToGoalWorker() {
        super(180.0);
    }
    
    public ExecState isFinished() {
        return ExecState.NOT_DONE;
    }
}
