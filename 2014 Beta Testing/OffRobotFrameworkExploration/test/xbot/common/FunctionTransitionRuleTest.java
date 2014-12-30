package xbot.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import xbox.common.statemachines.BaseWorkerResult;
import xbox.common.statemachines.FunctionTransitionRule;
import xbox.common.statemachines.StateMachine;
import xbox.common.statemachines.BaseWorker;

public class FunctionTransitionRuleTest {
	FunctionTransitionRule rule;
	BaseWorkerResult targetResult;
	BaseWorker newState;
	StateMachine machine;
	boolean state = false;
	@Before
	public void setup() {
		newState = Mockito.mock(BaseWorker.class);
		machine = Mockito.mock(StateMachine.class);
	}
	
	@Test
	public void testNullResponse() {
		rule = new FunctionTransitionRule(null, (sm) -> { return null; });
		assertEquals(null, rule.testForTransition(machine));
	}
	
	public boolean getState() {
		return this.state;
	}
	
	@Test
	public void testAccessingExternalVariables() {
		state = false;
		rule = new FunctionTransitionRule(null, (sm) -> { return getState() ? newState : null; });
		assertEquals(null, rule.testForTransition(machine));
		state = true;
		assertEquals(newState, rule.testForTransition(machine));
	}
}
