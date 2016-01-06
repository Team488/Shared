package xbot.commands.elevator;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmPistonSubsystem;
import xbot.subsystems.ElevatorHeightChooserSubsystem;

public class ElevatorDownOneLevel extends BaseCommand {
	ElevatorHeightChooserSubsystem elevatorHeightChooserSubsystem;
	ArmPistonSubsystem armPistonSubsystem;
	
	private static final Logger log = Logger
			.getLogger(ElevatorDownOneLevel.class);
	
	@Inject
	public ElevatorDownOneLevel(ElevatorHeightChooserSubsystem elevatorHeightChooserSubsystem,
	        ArmPistonSubsystem armPistonSubsystem){
		this.elevatorHeightChooserSubsystem = elevatorHeightChooserSubsystem;
		this.armPistonSubsystem = armPistonSubsystem;
		this.requires(elevatorHeightChooserSubsystem);
	}
	
	@Override
	public void initialize(){
		log.info("Initializing");
		armPistonSubsystem.pistonStateClose(false);
		elevatorHeightChooserSubsystem.downOneLevel();
	}
	
	@Override
	public void execute(){
	}
	
    @Override
    public boolean isFinished() {
        return true;
    }
}
