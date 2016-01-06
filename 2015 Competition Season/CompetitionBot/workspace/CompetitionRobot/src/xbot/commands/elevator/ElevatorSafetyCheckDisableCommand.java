//ElevatorSafetyCheckDisableCommand
package xbot.commands.elevator;


import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ElevatorSubsystem;
import xbot.subsystems.TranslationalSubsystem;

public class ElevatorSafetyCheckDisableCommand extends BaseCommand{
    
	ElevatorSubsystem elevatorSubsystem;
	
	private static final Logger log = Logger
            .getLogger(ElevatorSafetyCheckDisableCommand.class);
	
	@Inject
	public ElevatorSafetyCheckDisableCommand(ElevatorSubsystem elevatorSubsystem){
		this.elevatorSubsystem = elevatorSubsystem;
	}
	@Override
	public void initialize() {
		log.info("Turning off elevator safeties");
		elevatorSubsystem.setSafetyPositionChecksEnabled(false);
	}
	
	@Override
	public void execute() {		
	}
	
	@Override
	public void end(){
	    log.info("Turning on elevator safeties");
		elevatorSubsystem.setSafetyPositionChecksEnabled(true);
	}

}
