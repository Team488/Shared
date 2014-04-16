
 /* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.tests.workers;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import xbot.aerialassist.systems.CollectionCore;
import xbot.aerialassist.collection.*;
import xbot.test.common.BaseTest;

/**
 *
 * @author Nikhil
 */
public class CollectorRollerTest extends BaseTest {
    
    public Collector collector = context.getCollectionCore().getFrontCollector();
    public CollectorRollerTest(){
    }
    
    
    @Test 
    public void CollectorCollectTest(){
        
        CollectorRollerWorker frontCollectorWorker = new CollectorRollerWorker(collector,CollectorRollerState.COLLECT);
        frontCollectorWorker.init();
        assertEquals("Collector Running",CollectionCore.rollerCollectSpeed.get(),collector.getCollectorRollerMotor(),0.01);
       
    }
    
    @Test 
    public void CollectorEjectTest(){
        
        CollectorRollerWorker frontCollectorWorker = new CollectorRollerWorker(collector,CollectorRollerState.REVERSE);
        frontCollectorWorker.init();
        assertEquals("Collector Reversing",-CollectionCore.rollerCollectSpeed.get(),collector.getCollectorRollerMotor(),0.01);
       
    }
    
    @Test 
    public void CollectorStopTest(){
        
        CollectorRollerWorker frontCollectorWorker = new CollectorRollerWorker(collector,CollectorRollerState.STOP);
        frontCollectorWorker.init();
        assertEquals("Collector Stopped",0,collector.getCollectorRollerMotor(),0.01);
       
    }
    
    @Test
    public void otherCollectorsNotMoving(){
        
        CollectorRollerWorker frontCollectorWorker = new CollectorRollerWorker(collector,CollectorRollerState.COLLECT);
        frontCollectorWorker.init();
        assertEquals("Back Collector Stopped",0,context.getCollectionCore().getBackCollector().getCollectorRollerMotor(),.001);
    }
}