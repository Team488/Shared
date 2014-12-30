/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.properties;

import xbot.common.properties.PermanentStorageProxy;
import java.util.Vector;

/**
 * The PropertyManager keeps track of all properties in CoreCode. All properties are
 * implicitly added into its storage. It is capable of loading or saving all properties
 * via permanentStorage, and getting updates to these properties via the RandomAccessStore.
 * @author Alex
 */
public class PropertyManager {
    /**
     *
     */
    public Vector properties;
    /**
     *
     */
    public PermanentStorageProxy permanentStore;
    /**
     *
     */
    public ITableProxy randomAccessStore;

    /**
     *
     * @param permanentStore
     * @param randomAccessStore
     */
    public PropertyManager(PermanentStorageProxy permanentStore, ITableProxy randomAccessStore) {
        this.properties = new Vector();
        this.permanentStore = permanentStore;
        this.randomAccessStore = randomAccessStore;
    }
    
    /**
     * Adds the property to the local collection.
     * @param property
     */
    public void registerProperty(Property property) {
        properties.addElement(property);
    }
    
    /**
     * Loads all properties from storage into the PermanentStore's table.
     */
    public void LoadPropertiesFromStorage()
    {
        permanentStore.LoadFromDisk();
    }
    
    /**
     * Save all properties into permanent storage.
     */
    public void saveOutAllProperties() {
        
        int escape = 0;
        
        for(int i = 0; i < properties.size(); i++) {
            Property prop = (Property)properties.elementAt(i);
            prop.save();
            
            escape++;
            if (escape > 500)
            {
                break;
            }
        }
        
        // We also need to trigger the permanent storage proxy to actually write
        // to disk.
        permanentStore.SaveToDisk();
    }
}
