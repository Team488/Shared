package xbot.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import xbox.common.statemachines.SuccessWorkerResult;

public class WaitForTimeWorkerTest {
	
	@Test
	public void testTimeOut() throws InterruptedException {
		WaitForTimeWorker worker = new WaitForTimeWorker(500);
		assertEquals(null, worker.execute());
		Thread.sleep(501);
		assertEquals(SuccessWorkerResult.class, worker.execute().getClass());
	}
}
