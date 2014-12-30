/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.collection;

/**
 *
 * @author Nikhil
 */
public class CollectorRollerState {
    /**
     *
     */
    public String name;
    
    /**
     *
     * @param name
     */
    public CollectorRollerState(String name) {
        this.name = name;
    }
    
    /**
     *
     */
    public static CollectorRollerState COLLECT = new CollectorRollerState("COLLECT");
    /**
     *
     */
    public static CollectorRollerState STOP = new CollectorRollerState("STOP");
    /**
     *
     */
    public static CollectorRollerState REVERSE = new CollectorRollerState("REVERSE");
}
