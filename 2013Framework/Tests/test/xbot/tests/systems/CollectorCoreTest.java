/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.systems;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class CollectorCoreTest extends BaseTest {
    
    public CollectorCoreTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testCollector() {
        context.getCollectorCore().stop();
        assertEquals(
                0, 
                context.getCollectorCore().getRequestedDeviceMotorSpeed(), 
                0.001);
        
        context.getCollectorCore().collectDiscsSpeed();
        assertEquals(
                context.getCollectorCore().intakeSpeed.get(), 
                context.getCollectorCore().getRequestedDeviceMotorSpeed(), 
                0.001);
        
    }
}