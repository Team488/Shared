/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.aerialassist.systems.CollectionCore;
import xbot.aerialassist.collection.*;
import xbot.common.ExecState;
import xbot.common.logging.LogConsumer;
import xbot.common.logging.LogProducer;
import xbot.test.common.BaseTest;
import xbot.test.common.StringLogWriter;


/**
 * 
 * @author Alex
 */
public class CollectorDeployTest extends BaseTest {
    Collector collector;
    
    LogConsumer logConsumer;
    LogProducer logProducer;
    StringLogWriter writer;
    
    CollectionPositionMaintainer maintainWorker;
    
    public CollectorDeployTest() {
    }
    
    @Before
    public void setUp() {
        collector = context.getCollectionCore().getFrontCollector();
        writer = new StringLogWriter("Log");
        logConsumer = new LogConsumer(writer, LogProducer.LOGGING);
        logProducer = new LogProducer("test", LogProducer.LOGGING);
        maintainWorker = new CollectionPositionMaintainer(collector);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void deloy() {
        
        CollectorDeployWorker worker = new CollectorDeployWorker(collector, CollectorDeployState.DOWN);
        
        worker.init();
        worker.exec();
        logConsumer.Consume();
        
        assertEquals(CollectionCore.downDeploySpeed.get(), collector.getDeployMotor(), 
                001);
        
        collector.getSensors().SetScaledPositionTESTONLY(1);
        
        assertEquals(ExecState.SUCCESS, worker.isFinished());  
        logConsumer.Consume();
    }
    /*
    @Test
    public void up() {
        
        CollectorDeployWorker worker = new CollectorDeployWorker(collector, CollectorDeployState.UP);
        
        worker.init();
        worker.exec();
        logConsumer.Consume();
        
        assertEquals(CollectionCore.upDeploySpeed.get(), collector.getDeployMotor(), 0.001);
        
        collector.getSensors().SetScaledPositionTESTONLY(0); // set pos
        
        assertEquals(ExecState.SUCCESS, worker.isFinished());  
        logConsumer.Consume();
    }
    
    @Test
    public void firing() {
        // start with arms up
       collector.getSensors().SetScaledPositionTESTONLY(0);
        
        CollectorDeployWorker worker = new CollectorDeployWorker(collector, CollectorDeployState.FIRING);
        
        worker.init();
        worker.exec();
        logConsumer.Consume();
        
        assertEquals(CollectionCore.upDeploySpeed.get(), collector.getDeployMotor(), 0.001);
        
        collector.getSensors().SetScaledPositionTESTONLY(collector.getSensors().firingPositionLowerLimit.get());
        
        assertEquals(ExecState.SUCCESS, worker.isFinished());
        logConsumer.Consume();
    }
    * */
    /*
    @Test
    public void firingDeployed() {
        //start with arms up
        collector.getSensors().setDownVoltageTESTONLY(4);
        collector.getSensors().setUpVoltageTESTONLY(1);
        collector.getSensors().SetCurrentVoltage(4);
        collector.getSensors().GetCollectorPosition();
        //collector.getSensors().SetScaledPositionTESTONLY(1);
        
        CollectorDeployWorker worker = new CollectorDeployWorker(collector, CollectorDeployState.FIRING);
        
        worker.init();
        worker.exec();
        logConsumer.Consume();
        
        assertEquals(CollectionCore.upDeploySpeed.get(), collector.getDeployMotor(), 0.001);
        
       collector.getSensors().SetScaledPositionTESTONLY(1);
        
        assertEquals(ExecState.SUCCESS, worker.isFinished());  
        logConsumer.Consume();
    }*/
    
    @Test
    public void stopDeploy() {
        CollectorDeployWorker worker = new CollectorDeployWorker(collector, CollectorDeployState.STOP);
        
        worker.init();
        worker.exec();
        
        assertEquals(0, collector.getDeployMotor(), 0.001);
        
        assertEquals(ExecState.NOT_DONE, worker.isFinished());  
        logConsumer.Consume();
    }
    
    CollectionPositionMaintainer frontMaintain;
    CollectionPositionMaintainer backMaintain;
    FieldOrientedCollectionWorker foWorker;
        
    @Test
    public void FieldOrientedCollection()
    {        
        foWorker = new FieldOrientedCollectionWorker();
        foWorker.init();
        
        //0.062, -1
        CollectionCore core = context.getCollectionCore();
        Collector front = core.getFrontCollector();
        Collector back = core.getBackCollector();
        core.getFrontCollector().stopDeploy();
        core.getBackCollector().stopDeploy();
        
        //set sensors to have everything be up
        front.getSensors().setUpVoltageTESTONLY(0);
        front.getSensors().setDownVoltageTESTONLY(1);
        front.getSensors().SetCurrentVoltage(0.5);
        
        back.getSensors().setUpVoltageTESTONLY(0);
        back.getSensors().setDownVoltageTESTONLY(1);
        back.getSensors().SetCurrentVoltage(0.5);
        
        frontMaintain = new CollectionPositionMaintainer(front);
        backMaintain = new CollectionPositionMaintainer(back);
        
        frontMaintain.init();
        backMaintain.init();
        
        // Turn so that back of robot faces drivers
        context.GetSensorCore().DepositLocationInformationFromRobot(-90, 0, 0, 0);
        SetJoyAndStep(0, 1);
        
        assertTrue("Front collector going down", front.getDeployMotor() > 0);
        assertTrue("Back Collector going up", back.getDeployMotor() < 0);
        
        context.GetSensorCore().DepositLocationInformationFromRobot(-90+-0.7, 0, 0, 0);
        SetJoyAndStep(0.062, -1);        
        // With the new logic, we stay down until the joysticks come back to neutral
        assertTrue("Front collector going down", front.getDeployMotor() > 0);
        assertTrue("Back collector going up", back.getDeployMotor() < 0);
        
        ReturnToNeutral();
        
        context.GetSensorCore().DepositLocationInformationFromRobot(-90+0.7, 0, 0, 0);
        SetJoyAndStep(0.062, -1);
        assertTrue("Front collector going up", front.getDeployMotor() < 0);
        assertTrue("Back collector going down", back.getDeployMotor() > 0);
        
        ReturnToNeutral();
        
        context.GetSensorCore().DepositLocationInformationFromRobot(-90+45, 0, 0, 0);
        assertEquals(90+45, context.GetSensorCore().getCurrentHeading(), 0.001);
        SetJoyAndStep(0, 1);
        assertTrue("Front collector going down", front.getDeployMotor() > 0);
        assertTrue("Back collector going up", back.getDeployMotor() < 0);
        
        ReturnToNeutral();
       
        SetJoyAndStep(0, -1);
        assertTrue("Front collector going up", front.getDeployMotor() < 0);
        assertTrue("Back collector going down", back.getDeployMotor() > 0);
       
        
        context.GetSensorCore().DepositLocationInformationFromRobot(-90+45+90, 0, 0, 0);
        assertEquals(90+45+90, context.GetSensorCore().getCurrentHeading(), 0.001);
        SetJoyAndStep(0, 1);        
        assertTrue("Front collector going up", front.getDeployMotor() < 0);
        assertTrue("Back collector going down", back.getDeployMotor() > 0);
        
        ReturnToNeutral();        
        
        //core.DeployFieldOrientedCollection(new XYPair(0, -1));
        SetJoyAndStep(0, -1);        
        assertTrue("Front collector going down", front.getDeployMotor() > 0);
        assertTrue("Back collector going up", back.getDeployMotor() < 0);
    }
    
    private void SetJoyAndStep(double x, double y)
    {
        context.GetOICore().setOperatorJoyX(x);
        context.GetOICore().setOperatorJoyY(y);
        foWorker.exec();
        frontMaintain.exec();
        backMaintain.exec();
    }
    
    private void ReturnToNeutral()
    {
        CollectionCore core = context.getCollectionCore();
        Collector front = core.getFrontCollector();
        Collector back = core.getBackCollector();
        
        SetJoyAndStep(0, 0);
        
        assertTrue("Front collector going up", front.getDeployMotor() < 0);
        assertTrue("Back collector going up", back.getDeployMotor() < 0);
    }
    
    @Test
    public void PIDWorker()
    {
        collector.getSensors().setDownVoltageTESTONLY(4);
        collector.getSensors().setUpVoltageTESTONLY(1);
        collector.getSensors().SetCurrentVoltage(4);
        //collector.getSensors().SetScaledPositionTESTONLY(1);
        
        maintainWorker.init();
        maintainWorker.exec();
        
        CollectorDeployPIDWorker worker = new CollectorDeployPIDWorker(collector, CollectorDeployState.UP);
        worker.init();
        worker.exec();
        
        maintainWorker.exec();

        assertTrue("Deploy speed should be below zero", collector.getDeployMotor() < 0);
        assertEquals(ExecState.NOT_DONE, worker.isFinished());
        
        collector.getSensors().SetScaledPositionTESTONLY(0);
        
        assertEquals(ExecState.SUCCESS, worker.isFinished());
        
        logConsumer.Consume();
    }
    
    @Test
    public void setVoltageTest()
    {
        collector.getSensors().setDownVoltageTESTONLY(4);
        collector.getSensors().setUpVoltageTESTONLY(1);
        collector.getSensors().SetCurrentVoltage(3.14);
        double position = collector.getSensors().GetCollectorPosition();
        double scaledPosition = collector.getSensors().scaledPosition.get();
        double currentVoltage = collector.getSensors().currentVoltage.get();
        double upVoltage = collector.getSensors().upVoltage.get();
        double downVoltage = collector.getSensors().downVoltage.get();
        
        assertEquals("VoltageTest", (currentVoltage - upVoltage) / (downVoltage - upVoltage), scaledPosition, .001);
        collector.getSensors().printScaledPosition();
        logConsumer.Consume();
        
        //just changing the up and down volt ranges
        collector.getSensors().setDownVoltageTESTONLY(5);
        collector.getSensors().setUpVoltageTESTONLY(.5);
        collector.getSensors().SetCurrentVoltage(3);
        assertEquals("VoltageTest", (currentVoltage - upVoltage) / (downVoltage - upVoltage), scaledPosition, .001);
        collector.getSensors().printScaledPosition();
        logConsumer.Consume();
        
        // a currentvoltage that is greater than the range
        collector.getSensors().setDownVoltageTESTONLY(3.34);
        collector.getSensors().setUpVoltageTESTONLY(.001);
        collector.getSensors().SetCurrentVoltage(5);
        assertEquals("VoltageTest", (currentVoltage - upVoltage) / (downVoltage - upVoltage), scaledPosition, .001);
        collector.getSensors().printScaledPosition();
        logConsumer.Consume();
        
        // with up and down voltages of 0
        collector.getSensors().setDownVoltageTESTONLY(0.0);
        collector.getSensors().setUpVoltageTESTONLY(0.0);
        collector.getSensors().SetCurrentVoltage(5);
        assertEquals("VoltageTest", (currentVoltage - upVoltage) / (downVoltage - upVoltage), scaledPosition, .001);
        collector.getSensors().printScaledPosition();
        logConsumer.Consume();
    }
    
    @Test
    public void ManualBallCollectionTest()
    {
        collector.setIsManual(false);
        collector.getSensors().setDownVoltageTESTONLY(4);
        collector.getSensors().setUpVoltageTESTONLY(1);
        collector.getSensors().SetScaledPositionTESTONLY(.2);
        
        CollectorDeployPIDWorker pidWorker = new CollectorDeployPIDWorker(collector,CollectorDeployState.UP);
        CollectionPositionMaintainer maintainer = new CollectionPositionMaintainer(collector);
        pidWorker.init();
        //pidWorker.exec();
        maintainer.exec();
        assertTrue("Collector Moving Up",collector.getDeployMotor()<0);
        collector.setIsManual(true); 
        maintainer.exec();
        assertEquals("collector is stopped",0,collector.getDeployMotor(),.001);
        collector.getSensors().setManualDown(true);
        maintainer.exec();
        assertEquals("Collector Moving Down",CollectionCore.downDeploySpeed.get(),collector.getDeployMotor(),.001);
    }
}