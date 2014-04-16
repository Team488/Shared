/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.common.properties;

import xbot.test.common.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.common.CommonTools;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.StringProperty;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.ITableProxy;
import xbot.common.properties.TableProxy;
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
        DoubleProperty dbl = new DoubleProperty("speed", 1.0);
        BooleanProperty bool = new BooleanProperty("isTrue", true);
        StringProperty str = new StringProperty("string", "teststring");
        
        assertEquals(1.0, dbl.get(), 0.001);
        assertEquals(true,bool.get());
        assertEquals("teststring",str.get());
    }
    
    @Test
    public void testLoadingValue() {
        permanentStore.AddTestDouble("speed", 0.5);
        permanentStore.AddTestBoolean("isTrue", true);
        permanentStore.AddTestString("string", "teststring");
        permanentStore.LoadFromDisk();
        
        DoubleProperty dbl = new DoubleProperty("speed", 1.0);
        BooleanProperty bool = new BooleanProperty("isTrue", false);
        StringProperty str = new StringProperty("string", "blahblah");
        
        assertEquals(0.5, dbl.get(), 0.001);
        assertEquals(true,bool.get());
        assertEquals("teststring",str.get());
    }
    
    @Test
    public void testSavingValue() {
        DoubleProperty dbl = new DoubleProperty("speed", 1.0);
        BooleanProperty bool = new BooleanProperty("isTrue", true);
        StringProperty str = new StringProperty("string", "teststring");
        assertSame(null, permanentStore.getDouble("speed"));
        assertSame(null, permanentStore.getBoolean("isTrue"));
        assertSame(null, permanentStore.getString("string"));
        
        dbl.save();
        bool.save();
        str.save();
        assertEquals(1.0, permanentStore.getDouble("speed").doubleValue(), 0.001);
        assertEquals(true,permanentStore.getBoolean("isTrue").booleanValue());
        assertEquals("teststring",permanentStore.getString("string"));
    }
    
    @Test
    public void testTweakingRuntimeValue() {        
        DoubleProperty dbl = new DoubleProperty("speed", 1.0);
        BooleanProperty bool = new BooleanProperty("isTrue", true);
        StringProperty str = new StringProperty("string", "teststring");        
        assertEquals(1.0, dbl.get(), 0.001);
        assertEquals(true,bool.get());
        assertEquals("teststring",str.get());       
        
        // simulate value changed from smart dashboard
        randomAccessStore.setDouble("speed", 0.5);
        randomAccessStore.setBoolean("isTrue", false);
        randomAccessStore.setString("string", "blah");
        assertEquals(0.5, dbl.get(), 0.001);
        assertEquals(false,bool.get());
        assertEquals("blah",str.get());  
    }
}