package xbot.common;



import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import xbox.common.statemachines.BaseWorkerResult;
import xbox.common.statemachines.StateMachine;
import xbox.common.statemachines.TransitionRule;
import xbox.common.statemachines.BaseWorker;
import static org.mockito.Mockito.*;

class SimpleRule extends TransitionRule {

	public SimpleRule(List<Class<?>> applicableStates) {
		super(applicableStates);
	}

	@Override
	public BaseWorker testForTransition(StateMachine machine) {
		return null;
	}
}

class StateA extends BaseWorker {

	@Override
	public BaseWorkerResult execute() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class StateB extends BaseWorker {

	@Override
	public BaseWorkerResult execute() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

public class TransitionRuleTest {
	TransitionRule rule = null;
	StateMachine machine = null;
	
	@Before
	public void setup() {
		rule = new SimpleRule(null);
		machine = Mockito.mock(StateMachine.class);
	}
	
	@Test
	public void testApplicabilityAll(){
		assertTrue(rule.isRuleApplicable(machine));
	}
	
	@Test 
	public void testDoesntMatchClass() {
		rule = new SimpleRule(Arrays.asList(StateA.class));
		machine.currentState = new StateB();
		assertFalse(rule.isRuleApplicable(machine));
		
	}
	
	@Test 
	public void testDoesMatchClass() {
		rule = new SimpleRule(Arrays.asList(StateA.class));
		machine.currentState = new StateA();
		assertTrue(rule.isRuleApplicable(machine));	
	}
	
	
	
	
}
