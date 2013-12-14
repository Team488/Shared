/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import java.util.Vector;

/**
 *
 * @author Alex
 */
public class PropertyManager {
    public Vector properties;
    public PermanentStorageProxy permanentStore;
    public ITableProxy randomAccessStore;

    public PropertyManager(PermanentStorageProxy permanentStore, ITableProxy randomAccessStore) {
        this.properties = new Vector();
        this.permanentStore = permanentStore;
        this.randomAccessStore = randomAccessStore;
    }
    
    public void registerProperty(Property property) {
        properties.addElement(property);
    }
    
    public void LoadPropertiesFromStorage()
    {
        permanentStore.LoadFromDisk();
    }
    
    public void saveOutAllProperties() {
        for(int i = 0; i < properties.size(); i++) {
            Property prop = (Property)properties.elementAt(i);
            prop.save();
        }
    }
}
