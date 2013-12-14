/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.systems;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class BucketCoreTest extends BaseTest {
    
    public BucketCoreTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testSettingBucketState() {
        context.GetBucketCore().setBucketUp();
        assertEquals(true, context.GetBucketCore().getRequestSolenoidUp());
        context.GetBucketCore().setBucketDown();
        assertEquals(false, context.GetBucketCore().getRequestSolenoidUp());
    }
    
    @Test
    public void testSensorValueReading() {
        context.GetBucketCore().setDeviceBucketDownLimitSwitchPressed(true);
        assertEquals(true, context.GetBucketCore().isBucketDefinitelyDown());
        
        context.GetBucketCore().setDeviceBucketDownLimitSwitchPressed(false);
        assertEquals(false, context.GetBucketCore().isBucketDefinitelyDown());
        
        
        context.GetBucketCore().setDeviceBucketUpLimitSwitchPressed(true);
        assertEquals(true, context.GetBucketCore().isBucketDefinitelyUp());
        
        context.GetBucketCore().setDeviceBucketUpLimitSwitchPressed(false);
        assertEquals(false, context.GetBucketCore().isBucketDefinitelyUp());
    }
    
}