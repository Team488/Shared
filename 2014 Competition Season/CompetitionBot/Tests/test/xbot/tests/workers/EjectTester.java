/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.tests.workers;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import xbot.aerialassist.systems.CatapultCore;
import xbot.aerialassist.workers.EjectBallWorker;
import xbot.aerialassist.workers.ManualBallEjectionWorker;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.logging.LogConsumer;
import xbot.common.logging.LogProducer;
import xbot.test.common.BaseTest;
import xbot.test.common.StringLogWriter;

/**
 *
 * @author Nikhil
 */
public class EjectTester extends BaseTest {
    
    public EjectTester() {
    }
    
    private CatapultCore core;
    
    private EjectBallWorker ejectWorker;
    
    private LogConsumer logConsumer;
    
    
    @Before
    public void setUp() {
        core = context.GetCatapultCore();
        StringLogWriter writer = new StringLogWriter("Log");
        logConsumer = new LogConsumer(writer, LogProducer.LOGGING);
    }
    
    @After
    public void tearDown() {
        CommonTools.Get().logConsumer.Consume();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
//     @Test
//     public void EjectTest(){
//         ManualBallEjectionWorker worker = new ManualBallEjectionWorker(true);
//         worker.exec();
//         assertEquals("Ball Ejected",true,context.GetCatapultCore().isEjectorSolenoidPunching());
//     }
//     
//     @Test
//     public void RetractTest(){
//         ManualBallEjectionWorker worker = new ManualBallEjectionWorker(false);
//         worker.exec();
//         assertEquals("Ball Ejected",false,context.GetCatapultCore().isEjectorSolenoidPunching());
//         
//     }
//     
//     @Test
//     public void EjectBall() throws InterruptedException {
//         ManualBallEjectionWorker worker = new ManualBallEjectionWorker(false);
//         worker.exec();
//         assertEquals("Ball Ejected",false,context.GetCatapultCore().isEjectorSolenoidPunching());
//         
//     }
//     
//     @Test
//     public void testBallEjection() throws InterruptedException{
//         assertEquals("Ball Not Ejected",false,core.isEjectorSolenoidPunching());
//         ejectWorker.exec();
//         assertEquals("Ball Ejected",true,core.isEjectorSolenoidPunching());
//         Thread.sleep((int)ejectWorker.timeout.get()+10);
//         ejectWorker.exec();
//         assertEquals("Ejector Retracted",false,core.isEjectorSolenoidPunching());
//         Thread.sleep((int)ejectWorker.timeout.get()+10);
//         ejectWorker.exec();
//         assertEquals("Ejector Finished",ExecState.SUCCESS,ejectWorker.isFinished());
//     }
}

