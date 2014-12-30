/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist;

import xbot.aerialassist.systems.AutonomousCore;
import xbot.aerialassist.systems.CatapultCore;
import xbot.aerialassist.systems.CollectionCore;
import xbot.aerialassist.systems.DiagnosticCore;
import xbot.aerialassist.systems.DriveCore;
import xbot.aerialassist.systems.LightsCore;
import xbot.aerialassist.systems.OICore;
import xbot.aerialassist.systems.PneumaticCore;
import xbot.aerialassist.systems.SensorCore;
import xbot.aerialassist.systems.TelemetryCore;
import xbot.aerialassist.systems.VisionCore;
import xbot.common.CommonTools;


/**
 * RobotContext is a singleton that contains the "ideal" form of the 2014 Robot. It's home
 * to representations of all the 2014 robot's actuators, sensors, and the like.
 * @author Alex
 */
public class RobotContext {
    
    private static RobotContext instance = null;
   
    
    private RobotContext()
    {
        intializeSystems();
    }
    
    /**
     * This method returns the RobotContext singleton. It can only be called after
     * CommonTools has been initialized, since it depends on the CommonTools for things like
     * properties, logging, and time.
     * @return
     */
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
    
    /**
     * THIS METHOD IS SHOULD ONLY BE USED FOR TEST CASES! NEVER CALL THIS ON THE ROBOT!
     */
    public static void ResetRobotContextForTestingPurposesOnly() {
        instance = null;
    }
    
    private void intializeSystems() {
        this.driveCore = new DriveCore();
        this.oiCore = new OICore();
        this.sensorCore = new SensorCore();
        this.visionCore = new VisionCore();
        this.pneumaticCore = new PneumaticCore();
        this.catapultCore = new CatapultCore();
        this.telemetryCore = new TelemetryCore();
        this.diagnosticCore = new DiagnosticCore();
        this.collectionCore = new CollectionCore();
        this.lightsCore = new LightsCore();
        this.autonomousCore = new AutonomousCore();
    }
    
    private DriveCore driveCore;
    /**
     *
     * @return
     */
    public DriveCore GetDriveCore() {
        return driveCore;
    }
    
    private OICore oiCore;
    /**
     *
     * @return
     */
    public OICore GetOICore() {
        return oiCore;
    }

    private SensorCore sensorCore;
    /**
     *
     * @return
     */
    public SensorCore GetSensorCore() {
        return sensorCore;
    }
    
    private VisionCore visionCore;
    /**
     *
     * @return
     */
    public VisionCore GetVisionCore()
    {
        return visionCore;
    }
    
    private PneumaticCore pneumaticCore;
    /**
     *
     * @return
     */
    public PneumaticCore GetPneumaticCore()
    {
        return pneumaticCore;
    }
    
    private CatapultCore catapultCore;
    /**
     *
     * @return
     */
    public CatapultCore GetCatapultCore(){
        return catapultCore;
    }
    
    private TelemetryCore telemetryCore;
    /**
     *
     * @return
     */
    public TelemetryCore GetTelemetryCore()
    {
        return telemetryCore;
    }
    
    private DiagnosticCore diagnosticCore;
    /**
     *
     * @return
     */
    public DiagnosticCore GetDiagnosticCore()
    {
        return diagnosticCore;
    }
    
    private CollectionCore collectionCore;
    /**
     *
     * @return
     */
    public CollectionCore getCollectionCore() {
        return collectionCore;
    }

    private LightsCore lightsCore;

    /**
     * Get the value of lightsCore
     *
     * @return the value of lightsCore
     */
    public LightsCore getLightsCore() {
        return lightsCore;
    }
    
    private AutonomousCore autonomousCore;
    public AutonomousCore getAutonomousCore()
    {
        return autonomousCore;
    }

}
