/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.tests.common;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import xbot.common.CommonTools;
import xbot.common.PropertySaverWorker;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.TableProxy;
import xbot.test.common.BaseTest;
import xbot.test.common.PermanentStoreTest;

/**
 *
 * @author sterlingdorminey
 */
public class PropertySaverWorkerTest extends BaseTest {
    
    @Test
    public void testPropertySaver() {
        BooleanProperty property = new BooleanProperty("Foo", false);
        PropertySaverWorker worker = new PropertySaverWorker();
        worker.init();
        
        property.set(true);
        assertEquals(null, permanentStore.getBoolean("Foo"));
        worker.exec();
        assertEquals(true, permanentStore.getBoolean("Foo"));
        
        // Now that the properties have been saved, executing it further
        // without calling init() again should not make it save.
        
        property.set(false);
        assertEquals(true, permanentStore.getBoolean("Foo"));
        worker.exec();
        assertEquals(true, permanentStore.getBoolean("Foo"));
        
        // But now that we've called init() we can save again.
        worker.init();
        worker.exec();
        assertEquals(false, permanentStore.getBoolean("Foo"));
    }
}
