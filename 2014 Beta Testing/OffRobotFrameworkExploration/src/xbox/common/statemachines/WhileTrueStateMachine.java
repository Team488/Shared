package xbox.common.statemachines;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class WhileTrueStateMachine extends StateMachine {
	public WhileTrueStateMachine(Predicate<StateMachine> condition, 
			Supplier<BaseWorker> success, 
			Supplier<BaseWorker> defaultState) {
		this.rules.add(new PredicateTransitionRule(null,
				(stateMachine)-> {
					return condition.test(this);
				}, ()-> {
					return success.get();
				}));
		
		this.rules.add(new PredicateTransitionRule(null,
				(stateMachine)-> {
					return !condition.test(this);
				}, ()-> {
					return defaultState.get();
				}));
		
		this.currentState = defaultState.get();
	}
}
