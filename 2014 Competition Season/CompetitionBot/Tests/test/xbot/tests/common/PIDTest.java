/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.common.PID;

/**
 *
 * @author Alex
 */
public class PIDTest {
    PID pid;
    public PIDTest() {
    }
    
    @Before
    public void setUp() {
        pid = new PID();
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
    public void testP() 
    {
        double output;
        output = pid.calculate(100, 0, 1, 0, 0);
        assertTrue("positive p and positive error leads to positive output", output > 0);
        
        output = pid.calculate(-100, 0, 1, 0, 0);
        assertTrue("positive p and negative error leads to negative output", output < 0);
    }
    
    @Test
    public void testOnTarget() {
        double output;
        output = pid.calculate(100, 0, 1, 0, 0);
        assertTrue("on target when within range", pid.isOnTarget(101));
        assertFalse("not on target when outside range", pid.isOnTarget(99));
        
        // negative error case
        output = pid.calculate(-100, 0, 1, 0, 0);
        assertTrue("on target when within range", pid.isOnTarget(101));
        assertFalse("not on target when outside range", pid.isOnTarget(99));
    }
}