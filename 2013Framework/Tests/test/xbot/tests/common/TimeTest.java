/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.common;

import org.junit.After;
import org.junit.Before;
import xbot.common.Time;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Alex
 */
public class TimeTest {
    
    Time time;
    
    public TimeTest() {
    }
    
    @Before
    public void setUp() {
        time = new Time();
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
    public void testTimeInterval() throws InterruptedException {
        double marker = time.GetMarker();
        assertTrue("Marker >= zero", marker >= 0);
        
        Thread.sleep(200);
        
        double elapseTimeInMS = time.GetElapsedMSecFromMarker(marker);
        
        assertEquals("Time is roughly 200ms", 200, elapseTimeInMS, 5);
    }
}