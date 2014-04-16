/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.catapult;

import org.junit.Test;
import xbot.aerialassist.catapult.CatapultPIDWorker;
import xbot.aerialassist.systems.CatapultCore;
import xbot.test.common.BaseTest;
import static org.junit.Assert.*;
import org.junit.Before;
/**
 *
 * @author sterlingdorminey
 */
public class CatapultPIDWorkerTests extends BaseTest {
    
    CatapultPIDWorker worker;
    
    @Before
    public void setup()
    {
        context.GetCatapultCore().shouldCockCatapult.set(true);
    }
    
    @Test
    public void basicTest()
    {
        worker = new CatapultPIDWorker(0.1);
        CatapultCore cataCore = context.GetCatapultCore();
        cataCore.setCatapultScaledPositionTESTONLY(1);
        
        worker.init();
        worker.exec();
        
        assertTrue("Motor should be driving forward", cataCore.getCatapultMotor() > 0.9);
        
        cataCore.setCatapultScaledPositionTESTONLY(0.1);
        worker.exec();
        
        assertEquals("Motor should be stopped when at target", 0, cataCore.getCatapultMotor(), 0.001);
        
        cataCore.setCatapultScaledPositionTESTONLY(0);
        worker.exec();
        
        assertEquals("Motor should be stopped when beyond target", 0, cataCore.getCatapultMotor(), 0.001);
    }
}
