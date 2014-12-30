package xbot.arial;

import xbox.common.statemachines.PredicateTransitionRule;
import xbox.common.statemachines.StateMachine;

public class TriggerCollectorStateMachine extends StateMachine {
	AerialRobot robot;
	
	public TriggerCollectorStateMachine(AerialRobot robot) {
		this.robot = robot;
		
		this.rules.add(new PredicateTransitionRule(null,
				(stateMachine)-> {
					return robot.trigger.isPressed;
				}, ()-> {
					return new CollectorWorker(robot, 1.0);
				}));
		
		this.rules.add(new PredicateTransitionRule(null,
				(stateMachine)-> {
					return !robot.trigger.isPressed;
				}, ()-> {
					return new CollectorWorker(robot, 0);
				}));
		
		this.currentState = new CollectorWorker(robot, 0);
	}
}
