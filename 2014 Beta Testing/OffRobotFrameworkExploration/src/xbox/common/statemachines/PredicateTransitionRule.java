package xbox.common.statemachines;

import java.util.List;
import java.util.function.*;

public class PredicateTransitionRule extends TransitionRule {
	Predicate<StateMachine> predicatefunction;
	Supplier<BaseWorker> successWorkerFunction;
	
	interface TransitionFunction {
        BaseWorker operation(StateMachine machine);   
    }
	
	public PredicateTransitionRule(List<Class<?>> applicableStates, 
			Predicate<StateMachine> predicatefunction, 
			Supplier<BaseWorker> successWorkerFunction) {
		super(applicableStates);
		this.predicatefunction = predicatefunction;
		this.successWorkerFunction = successWorkerFunction;
	}
	
	@Override
	public BaseWorker testForTransition(StateMachine machine) {
		if(this.predicatefunction.test(machine)) {
			return this.successWorkerFunction.get();
		}
		return NO_STATE_CHANGE;
	}
	

}
