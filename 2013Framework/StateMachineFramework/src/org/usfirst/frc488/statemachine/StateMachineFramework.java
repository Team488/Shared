package org.usfirst.frc488.statemachine;

/**
 *
 * @author XbotAdmin
 */
public class StateMachineFramework {
    static Scheduler scheduler;
    private static Integer nextUniqueId;
    private static boolean isInitialized = false;
    
    /**
     * Initializes the state machine framework.
     */
    public static void Initialize() {
        if (isInitialized) {
            return;
        }
        
        nextUniqueId = new Integer(0);
        scheduler = new Scheduler();
    }
    
    /**
     * Tries running a step
     * @return true if a step was run.
     */
    public static boolean runStep() {
        return scheduler.pump();
    }
    
    /**
     * Returns a unique identifier 
     * @return 
     */
    static Integer GetUniqueId() {
        nextUniqueId = new Integer(nextUniqueId.intValue()+1);
        return nextUniqueId;
    }
}
