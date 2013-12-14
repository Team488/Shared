package org.usfirst.frc488.statemachine;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;

/**
 *
 * @author XbotAdmin
 */
public abstract class State {
    private Hashtable successorStates;
    
    public State() {
        successorStates = new Hashtable();
    }
    
    /**
     * Adds a transition, upon receiving a message with name messageName,
     * to the nextState state.
     * @param nextState 
     */
    public void addTransition(State nextState) {
        String messageName = nextState.getMessageName();
        
        if (successorStates.containsKey(messageName)) {
            throw new IllegalStateException(
                    "Two transitions from the same state " +
                    "cannot share mssage names.");
        }
        successorStates.put(messageName, nextState);
    }
    
    /**
     * Given a message, returns the next state to transition to
     * to receive the message.
     * @param message 
     */
    State getNextState(Message message) {
        String name = message.getName();
        if (!successorStates.containsKey(name)) {
            throw new IllegalStateException(
                    "Can't handle a " +
                    name +
                    " message from current state");
        }
        return (State) successorStates.get(name);
    }
    
    /**
     * Gets the name of the message that the state accepts.
     * @return 
     */
    protected abstract String getMessageName();
    
    /**
     * Handles entering the state by receiving the message above.
     * Passes the StateMachine in, as a context where state can be stored
     * for one run of the state machine.
     * @param context
     * @param message 
     */
    protected abstract void deliver(StateMachine context, Message message);
}
