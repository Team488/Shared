/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc488.statemachine.test;

import org.usfirst.frc488.statemachine.Message;

/**
 *
 * @author XbotAdmin
 */
public class AutonomousModeInitMessage extends Message {
    public IVisionSubsystem vision;
    
    public static final String Name = "AutonomousModeInitMessage";
    
    @Override
    public String getName() {
        return Name;
    }
}
