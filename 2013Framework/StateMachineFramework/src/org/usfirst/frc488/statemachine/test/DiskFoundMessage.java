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
public class DiskFoundMessage extends Message {
    public int bearing;
    public int distance;
    
    public static final String Name = "DiskFoundMessage";
    
    @Override
    public String getName() {
        return Name;
    }
}
