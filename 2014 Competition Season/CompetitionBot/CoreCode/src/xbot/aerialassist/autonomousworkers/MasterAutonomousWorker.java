/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.autonomousworkers;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.autonomousworkers.oneballauto.OneBallAutonomousWorker;
import xbot.aerialassist.autonomousworkers.threeballauto.ThreeBallAutonomousWorker;
import xbot.aerialassist.autonomousworkers.twoballauto.TwoBallAutonomousWorker;
import xbot.aerialassist.autonomousworkers.zeroballauto.ZeroBallAutonomousWorker;
import xbot.aerialassist.systems.AutonomousCore;
import xbot.common.ExecState;

/**
 *
 * @author John
 */
public class MasterAutonomousWorker extends AerialWorkerBase {
    
    private AerialWorkerBase chosenWorker;
    private ThreeBallAutonomousWorker threeBall = new ThreeBallAutonomousWorker();
    private TwoBallAutonomousWorker twoBall = new TwoBallAutonomousWorker();
    private OneBallAutonomousWorker oneBall = new OneBallAutonomousWorker();
    private ZeroBallAutonomousWorker zeroBall = new ZeroBallAutonomousWorker();
    private AutoDriveForwardWorker justDrive = new AutoDriveForwardWorker();
    
    public MasterAutonomousWorker()
    {
        super("MasterAutonomousWorker");
    }
    
    public void init()
    {
        Info("Initializing");
        
        int leftAutoSwitch = robot().GetOICore().getLeftAutoSwitch();
        int middleAutoSwitch = robot().GetOICore().getMiddleAutoSwitch();
        int rightAutoSwitch = robot().GetOICore().getRightAutoSwitch();
        
        // The middle switch determines if we start the match pre-cocked (at least for 2&3 ball auto for now)
        if (middleAutoSwitch == 2)
        {
            AutonomousCore.autoShouldDriveForwardFirstProperty.set(true);
        }
        else
        {
            AutonomousCore.autoShouldDriveForwardFirstProperty.set(false);
        }
        
        // N-ball auto. Right auto switch selects how many.
        if (leftAutoSwitch == 2)
        {
            setupProgram(rightAutoSwitch+1);
        }
        // Zero ball auto (just drive)
        else if (leftAutoSwitch == 1)
        {
            chosenWorker = zeroBall;
        }
        // Stil zero ball auto (just drive)
        else
        {
            chosenWorker = zeroBall;
        }
        
        Info("Chosen:" + chosenWorker.getName());
        
        chosenWorker.init();
    }
    
    private void setupProgram(int desiredBalls)
    {
        if (desiredBalls == 0) // Zero ball is trivial and always the same
        {
            chosenWorker = zeroBall;
        }
        if (desiredBalls == 1) // Same with one ball - one ball is mostly focused on hot or not
        {
            chosenWorker = oneBall;
        }
        if (desiredBalls == 2)
        {
            chosenWorker = threeBall;
//            if (startCocked)
//            {
                AutonomousCore.useFrontProperty.set(true);
                AutonomousCore.useBackProperty.set(false);
                AutonomousCore.fireThirdBallProperty.set(false);
//            }
//            else // Since don't start cocked, we need to use back and front and fire both
//            {
//                AutonomousCore.useFrontProperty.set(true);
//                AutonomousCore.useBackProperty.set(true);
//                AutonomousCore.fireThirdBallProperty.set(true);
//            }
        }
        if (desiredBalls == 3) // For 3, it's pretty simple. We can't cock 3 times in auto.
        {
            chosenWorker = threeBall;
            AutonomousCore.useFrontProperty.set(true);
            AutonomousCore.useBackProperty.set(true);
            AutonomousCore.fireThirdBallProperty.set(false);
        }
    }
    
    public void exec()
    {
        chosenWorker.exec();
    }
    
    public ExecState isFinished()
    {
        return chosenWorker.isFinished();
    }
}
