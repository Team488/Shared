package xbot.subsystems;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.RobotMap;
import xbot.common.properties.*;
import xbot.common.wpi_extensions.BaseSubsystem;

@Singleton
public class VisionSubsystem extends BaseSubsystem {
	
	private static Logger log = Logger.getLogger(VisionSubsystem.class);
	
	public DoubleProperty xCog;
	public DoubleProperty yCog;
	public DoubleProperty blobCount;
	
	public DoubleProperty imageWidth;
	public DoubleProperty imageHeight;
	
	@Inject
	public VisionSubsystem(RobotMap map, PropertyManager manager) {
		
		log.info("Creating vision subsystem");
		
		xCog = new DoubleProperty("COG_X", 0, manager);
		yCog = new DoubleProperty("COG_Y", 0, manager);
		imageWidth = new DoubleProperty("IMAGE_WIDTH", 0, manager);
		imageHeight = new DoubleProperty("IMAGE_HEIGHT", 0, manager);
		blobCount = new DoubleProperty("BLOB_COUNT", 0, manager);
	}
	
	
}
