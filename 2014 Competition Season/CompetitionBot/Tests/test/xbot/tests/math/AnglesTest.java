/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.math;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.common.math.Angles;
/**
 *
 * @author John
 */
public class AnglesTest {
    
    public AnglesTest(){}
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testVariousAngles() 
    {
        assertEquals(-11.0, Angles.DifferenceBetweenDegrees(1, 350), 0.001);
        
        assertEquals(11.0, Angles.DifferenceBetweenDegrees(350, 1), 0.001);
        
        assertEquals(0.0, Angles.DifferenceBetweenDegrees(100, 100), 0.001);
        
        assertEquals(0.0, Angles.DifferenceBetweenDegrees(370, 10), 0.001);
    }
    
    @Test
    public void testNegativeAngles()
    {
        assertEquals(30, Angles.DifferenceBetweenDegrees(-30, 0), 0.001);
        assertEquals(30, Angles.DifferenceBetweenDegrees(-40, -10), 0.001);
        assertEquals(30, Angles.DifferenceBetweenDegrees(-50, -20), 0.001);
        assertEquals(-30, Angles.DifferenceBetweenDegrees(-20, -50), 0.001);
    }
    
    @Test
    public void test180() {
        for(int i = 0; i < 360; i++) {
            assertTrue("testing distance angle " + i + " from 180", Math.abs(Angles.DifferenceBetweenDegrees(i, 180)) <= 180);
        }
    }
}
