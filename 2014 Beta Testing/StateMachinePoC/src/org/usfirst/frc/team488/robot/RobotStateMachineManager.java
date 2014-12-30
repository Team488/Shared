package org.usfirst.frc.team488.robot;

import java.util.ArrayList;
import java.util.List;

public class RobotStateMachineManager {
	List<RobotStateMachine<?, ?>> statemachineList;
	
	public RobotStateMachineManager()
	{
		statemachineList = new ArrayList<RobotStateMachine<?,?>>();
	}
	
	public RobotStateMachineManager addStateMachine(RobotStateMachine<?, ?> statemachine)
	{
		assert statemachine != null : "statemachine is null";
		
		statemachineList.add(statemachine);
		
		return this;
	}
	
	public void heartbeat()
	{
		for (RobotStateMachine<?, ?> statemachine: statemachineList)
		{
			statemachine.heartbeat();
		}
	}
}
