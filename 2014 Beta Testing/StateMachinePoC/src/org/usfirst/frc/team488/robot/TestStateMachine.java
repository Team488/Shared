package org.usfirst.frc.team488.robot;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.usfirst.frc.team488.robot.SimpleRobotStateMachine.SimpleRobotTrigger;

public class TestStateMachine {
	RobotStateMachineManager statemachineManager;
	SimpleRobotStateMachine statemachine;
	
	@Before
	public void setUp() throws Exception {
		statemachineManager = new RobotStateMachineManager();
		
		statemachine = new SimpleRobotStateMachine();
		statemachineManager.addStateMachine(statemachine);
	}

	@Test
	public void test() {
		for (int i=0; i < 40; i++)
		{
			statemachineManager.heartbeat();
			
			if (i == 10)
			{
				statemachine.fire(SimpleRobotTrigger.JoystickMoved);
			}
			
			if (i > 15 && i < 20)
			{
				statemachine.fire(SimpleRobotTrigger.JoystickMoved);
			}
		}
	}
}
