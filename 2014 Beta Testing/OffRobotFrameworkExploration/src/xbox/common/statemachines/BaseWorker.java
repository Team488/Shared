package xbox.common.statemachines;

import org.apache.log4j.Logger;

public abstract class BaseWorker {
	public static Logger logger = Logger.getLogger(BaseWorker.class);
	
	public abstract BaseWorkerResult execute();
}
