package xbot.arial;

import java.util.Arrays;
import java.util.List;

import xbot.common.WaitForTimeWorker;
import xbox.common.statemachines.FunctionTransitionRule;
import xbox.common.statemachines.PredicateTransitionRule;
import xbox.common.statemachines.SimpleStateMapRule;
import xbox.common.statemachines.StateMachine;
import xbox.common.statemachines.SuccessWorkerResult;
import xbox.common.statemachines.TransitionRule;

public class DriveRotationStateMachine extends StateMachine {
	AerialRobot robot;
	
	public DriveRotationStateMachine(AerialRobot robot) {
		this.robot = robot;
		
		rules.add(new SimpleStateMapRule(
				Arrays.asList(WaitForTimeWorker.class), 
				SuccessWorkerResult.class, 
				new MaintainHeadingWorker()));
		rules.add(new PredicateTransitionRule(
				Arrays.asList(WaitForTimeWorker.class, MaintainHeadingWorker.class), 
				(stateMachine) -> {
					return robot.leftJoystick.x != 0;
				}, ()-> {
					return new TankDriveWorker(robot);
				}));
		rules.add(new PredicateTransitionRule(
				Arrays.asList(TankDriveWorker.class), 
				(stateMachine) -> {
					return robot.leftJoystick.x == 0;
				}, ()-> {
					return new WaitForTimeWorker(500);
				}));
		
		this.currentState = new MaintainHeadingWorker();
	}
}
