/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.collection;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.collection.CollectionPositionMaintainer;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.systems.CollectionCore;
import xbot.test.common.BaseTest;

/**
 *
 * @author sterlingdorminey
 */
public class CollectionPositionMaintainerTest extends BaseTest {
    
    CollectionPositionMaintainer maintainer;
    Collector collector;
    
    @Before
    public void before()
    {
        RobotContext.ResetRobotContextForTestingPurposesOnly();
        collector = new Collector("Foo");
        maintainer = new CollectionPositionMaintainer(collector); 
    }
    
    @Test
    public void testMaintainer() {        
        maintainer.init();
        
        collector.getSensors().SetScaledPositionTESTONLY(0);
        collector.setTargetScaledPosition(1);
        maintainer.exec();
        assertTrue(collector.getDeployMotor() > 0);
        
        collector.getSensors().SetScaledPositionTESTONLY(1);
        collector.setTargetScaledPosition(0);
        maintainer.exec();
        assertTrue(collector.getDeployMotor() < 0);
    }
    
    @Test
    public void testManualOverride()
    {
        maintainer.init();
        
        collector.setIsManual(true);
        
        collector.getSensors().setManualDown(true);
        maintainer.exec();
        
        assertEquals("Should be moving arm down", CollectionCore.downDeploySpeed.get(), collector.getDeployMotor(), 0.001);
        
        collector.getSensors().setManualUp(true);
        maintainer.exec();
        
        assertEquals("Arm should not move", 0, collector.getDeployMotor(), 0.001);
        
        collector.getSensors().setManualDown(false);
        maintainer.exec();
        
        assertEquals("Should be moving arm up", CollectionCore.upDeploySpeed.get(), collector.getDeployMotor(), 0.001);
    }
    
    @Test
    public void TestManualOverrideWithLimitSwitches()
    {
        collector.setIsManual(true);
        RobotContext.Get().getCollectionCore().useLimitSwitchesForArms.set(true);
        
        collector.getSensors().setManualDown(true);
        maintainer.exec();
        
        assertArmGoingDown();
        
        collector.getSensors().setLimitSwitches(false, true);
        maintainer.exec();
        
        assertArmStopped();
        
        collector.getSensors().setManualDown(false);
        collector.getSensors().setManualUp(true);
        maintainer.exec();
        
        assertArmGoingUp();
        
        collector.getSensors().setLimitSwitches(true, false);
        maintainer.exec();
        
        assertArmStopped();
    }
    
    @Test
    public void TestMaintainerGoingToSafe()
    {
        collector.getSensors().SetScaledPositionTESTONLY(0);
        collector.setTargetDeployState(CollectorDeployState.FIRING);
        
        maintainer.exec();
        
        assertArmPowerPositive();
        
        //Move to the "good enough" position
        
        collector.getSensors().SetScaledPositionTESTONLY(
                collector.getSensors().firingPositionLowerLimit.get());
        
        //verify the arm says it's good
        assertTrue("Arm registers as good to fire", collector.getSensors().GetIsSafeToFire());
        
        maintainer.reset();
        //verify pid is still trying to go to the best position
        maintainer.exec();
        
        
        assertArmPowerPositive();
    }
    
    private void assertArmPowerPositive()
    {
        assertTrue("Arm power should be positive (going down)", collector.getDeployMotor() > 0);
    }
    
    private void assertArmPowerNegative()
    {
        assertTrue("Arm power should be negative (going up)", collector.getDeployMotor() < 0);
    }
    
    private void assertArmGoingUp()
    {
        assertEquals("Should be moving arm up", CollectionCore.upDeploySpeed.get(), collector.getDeployMotor(), 0.001);
    }
    
    private void assertArmGoingDown()
    {
        assertEquals("Should be moving arm down", CollectionCore.downDeploySpeed.get(), collector.getDeployMotor(), 0.001);
    }
    
    private void assertArmStopped()
    {
        assertEquals("Arm should not move", 0, collector.getDeployMotor(), 0.001);
    }
}
