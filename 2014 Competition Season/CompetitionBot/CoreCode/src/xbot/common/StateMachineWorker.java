/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.common;

import java.util.*;
import xbot.aerialassist.AerialWorkerBase;

/**
 *
 * @author jogilber
 */
public class StateMachineWorker extends AerialWorkerBase {

    // workers[i] is the worker executed in state i.
    private Vector workers;
    
    // we transition from i -> failureActions[i] if the worker failed.
    private Vector failureActions;
    
    // we transition from i -> successActions[i] if the worker succeeded.
    private Vector successActions;
    
    // maps worker name -> index of worker.
    private Hashtable nameToWorkerIndex;
    
    // maps i -> name of worker at i.
    private Hashtable workerIndexToName;
    
    // transition to SUCCESS when the state machine is done.
    /**
     *
     */
    public static int SUCCESS = -1;
    /**
     *
     */
    public static int FAILURE = -2;
    /**
     *
     */
    public static int UNDEFINED = -3;
    
    private int currentWorker;
    
    private int startWorker;
    
    /**
     *
     * @param name
     */
    public StateMachineWorker(String name) {
        super(name);
        this.workers = new Vector();
        this.failureActions = new Vector();
        this.successActions = new Vector();
        this.nameToWorkerIndex = new Hashtable();
        this.workerIndexToName = new Hashtable();
        this.startWorker = UNDEFINED;
    }
    
    /**
     * Adds a worker to the state machine as a state.
     * @param worker worker to execute in the state.
     * @return id for the newly-added state.
     */
    public int addWorker(WorkerBase worker) {
        this.workers.addElement(worker);
        int id = this.workers.size()-1;
        
        this.Verbose("Added worker " + worker.getName() + " with id " + id);
        
        // The default is that any failure action brings the state machine
        // into the FAILED state.
        this.failureActions.addElement(new Integer(FAILURE));
        // We require all successes to be explicitly mappe by the user.
        this.successActions.addElement(new Integer(UNDEFINED));
        return id;
    }
    
    /**
     * Adds a worker with the specified name to the state machine as a state.
     * @param workerName name of the worker.
     * @param worker worker to execute.
     * @return id of the worker.
     */
    public int addWorker(String workerName, WorkerBase worker) {
        int workerIndex = this.addWorker(worker);
        this.nameToWorkerIndex.put(workerName, new Integer(workerIndex));
        this.workerIndexToName.put(new Integer(workerIndex), workerName);
        return workerIndex;
    }
    
    /**
     *
     * @param source
     */
    public void onStart(int source) {
        this.startWorker = source;
    }
    
    /**
     *
     * @param source
     * @param destination
     */
    public void onSuccess(int source, int destination) {
        this.Verbose("On success: " + source + " -> " + destination);
        this.successActions.setElementAt(new Integer(destination), source);
    }
    
    /**
     *
     * @param source
     * @param destination
     */
    public void onFailure(int source, int destination) {
        this.Verbose("On failure: " + source + " -> " + destination);
        this.failureActions.setElementAt(new Integer(destination), source);        
    }
    
    /**
     *
     */
    public void init() {
        // Perform validation.
        if (!this.validateAction(this.startWorker)) {
            Warning("Must define start state.");
            this.startWorker = FAILURE;
            this.currentWorker = FAILURE;
            return;
            //throw new RuntimeException("Must define start state.");
        }
        for (int i = 0; i < this.workers.size(); i++) {
            int successAction = this.getSuccessAction(i);
            int failureAction = this.getFailureAction(i);
            
            if(!validateAction(successAction)) {
                this.createTransitionFailureMessage(i, "success");
            }
            if(!validateAction(failureAction)) {
                this.createTransitionFailureMessage(i, "failure");
            }
        }
        
        this.currentWorker = this.startWorker;
        this.getCurrentWorker().init();
        
        Info("Initialized state machine");
    }
    
    /**
     *
     */
    public void exec() {
        if (this.currentWorker < 0) {
            //Verbose("State machine is done, not executing.");
            return;
        }
        
        WorkerBase worker = this.getCurrentWorker();
        worker.exec();
        ExecState execState = worker.isFinished();
        if (execState == ExecState.NOT_DONE) {
            return;
        }
        
        int nextWorker;
        if (execState == ExecState.SUCCESS) {
            nextWorker = this.getSuccessAction(this.currentWorker);
        } else if (execState == ExecState.FAILURE) {
            nextWorker = this.getFailureAction(this.currentWorker);
        } else {
            Warning("Unrecognized new ExecState.");
            return;
            //throw new RuntimeException("Unrecognized new ExecState.");
        }
        
        Info("Transitioned from worker " + this.currentWorker +
                " (" + this.getWorkerName(this.currentWorker) +
                ") to worker " + nextWorker + " (" +
                this.getWorkerName(nextWorker) + ")");
        
        this.currentWorker = nextWorker;
        
        if (this.currentWorker >= 0) {
            this.getCurrentWorker().init();
        }
    }
    
    
    /**
     *
     * @return
     */
    public ExecState isFinished() {
        if (this.currentWorker == SUCCESS) {
            return ExecState.SUCCESS;
        }
        
        if (this.currentWorker == FAILURE) {
            return ExecState.FAILURE;
        }
        
        return ExecState.NOT_DONE;
    }
    
    /**
     *
     * @return
     */
    public String getCurrentWorkerName()
    {
        return this.getWorkerName(this.currentWorker);
    }
    
    /**
     * Returns the name of the current state.
     * This is distinct from the worker name.. there may be two workers with the
     * same name added to the 
     * @return 
     */
    public String getCurrentStateName() {
        Integer currentWorkerIndex = new Integer(this.currentWorker);
        
        if (this.currentWorker == SUCCESS || this.currentWorker == FAILURE) {
            return this.getWorkerName(this.currentWorker);
        }
        
        if (this.workerIndexToName.containsKey(currentWorkerIndex)) {
            return (String) this.workerIndexToName.get(currentWorkerIndex);
        }
        
        return "Unknown";
    }
    
    public int getCurrentState() {
        return this.currentWorker;
    }
    
    /**
     * Sets the current state to the state named stateName
     * @param stateName name of the state to set the state machine to.
     */
    public void setStateTestOnly(String stateName) {
        Integer stateIndex = (Integer) this.nameToWorkerIndex.get(stateName);
        this.currentWorker = stateIndex.intValue();
        this.getCurrentWorker().init();
    }
    
    private String getWorkerName(int worker) {
        if (worker == SUCCESS) {
            return "Success";
        }
        if (worker == FAILURE) {
            return "Failure";
        }
        return this.getWorkerAt(worker).getName(); 
    }
    
    private void createTransitionFailureMessage(int stateIndex, String transitionType) {
        String message =  "No " + transitionType + " transition defined from state "
                                + stateIndex + ". Name: "
                                + this.getWorkerAt(stateIndex).getName();
        Warning(message);
        //throw new RuntimeException(message);
    }
    
    private WorkerBase getCurrentWorker() {
        return this.getWorkerAt(this.currentWorker);
    }
    
    private WorkerBase getWorkerAt(int index) {
        return (WorkerBase) this.workers.elementAt(index);
    }
    
    private int getSuccessAction(int index) {
        return ((Integer) this.successActions.elementAt(index)).intValue();
    }
    
    private int getFailureAction(int index) {
        return ((Integer) this.failureActions.elementAt(index)).intValue();
    }
    
    private boolean validateAction(int action) {
        return (action >= 0 || (action == SUCCESS || action == FAILURE));
    }
}
