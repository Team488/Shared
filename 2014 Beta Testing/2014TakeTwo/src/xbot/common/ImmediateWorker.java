/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

/**
 *
 * @author Alex
 */
public class ImmediateWorker extends WorkerBase {
    WorkerBase worker;
    public ImmediateWorker(WorkerBase worker) {
        super("ImmediateWorker");
        this.worker = worker;
    }
    
    public void init() {
        worker.init();
    }
    
    public void exec() {
        worker.exec();
    }
    
    public ExecState isFinished() {
        return ExecState.SUCCESS;
    }
}
