/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.properties;

import xbot.common.CommonTools;

/**
 * There are many values on the robot that we want to configure on the fly as well as
 * persist once we're happy with the result. We call these Properties. They can be read/written
 * to rapidly using a RandomAccessStore, and know how to save themselves to PermanentStorage when
 * a save is necessary.
 * @author Alex
 */
public abstract class Property {
    /**
     *
     */
    public String key;
    
    ITableProxy permanentStore;
    ITableProxy randomAccessStore;
    
    /**
     * The name of the property. This should be unique unless you really know
     * what you're doing.
     * @param key
     */
    public Property(String key) {
        this.key = key;
        this.permanentStore = CommonTools.Get().propertyManager.permanentStore;
        this.randomAccessStore = CommonTools.Get().propertyManager.randomAccessStore;
        CommonTools.Get().propertyManager.registerProperty(this);
    }
    
    /**
     * Save the property permanently. This shouldn't happen very often (I/O is expensive).
     */
    public abstract void save();
    /**
     * Load the property from storage. This shouldn't happen very often (I/O is expensive). 
     */
    public abstract void load();
}
