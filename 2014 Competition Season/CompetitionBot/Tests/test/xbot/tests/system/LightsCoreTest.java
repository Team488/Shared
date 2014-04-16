/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.aerialassist.systems.LightsCore;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class LightsCoreTest extends BaseTest {
    LightsCore core;
    public LightsCoreTest() {
    }
    
    @Before
    public void setUp() {
        core = context.getLightsCore();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testRed() {
        context.GetOICore().setIsRedAlliance(true);
        core.updateLights();
        assertTrue(core.isArduino1());
    }
    
    @Test
    public void testBlue() {
        context.GetOICore().setIsRedAlliance(false);
        core.updateLights();
        assertTrue(!core.isArduino1());
    }
    
    @Test
    public void testDisabled() {
        context.GetTelemetryCore().setIsDisabled(true);
        core.updateLights();
        assertTrue(core.isArduino2());
        assertTrue(!core.isArduino3());
    }
    
    @Test
    public void testIsAsking() {
        context.GetTelemetryCore().setIsDisabled(false);
        core.setIsAskingForBall(true);
        core.updateLights();
        assertTrue(!core.isArduino2());
        assertTrue(core.isArduino3());
    }
    
    @Test
    public void testArmsManual() {
        context.GetTelemetryCore().setIsDisabled(false);
        core.setIsAskingForBall(false);
        context.getCollectionCore().getFrontCollector().setIsManual(true);
        core.updateLights();
        assertTrue(core.isArduino2());
        assertTrue(core.isArduino3());
    }
}