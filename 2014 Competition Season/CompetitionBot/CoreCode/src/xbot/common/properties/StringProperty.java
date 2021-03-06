/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.common.properties;

/**
 * A type of Property that manages a String value.
 * @author Sterling
 */
public class StringProperty extends Property {
    private String defaultValue;
    /**
     *
     * @param name
     * @param defaultValue
     */
    public StringProperty(String name, String defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
        load();
    }
    
    /**
     *
     * @return
     */
    public String get() {
        return randomAccessStore.getString(key);
    }
    
    /**
     *
     * @param value
     */
    public void set(String value) {
        randomAccessStore.setString(key, value);
    }
    
    /**
     *
     */
    public void save() {
        permanentStore.setString(key, randomAccessStore.getString(key));
    }

    /**
     *
     */
    public void load() {
        String value = permanentStore.getString(key);
        if(value != null) {
            set(value);
        } else {
            set(defaultValue);
        }
    }
}
