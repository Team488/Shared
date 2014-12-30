/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.catapult;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.systems.CatapultCore;
import xbot.common.CommonUtils;
import xbot.common.PID;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author John
 */
public class CatapultPIDWorker extends AerialWorkerBase{
    
    double goalPosition = 0;
    protected PID positionPID = new PID();
    
    private DoubleProperty catapultP = new DoubleProperty("CatapultP", 6.0);
    private DoubleProperty catapultI = new DoubleProperty("CatapultI", 0.0);
    private DoubleProperty catapultD = new DoubleProperty("CatapultD", 0.0);
    
    // 3 ranges, two doubleProperties defining them
    DoubleProperty pidBeginsAt = new DoubleProperty("CatpaultPIDBeginsAt", 0.2);
    
    public CatapultPIDWorker(String name)
    {
        super(name);
        SetGoalPositionFromProperties();
    }
    
    public CatapultPIDWorker() {
        super("CatapultPIDWorker - cock");
        SetGoalPositionFromProperties();
    }
    
    public CatapultPIDWorker(double goalPosition)
    {
        super("CatapultPIDWorker:" + goalPosition);
        this.goalPosition = goalPosition;
    }
    
    private void SetGoalPositionFromProperties()
    {
        this.goalPosition = robot().GetCatapultCore().catapultCockedPosition.get();
    }
    
    public void init()
    {
        Info("Initializing");
        positionPID.reset();
    }
    
    public void exec()
    {
        CatapultCore cataCore = robot().GetCatapultCore();
        
        if(!robot().GetCatapultCore().shouldCockCatapult.get())
        {
            positionPID.reset();
            cataCore.stopCatapult();
            return;
        }
        
        if (cataCore.getCatapultScaledPosition() > pidBeginsAt.get())
        {
            positionPID.reset();
            cataCore.cockCatapult();
        }
        else if (cataCore.getCatapultScaledPosition() >= cataCore.catapultCockedPosition.get())
        {
            // in range
            double potentialPower = positionPID.calculate(
                    goalPosition,
                    robot().GetCatapultCore().getCatapultScaledPosition(),
                    catapultP.get(),
                    catapultI.get(),
                    catapultD.get());

            potentialPower *= -1;

            double safePower = CommonUtils.CoerceDouble(potentialPower, 1, 0);

            robot().GetCatapultCore().setCatapultMotorSpeed(safePower);
        }
        else
        {
            // do nothing
            positionPID.reset();
            cataCore.stopCatapult();
        }
    }
    
    public void interrupted() {
        Info(" was interrupted!");
        StopCatapult();
    }
    
    private void StopCatapult()
    {
        robot().GetCatapultCore().stopCatapult();
    }
}
