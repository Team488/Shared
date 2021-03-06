/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.drive;
import xbot.common.ExecState;

import xbot.aerialassist.AerialWorkerBase;
import xbot.common.CommonTools;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author X-Bot(Noobies)
 */

public class DriveForTimeWorker extends AerialWorkerBase{
    DriveHeadingMaintainer headingMaintainer = new DriveHeadingMaintainer();
    double timeMarker;
    double getTime;
    double x;
    double y;
    double timeOutInMs; 
    boolean isMecanum;
    DoubleProperty timeOutProperty = null;
    DoubleProperty xProperty;
    DoubleProperty yProperty;
    
    /**
     *
     * @param x
     * @param y
     * @param timeOutInMs
     */
    public DriveForTimeWorker(double x, double y, double timeOutInMs, boolean isMecanum)
    { 
        super ("DriveForTimeWorker");
        this.x = x;
        this.y = y;
        this.timeOutInMs = timeOutInMs;
        this.isMecanum = isMecanum;
    }
    
/**
     *
     * @param x
     * @param y
     * @param timeOutInMs
     */
    public DriveForTimeWorker(double x, double y, DoubleProperty timeOut, boolean isMecanum)
    { 
        super ("DriveForTimeWorker");
        this.x = x;
        this.y = y;
        this.timeOutProperty = timeOut;
        this.isMecanum = isMecanum;
        
    }
    
    public DriveForTimeWorker(DoubleProperty x, DoubleProperty y, DoubleProperty timeout, boolean isMecanum)
    {
        super("DriveForTimeWorker");
        this.xProperty = x;
        this.yProperty = y;
        this.timeOutProperty = timeout;
        this.isMecanum = isMecanum;
    }
    
    /**
     *
     */
    public void init ()
    {
        // if using a property, get the value from there
        if (xProperty != null) {
            x = xProperty.get();
        }
        
        if (yProperty != null) {
            y = yProperty.get();
        }
        
        if(timeOutProperty != null) {
            timeOutInMs = timeOutProperty.get();
        }
        
        Info("init x = " + x + ", y = " + y + ", timeout = " + timeOutInMs);
        headingMaintainer.SetSetpoint(robot().GetSensorCore().getCurrentHeading());
        headingMaintainer.init();
        timeMarker = CommonTools.Get().time.GetMarker();
        
    }
        
    public void exec() {
        double rotationalForce = headingMaintainer.MaintainHeading();
        if(isMecanum)
        {
            robot().GetDriveCore().mecanumDrive_Cartesian(x, y, 
                rotationalForce, robot().GetSensorCore().getCurrentHeading());
        }
        else
        {
            robot().GetDriveCore().tankDrive(x,y);
        }
    }    
        
    /**
     *
     * @return
     */
    public ExecState isFinished(){
        double timeSinceMarker = CommonTools.Get().time.GetElapsedMSecFromMarker(timeMarker);
        if (timeSinceMarker < timeOutInMs){
            return ExecState.NOT_DONE;
        }else {
            robot().GetDriveCore().stop();
            return ExecState.SUCCESS;
        }
                
        
        
    }
              
        
    
  
        
}
        
             
    

