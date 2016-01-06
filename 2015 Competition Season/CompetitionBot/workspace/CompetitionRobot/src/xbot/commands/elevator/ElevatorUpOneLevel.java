package xbot.commands.elevator;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmPistonSubsystem;
import xbot.subsystems.ElevatorHeightChooserSubsystem;

public class ElevatorUpOneLevel extends BaseCommand {
	ElevatorHeightChooserSubsystem elevatorHeightChooserSubsystem;
	ArmPistonSubsystem armPistonSubsystem;
	
	private static final Logger log = Logger
			.getLogger(ElevatorUpOneLevel.class);
	
	@Inject
	public ElevatorUpOneLevel(ElevatorHeightChooserSubsystem elevatorHeightChooserSubsystem, 
	        ArmPistonSubsystem armPistonSubsystem){
	    this.armPistonSubsystem = armPistonSubsystem;
		this.elevatorHeightChooserSubsystem = elevatorHeightChooserSubsystem;
		this.armPistonSubsystem = armPistonSubsystem;
		this.requires(elevatorHeightChooserSubsystem);
	}
	
	public void setToteRaiseAmount(double tote){
	}
	
	@Override
	public void initialize(){
		log.info("Initializing"); 
		armPistonSubsystem.pistonStateClose(false);
		elevatorHeightChooserSubsystem.upOneLevel();
	}
	
	@Override
	public void execute(){
	   
	}
	
	@Override
    public boolean isFinished() {
        return true;
    }
}