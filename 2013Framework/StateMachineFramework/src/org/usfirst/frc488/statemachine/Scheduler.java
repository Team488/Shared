package org.usfirst.frc488.statemachine;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author XbotAdmin
 */

import java.util.*;

public class Scheduler {
    private LinkedList messages;
    private Hashtable stateMachines;
    
    public Scheduler() {
        messages = new LinkedList();
    }
    
    /**
     * Sends a message.
     * @param message 
     */
    public void push(Message message) {
        messages.addLast(message);
    }
    
    /**
     * Dequeues and delivers one message.
     */
    public boolean pump() {
        if (messages.isEmpty()) {
            return false;
        }
        
        Message message = (Message) messages.pop();
        int destAddress = message.destination;
        
        if (!stateMachines.containsKey(destAddress)) {
            throw new IllegalStateException(
                    "Couldn't deliver message addressed to non-existent state machine" + destAddress);
        }
        
        StateMachine stateMachine = (StateMachine) stateMachines.get(destAddress);
        stateMachine.deliver(message);
        
        return true;
    }
    
    /**
     * Registers the state machine as a recipient of messages.
     * @param machine 
     */
    void addNewStateMachine(StateMachine machine) {
        stateMachines.put(machine.address, machine);
    }
}
