/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.system;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import xbot.aerialassist.collection.RangeSensor;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class RangeSensorTest extends BaseTest{
    
    private RangeSensor ranger;
    
    public RangeSensorTest() {
    }
    
    @Before
    public void setUp() {
        ranger = new RangeSensor("Test");
    }
    
    @After
    public void tearDown() {
    }
     
     @Test
     public void TestExtremelyClose() {
            ranger.SetVoltage(0);
            assertTrue("Range should be very far", ranger.GetRange() > 100);
     }
     
     @Test
     public void TestExtremelyFar() {
            ranger.SetVoltage(5);
            double range = ranger.GetRange();
            assertTrue("Range should be pretty small zero but is " + range, range < 10);
     }
}