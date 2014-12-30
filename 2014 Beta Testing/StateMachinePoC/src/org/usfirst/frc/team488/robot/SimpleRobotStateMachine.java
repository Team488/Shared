package org.usfirst.frc.team488.robot;

import com.github.oxo42.stateless4j.StateMachineConfig;

import org.usfirst.frc.team488.robot.SimpleRobotStateMachine.SimpleRobotState;
import org.usfirst.frc.team488.robot.SimpleRobotStateMachine.SimpleRobotTrigger;

public class SimpleRobotStateMachine extends RobotStateMachine<SimpleRobotState, SimpleRobotTrigger> {
	private int waitingCounter = 0;
	
	public enum SimpleRobotState {
		MaintainHeading,  ManualDrive, Waiting
	}
	
	public enum SimpleRobotTrigger {
		JoystickMoved, JoystickTimeout, WaitingTimeOut
	}
	
	public SimpleRobotStateMachine()
	{
		super();
		
		start(SimpleRobotState.MaintainHeading);
	}

	@Override
	public void configure(StateMachineConfig<SimpleRobotState, SimpleRobotTrigger> config) {
		config.configure(SimpleRobotState.MaintainHeading)
			.onHeartbeat(this::executeMaintainHeading)
			.permit(SimpleRobotTrigger.JoystickMoved, SimpleRobotState.ManualDrive);
		
		config.configure(SimpleRobotState.ManualDrive)
			.onHeartbeat(this::executeManualDrive)
			.permit(SimpleRobotTrigger.JoystickTimeout, SimpleRobotState.Waiting);
		
		config.configure(SimpleRobotState.Waiting)
			.onHeartbeat(this::executeWaiting)
			.permit(SimpleRobotTrigger.WaitingTimeOut, SimpleRobotState.MaintainHeading)
			.permit(SimpleRobotTrigger.JoystickMoved, SimpleRobotState.ManualDrive);
	}
	
	private void executeMaintainHeading()
	{
		System.out.println("Maintaining Heading");
	}
	
	private void executeManualDrive()
	{
		waitingCounter = 0;
		
		System.out.println("Manual Drive");
		
		statemachine.fire(SimpleRobotTrigger.JoystickTimeout);
	}
	
	private void executeWaiting()
	{
		waitingCounter++;
		
		System.out.println("Waiting");
		
		if (waitingCounter == 10)
		{
			statemachine.fire(SimpleRobotTrigger.WaitingTimeOut);
		}
	}
}
