/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.core;

import xbot.common.CommonTools;
import xbot.common.ITableProxy;
import xbot.common.logging.LogWriter;
import xbot.common.Time;
import xbot.core.systems.BucketCore;
import xbot.core.systems.CollectorCore;
import xbot.core.systems.DriveCore;
import xbot.core.systems.OICore;
import xbot.core.systems.ShooterCore;

/**
 *
 * @author Alex
 */
public class RobotContext {
    
    private static RobotContext instance = null;
    
    private RobotContext()
    {
        intializeSystems();
    }
    
    public static RobotContext Get() 
    {
        if(CommonTools.Get() == null) {
            throw new IllegalStateException("Need to initialize CommonTools before calling on it");
        }
        if (instance == null)
        {
            instance = new RobotContext();
        }
        return instance;
    }
    
    public static void ResetRobotContextForTestingPurposesOnly() {
        instance = null;
    }
    
    private void intializeSystems() {
        this.driveCore = new DriveCore();
        this.bucketCore = new BucketCore();
        this.oiCore = new OICore();
        this.shooterCore = new ShooterCore();
        this.collectorCore = new CollectorCore();
    }
    
    private DriveCore driveCore;
    public DriveCore GetDriveCore() {
        return driveCore;
    }
    
    private BucketCore bucketCore;
    public BucketCore GetBucketCore() {
        return bucketCore;
    }
    
    private OICore oiCore;
    public OICore GetOICore() {
        return oiCore;
    }
    
    private ShooterCore shooterCore;
    public ShooterCore getShooterCore() {
        return shooterCore;
    }
    
    private CollectorCore collectorCore;
    /**
     * Get the value of collectorCore
     *
     * @return the value of collectorCore
     */
    public CollectorCore getCollectorCore() {
        return collectorCore;
    }


}
