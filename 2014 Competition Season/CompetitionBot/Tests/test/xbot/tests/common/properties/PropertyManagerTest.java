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
import xbot.common.logging.LogProducer;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.StringProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.properties.TableProxy;
import xbot.test.common.PermanentStoreTest;

/**
 *
 * @author Alex
 */
public class PropertyManagerTest extends BaseTest {
    
    LogProducer l;
        
    
    public PropertyManagerTest() {
        l = new LogProducer("PropertyManagerTest", LogProducer.LOGGING);
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
        DoubleProperty dbl = new DoubleProperty("speed", 1.0);
        assertEquals("property was registered", previousSize + 1, CommonTools.Get().propertyManager.properties.size());
        previousSize = CommonTools.Get().propertyManager.properties.size();
        BooleanProperty bool = new BooleanProperty("test",true);
        assertEquals("property was registered", previousSize + 1, CommonTools.Get().propertyManager.properties.size());
        previousSize = CommonTools.Get().propertyManager.properties.size();
        StringProperty str = new StringProperty("test","test");
        assertEquals("property was registered", previousSize + 1, CommonTools.Get().propertyManager.properties.size());
    }
    
    @Test
    public void testSavingAllProperties() {
        DoubleProperty speedProperty = new DoubleProperty("speed", 1.0);
        BooleanProperty trueProperty = new BooleanProperty("boolTest",true);
        StringProperty blahProperty = new StringProperty("strTest","blah");
        
        assertEquals(0, permanentStore.table.size());
        
        CommonTools.Get().propertyManager.saveOutAllProperties();
        
        assertEquals(3, permanentStore.table.size());
        
        // now, force the save
        permanentStore.SaveToDisk();
        
        // verify the string makes sense
        TestLog(permanentStore.savedText);
        
        assertTrue(permanentStore.savedText.contains("double,speed,1.0"));
        assertTrue(permanentStore.savedText.contains("boolean,boolTest,true"));
        assertTrue(permanentStore.savedText.contains("string,strTest,blah"));
    }
    
    @Test
    public void testLoadingAllProperties()
    {
        DoubleProperty speedProperty = new DoubleProperty("speed", 1.0);
        BooleanProperty trueProperty = new BooleanProperty("boolTest",true);
        StringProperty blahProperty = new StringProperty("strTest","blah");
        speedProperty.set(5.0);
        trueProperty.set(false);
        blahProperty.set("boo");
        
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
        assertEquals(5.0,speedProperty.get(),0.001);
        assertEquals(false,trueProperty.get());
        assertEquals("boo",blahProperty.get());
        
        speedProperty = new DoubleProperty("speed", 1.0);
        trueProperty = new BooleanProperty("boolTest",true);
        blahProperty = new StringProperty("strTest","blah");
        
        assertTrue(speedProperty.get() == 5.0);
        assertTrue(!(trueProperty.get()));
        assertTrue(blahProperty.get().equals("boo")); 

        
    }
        
    @Test
    public void testSaveInvalidProperty() {
        permanentStore.savedText = "double,Foo,invalid";
        
        permanentStore.LoadFromDisk();
        
        DoubleProperty property = new DoubleProperty("Foo", 4.0);
        
        assertEquals(4.0, property.get(), 0);
    }
    
    private void saveAndReloadProperties() {
        this.saveProperties();
        
        this.reloadProperties();
    }
    
    private void saveProperties() {
        permanentStore.SaveToDisk();
        
        TestLog(permanentStore.savedText);
    }
    
    private void reloadProperties() {
        //clear the table, force a load
        permanentStore.table.clear();
        
        // verify table is actually clear
        assertTrue(permanentStore.table.isEmpty());
        
        permanentStore.LoadFromDisk();
    }
}