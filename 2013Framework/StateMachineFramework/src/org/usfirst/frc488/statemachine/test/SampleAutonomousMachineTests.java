/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc488.statemachine.test;

import junit.framework.TestCase;
import org.usfirst.frc488.statemachine.StateMachineFramework;

/**
 *
 * @author XbotAdmin
 */
public class SampleAutonomousMachineTests extends TestCase {
    private MockVisionSubsystem vision;
    
    public SampleAutonomousMachineTests() {
        vision = new MockVisionSubsystem();
        StateMachineFramework.Initialize();
    }
    
    public void testDiskFound() {
        DiskFoundMessage visionMessage = new DiskFoundMessage();
        visionMessage.bearing = 30;
        visionMessage.distance = 10;
        
        SampleAutonomousMachine machine = new SampleAutonomousMachine();
        
        AutonomousModeInitMessage initMessage = new AutonomousModeInitMessage();
        initMessage.vision = vision;
        
        machine.push(initMessage);
        
        // Send all the messages we have in the queue.
        while(StateMachineFramework.runStep()) { }
    }
}
