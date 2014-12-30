package xbot.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import xbox.common.statemachines.BaseWorker;
import xbox.common.statemachines.BaseWorkerResult;
import xbox.common.statemachines.WhileTrueStateMachine;





public class WhileTrueStateMachineTest {
	WhileTrueStateMachine machine;
	boolean result;
	
	class SuccessWorker extends BaseWorker {
		@Override
		public BaseWorkerResult execute() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	class DefaultWorker extends BaseWorker {
		@Override
		public BaseWorkerResult execute() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	@Before
	public void setup() {
		this.machine = new WhileTrueStateMachine((obj) -> {
			return result;
		}, 
		()-> { return new SuccessWorker(); }, 
		()-> { return new DefaultWorker(); });
	}
	
	@Test
	public void test() {
		assertTrue(DefaultWorker.class.isInstance(this.machine.currentState));
		machine.execute();
		assertTrue(DefaultWorker.class.isInstance(this.machine.currentState));
		result = true;
		machine.execute();
		assertTrue(SuccessWorker.class.isInstance(this.machine.currentState));
		result = false;
		machine.execute();
		assertTrue(DefaultWorker.class.isInstance(this.machine.currentState));
	}

}
