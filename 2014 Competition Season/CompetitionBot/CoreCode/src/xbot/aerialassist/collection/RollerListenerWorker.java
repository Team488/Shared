/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.collection;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.systems.*;

/**
 *
 * @author John
 */
public class RollerListenerWorker extends AerialWorkerBase {
    
    private Collector collector;
    
    public RollerListenerWorker(Collector collector, String position)
    {
        super(position+"RollerListenerWorker");
        this.collector = collector;
    }
    
    public void init()
    {
        Info("Initializing");
    }
    
    public void exec()
    {
        boolean intakeBalls;
        boolean expelBalls;
        
        if (!collector.getIsManual())
        {
            // Normal mode.
            intakeBalls = this.robot().GetOICore().getRollerShouldIntake();
            expelBalls = this.robot().GetOICore().getRollerShouldExpel();
        }
        else
        {
            // manual override!
            intakeBalls = collector.getSensors().isManualIntake();
            expelBalls = collector.getSensors().isManualExpel();
        }

        if (intakeBalls == expelBalls)
        {
            collector.stopRoller();
        }
        else if (intakeBalls)
        {
            collector.intakeBalls();
        }
        else if (expelBalls)
        {
            collector.ejectBalls();
        }
    }
}
