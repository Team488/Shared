/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.tests.common;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import xbot.common.Formatter;

/**
 *
 * @author sterlingdorminey
 */
public class FormatterTest {
    @Test
    public void testFormatter() {
        boolean wasException = false;
        assertEquals("NaN", Formatter.format(Double.NaN, 100));
        assertEquals("2.0", Formatter.format(2.0, 100));
        assertEquals("2.3", Formatter.format(2.3, 100));
        assertEquals("2.3", Formatter.format(2.31, 1));
        assertEquals("1.525E-5", Formatter.format(Math.pow(2.0, -16), 3));
        try {
            Formatter.format(2.0, 0);
        } catch (IllegalArgumentException e) {
            wasException = true;
        }
        assertEquals(true, wasException);
    }
}
