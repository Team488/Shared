package xbox.common.statemachines;

import java.util.List;

public class SimpleStateMapRule extends TransitionRule {

	Class<?> targetResult;
	BaseWorker newState;
	
	
	public SimpleStateMapRule(List<Class<?>> applicableStates, Class<?> targetResult, BaseWorker newState) {
		super(applicableStates);
		this.targetResult = targetResult;
		this.newState = newState;
	}
	
	@Override
	public BaseWorker testForTransition(StateMachine machine) {
		if(targetResult.isAssignableFrom(machine.result.getClass())) {
			return newState;
		}
		return NO_STATE_CHANGE;
	}
	
}
