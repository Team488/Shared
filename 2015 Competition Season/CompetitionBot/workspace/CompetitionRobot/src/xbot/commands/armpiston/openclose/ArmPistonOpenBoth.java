package xbot.commands.armpiston.openclose;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmPistonSubsystem;

public class ArmPistonOpenBoth extends BaseCommand {
    ArmPistonSubsystem armPistonSubsystem;
    
    private static final Logger log = Logger
            .getLogger(ArmPistonOpenBoth.class);
    
    @Inject
    public ArmPistonOpenBoth(ArmPistonSubsystem armPistonSubsystem){
        this.armPistonSubsystem = armPistonSubsystem;
        this.requires(armPistonSubsystem);
    }
    
    @Override
    public void initialize() {
        log.info("Initializing");
        armPistonSubsystem.pistonStateClose(false);
    }

    @Override
    public void execute() {
        
    }
}
