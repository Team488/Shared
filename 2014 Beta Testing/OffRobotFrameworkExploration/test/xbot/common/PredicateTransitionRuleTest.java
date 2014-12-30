package xbot.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import xbox.common.statemachines.FunctionTransitionRule;
import xbox.common.statemachines.PredicateTransitionRule;
import xbox.common.statemachines.StateMachine;
import xbox.common.statemachines.TransitionRule;
import xbox.common.statemachines.BaseWorker;

public class PredicateTransitionRuleTest {
	PredicateTransitionRule rule;
	BaseWorker newState;
	StateMachine machine;
	@Before
	public void setup() {
		newState = Mockito.mock(BaseWorker.class);
		machine = Mockito.mock(StateMachine.class);
	}
	
	@Test
	public void testTrueResponse() {
		rule = new PredicateTransitionRule(
				null, 
				(sm) -> { return true;}, 
				() -> { return newState;});
		assertEquals(newState, rule.testForTransition(machine));
	}
	
	@Test
	public void testFalseResponse() {
		rule = new PredicateTransitionRule(
				null, 
				(sm) -> { return false;}, 
				() -> { return newState;});
		assertEquals(TransitionRule.NO_STATE_CHANGE, rule.testForTransition(machine));
	}
}
