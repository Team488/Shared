/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.collection;

import org.junit.Test;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.RollerListenerWorker;
import xbot.test.common.BaseTest;
import static org.junit.Assert.*;
import xbot.aerialassist.systems.OICore;

/**
 *
 * @author sterlingdorminey
 */
public class RollerListenerTest extends BaseTest {
    
    
    @Test
    public void testMaintainer() {
        Collector front = RobotContext.Get().getCollectionCore().getFrontCollector();
        OICore oi = this.context.GetOICore();
        RollerListenerWorker worker = new RollerListenerWorker(front, "Front");
                
        worker.init();
        
        oi.setRollerShouldExpel(false);
        oi.setRollerShouldIntake(false);
        
        worker.exec();
        
        assertEquals("Collector roller power should be zero", 0, front.getCollectorRollerMotor(), 0.001);
        
        oi.setRollerShouldIntake(true);
        oi.setRollerShouldExpel(false);
        
        worker.exec();
        
        assertTrue("Collector roller powre should be be above zero", front.getCollectorRollerMotor() > 0);
        
        oi.setRollerShouldIntake(false);
        oi.setRollerShouldExpel(true);
        
        worker.exec();
        
        assertTrue("Collector roller powre should be be above zero", front.getCollectorRollerMotor() < 0);
        
        oi.setRollerShouldIntake(true);
        oi.setRollerShouldExpel(true);
        
        worker.exec();
        
        assertEquals("Collector roller power should be zero", 0, front.getCollectorRollerMotor(), 0.001);
        
    }
}
