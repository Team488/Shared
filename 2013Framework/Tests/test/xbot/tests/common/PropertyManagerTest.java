/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.common;

import xbot.test.common.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xbot.common.DoubleProperty;
import xbot.common.ITableProxy;
import xbot.common.TableProxy;
import xbot.core.RobotContext;
import static org.junit.Assert.*;
import xbot.common.CommonTools;
import xbot.common.PropertyManager;
import xbot.common.logging.LogProducer;
import xbot.test.common.PermanentStoreTest;

/**
 *
 * @author Alex
 */
public class PropertyManagerTest extends BaseTest {
    
    LogProducer l;
        
    
    public PropertyManagerTest() {
        l = new LogProducer("PropertyManagerTest");
    }
    
    private void TestLog(String message)
    {
        l.Log(LogProducer.INFO, message);
        CommonTools.Get().logConsumer.Consume();
    }
    
    @Before
    public void setUp() {
        permanentStore = new PermanentStoreTest();
        randomAccessStore = new TableProxy("TestRandomAccessStore");
        
        CommonTools.Get().propertyManager = new PropertyManager(permanentStore, randomAccessStore);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testRegisteringProperty() {
        int previousSize = CommonTools.Get().propertyManager.properties.size();
        DoubleProperty myProperty = new DoubleProperty("speed", 1.0);
        assertEquals("property was registered", previousSize + 1, CommonTools.Get().propertyManager.properties.size());
    }
    
    @Test
    public void testSavingAllProperties() {
        DoubleProperty speedProperty = new DoubleProperty("speed", 1.0);
        DoubleProperty speedProperty2 = new DoubleProperty("speed2", 0.5);
        
        assertEquals(0, permanentStore.table.size());
        
        CommonTools.Get().propertyManager.saveOutAllProperties();
        
        assertEquals(2, permanentStore.table.size());
        
        // now, force the save
        permanentStore.SaveToDisk();
        
        // verify the string makes sense
        TestLog(permanentStore.savedText);
        
        assertTrue(permanentStore.savedText.contains("speed,1.0"));
        assertTrue(permanentStore.savedText.contains("speed2,0.5"));
    }
    
    @Test
    public void testLoadingAllProperties()
    {
        DoubleProperty speedProperty = new DoubleProperty("speed", 1.0);
        DoubleProperty speedProperty2 = new DoubleProperty("speed2", 0.5);
        speedProperty.set(5.0);
        speedProperty2.set(7.0);
        
        // now, force the save
        CommonTools.Get().propertyManager.saveOutAllProperties();
        permanentStore.SaveToDisk();
        
        TestLog(permanentStore.savedText);
        
        //clear the table, force a load
        permanentStore.table.clear();
        
        // verify table is actually clear
        assertTrue(permanentStore.table.isEmpty());
        
        permanentStore.LoadFromDisk();
        
        //verify table has what we expect
        assertTrue((Double)permanentStore.table.get("speed") == 5.0);
        assertTrue((Double)permanentStore.table.get("speed2") == 7.0);
        
        speedProperty = new DoubleProperty("speed", 1.0);
        speedProperty2 = new DoubleProperty("speed2", 0.5);
        
        assertTrue(speedProperty.get() == 5.0);
        assertTrue(speedProperty2.get() == 7.0);
        
    }
    
}