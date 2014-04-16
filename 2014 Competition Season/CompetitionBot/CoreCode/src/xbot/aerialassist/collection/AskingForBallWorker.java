/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.collection;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.RobotContext;
import xbot.common.ExecState;

/**
 *
 * @author Maya
 */
public class AskingForBallWorker extends AerialWorkerBase{
    
    public AskingForBallWorker(){
        super("AskingForBallWorker");
    }
    
    public void init ()
    {
        RobotContext.Get().getLightsCore().setIsAskingForBall(true);
    }
    
    public void interrupted()
    {
        RobotContext.Get().getLightsCore().setIsAskingForBall(false);
    }
            
    
    public ExecState isFinished()
    {
        return ExecState.NOT_DONE;
    }
    
}
