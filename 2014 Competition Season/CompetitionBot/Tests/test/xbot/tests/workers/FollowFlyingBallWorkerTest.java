/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers;

import org.junit.Before;
import org.junit.Test;
import xbot.aerialassist.workers.FollowFlyingBallWorker;
import xbot.common.CommonTools;
import static org.junit.Assert.*;
import xbot.common.logging.LogConsumer;
import xbot.common.logging.LogProducer;
import xbot.test.common.BaseTest;
import xbot.test.common.StringLogWriter;
/**
 *
 * @author John
 */
public class FollowFlyingBallWorkerTest extends BaseTest {
    
    LogConsumer logConsumer;
    LogProducer logProducer;
    StringLogWriter writer;
    
    public FollowFlyingBallWorkerTest()
    {
        
    }
    
    private FollowFlyingBallWorker worker;
    
    @Before
    public void setUp() {
        worker = new FollowFlyingBallWorker();
        worker.init();
        writer = new StringLogWriter("Log");
        logConsumer = new LogConsumer(writer, LogProducer.LOGGING);
        logProducer = new LogProducer("test", LogProducer.LOGGING);
    }
    
    @Test
    public void ballToTheLeft()
    {
        SetBallLocation(50, 100, 200, 200);
        worker.exec();
        VerifyMotorDirection(false, true, true, false);
        logConsumer.Consume();
        
    }
    
    @Test
    public void ballToTheRight()
    {
        SetBallLocation(150, 100, 200, 200);
        worker.exec();
        VerifyMotorDirection(true, false, false, true);
        logConsumer.Consume();
    }
    
    @Test
    public void ballTooHigh()
    {
        SetBallLocation(100, 150, 200, 200);
        worker.exec();
        VerifyMotorDirection(false, false, false, false);
        logConsumer.Consume();
    }
    
    @Test
    public void ballTooLow()
    {
        SetBallLocation(100, 50, 200, 200);
        worker.exec();
        VerifyMotorDirection(true, true, true, true);
        logConsumer.Consume();
    }
    
    private void VerifyMotorDirection(boolean LF, boolean LB, boolean RF, boolean RB)
    {
        boolean lfForward = context.GetDriveCore().getLeftMotorFront() > 0;
        boolean lbForward = context.GetDriveCore().getLeftMotorRear()> 0;
        boolean rfForward = context.GetDriveCore().getRightMotorFront() > 0;
        boolean rbForward = context.GetDriveCore().getRightMotorRear() > 0;
        
        assertTrue("LeftFront", LF == lfForward);
        assertTrue("LeftRear", LB == lbForward);
        assertTrue("RightFront", RF == rfForward);
        assertTrue("RightRear", RB == rbForward);
    }
    
    private void SetBallLocation(double cogx, double cogy, double height, double width)
    {
        CommonTools.Get().propertyManager.randomAccessStore.setDouble("COG_X", cogx);
        CommonTools.Get().propertyManager.randomAccessStore.setDouble("COG_Y", cogy);
        CommonTools.Get().propertyManager.randomAccessStore.setDouble("IMAGE_HEIGHT", height);
        CommonTools.Get().propertyManager.randomAccessStore.setDouble("IMAGE_WIDTH", width);
    }
}
