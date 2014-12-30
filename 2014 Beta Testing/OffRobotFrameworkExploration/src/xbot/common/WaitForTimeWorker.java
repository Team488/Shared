package xbot.common;

import xbox.common.statemachines.BaseWorkerResult;
import xbox.common.statemachines.SuccessWorkerResult;
import xbox.common.statemachines.BaseWorker;

public class WaitForTimeWorker extends BaseWorker {
	long start_time;
	long end_time;
	
	public WaitForTimeWorker(int timeInMS) {
		start_time = System.currentTimeMillis();
		
		end_time = start_time + timeInMS;
	}
	
	@Override
	public BaseWorkerResult execute() {
		if(System.currentTimeMillis() > end_time) {
			return new SuccessWorkerResult();
		}
		return null;
	}
	

}
