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
public class DiskNotFoundMessage extends Message {
    public static final String Name = "DiskNotFoundMessage";
    
    @Override
    public String getName() {
        return Name;
    }
}
