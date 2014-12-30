package xbot.arial;

import xbox.common.statemachines.BaseWorkerResult;
import xbox.common.statemachines.BaseWorker;

public class TankDriveWorker extends BaseWorker {
	AerialRobot robot;
	public TankDriveWorker(AerialRobot robot) {
		this.robot = robot;
	}
	
	public BaseWorkerResult execute() {
		double left = robot.leftJoystick.y;
		double right = robot.rightJoystick.y;
		
		robot.frontLeft.value = left;
		robot.backLeft.value = left;
		robot.frontRight.value = right;
		robot.backRight.value = right;
		return BaseWorkerResult.NOT_DONE;
	}
}
