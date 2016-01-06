package xbot.commands.elevator;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ElevatorHeightChooserSubsystem;
import xbot.subsystems.ElevatorSubsystem;

public class ElevatorManualOverrideUp extends BaseCommand
{
    ElevatorSubsystem elevatorSubsystem;
    ElevatorHeightChooserSubsystem elevatorHeightSubsystem;
    
    private static final Logger log = Logger
            .getLogger(ElevatorManualOverrideUp.class);
    
    @Inject
    public ElevatorManualOverrideUp(ElevatorSubsystem elevatorSubsystem,
    		ElevatorHeightChooserSubsystem elevatorHeightSubsystem)
    {
        log.info("Creating ElevatorManualOverrideUp");
    	this.elevatorHeightSubsystem = elevatorHeightSubsystem;
        this.elevatorSubsystem = elevatorSubsystem;
        this.elevatorHeightSubsystem = elevatorHeightSubsystem;
        this.requires(elevatorSubsystem);
    }
    
    @Override
    public void initialize()
    {
        log.info("Initializing");
        elevatorSubsystem.setLocked(false);
    }
    
    @Override
    public void execute() {
        elevatorSubsystem.goUpManually();
    }
    
    @Override
    public void end()
    {
    	elevatorHeightSubsystem.setTargetHeightToCurrentHeight();
    	elevatorSubsystem.setLocked(true);
    }
}
