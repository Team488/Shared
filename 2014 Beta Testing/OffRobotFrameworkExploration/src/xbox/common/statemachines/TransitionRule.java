package xbox.common.statemachines;

import java.util.ArrayList;
import java.util.List;

public abstract class TransitionRule {
	public static final BaseWorker NO_STATE_CHANGE = new BaseWorker() {
		
		@Override
		public BaseWorkerResult execute() {
			throw new RuntimeException("This worker should never be run");
		}
	};
	
	protected ArrayList<Class<?>> applicableStates = new ArrayList<Class<?>>();
	
	public TransitionRule(List<Class<?>> applicableStates) {
		if(applicableStates != null) {
			this.applicableStates.addAll(applicableStates);
		}
	}
	
	public boolean isRuleApplicable(StateMachine machine) {
		if(applicableStates.isEmpty()) {
			return true;
		} else {
			for(Class c : applicableStates) {
				if(machine.currentState.getClass() == c) {
					return true;
				}
			}
		}
		return false;
	}
	
	public abstract BaseWorker testForTransition(StateMachine machine);
}
