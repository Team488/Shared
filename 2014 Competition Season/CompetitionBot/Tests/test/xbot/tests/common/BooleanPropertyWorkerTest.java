/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.tests.common;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import xbot.common.BooleanPropertyWorker;
import xbot.common.ExecState;
import xbot.common.properties.BooleanProperty;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
public class BooleanPropertyWorkerTest extends BaseTest {
    @Test
    public void testWorker() {
        BooleanProperty bool = new BooleanProperty("Foo", true);
        BooleanPropertyWorker worker = new BooleanPropertyWorker(bool);
        
        assertEquals(ExecState.SUCCESS, worker.isFinished());
        bool.set(false);
        assertEquals(ExecState.FAILURE, worker.isFinished());
    }
}
