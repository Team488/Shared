/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.properties;

import java.util.Hashtable;

import xbot.common.CommonUtils;
import xbot.common.Loggable;

/**
 * A simple implementation of ITableProxy. Uses a HashTable as the table to
 * store and save properties.
 * @author Alex
 */
public class TableProxy extends Loggable implements ITableProxy {
    /**
     *
     */
    public Hashtable table;

    /**
     *
     * @param name
     */
    public TableProxy(String name) {
        super(name);
        this.table = new Hashtable();
    }
    
    /**
     *
     * @param key
     * @param value
     */
    public void setDouble(String key, double value) {
        table.put(key, Double.toString(value));
    }

    /**
     *
     * @param key
     * @return
     */
    public Double getDouble(String key) {
        if (table.get(key)==null){
            return null;
        }
        else {
            return Double.valueOf(table.get(key).toString());
        }
    }
    
    /**
     *
     * @param key
     * @param value
     */
    public void setBoolean(String key, boolean value) {
        table.put(key, CommonUtils.booleanToString(value));
    }

    /**
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        if (table.get(key)==null){
            return null;
        }
        else {
            return CommonUtils.parseObjectBoolean(table.get(key).toString());
        }
    }
    
    /**
     *
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        table.put(key, value);
    }

    /**
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return (String) table.get(key);
    }
    
}
