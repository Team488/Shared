package xbot.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

import xbox.common.statemachines.BaseWorkerResult;
import xbox.common.statemachines.FailureWorkerResult;
import xbox.common.statemachines.SimpleStateMapRule;
import xbox.common.statemachines.StateMachine;
import xbox.common.statemachines.SuccessWorkerResult;
import xbox.common.statemachines.TransitionRule;
import xbox.common.statemachines.BaseWorker;

public class SimplestateMapRuleTest {

	SimpleStateMapRule rule;
	BaseWorkerResult targetResult;
	BaseWorker newState;
	StateMachine machine;
	@Before
	public void setup() {
		targetResult = new SuccessWorkerResult();
		newState = Mockito.mock(BaseWorker.class);
		machine = Mockito.mock(StateMachine.class);
		rule = new SimpleStateMapRule(null, SuccessWorkerResult.class, newState);
	}
	
	@Test
	public void testRuleApplies() {
		machine.result = targetResult;
		assertEquals(newState, rule.testForTransition(machine));
	}
	
	@Test
	public void testRuleDoesntApply() {
		machine.result = new FailureWorkerResult();
		assertEquals(TransitionRule.NO_STATE_CHANGE, rule.testForTransition(machine));
	}

}
