/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.collection;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.systems.CollectionCore;
import xbot.common.CommonUtils;
import xbot.common.properties.DoubleProperty;
import xbot.common.PID;
/**
 *
 * @author Ken
 */
public class CollectionPositionMaintainer extends AerialWorkerBase {

    PID rotationalCollectorPid = new PID();
    DoubleProperty upCollectorRotationP;
    DoubleProperty upCollectorRotationI;
    DoubleProperty upCollectionRotationD;
    DoubleProperty downCollectorRotationP;
    DoubleProperty downCollectorRotationI;
    DoubleProperty downCollectionRotationD;
    
    private Collector collector;
    
    public CollectionPositionMaintainer(Collector collector)
    {
        super("CollectionPositionMaintainer");
        
        upCollectorRotationP = 
            new DoubleProperty(collector.getName() + ":UpRotationP", 0.6);
        upCollectorRotationI = 
            new DoubleProperty(collector.getName() + ":UpRotationI", 0.005);
        upCollectionRotationD = 
            new DoubleProperty(collector.getName() + ":UpRotationD", 1.0);
        downCollectorRotationP = 
            new DoubleProperty(collector.getName() + ":DownRotationP", 0.6);
        downCollectorRotationI = 
            new DoubleProperty(collector.getName() + ":DownRotationI", 0.005);
        downCollectionRotationD = 
            new DoubleProperty(collector.getName() + ":DownRotationD", 1.0);
        
        this.collector = collector;
    }
    
    public void reset()
    {
        rotationalCollectorPid.reset();
    }
    
    public void init() {
        Info("init");
        reset();
    }
    
    public void exec()
    {
        if(!collector.getIsManual())
        {
            ResetPidIfAtLimit();
        
            double currentPosition = collector.getSensors().GetCollectorPosition();
            double targetPosition = collector.getTargetScaledPosition();
            
            if (robot().GetCatapultCore().getIsFiring())
            {
                double min = collector.getSensors().duressPosition.get();
                targetPosition = CommonUtils.CoerceDouble(targetPosition, 1, min);
            }
            
            double output = CalculateCollectorOutput(targetPosition, currentPosition);
            collector.setDeployMotor(output);
        }
        else {            
            boolean up = collector.getSensors().isManualUp();
            boolean down = collector.getSensors().isManualDown();
            
            if (up == down)
            {
                collector.stopDeploy();
            }
            else if (up)
            {
                collector.DangerousSetDeployMotor(CollectionCore.upDeploySpeed.get());
            }
            else if (down)
            {
                collector.DangerousSetDeployMotor(CollectionCore.downDeploySpeed.get());
            }
        }
    }
    
    private double CalculateCollectorOutput (
            double targetPosition, double currentPosition) {
        double output;
        
        if (targetPosition == 0.0) {
            output =  rotationalCollectorPid.calculate( 
            targetPosition,
            currentPosition,
            upCollectorRotationP.get(),
            upCollectorRotationI.get(),
            upCollectionRotationD.get());
        }
        else {
            output =  rotationalCollectorPid.calculate( 
            targetPosition,
            currentPosition,
            downCollectorRotationP.get(),
            downCollectorRotationI.get(),
            downCollectionRotationD.get());
        }
        
        return output; 
    }
    
    private void ResetPidIfAtLimit()
    {
        if (collector.getTargetScaledPosition() < 0.1)
        {
            // we want to be closed. If we are there, reset the PID
            if (collector.getSensors().getIsFullyUp())
            {
                reset();
            }
        } else if (collector.getTargetScaledPosition() > 0.9)
        {
            //we want to be open. If we are there, reset the PID
            if (collector.getSensors().getIsFullyDown())
            {
                reset();
            }
        }
    }
        
}
    
    

