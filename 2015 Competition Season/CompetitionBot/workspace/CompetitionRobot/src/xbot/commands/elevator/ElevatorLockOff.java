package xbot.commands.elevator;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ElevatorSubsystem;

public class ElevatorLockOff extends BaseCommand {
	ElevatorSubsystem elevatorSubsystem;
	
	private static final Logger log = Logger
			.getLogger(ElevatorLockOff.class);
	
	@Inject
	public void elevatorLock(ElevatorSubsystem elevatorSubsystem){
		this.elevatorSubsystem = elevatorSubsystem;
		this.requires(elevatorSubsystem);
	}
	
	@Override
	public void initialize(){
		log.info("Initializing");
		elevatorSubsystem.setLocked(false);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}