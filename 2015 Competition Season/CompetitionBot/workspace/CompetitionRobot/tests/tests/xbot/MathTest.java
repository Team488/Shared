package tests.xbot;

import static org.junit.Assert.*;

import org.junit.Test;

import xbot.common.math.XYPair;

public class MathTest extends BaseTestCase
{
    @Test
    public void testXYPair()
    {
        XYPair pair = new XYPair(1, 0);
        
        assertEquals(0, pair.getAngle(), 0);
        assertEquals(1, pair.getMagnitude(), 0);
        
        pair.rotate(90);
        
        assertEquals(0, pair.x, 0.0001);
        assertEquals(1, pair.y, 0.0001);
        assertEquals(90, pair.getAngle(), 0);
        assertEquals(1, pair.getMagnitude(), 0);
        
    }
}
