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
public class CollectorDeployState {
    /**
     *
     */
    public String name;
    
    /**
     *
     * @param name
     */
    public CollectorDeployState(String name) {
        this.name = name;
    }
    
    /**
     *
     */
    public static CollectorDeployState FIRING = new CollectorDeployState("FIRING");
    /**
     *
     */
    public static CollectorDeployState UP = new CollectorDeployState("UP");
    /**
     *
     */
    public static CollectorDeployState DOWN = new CollectorDeployState("DOWN");
    /**
     *
     */
    public static CollectorDeployState STOP = new CollectorDeployState("STOP");
    /**
     * Collector holds the ball away from the catapult until it's ready to push it in.
     */
    public static CollectorDeployState DURESS = new CollectorDeployState("DURESS");
    
    public static CollectorDeployState SAFE = new CollectorDeployState("SAFE");
}
