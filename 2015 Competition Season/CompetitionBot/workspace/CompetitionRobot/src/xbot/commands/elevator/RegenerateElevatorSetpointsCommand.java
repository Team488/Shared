package xbot.commands.elevator;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmPistonSubsystem;
import xbot.subsystems.ElevatorHeightChooserSubsystem;

public class RegenerateElevatorSetpointsCommand extends BaseCommand {
    ElevatorHeightChooserSubsystem elevatorHeightChooserSubsystem;
    
    private static final Logger log = Logger
            .getLogger(RegenerateElevatorSetpointsCommand.class);
    
    @Inject
    public RegenerateElevatorSetpointsCommand(ElevatorHeightChooserSubsystem elevatorHeightChooserSubsystem) {
        this.elevatorHeightChooserSubsystem = elevatorHeightChooserSubsystem;
        this.requires(elevatorHeightChooserSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        elevatorHeightChooserSubsystem.generateElevatorSetpoints();
    }
    
    @Override
    public void execute(){
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
