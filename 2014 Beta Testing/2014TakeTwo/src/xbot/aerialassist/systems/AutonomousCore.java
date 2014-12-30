/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.systems;

import xbot.aerialassist.RobotContext;
import xbot.common.CommonTools;
import xbot.common.Loggable;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author John
 */
public class AutonomousCore extends Loggable {
    
    public static DoubleProperty goalHotWaitTime = new DoubleProperty("GoalHotWaitTime", 2000);
    public static DoubleProperty afterHotWaitTime = new DoubleProperty("AfterGoalHotWaitTime", 3000);
    
    public static BooleanProperty useFrontProperty = new BooleanProperty("AutonomousUseFront", true);
    public static BooleanProperty useBackProperty = new BooleanProperty("AutonomousUseBack", false);
    
    public static BooleanProperty fireThirdBallProperty = new BooleanProperty("AutoFireThirdBall", false);
    public static BooleanProperty autoShouldDriveForwardFirstProperty = new BooleanProperty("AutoDriveForwardFirst", true);
    
    public static DoubleProperty timeToWaitForBallToSettleInitially = new 
                DoubleProperty("Auto-timeToWaitForBallToSettleInitially", 1000.0);
    
    
    private double autonomousStartMarker;
    
    
    public AutonomousCore()
    {
        super ("AutonomousCore");
    }
    
    public void setMarker()
    {
        autonomousStartMarker = CommonTools.Get().time.GetMarker();
    }
    
    public double timeInMSecSinceStartofAuto()
    {
        return CommonTools.Get().time.GetElapsedMSecFromMarker(autonomousStartMarker);
    }
}
