/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

/**
 *
 * @author Alex
 */
public class DoubleProperty extends Property {
    double defaultValue;
    public DoubleProperty(String name, double defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
        load();
    }
    
    public double get() {
        return randomAccessStore.getDouble(key).doubleValue();
    }
    
    public void set(double value) {
        randomAccessStore.setDouble(key, value);
    }
    
    public void save() {
        permanentStore.setDouble(key, get());
    }

    public void load() {
        Double value = permanentStore.getDouble(key);
        if(value != null) {
            set(value.doubleValue());
        } else {
            set(defaultValue);
        }
    }
    
}
