/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.test.common;

import xbot.common.ExecState;
import xbot.common.WorkerBase;

/**
 *
 * @author Alex
 */
public class TestableGenericWorker extends WorkerBase {
        ExecState result = ExecState.NOT_DONE;
        
        public int initCallCount;
        public int execCallCount;
        
        public TestableGenericWorker() {
            super("TestableGenericWorker");
        }
        
        public void setFinishedState(ExecState state) {
            this.result = state;
        }
        
        public void init() {
            initCallCount++;
        }
        
        public void exec() {
            execCallCount++;
        }
        
        public ExecState isFinished() {
            return this.result;
        }
}
