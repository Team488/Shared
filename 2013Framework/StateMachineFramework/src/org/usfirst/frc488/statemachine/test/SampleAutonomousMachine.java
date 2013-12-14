/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc488.statemachine.test;

import org.usfirst.frc488.statemachine.Message;
import org.usfirst.frc488.statemachine.State;
import org.usfirst.frc488.statemachine.StateMachine;

/**
 *
 * @author XbotAdmin
 */
public class SampleAutonomousMachine extends StateMachine {

    // These are variables that track with the lifetime of a state machine run.
    int latestDiskBearing;
    int latestDiskDistance;
    
    @Override
    protected State getDefinition() {
        // State declaration
        
        // This is the starting state for autonomous mode.
        // We begin by searching for disks.
        State searchingForDiskState = new SearchingForDiskState();
        // If we find a disk, we execute the deliver() function of this state.
        State diskFoundState = new DiskFoundState();
        // If we don't find a disk, we execute this deliver() function instead.
        State diskNotFoundState = new DiskNotFoundState();
        
        searchingForDiskState.addTransition(diskFoundState);
        searchingForDiskState.addTransition(diskNotFoundState);
        
        return searchingForDiskState;
    }
    
    class SearchingForDiskState extends State {
        @Override
        protected String getMessageName() {
            return AutonomousModeInitMessage.Name;
        }
        
        @Override
        protected void deliver(StateMachine ctx, Message msg) {
            SampleAutonomousMachine context = (SampleAutonomousMachine) ctx;
            AutonomousModeInitMessage message = (AutonomousModeInitMessage) msg;
            
            message.vision.SearchForDisk(ctx, 10);
            
            System.out.println("Searching for disk..");
        }
    }
    
    class DiskFoundState extends State {
        @Override
        protected String getMessageName() {
            return DiskFoundMessage.Name;
        }
        
        @Override
        protected void deliver(StateMachine ctx, Message msg) {
            SampleAutonomousMachine context = (SampleAutonomousMachine) ctx;
            DiskFoundMessage message = (DiskFoundMessage) msg;
        
            context.latestDiskBearing = message.bearing;
            context.latestDiskDistance = message.distance;
            
            System.out.printf("Disk found! Bearing: %d, Distance: %d\n",
                    context.latestDiskBearing,
                    context.latestDiskDistance);
        }
    }
    
    class DiskNotFoundState extends State {
        @Override
        protected String getMessageName() {
            return DiskNotFoundMessage.Name;
        }
        
        @Override
        protected void deliver(StateMachine ctx, Message msg) {
            SampleAutonomousMachine context = (SampleAutonomousMachine) ctx;
            DiskNotFoundMessage message = (DiskNotFoundMessage) msg;
            
            System.out.println("Disk not found!");
        }
    }
}
