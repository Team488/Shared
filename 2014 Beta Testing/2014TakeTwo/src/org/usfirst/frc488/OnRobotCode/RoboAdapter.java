/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc488.OnRobotCode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.systems.CatapultCore;
import xbot.aerialassist.systems.DriveCore;
import xbot.aerialassist.systems.OICore;
import xbot.aerialassist.systems.SensorCore;
import xbot.aerialassist.systems.LightsCore;
import xbot.common.CommonTools;
import xbot.common.Loggable;
import xbot.common.logging.Profiler;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author John
 */
public class RoboAdapter extends Loggable {
    
    private static RoboAdapter instance;
    Double previousUpdate = null;
    BooleanProperty isBoggedDown = 
            new BooleanProperty("isBoggedDown", false);
    DoubleProperty currentUpdateDelayMS = 
            new DoubleProperty("currentUpdateDelayMS", 0.0);
    
    Profiler senseProfiler = new Profiler("RoboAdapter:readVolt", 100);
    Profiler imuProfiler = new Profiler("RoboAdapter:IMU", 100);
    Profiler volProfiler = new Profiler("RoboAdapter:writeVolt", 100);
    
    BooleanProperty arduino1 = new BooleanProperty("Arduino1", false);
    BooleanProperty arduino2 = new BooleanProperty("Arduino2", false);
    BooleanProperty arduino3 = new BooleanProperty("Arduino3", false);
    
    
    protected RoboAdapter()
    {
        super("RoboAdapter");
        //just to make sure people don't accidentially initialize the object directly. 
    }
    
    public static RoboAdapter GetInstance()
    {
        if (instance == null)
        {
            instance = new RoboAdapter();
        }
        
        return instance;
    }
    
    boolean oldPanelShouldCock = false;
    
    public void UpdateNormal()
    {
        if(previousUpdate != null) {
            double deltaInMS = CommonTools.Get().time.GetElapsedMSecFromMarker(previousUpdate.doubleValue());
            currentUpdateDelayMS.set(deltaInMS);
            // robot normally should be calling this every 20-50ms, warn if longer
            if(deltaInMS > 200) {
                Warning("Time between update calls was " + deltaInMS + "ms");
                isBoggedDown.set(true);
            } else {
                isBoggedDown.set(false);
            }
        }
        previousUpdate = new Double(CommonTools.Get().time.GetMarker());
        
        //here, we grab the RobotContext instance and assign values to the motors.
        //drive system
        DriveCore drive = RobotContext.Get().GetDriveCore();
        OICore oi = RobotContext.Get().GetOICore();
        Collector frontCollector = RobotContext.Get().getCollectionCore().getFrontCollector();
        Collector backCollector = RobotContext.Get().getCollectionCore().getBackCollector();
        SensorCore sensors = RobotContext.Get().GetSensorCore();
        CatapultCore catapult = RobotContext.Get().GetCatapultCore();
        LightsCore lights = RobotContext.Get().getLightsCore();
        
        //Drive
        RobotMap.actuatorsLeftFrontController.set(-drive.getLeftMotorFront());
        RobotMap.actuatorsLeftRearController.set(-drive.getLeftMotorRear());
        //Since motors are mirrored, invert the right motors so they go in the same
        //direction
        RobotMap.actuatorsRightFrontController.set(drive.getRightMotorFront());
        RobotMap.actuatorsRightRearController.set(drive.getRightMotorRear());
        
        RobotMap.actuatorsShiftSolenoid.set(!drive.getIsMecanum());
        
        // Collectors
        RobotMap.actuatorsFrontCollectorRoller.set(frontCollector.getCollectorRollerMotor());
        RobotMap.actuatorsFrontDeployMotor.set(-1 * frontCollector.getDeployMotor());
        RobotMap.actuatorsRearCollectorRoller.set(backCollector.getCollectorRollerMotor());
        RobotMap.actuatorsRearDeployMotor.set(backCollector.getDeployMotor());
        
        RobotMap.actuatorsCatapultController.set(catapult.getCatapultMotor());
        
        lights.updateLights();
        RobotMap.actuatorsArduino1.set(lights.isArduino1());
        RobotMap.actuatorsArduino2.set(lights.isArduino2());
        RobotMap.actuatorsArduino3.set(lights.isArduino3());
        
        //RobotMap.actuatorsArduino1.set(arduino1.get());
        //RobotMap.actuatorsArduino2.set(arduino2.get());
        //RobotMap.actuatorsArduino3.set(arduino3.get());
        
        // We no longer have enough digital outputs for Arduino4. Limit switches took the space.
        ///RobotMap.actuatorsArduino4.set(lights.isArduino4());
        
        //make the compressor go?
        if(RobotContext.Get().GetPneumaticCore().isCompressorOn()){
             RobotMap.actuatorsCompressor.start();
        }else{
            RobotMap.actuatorsCompressor.stop();
        }
        
        RobotMap.actuatorsEjectorSolenoid.set(
                catapult.isEjectorSolenoidPunching());
        
        oi.setIsRedAlliance(
                DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red);
        
        //now, we grab all our "sensors" and publish them to the Context.
        //  Right now, that's just the OI joysticks.
        oi.setLeftJoyX(Robot.oi.leftJoystick.getX());
        oi.setLeftJoyY(-Robot.oi.leftJoystick.getY());
        oi.setRightJoyX(-Robot.oi.rightJoystick.getX());
        oi.setRightJoyY(-Robot.oi.rightJoystick.getY());
        
        oi.setOperatorJoyX(Robot.oi.operatorJoystick.getX());
        oi.setOperatorJoyY(-Robot.oi.operatorJoystick.getY());

        // Catapult
        catapult.setCatapultPositionVoltage(
                RobotMap.sensorsCatapultPotentiometer.getVoltage());
        catapult.SetBallSensorVoltage(RobotMap.sensorsBallSettledInCatapult.getVoltage());
        
        //senseProfiler.startProfile();
        final double frontVoltage = RobotMap.sensorsFrontCollectorPot.getVoltage();
        final double backVoltage = RobotMap.sensorsBackCollectorPot.getVoltage();
        senseProfiler.stopProfile();
        
        //volProfiler.startProfile();

        boolean panelShouldCock = Robot.oi.getOperatorPanel().getRawButton(11);
        
        if (panelShouldCock != oldPanelShouldCock)
        {
            catapult.shouldCockCatapult.set(panelShouldCock);
        }
        
        oldPanelShouldCock = panelShouldCock;
        
        frontCollector.getSensors().SetCurrentVoltage(frontVoltage);
        frontCollector.setIsManual(!Robot.oi.operatorPanel.getRawButton(5));
        frontCollector.getSensors().setManualDown(Robot.oi.operatorPanel.getRawButton(1));
        frontCollector.getSensors().setManualIntake(Robot.oi.operatorPanel.getRawButton(4));
        frontCollector.getSensors().setManualExpel(Robot.oi.operatorPanel.getRawButton(3));
        frontCollector.getSensors().setLimitSwitches(
                !RobotMap.sensorsFrontUpLimitSwitch.get(), 
                !RobotMap.sensorsFrontDownLimitSwitch.get());
        
        // set the blobcount on vision core based on the smartdashboard value from the driver station
        try {
            double blobCount = SmartDashboard.getNumber("BLOB_COUNT");
            RobotContext.Get().GetVisionCore().blobCount.set(blobCount);
        } catch (TableKeyNotDefinedException e) {
            RobotContext.Get().GetVisionCore().blobCount.set(0);
        }
        
        boolean frontManualUp;
        boolean backManualUp;
                
//        if (Robot.oi.operatorPanel.getThrottle()> 0.75)
//        {
//            frontManualUp = true;
//            backManualUp = true;
//        }
//        else
//        {
            frontManualUp = Robot.oi.operatorPanel.getRawButton(2);
            backManualUp = Robot.oi.operatorPanel.getRawButton(9);
        //}
        frontCollector.getSensors().setManualUp(frontManualUp);
        backCollector.getSensors().setManualUp(backManualUp);
        
        
        backCollector.getSensors().SetCurrentVoltage(backVoltage);
        backCollector.setIsManual(!Robot.oi.operatorPanel.getRawButton(6));
        backCollector.getSensors().setManualDown(Robot.oi.operatorPanel.getRawButton(10));
        backCollector.getSensors().setManualIntake(Robot.oi.operatorPanel.getRawButton(7));
        backCollector.getSensors().setManualExpel(Robot.oi.operatorPanel.getRawButton(8));
        backCollector.getSensors().setLimitSwitches(
                !RobotMap.sensorsBackUpLimitSwitch.get(),
                !RobotMap.sensorsBackDownLimitSwitch.get());
        
        oi.setRollerShouldIntake(Robot.oi.operatorButton2.get());
        oi.setRollerShouldExpel(Robot.oi.operatorButton3.get());
        
        // Autonomous
        oi.setLeftAutoSwitch(AxisToAutoNumber(Robot.oi.operatorPanel.getX()));
        oi.setMiddleAutoSwitch(AxisToAutoNumber(Robot.oi.operatorPanel.getY()));
        oi.setRightAutoSwitch(AxisToAutoNumber(Robot.oi.operatorPanel.getZ()));
        
        //volProfiler.stopProfile();
        //gyro publish
        //imuProfiler.startProfile();
        sensors.DepositLocationInformationFromRobot(
        		0,0,0,0);
        //imuProfiler.stopProfile();
    }
    
    private int AxisToAutoNumber(double axis)
    {
        if (axis < -0.33)
        {
            // Switch up
            return 2;
        }
        if (axis > 0.33)
        {
            // Switch down
            return 0;
        }
        // Switch in the middle
        return 1;
    }
}
