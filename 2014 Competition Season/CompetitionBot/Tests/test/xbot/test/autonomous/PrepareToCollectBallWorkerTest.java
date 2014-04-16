/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import xbot.aerialassist.autonomousworkers.AutonomousWorkerFactory;
import xbot.aerialassist.autonomousworkers.PrepareToCollectBallWorker;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
public class PrepareToCollectBallWorkerTest extends BaseTest {
    private final Collector main = this.context.getCollectionCore().getFrontCollector();
    private final Collector side = this.context.getCollectionCore().getBackCollector();
    private final AutonomousWorkerFactory factory = new AutonomousWorkerFactory();
    private final PrepareToCollectBallWorker worker =
                new PrepareToCollectBallWorker(factory, main, side);

    
    @After
    public void tearDown() {
        CommonTools.Get().logConsumer.Consume();
    }
    
    @Test
    public void testSuccess() {
        // Start with both arms in firing, and the catapult not cocked.
        Helpers.setCollectorState(main, CollectorDeployState.FIRING);
        Helpers.setCollectorState(side, CollectorDeployState.FIRING);
        Helpers.setCatapultNotCocked();
        
        worker.init();
        
        // There are many possible interleavings of done-ness.
        // I am testing one. We should maybe test others.
        Helpers.setCollectorState(main, CollectorDeployState.DOWN);
        this.execAndAssertNotFinished();
        this.mockTime.incrementTime(1000);
        this.execAndAssertNotFinished();
        Helpers.setCollectorState(main, CollectorDeployState.SAFE);
        this.execAndAssertNotFinished();
        this.execAndAssertNotFinished();
        Helpers.setCollectorState(side, CollectorDeployState.UP);
        this.execAndAssertNotFinished();
        Helpers.setCollectorState(side, CollectorDeployState.UP);
        Helpers.setCatapultCocked();
        this.worker.exec();
        assertEquals(ExecState.SUCCESS, this.worker.isFinished());
    }
    
    private void execAndAssertNotFinished() {
        this.worker.exec();
        assertEquals(ExecState.NOT_DONE, this.worker.isFinished());
    }
}
