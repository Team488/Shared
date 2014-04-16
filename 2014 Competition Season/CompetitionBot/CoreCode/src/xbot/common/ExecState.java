/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.common;

/**
 * These states are used in Workers' isFinished() to signal their completion status.
 * @author jogilber
 */
public class ExecState extends FauxEnum {
    String name;
    /**
     *
     */
    public static ExecState NOT_DONE = new ExecState(0, "Not done");
    /**
     *
     */
    public static ExecState SUCCESS = new ExecState(1, "Success");
    /**
     *
     */
    public static ExecState FAILURE = new ExecState(2, "Failure");
    
    private ExecState(int id, String name) {
        super(id);
        this.name = name;
    }
    
    /**
     * Joins the execution state of two ExecStates. 
     * @param first
     * @param second
     * @return
     */
    public static ExecState Join(ExecState first, ExecState second) {
        if (first == FAILURE || second == FAILURE) {
            return FAILURE;
        }
        
        if (first == NOT_DONE || second == NOT_DONE) {
            return NOT_DONE;
        }
        
        return SUCCESS;
    }
    
    public String toString() {
        return name;
    }
}
