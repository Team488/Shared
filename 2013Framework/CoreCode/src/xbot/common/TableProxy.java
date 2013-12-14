/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

import java.util.Hashtable;

/**
 *
 * @author Alex
 */
public class TableProxy extends Loggable implements ITableProxy {
    public Hashtable table;

    public TableProxy(String name) {
        super(name);
        this.table = new Hashtable();
    }
    
    public void setDouble(String key, double value) {
        table.put(key, Double.valueOf(value));
    }

    public Double getDouble(String key) {
        return (Double)table.get(key);
    }
    
}
