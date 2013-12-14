package org.usfirst.frc488.statemachine;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author XbotAdmin
 */
public class Message {
    Integer destination;
    
    public String getName() {
        return "Message";
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
