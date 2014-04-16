/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

/**
 * Since Java ME doesn't have enums, we built our own.
 * @author John
 */
public class FauxEnum {
    
    private int id;
    
    /**
     * 
     * @param id The value this enum wraps
     */
    public FauxEnum(int id)
    {
        this.id = id;
    }
    
    /**
     * Returns the integer value of this enum
     * @return
     */
    public int Value()
    {
        return id;
    }
    
    public String toString() {
        return "Enum:" + id;
    }
}
