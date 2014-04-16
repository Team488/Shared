/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.workers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author Nikhil
 */
public class ManualBallEjectionWorker extends AerialWorkerBase {
    
    private boolean shouldEject;
    
    public ManualBallEjectionWorker(boolean shouldEject){
        super("ManualBallEjectBallWorker");
        this.shouldEject = shouldEject;
    }
    
    public void init(){
        Info("init: shouldEject: " + shouldEject);
        if(shouldEject && !robot().GetCatapultCore().isEjectorSolenoidPunching()){
            robot().GetCatapultCore().ejectBall();
        }
        else
        {
            robot().GetCatapultCore().retractEjector();
        }
    }
    
}
