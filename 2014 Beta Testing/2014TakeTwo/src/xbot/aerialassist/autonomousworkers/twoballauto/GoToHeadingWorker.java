/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.aerialassist.AerialWorkerBase;
import xbot.aerialassist.drive.SetDesiredHeadingWorker;
import xbot.aerialassist.drive.DriveHeadingMaintainer;
import xbot.common.ExecState;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author sterlingdorminey
 */
public class GoToHeadingWorker extends AerialWorkerBase {
    private final DoubleProperty rotationTolerance =
            new DoubleProperty("RotationTolerance", 3.0);
    
    private final DoubleProperty orientation;
    
    private final DriveHeadingMaintainer maintainer;
    
    public GoToHeadingWorker(DoubleProperty orientation) {
        super("MaintainHeadingWorker");
        
        this.orientation = orientation;
        this.maintainer = new DriveHeadingMaintainer();
    }
    
    public void init() {
        SetDesiredHeadingWorker setHeading = 
                new SetDesiredHeadingWorker(this.orientation.get());
        setHeading.init();
        setHeading.exec();
        
        // We need to call this initially so that it will establish
        // input and target values for the maintainer, so that
        // isOnTarget() will work.
        this.maintainer.MaintainHeading();
        
        super.init();
    }
    
    public void exec() {
        double gyroAngle = this.robot().GetSensorCore().getCurrentHeading();
        double rotation = this.maintainer.MaintainHeading();
        this.robot().GetDriveCore().mecanumDrive_Cartesian(0, 0, rotation, gyroAngle);
    }
    
    public ExecState isFinished() {
        if (this.maintainer.isOnTarget(this.rotationTolerance.get())) {
            return ExecState.SUCCESS;
        }
        
        return ExecState.NOT_DONE;
    }
}
