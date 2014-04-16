/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.tests.math;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import xbot.common.math.XYPair;

/**
 *
 * @author sterlingdorminey
 */
public class XYPairTest {

    private class TestEntry {
        public XYPair first;
        
        public XYPair second;
        
        public double expected;
        
        public TestEntry(XYPair first, XYPair second, double expected) {
            this.first = first;
            this.second = second;
            this.expected = expected;
        }
        
        public void assertCorrect(int index) {
            double actual = this.first.project(this.second);
            
            assertEquals("Test " + index + " failed.", expected, actual, 0.01);
        }
    }
    
    private final List<TestEntry> tests;
    
    public XYPairTest() {
        this.tests = Arrays.asList(
                new TestEntry(new XYPair(1, 0), new XYPair(1, 0), 1),
                new TestEntry(new XYPair(1, 1), new XYPair(0, 0), 0),
                new TestEntry(new XYPair(1, 0), new XYPair(0, 1), 0),
                new TestEntry(new XYPair(1, 1), new XYPair(-1, -1), -Math.sqrt(2)),
                new TestEntry(new XYPair(-1, -1), new XYPair(1, 1), -Math.sqrt(2)),
                new TestEntry(new XYPair(1, 1), new XYPair(0, 0), 0),
                // Chosen from wolfram alpha.
                new TestEntry(new XYPair(4.5, 3.5), new XYPair(9.6, 3.4), 9.66518)
        );
    }
    @Test
    public void testProjectVector() {
        int i = 0;
        for (TestEntry test : tests) {
            test.assertCorrect(i++);
        }
    }
    
    @Test
    public void testMagnitude() {
        assertEquals(1, new XYPair(1, 0).magnitude(), 0.01);
        assertEquals(1, new XYPair(0, 1).magnitude(), 0.01);
        assertEquals(Math.sqrt(2), new XYPair(1, 1).magnitude(), 0.01);
        assertEquals(0, new XYPair(0, 0).magnitude(), 0.01);
        assertEquals(1, new XYPair(-1, 0).magnitude(), 0.01);
        assertEquals(Math.sqrt(2), new XYPair(-1, -1).magnitude(), 0.01);
    }
    
    @Test
    public void testFromPolar() {
        assertTrue(new XYPair(1, 0).equals(XYPair.fromPolarCoordinates(1, 0), 0.01));
        assertTrue(new XYPair(0, 1).equals(XYPair.fromPolarCoordinates(1, 90), 0.01));
        assertTrue(new XYPair(0, 0).equals(XYPair.fromPolarCoordinates(0, 32), 0.01));
    }
}
