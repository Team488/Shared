/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

/**
 *
 * @author Alex
 */
public abstract class Property {
    public String key;
    
    ITableProxy permanentStore;
    ITableProxy randomAccessStore;
    
    public Property(String key) {
        this.key = key;
        this.permanentStore = CommonTools.Get().propertyManager.permanentStore;
        this.randomAccessStore = CommonTools.Get().propertyManager.randomAccessStore;
        CommonTools.Get().propertyManager.registerProperty(this);
    }
    
    public abstract void save();
    public abstract void load();
}
