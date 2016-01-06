package tests.vision;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.*;

import tests.xbot.BaseTestCase;
import xbot.common.properties.ITableProxy;
import xbot.subsystems.VisionSubsystem;

public class VisionSubsystemTest extends BaseTestCase {
	
	String xCogName = "COG_X";
	String yCogName = "COG_Y";
	String imageWidthName = "IMAGE_WIDTH";
	String imageHeightName = "IMAGE_HEIGHT";
	String blobCountName = "BLOB_COUNT";
	
	private static Logger log = Logger.getLogger(VisionSubsystemTest.class);
	
	@Test
	public void test()
	{
		log.info("Staring first test");
		ITableProxy randomStore = injector.getInstance(ITableProxy.class);
		VisionSubsystem visionSub = injector.getInstance(VisionSubsystem.class);
		// How to send "fake" data?
		
		randomStore.setDouble(xCogName, 15);
		visionSub.getClass();
		
		//AssertEquals compares the 1st and 2nd number and makes sure they only have a 
		// maximum absolute difference of the 3rd number. Otherwise it complains and the test fails.
		assertEquals(4, 4, 0.001);
	}
}
