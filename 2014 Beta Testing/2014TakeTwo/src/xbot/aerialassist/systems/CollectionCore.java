/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.systems;

import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.Loggable;
import xbot.common.math.XYPair;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;

/**
 * This class handles all things related to collection. It contains two collectors,
 * front and back, and also has some methods that act on both (such as field-oriented
 * collection).
 * @author Nikhil
 */
public class CollectionCore extends Loggable {
    
    DoubleProperty operatorJoystickDeadband 
            = new DoubleProperty("operatorJoystickDeadband", 0.2);
    
    /**
     *
     */
    public CollectionCore(){
        super("CollectionCore");
    }
    
    private Collector frontCollector = new Collector("FrontCollector");

    /**
     * The robot's Front collector.
     * @return
     */
    public Collector getFrontCollector() {
        return frontCollector;
    }

    private Collector backCollector = new Collector("BackCollector");
    
    /**
     * The robot's Back collector.
     * @return
     */
    public Collector getBackCollector() {
        return backCollector;
    }
    
    /**
     * Whether or not to use the limit switches as part of logic.
     */
    public BooleanProperty useLimitSwitchesForArms = new BooleanProperty("UseLimitSwitchesForArms", false);
    
    /**
     * How fast to spin the collection rollers.
     */
    public static DoubleProperty rollerCollectSpeed = 
        new DoubleProperty("collectorSpeed", 1.0);
    
    /**
     * How much power to apply when raising the collector.
     */
    public static DoubleProperty upDeploySpeed =
       new DoubleProperty("upDeploySpeed", -1.0);
    
    /**
     * How much power to apply when lowering the collector.
     */
    public static DoubleProperty downDeploySpeed = 
       new DoubleProperty("downDeploySpeed", 0.25);
    
    private DoubleProperty fieldOrientedDeploymentFactor =
            new DoubleProperty("FO-Collection Deployment Factor", 0.0);
    
    /**
     * Move all the arms up, and stop all rollers.
     */
    public void ReturnToStartingPosition()
    {
        frontCollector.setTargetDeployState(CollectorDeployState.UP);
        backCollector.setTargetDeployState(CollectorDeployState.UP);
    }
    
    private XYPair GetRobotOrientedDirectionOfOperatorJoystick()
    {
        XYPair fieldOrientedDirection = 
                new XYPair(RobotContext.Get().GetOICore().getOperatorJoyX(), RobotContext.Get().GetOICore().getOperatorJoyY());
        
        double orientation = RobotContext.Get().GetSensorCore().getCurrentHeading();
        // Then, subtract default orientation
        orientation = orientation - RobotContext.Get().GetSensorCore().GetDefaultOrientation();
        // Finally rotate by the negative of that result.
        XYPair robotOrientedDirection = fieldOrientedDirection.rotate(-orientation);
        
        return robotOrientedDirection;
    }
    
    public boolean isInFrontCollectorRange()
    {
        XYPair robotOrientedDirection = GetRobotOrientedDirectionOfOperatorJoystick();
        // later, we may want tighter angles here
        if (robotOrientedDirection.Y > fieldOrientedDeploymentFactor.get() * robotOrientedDirection.X)
        {
            return true;
        }
        return false;
    }
    
    public boolean isInBackCollectorRange()
    {
        XYPair robotOrientedDirection = GetRobotOrientedDirectionOfOperatorJoystick();
        
        if (robotOrientedDirection.Y < -fieldOrientedDeploymentFactor.get() * robotOrientedDirection.X)
        {
            return true;
        }
        return false;
    }
    
    public boolean isOperatorJoystickSufficientlyDisplaced()
    {
        if (GetRobotOrientedDirectionOfOperatorJoystick().internalAbsoluteSum() > operatorJoystickDeadband.get())
        {
            return true;
        }
        return false;
    }
}
