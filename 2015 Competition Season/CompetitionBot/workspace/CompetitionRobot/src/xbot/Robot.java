
package xbot;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import xbot.commands.translation.AutonomousCommand;
import xbot.common.injection.RobotModule;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.XScheduler;
import xbot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
    
    private static Logger log = Logger.getLogger(Robot.class);
    
    private PropertyManager propertyManager;
    private DebugInfoSubsystem debugSubsystem;
    private ElevatorHeightChooserSubsystem heightChooser;
    private XScheduler xScheduler;
    
    private Provider<AutonomousCommand> autoProvider;
    
    Command autonomousCommand;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     * 
     * Info on the warning suppression
     * http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.jdt.doc.user%2Ftasks%2Ftask-suppress_warnings.htm
     */
    public void robotInit() {
    	
    	// Get our logging config
    	try
    	{
    		DOMConfigurator.configure("log4j.xml");
    	}
    	catch (Exception e)
    	{
    		// Had a problem loading the config. Robot should continue!
    		System.out.println("Couldn't configure logging - file probably missing or malformed");
    	}
        
        Injector inj = Guice.createInjector(new CompetitionRobotModule());
        
        // Get the property manager and get all properties from the robot disk
        propertyManager = inj.getInstance(PropertyManager.class);
        //propertyManager.loadPropertiesFromStorage();

    	// For now, we need this heading subsystem to pump sensor values in disabled
    	debugSubsystem = inj.getInstance(DebugInfoSubsystem.class);
    	heightChooser = inj.getInstance(ElevatorHeightChooserSubsystem.class);
    	xScheduler = inj.getInstance(XScheduler.class);    	

        RobotContext.initializeRobotSystems(inj);
    	
    	SmartDashboard.putData(Scheduler.getInstance());
    	
    	autoProvider = inj.getProvider(AutonomousCommand.class);
    }
    
    @Override
    public void disabledInit() {
        log.info("Disabled init");
    	propertyManager.saveOutAllProperties();
    }
	
	public void disabledPeriodic() {
	    xScheduler.run();
		debugSubsystem.updateGyro();
		debugSubsystem.updateTranslationalMeasurements();
		debugSubsystem.updateElevatorSensors();
		debugSubsystem.updateDistanceSensors();
		debugSubsystem.updateAutoSwitch();
	}

    public void autonomousInit() {
        log.info("Autonomous init");
        heightChooser.setTargetHeightToCurrentHeight();
        
        autonomousCommand = autoProvider.get();
        
        if (autonomousCommand != null) {
            autonomousCommand.start();
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        xScheduler.run();
    }

    public void teleopInit() {
        log.info("Teleop init");
        heightChooser.setTargetHeightToCurrentHeight();
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        xScheduler.run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
