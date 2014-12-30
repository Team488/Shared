package xbox.common.statemachines;
import java.util.List;
import java.util.function.Function;
public class FunctionTransitionRule extends TransitionRule {
	Function<StateMachine, BaseWorker> function;
	
	interface TransitionFunction {
        BaseWorker operation(StateMachine machine);   
    }
	
	public FunctionTransitionRule(List<Class<?>> applicableStates, Function<StateMachine, BaseWorker> function) {
		super(applicableStates);
		this.function = function;
	}
	
	@Override
	public BaseWorker testForTransition(StateMachine machine) {
		return this.function.apply(machine);
	}
	

}
