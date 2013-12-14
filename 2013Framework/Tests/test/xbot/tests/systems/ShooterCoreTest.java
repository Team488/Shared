/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.systems;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.common.Time;
import xbot.core.RobotContext;
import xbot.core.systems.ShooterCore;
import xbot.test.common.BaseTest;
import xbot.test.common.ControlLoopPhysicsSimulator;

/**
 *
 * @author Alex
 */
public class ShooterCoreTest extends BaseTest {
    ShooterCore core;
    public ShooterCoreTest() {
    }
    
    @Before
    public void setUp() {
        core = context.getShooterCore();
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void testManualPower() {
        context.getShooterCore().setShootingPowerMode(ShooterCore.MANUAL_POWER);
        
        assertEquals("wheel power equals manual property", context.getShooterCore().innerShooterManualSpeedP.get(), 
                context.getShooterCore().getInnerShooterWheelSpeed(), 0.001);
        assertEquals("wheel power equals manual property", context.getShooterCore().outerShooterManualSpeedP.get(), 
                context.getShooterCore().getOuterShooterWheelSpeed(), 0.001);
        
        // simulate changing values on smart dashboard
        randomAccessStore.setDouble(context.getShooterCore().innerShooterManualSpeedP.key, 0.66);
        randomAccessStore.setDouble(context.getShooterCore().outerShooterManualSpeedP.key, 1.0);
        
        context.getShooterCore().setShootingPowerMode(ShooterCore.MANUAL_POWER);
        
        assertEquals(0.66, 
                context.getShooterCore().getInnerShooterWheelSpeed(), 0.001);
        assertEquals(1.0, 
                context.getShooterCore().getOuterShooterWheelSpeed(), 0.001);
        
        
        context.getShooterCore().setShootingPowerMode(ShooterCore.STOP);
        
        assertEquals(0, 
                context.getShooterCore().getInnerShooterWheelSpeed(), 0.001);
        assertEquals(0, 
                context.getShooterCore().getOuterShooterWheelSpeed(), 0.001);
    }
    
    @Test
    public void testPIDPower() throws InterruptedException {
        // TODO: these values are tuned for the below fake setup, once
        // we've tuned the real values, tweak the fake setup below to match
        context.getShooterCore().outerShooterPIDPP.set(0.8);
        context.getShooterCore().outerShooterPIDDP.set(0.5);
        
        context.getShooterCore().setShootingPowerMode(ShooterCore.PID_POWER);
        Time time = new Time();
        double fromStartMarker = time.GetMarker();
        double marker = time.GetMarker();
        while(!context.getShooterCore().isAtSpeed()) {
            // timeout after 5s of trying
            if(time.GetElapsedMSecFromMarker(fromStartMarker) > 5000) {
                assertFalse("Timed out getting to PID goal", true);
                break;
            }
            double elapsedMS = time.GetElapsedMSecFromMarker(marker);
            marker = time.GetMarker();
            // update values with physics sim
            double newValue = ControlLoopPhysicsSimulator.update(
                    context.getShooterCore().getOuterShooterWheelSpeed(), 
                    context.getShooterCore().pidGet(), 0.998, 10, elapsedMS);
            context.getShooterCore().setOuterShooterEncoderRate(newValue);
            Thread.sleep(50);
        }
    }
    
    @Test
    public void testFinger() {
        assertEquals("finger defaults to not extended",false, core.isDesiredFingerSolonoidExtended());
        
        core.extendFinger();
        assertEquals("finger extends on demand", true, core.isDesiredFingerSolonoidExtended());
        
        core.setFingerRetractedLimitSwitchPressed(false);
        assertEquals(false, core.isFingerDefinitelyRetracted());
        
        core.setFingerRetractedLimitSwitchPressed(true);
        assertEquals(true, core.isFingerDefinitelyRetracted());
    }
    
    
}