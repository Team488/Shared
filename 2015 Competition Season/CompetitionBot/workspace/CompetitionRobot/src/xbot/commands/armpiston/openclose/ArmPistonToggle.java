package xbot.commands.armpiston.openclose;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmPistonSubsystem;

public class ArmPistonToggle extends BaseCommand {
	ArmPistonSubsystem armPistonSubsystem;
	
	private static final Logger log = Logger
			.getLogger(ArmPistonToggle.class);
	
	@Inject
	public ArmPistonToggle(ArmPistonSubsystem armPistonSubsystem){
		this.armPistonSubsystem = armPistonSubsystem;
		this.requires(armPistonSubsystem);
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");
		armPistonSubsystem.pistonStateClose(!armPistonSubsystem.intakePosition);
	}

	@Override
	public void execute() {
		
	}
	
	   @Override
	    public boolean isFinished() {
	        return true;
	    }
}

