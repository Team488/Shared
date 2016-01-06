package xbot.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.*;

public class ReportDebugInfoCommand extends BaseCommand
{
    private DebugInfoSubsystem debugSubsystem;
    
    private static Logger log = Logger.getLogger(ReportDebugInfoCommand.class);
    
    @Inject
    public ReportDebugInfoCommand(DebugInfoSubsystem subsystem)
    {
        debugSubsystem = subsystem;
        this.requires(debugSubsystem);
    }
    
    @Override
    public void initialize()
    {
    	log.info("Initializing");
    }
    
    @Override
    public void execute()
    {
        debugSubsystem.updateGyro();
        debugSubsystem.updateElevatorSensors();
        debugSubsystem.updateTranslationalMeasurements();
        debugSubsystem.updateDistanceSensors();
        debugSubsystem.updatePowerPanel();
        debugSubsystem.updateAutoSwitch();
    }
}
