package xbot.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import xbox.common.statemachines.BaseWorker;
import static org.mockito.Mockito.*;

public class BaseRobotTest {
	BaseRobot robot;
	@Before
	public void setUp() throws Exception {
		robot = new BaseRobot();
	}

	@Test
	public void testWorkersExecute() {
		BaseWorker mockWorker = mock(BaseWorker.class);
		
		robot.workers.add(mockWorker);
		
		robot.tick();
		
		verify(mockWorker).execute();
	}

}
