package xbox.common.statemachines;

import java.util.ArrayList;

public class StateMachine extends BaseWorker {
	protected ArrayList<TransitionRule> rules = new ArrayList<TransitionRule>();
	
	public BaseWorker currentState = null;
	public BaseWorkerResult result = null;
	
	public StateMachine() {
		
	}
	
	public BaseWorkerResult execute() {
		// execute current state (if there is one)
		if(currentState != null) {
			result = currentState.execute();
		}
		
		// evaluate rules to determine if state change is required
		for(TransitionRule rule : rules) {
			if(rule.isRuleApplicable(this)) {
				BaseWorker newState = rule.testForTransition(this);
				if(newState != TransitionRule.NO_STATE_CHANGE) {
					logger.info("Transitioning to state: " + newState);
					currentState = newState;
					break;
				}
			}
		}
		
		// TODO: Decide if/how statemachines should have return values, maybe the transition rules
		// can specify a result and if they don't its not_done?
		return BaseWorkerResult.NOT_DONE;
	}
}
