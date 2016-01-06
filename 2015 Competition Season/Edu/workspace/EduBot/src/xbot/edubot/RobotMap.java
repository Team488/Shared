package xbot.edubot;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.wpi_extensions.mechanism_wrappers.XDigitalInput;
import xbot.common.wpi_extensions.mechanism_wrappers.XSpeedController;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
@Singleton
public class RobotMap {
	
	private static Logger log = Logger.getLogger(RobotMap.class);
	
	public XSpeedController frontLeft;
	public XSpeedController rearLeft;
	public XSpeedController frontRight;
	public XSpeedController rearRight;
	public XSpeedController arm;
	
	public XDigitalInput upperLimitSwitch;
	public XDigitalInput lowerLimitSwitch;
	
	@Inject
	public RobotMap (WPIFactory factory) {
		log.info("Initializing RobotMap");
		// instantiate speed controllers and sensors here, save them as class members
		frontLeft = factory.getSpeedController(1);
		rearLeft = factory.getSpeedController(3);
		frontRight = factory.getSpeedController(2);
		rearRight = factory.getSpeedController(4);
		arm = factory.getSpeedController(5);
		
		upperLimitSwitch = factory.getDigitalInput(0);
		lowerLimitSwitch = factory.getDigitalInput(1);
	}
	
}
