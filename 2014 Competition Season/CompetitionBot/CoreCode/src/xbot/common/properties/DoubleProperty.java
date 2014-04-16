/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.properties;

/**
 * This manages a double in the property system.
 * @author Alex
 */
public class DoubleProperty extends Property {
    double defaultValue;
    /**
     *
     * @param name
     * @param defaultValue
     */
    public DoubleProperty(String name, double defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
        load();
    }
    
    /**
     *
     * @return
     */
    public double get() {
        return randomAccessStore.getDouble(key).doubleValue();
    }
    
    /**
     *
     * @param value
     */
    public void set(double value) {
        randomAccessStore.setDouble(key, value);
    }
    
    /**
     *
     */
    public void save() {
        permanentStore.setDouble(key, randomAccessStore.getDouble(key).doubleValue());
    }

    /**
     *
     */
    public void load() {
        Double value = permanentStore.getDouble(key);
        if(value != null) {
            randomAccessStore.setDouble(key, value.doubleValue());
        } else {
            set(defaultValue);
        }
    }
    
}
