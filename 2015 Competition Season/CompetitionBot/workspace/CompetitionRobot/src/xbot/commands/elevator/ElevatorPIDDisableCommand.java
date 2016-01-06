package xbot.commands.elevator;


import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.OI;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.common.wpi_extensions.mechanism_wrappers.XJoystick;
import xbot.subsystems.ArmPistonSubsystem;
import xbot.subsystems.ElevatorHeightChooserSubsystem;
import xbot.subsystems.ElevatorSubsystem;
import xbot.subsystems.TranslationalSubsystem;

public class ElevatorPIDDisableCommand extends BaseCommand{

    
    private static Logger log = Logger.getLogger(ElevatorPIDDisableCommand.class);
    
	ElevatorSubsystem elevatorSubsystem;
	OI oi;
	ElevatorHeightChooserSubsystem heightChooser;
	ArmPistonSubsystem arms;
	
	@Inject
	public ElevatorPIDDisableCommand(
	        ElevatorSubsystem elevatorSubsystem,
	        OI oi,
	        ElevatorHeightChooserSubsystem heightChooser,
	        ArmPistonSubsystem arms){
		this.elevatorSubsystem = elevatorSubsystem;
		this.oi = oi;
		this.heightChooser = heightChooser;
		this.arms = arms;
		this.requires(elevatorSubsystem);
		this.requires(heightChooser);
		this.requires(arms);
	}
	@Override
	public void initialize() {
	    log.info("Initializing");
		elevatorSubsystem.setElevatorPidEnabled(false);
		arms.pistonStateClose(false);
	}

	
	@Override
	public void execute() {
		// while in this mode, use raw joystick values for operation.
	    double potentialPower = oi.operatorJoystick.getVector().y;
	    boolean negate = (potentialPower < 0);
	    potentialPower = Math.pow(potentialPower, 2);
	    if (negate)
	    {
	        potentialPower *= -1;
	    }
	    
	    
	    if (Math.abs(potentialPower) > .05) {
	        elevatorSubsystem.setLocked(false);
	    }
	    else {
	        potentialPower = 0;
	        elevatorSubsystem.setLocked(true);
	    }
	    
	    elevatorSubsystem.setManualPower(potentialPower);
	}
	@Override
	public void end(){
		elevatorSubsystem.setElevatorPidEnabled(true);
		heightChooser.setTargetHeightToCurrentHeight();
	}

}
