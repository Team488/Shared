/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.system;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import xbot.aerialassist.systems.SensorCore;
import xbot.test.common.BaseTest;
import xbot.aerialassist.workers.ResetGyroWorker;
import xbot.common.CommonTools;
import xbot.common.math.XYPair;

/**
 *
 * @author Alex
 */
public class SensorCoreTest extends BaseTest{
    
    public SensorCoreTest() {
    }
    
    @Before
    public void setUp() {
    }
    SensorCore sensor;
    
    @After
    public void tearDown() {
    }
     
     @Test
     public void GyroTestFromZero() {
         sensor = new SensorCore();
         assertEquals("Initialize gyros", 180, sensor.getCurrentHeading(),0.001);    
         sensor.setGyroAngles(15.0, false);
         assertEquals("Rotated 15 degrees",195,sensor.getCurrentHeading(),0.001);
         sensor.setGyroAngles(0, false);
         assertEquals("Rotated -15 degrees", 180, sensor.getCurrentHeading(),0.001);         
     }
     
     @Test
     public void Over360HeadingTest() {
         context.GetSensorCore().resetHeading();
         assertEquals("Initialize gyros", 90, context.GetSensorCore().getCurrentHeading(),0.001);
         context.GetSensorCore().setGyroAngles(90, false);
         context.GetSensorCore().setGyroAngles(180, false);
         context.GetSensorCore().setGyroAngles(270, false);
         context.GetSensorCore().setGyroAngles(370, false);
         assertEquals("Rotate 370 degrees", 100, context.GetSensorCore().getCurrentHeading(),0.001);
         context.GetSensorCore().setGyroAngles(250, false);
         assertEquals("Rotate -120 degrees", 340, context.GetSensorCore().getCurrentHeading(),0.001);
     }
     
     @Test
     public void IMUValuesTest() {
         context.GetSensorCore().resetHeading();
         assertEquals("Initialize gyros", 90, context.GetSensorCore().getCurrentHeading(),0.001);
         context.GetSensorCore().setGyroAngles(170, false);
         assertEquals("Rotate 170 degrees", 260, context.GetSensorCore().getCurrentHeading(), 0.001);
         context.GetSensorCore().setGyroAngles(-170, false);
         assertEquals("Rotate 20 degrees", 280, context.GetSensorCore().getCurrentHeading(),0.001);
         context.GetSensorCore().setGyroAngles(170, false);
         assertEquals("Rotate -20 degrees", 260, context.GetSensorCore().getCurrentHeading(), 0.001);
     }
     
     @Test
     public void ResetHeadingTest() {
         ResetGyroWorker resetGyro = new ResetGyroWorker();
         resetGyro.exec();
         assertEquals("Initialize gyros", 90, context.GetSensorCore().getCurrentHeading(),0.001);
         context.GetSensorCore().setGyroAngles(100, false);
         assertEquals("Rotate 100 degrees", 190, context.GetSensorCore().getCurrentHeading(), 0.001);
         resetGyro.exec();
         context.GetSensorCore().setGyroAngles(110, false);
         assertEquals("Reset and rotate 10 degrees", 100, context.GetSensorCore().getCurrentHeading(), 0.001);
     }
     
     @Test
     public void SomethingTest() 
     {
         /*
          * private DoubleProperty currentHeading = new DoubleProperty("currentHeading", GyroOffset);
    private DoubleProperty gyroGain = new DoubleProperty("gyroGain", 1);
    private DoubleProperty enableOdometry = new DoubleProperty("EnableOdometry", 0.0);
    * private DoubleProperty leftWheelGain = 
            new DoubleProperty("LeftFollowWheelGain", 1.0);
    private DoubleProperty rightWheelGain = 
            new DoubleProperty("LeftFollowWheelGain", -1.0);
    private DoubleProperty centerWheelGain = 
            new DoubleProperty("LeftFollowWheelGain", 1.0);
    
    private DoubleProperty samplesToAverage = 
            new DoubleProperty("VelocitySamplesToAverage", 5.0);
    private DoubleProperty sampleDecay = 
            new DoubleProperty("VelocityAverageDecayValue", 1.0);
            * 
            * 
    private DoubleProperty wheelCircumference = 
            new DoubleProperty("WheelCircumference", 1.0);
    private DoubleProperty ticksPerRevolution = 
            new DoubleProperty("TicksPerWheelEncoderRevolution", 360.0);
          */
         context.GetSensorCore().resetHeading();
         
         CommonTools.Get().propertyManager.randomAccessStore.setBoolean("EnableOdometry", true);
         CommonTools.Get().propertyManager.randomAccessStore.setDouble("WheelCircumference", 1.0);
         CommonTools.Get().propertyManager.randomAccessStore.setDouble("TicksPerWheelEncoderRevolution", 1.0);
         
         
         // Test Y
         context.GetSensorCore().DepositLocationInformationFromRobot(0, 10, 10, 0);
         XYPair robotPosition = context.GetSensorCore().odometry.GetFieldOrientedPosition();
         assertEquals("Robot X Position", 0, robotPosition.X, 0.001);
         assertEquals("Robot Y Position", 10, robotPosition.Y, 0.001);
         
         // Test X
         context.GetSensorCore().DepositLocationInformationFromRobot(0, 10, 10, 10);
         robotPosition = context.GetSensorCore().odometry.GetFieldOrientedPosition();
         assertEquals("Robot X Position", 10, robotPosition.X, 0.001);
         assertEquals("Robot Y Position", 10, robotPosition.Y, 0.001);
         
         // Test that when wheels are stationary, gyro is not updated
         context.GetSensorCore().DepositLocationInformationFromRobot(90, 10, 10, 10);
         robotPosition = context.GetSensorCore().odometry.GetFieldOrientedPosition();
         assertEquals("Robot X Position", 10, robotPosition.X, 0.001);
         assertEquals("Robot Y Position", 10, robotPosition.Y, 0.001);
         assertEquals("Robot orientation should not updated when wheels are still", 90, context.GetSensorCore().getCurrentHeading(), 0.001);
         
         // Test that when gyro is updated and wheels move, field oriented position updates.
         context.GetSensorCore().DepositLocationInformationFromRobot(180, 20, 20, 10);
         robotPosition = context.GetSensorCore().odometry.GetFieldOrientedPosition();
         assertEquals("Robot X Position", 0, robotPosition.X, 0.001);
         assertEquals("Robot Y Position", 10, robotPosition.Y, 0.001);
         assertEquals("Robot orientation should be updated when wheels are in motion", 180, context.GetSensorCore().getCurrentHeading(), 0.001);
     }
}