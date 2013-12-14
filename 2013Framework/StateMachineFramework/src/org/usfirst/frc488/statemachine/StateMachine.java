package org.usfirst.frc488.statemachine;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author XbotAdmin
 */
public abstract class StateMachine {
    public Integer address;
    private State currentState;
    private Scheduler scheduler;
    
    protected StateMachine() {
        address = StateMachineFramework.GetUniqueId();
        currentState = null;
        scheduler = StateMachineFramework.scheduler;
        
        // Register us for receiving a message.
        scheduler.addNewStateMachine(this);
    }
    
    /**
     * Get the definition of this state machine.
     * Returns the initial state.
     * @return 
     */
    protected abstract State getDefinition();
    
    /**
     * Pushes a message to this state machine.
     * @param message 
     */
    public void push(Message message) {
        message.destination = address;
        scheduler.push(message);
    }
    
    /**
     * Called by the state machine scheduler to deliver a message to the state
     * machine.
     * @param message 
     */
    void deliver(Message message) {
        if (currentState == null) {
            currentState = getDefinition();
        }
        else {
            currentState = currentState.getNextState(message);
        }
        
        currentState.deliver(this, message);
    }
}
