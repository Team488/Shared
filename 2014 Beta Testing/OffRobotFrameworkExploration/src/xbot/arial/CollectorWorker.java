package xbot.arial;

import xbox.common.statemachines.BaseWorker;
import xbox.common.statemachines.BaseWorkerResult;

public class CollectorWorker extends BaseWorker {
	AerialRobot robot;
	double power;
	
	public CollectorWorker(AerialRobot robot, double power) {
		this.robot = robot;
		this.power = power;
	}
	
	@Override
	public BaseWorkerResult execute() {
		robot.collector.value = power;
		return BaseWorkerResult.NOT_DONE;
	}

}
