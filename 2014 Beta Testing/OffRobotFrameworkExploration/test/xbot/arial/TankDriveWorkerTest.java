package xbot.arial;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TankDriveWorkerTest {
	AerialRobot robot;
	TankDriveWorker worker;

	@Before
	public void setup() {
		robot = new AerialRobot();
		worker = new TankDriveWorker(robot);
	}

	@Test
	public void testZeros() {
		worker.execute();
		assertDrive(0, 0);
	}

	@Test
	public void testFullForward() {
		robot.leftJoystick.y = 1.0;
		robot.rightJoystick.y = 1.0;
		worker.execute();
		assertDrive(1, 1);
	}

	public void assertDrive(double left, double right) {
		assertEquals(left, robot.frontLeft.value, 0.001);
		assertEquals(left, robot.backLeft.value, 0.001);
		assertEquals(right, robot.frontRight.value, 0.001);
		assertEquals(right, robot.frontRight.value, 0.001);
	}
}
