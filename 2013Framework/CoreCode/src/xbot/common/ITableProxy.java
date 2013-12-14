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
public interface ITableProxy {
        
    public void setDouble(String key, double value);
    
    public Double getDouble(String key);
}
