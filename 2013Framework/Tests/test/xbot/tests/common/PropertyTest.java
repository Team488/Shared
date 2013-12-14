/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.common;

import xbot.test.common.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.common.CommonTools;
import xbot.common.DoubleProperty;
import xbot.common.ITableProxy;
import xbot.common.TableProxy;
import xbot.core.RobotContext;
import xbot.test.common.PermanentStoreTest;

/**
 *
 * @author Alex
 */
public class PropertyTest extends BaseTest {
    PermanentStoreTest permanentStore;
    ITableProxy randomAccessStore;
    
    public PropertyTest() {
    }
    
    @Before
    public void setUp() {
        
        permanentStore = new PermanentStoreTest();
        randomAccessStore = new TableProxy("TestRandomAccessStore");
        CommonTools.Get().propertyManager.permanentStore = permanentStore;
        CommonTools.Get().propertyManager.randomAccessStore = randomAccessStore;
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testDefaultValue() {
        DoubleProperty myProperty = new DoubleProperty("speed", 1.0);
        assertEquals(1.0, myProperty.get(), 0.001);
    }
    
    @Test
    public void testLoadingValue() {
        permanentStore.AddTestProperty("speed", 0.5);
        permanentStore.LoadFromDisk();
        
        DoubleProperty myProperty = new DoubleProperty("speed", 1);
        assertEquals(0.5, myProperty.get(), 0.001);
    }
    
    @Test
    public void testSavingValue() {
        DoubleProperty myProperty = new DoubleProperty("speed", 1.0);
        assertSame(null, permanentStore.getDouble("speed"));
        myProperty.save();
        assertEquals(1.0, permanentStore.getDouble("speed"), 0.001);
    }
    
    @Test
    public void testTweakingRuntimeValue() {        
        DoubleProperty myProperty = new DoubleProperty("speed", 1.0);
        assertEquals(1.0, myProperty.get(), 0.001);
        
        // simulate value changed from smart dashboard
        randomAccessStore.setDouble("speed", 0.5);
        assertEquals(0.5, myProperty.get(), 0.001);
    }
}