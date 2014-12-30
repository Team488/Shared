package org.usfirst.frc.team488.robot;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;

public abstract class RobotStateMachine<S, T> {
	private StateMachineConfig<S, T> statemachineConfig = null; 
	protected StateMachine<S, T> statemachine = null;
		
	public abstract void configure(StateMachineConfig<S, T> config);
	
	public RobotStateMachine()
	{
		statemachineConfig = new StateMachineConfig<S, T>();
		configure(statemachineConfig);
	}
	
	public void start(S initialState)
	{
		assert statemachine == null : "we expect that the statemachine is only set once";
		
		if (statemachine != null)
		{
			statemachine = null;
		}
		
		statemachine = new StateMachine<S, T>(initialState, statemachineConfig);
	}
	
	public void fire(T trigger)
	{
		statemachine.fire(trigger);
	}
	
	public void heartbeat()
	{
		statemachine.heartbeat();
	}
}
