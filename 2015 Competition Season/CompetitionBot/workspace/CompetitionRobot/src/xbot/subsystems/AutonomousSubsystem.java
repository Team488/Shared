package xbot.subsystems;

import org.apache.log4j.Logger;

import xbot.OI;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AutonomousSubsystem {
    
    private static final Logger log = Logger
            .getLogger(AutonomousSubsystem.class);
    
    DoubleProperty autoProgram;
    OI oi;
    
    @Inject
    public AutonomousSubsystem(PropertyManager propMan, OI oi) {
        autoProgram = propMan.createProperty("Autonomous Program", 2d);
        this.oi = oi;
    }
    
    public void updateAutoSwitch() {
        
        // Read the axis value of the auto switch
        double autoSwitch = oi.operatorPanel.getRawAxis(2);
        
        if (autoSwitch < -.66)
        {
            //switch to the left: robot facing backwards, get container, drive forward
            autoProgram.set(3d);
        }
        else if (autoSwitch > -.66 && autoSwitch < .66)
        {
            // switch in the middle: facing left and holding yellow tote, then driving and orienting.
            autoProgram.set(2d);
        }
        else if (autoSwitch > .66)
        {
            // switch all the way right, facing away and driving forwards
            autoProgram.set(1d);
        }
    }
    
    public double getAutoProgram() {
        return autoProgram.get();
    }
}
