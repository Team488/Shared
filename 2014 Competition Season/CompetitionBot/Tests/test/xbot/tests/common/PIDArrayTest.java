/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.common.PIDArray;

/**
 *
 * @author Alex
 */
public class PIDArrayTest {
    PIDArray pidArray;
    
    public PIDArrayTest() {
    }
    
    @Before
    public void setUp() {
        pidArray = new PIDArray();
    }
    

    @Test
    public void testArrayP() {
        double[] output;
        
        double[] goals = new double[2];
        double[] currents = new double[2];
        
        goals[0] = goals[1] = 100;
        currents[0] = currents[1] = 0;
        
        output = pidArray.calculate(goals, currents, 1, 0, 0);
        
        assertTrue("positive p and positive error leads to positive output", output[0] > 0);
        assertTrue("positive p and positive error leads to positive output", output[1] > 0);
        
        goals[0] = - 100;
        goals[1] = 100;
        currents[0] = currents[1] = 0;
        
        output = pidArray.calculate(goals, currents, 1, 0, 0);
        
        assertTrue("positive p and negative error leads to negative output", output[0] < 0);
        assertTrue("positive p and positive error leads to positive output", output[1] > 0);
        
    }
}