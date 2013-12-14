/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc488.statemachine.test;

import org.usfirst.frc488.statemachine.StateMachine;

/**
 *
 * @author XbotAdmin
 */
public interface IVisionSubsystem {
    /**
     * Searches for a disk using vision processing.
     * Enqueues a DiskLeftMessage if the disk is left,
     * or a DiskRightMessage if right,
     * or a DiskNotFoundMessage if we didn't find the disk in timeout seconds.
     * @param timeoutSeconds 
     * @param caller 
     */
    void SearchForDisk(StateMachine caller, int timeoutSeconds);
}
