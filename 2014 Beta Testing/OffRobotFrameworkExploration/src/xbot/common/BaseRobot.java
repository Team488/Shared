package xbot.common;

import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import xbox.common.statemachines.BaseWorker;

public class BaseRobot {
	public ArrayList<Object> mechanisms = new ArrayList<Object>();
	public ArrayList<BaseWorker> workers = new ArrayList<BaseWorker>();
	
	public static Logger logger = Logger.getLogger(BaseRobot.class);
	
	public BaseRobot() {
		logger.warn("Starting initialization");

	}
	
	public void tick() {
		for(BaseWorker worker : workers) {
			worker.execute();
		}
	}
}
