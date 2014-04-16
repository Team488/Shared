/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xbot.aerialassist.RobotContext;
import xbot.test.common.BaseTest;
import static org.junit.Assert.*;
import xbot.aerialassist.systems.CatapultCore;

/**
 *
 * @author Alex
 */
public class CatapultCoreTest extends BaseTest{
    CatapultCore catapult;
    
    public CatapultCoreTest() {
        catapult = context.GetCatapultCore();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
     
     @Test
     public void TestBallInCatapult() {
         RobotContext.Get().GetCatapultCore().SetBallSensorVoltage(0);
         assertTrue("Ball should not be in catapult", !RobotContext.Get().GetCatapultCore().IsBallInCatapult());
            
         RobotContext.Get().GetCatapultCore().SetBallSensorVoltage(5);
         assertTrue("Ball should be in catapult", RobotContext.Get().GetCatapultCore().IsBallInCatapult());            
     }
     
     @Test
     public void TestBallSettledInCatapult() {
         RobotContext.Get().GetCatapultCore().SetBallSensorVoltage(0);
         assertTrue("Ball should not be in catapult", !RobotContext.Get().GetCatapultCore().IsBallInCatapult());

         RobotContext.Get().GetCatapultCore().SetBallSensorVoltage(5);
         assertTrue("Ball should be in catapult", RobotContext.Get().GetCatapultCore().IsBallInCatapult());

         assertTrue("Ball should not be settled", !RobotContext.Get().GetCatapultCore().IsBallSettled());

        // wait for a while
         mockTime.incrementTime(10000);

         assertTrue("Ball should not be settled", RobotContext.Get().GetCatapultCore().IsBallSettled());
     }
     
     @Test
     public void testSettingZeroPosition() {
         catapult.setCatapultPositionVoltage(0);
         assertEquals(catapult.getCatapultScaledPosition(), 0, 0.01);
     }
     
     @Test
     public void testSettingMaxPosition() {
         catapult.setCatapultPositionVoltage(5);
         assertEquals(catapult.getCatapultScaledPosition(), 1, 0.01);
     }
     
     @Test
     public void testOffsetLooping() {
         catapult.catapultPositionOffset.set(0.5);
         catapult.setCatapultPositionVoltage(5);
         assertEquals(catapult.getCatapultScaledPosition(), 0.5, 0.01);
     }
     
}