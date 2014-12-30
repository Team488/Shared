
package xbot;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import xbot.common.injection.RobotModule;
import xbot.subsystems.DriveSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	// Pass in module that will tell juice how to build all of the real on-robot classes
        Injector injector = Guice.createInjector(new RobotModule());

        // get instances of all key systems, since they are singletons this will essentially initialize them since we
        // probably don't want to take the hit of lazy loading later? But the flip side is we could keep this class 
        // really lean without it
        
        // setup
        injector.getInstance(RobotMap.class);
        injector.getInstance(OI.class);
        
        // subsystems
        injector.getInstance(DriveSubsystem.class);
        
        // default command mapping
        injector.getInstance(DefaultCommandMap.class);
        
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
