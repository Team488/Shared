package xbot.commands.elevator;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmPistonSubsystem;
import xbot.subsystems.ElevatorHeightChooserSubsystem;

public class ElevatorSetTargetLevelCommand extends BaseCommand {
	ElevatorHeightChooserSubsystem elevatorHeightChooserSubsystem;
	ArmPistonSubsystem arms;
	public int targetIndex = 0;
	
	private static final Logger log = Logger
			.getLogger(ElevatorSetTargetLevelCommand.class);
	
	@Inject
	public ElevatorSetTargetLevelCommand(
	        ElevatorHeightChooserSubsystem elevatorHeightChooserSubsystem,
	        ArmPistonSubsystem arms){
		this.elevatorHeightChooserSubsystem = elevatorHeightChooserSubsystem;
		this.arms = arms;
		this.requires(elevatorHeightChooserSubsystem);
		this.requires(arms);
	}
	
	public void setAbsoluteTargetPositionSetpoint(int targetIndex){
	    this.targetIndex = targetIndex;
	}
	
	public void setToteRelativeHeight(int target){
        this.targetIndex = elevatorHeightChooserSubsystem.getIndexForToteLevel(target);
    }
	
	@Override
	public void initialize(){
		log.info("Initializing");
		arms.pistonStateClose(false);
		elevatorHeightChooserSubsystem.setAbsoluteForkPositionIndex(targetIndex);
	}
	
	@Override
	public void execute(){
		
	}

    @Override
    public boolean isFinished()
    {
        return elevatorHeightChooserSubsystem.isInTargetRange();
    }
}