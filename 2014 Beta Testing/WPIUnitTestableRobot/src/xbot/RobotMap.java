package xbot;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.injection.wpi_factories.WPIFactory;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
@Singleton
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	public SpeedController leftDrive;
	public SpeedController rightDrive;
	
	@Inject
	public RobotMap (WPIFactory factory) {
		leftDrive = factory.getSpeedController(1);
		rightDrive = factory.getSpeedController(2);
	}
	
}
