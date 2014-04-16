/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.catapult;

import org.junit.Test;
import xbot.aerialassist.systems.CatapultCore;
import xbot.test.common.BaseTest;
import static org.junit.Assert.*;
import org.junit.Before;
import xbot.aerialassist.catapult.CatapultPIDThatFinishesWorker;
import xbot.common.ExecState;
/**
 *
 * @author sterlingdorminey
 */
public class CatapultPIDThatFinishesWorkerTests extends BaseTest {
    
    CatapultPIDThatFinishesWorker worker;
    
    @Before
    public void before()
    {
        context.GetCatapultCore().shouldCockCatapult.set(true);
    }
    
    @Test
    public void basicTest()
    {
        
        worker = new CatapultPIDThatFinishesWorker();
        CatapultCore cataCore = context.GetCatapultCore();
        cataCore.setCatapultScaledPositionTESTONLY(1);
        
        worker.init();
        worker.exec();
        
        assertTrue("Motor should be driving forward", cataCore.getCatapultMotor() > 0);
        assertTrue("Worker should NOT be finished", worker.isFinished() == ExecState.NOT_DONE);
        
        cataCore.setCatapultScaledPositionTESTONLY(context.GetCatapultCore().catapultCockedPosition.get());
        worker.exec();
        
        assertTrue("Worker should be finished", worker.isFinished() == ExecState.SUCCESS);
        assertEquals("Motor should be stopped when at target", 0, cataCore.getCatapultMotor(), 0.001);
        
        cataCore.setCatapultScaledPositionTESTONLY(0);
        worker.exec();
        
        assertEquals("Motor should be stopped when beyond target", 0, cataCore.getCatapultMotor(), 0.001);
        assertTrue("Worker should be finished", worker.isFinished() == ExecState.SUCCESS);
    }
}
