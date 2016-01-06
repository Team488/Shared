package tests.elevator;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class ElevatorStallTests extends BaseElevatorTest {
	
	private static final Logger log = Logger.getLogger(ElevatorStallTests.class);
	
	@Before
	public void setup() {
	    randomStore.setBoolean("Elevator stall detection enabled", true);
	}
	
	@Test
	public void stall()
	{
		log.info("=========BEGINNING STALL TEST=========");
		// set huge power
		// set no motion
		elevator.setAutomaticPower(1);
		elevator.updateStallTracking();
		assertTrue(elevator.isStalled());
	}
	
	@Test
	public void stallThenStopStalling()
	{
		log.info("=========BEGINNING STOPTHENSTOPSTALLING TEST=========");
		// set huge power
		// set no motion
		elevator.setAutomaticPower(1);
		elevator.updateStallTracking();
		assertTrue(elevator.isStalled());
		
		// Set no power, should still report stalled
		elevator.setAutomaticPower(0);
		elevator.updateStallTracking();
		assertTrue(elevator.isStalled());
		
		// Wait a while
		for (int i = 0; i < 5; i++)
		{
			elevator.updateStallTracking();
		}
		assertTrue(elevator.isStalled());
		
		// Wait long enough to forget about stall
		for (int i = 0; i < 5; i++)
		{
			elevator.updateStallTracking();
		}
		assertTrue(!elevator.isStalled());
	}

}
