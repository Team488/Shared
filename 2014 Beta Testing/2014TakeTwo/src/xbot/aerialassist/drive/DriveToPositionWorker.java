/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.drive;

import xbot.aerialassist.drive.DriveHeadingMaintainer;
import xbot.aerialassist.AerialWorkerBase;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.PIDArray;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author Alex
 */
public class DriveToPositionWorker extends AerialWorkerBase {
    final double onTargetWaitTimeMS = 500;
    
    PIDArray pidArray = new PIDArray();
    
    DoubleProperty translationPositionP = 
            new DoubleProperty("translationPositionP", 0.1);
    DoubleProperty translationPositionI = 
            new DoubleProperty("translationPositionI", 0.0);
    DoubleProperty translationPositionD = 
            new DoubleProperty("translationPositionD", 0.0);
    
    double[] outputForce = new double[2];
    
    XYPair target = new XYPair();
    
    DriveHeadingMaintainer headingMaintainer = new DriveHeadingMaintainer();
    
    /**
     *
     */
    public DriveToPositionWorker() {
        super("DriveToPositionWorker");
    }
    
    /**
     *
     * @param target
     */
    public DriveToPositionWorker(XYPair target) {
        super("DriveToPositionWorker");
        this.target = target;
    }
    
    /**
     *
     * @param target
     */
    public void setSetpoint(XYPair target) {
        pidArray.reset();
        this.target = target;
    }
    
    /**
     *
     */
    public void init() {
        headingMaintainer.SetSetpoint(robot().GetSensorCore().getCurrentHeading());
        headingMaintainer.init();
        pidArray.reset();
    }
    
    /**
     *
     */
    public void exec() {
        XYPair curPos = robot().GetSensorCore().odometry.GetFieldOrientedPosition();
        double [] output = pidArray.calculate(target.toArray(), curPos.toArray(), 
                translationPositionP.get(), 
                translationPositionI.get(), 
                translationPositionD.get());
        
        double rotationalForce = headingMaintainer.MaintainHeading();
        
        robot().GetDriveCore().mecanumDrive_Cartesian(output[0], output[1], 
                rotationalForce, robot().GetSensorCore().getCurrentHeading());
    }

    boolean previousOnTarget = false;
    double enteredOnTargetMarker = -1;
    
    /**
     *
     * @return
     */
    public ExecState isFinished() {
        boolean currentOnTarget = pidArray.isOnTarget(0.5);
        if(!previousOnTarget && currentOnTarget) {
            enteredOnTargetMarker = CommonTools.Get().time.GetMarker();
        } else if(previousOnTarget && 
                currentOnTarget && 
                CommonTools.Get().time.
                GetElapsedMSecFromMarker(enteredOnTargetMarker) > onTargetWaitTimeMS) {
            return ExecState.SUCCESS;
        }
        
        previousOnTarget = currentOnTarget;

        return ExecState.NOT_DONE;
    }
}
