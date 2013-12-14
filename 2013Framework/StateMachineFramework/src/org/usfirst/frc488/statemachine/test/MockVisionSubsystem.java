/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc488.statemachine.test;

import org.usfirst.frc488.statemachine.Message;
import org.usfirst.frc488.statemachine.StateMachine;

/**
 *
 * @author XbotAdmin
 */
public class MockVisionSubsystem implements IVisionSubsystem {
    
    public Message messageToReturn;
    
    @Override
    public void SearchForDisk(StateMachine caller, int timeoutSeconds) {
        System.out.printf(
                "SearchForDisk() called. Returning message %s\n",
                messageToReturn.toString());
        
        caller.push(messageToReturn);
    }
    
}
