package xbot.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.*;

public class SetStatusLEDsCommand extends BaseCommand
{
    private ArduinoCommunicationSubsystem arduinoSubsystem;
    private ArmWheelSubsystem collectorSubsystem;
    
    private static Logger log = Logger.getLogger(SetStatusLEDsCommand.class);
    
    @Inject
    public SetStatusLEDsCommand(
            ArduinoCommunicationSubsystem arduinoSubsystem,
            ArmWheelSubsystem collectorSubsystem)
    {
        this.arduinoSubsystem = arduinoSubsystem;
        this.collectorSubsystem = collectorSubsystem;
        this.requires(arduinoSubsystem);
    }
    
    @Override
    public void initialize()
    {
    	log.info("Initializing");
    }
    
    @Override
    public void execute()
    {
        boolean isInRange = collectorSubsystem.isToteWithinDetectionRange();
        boolean isInPlace = collectorSubsystem.isToteInPosition();
        
        // 1 is "not in range"
        // 2 is "in range but not ready for pickup"
        // 3 is "ready for pickup"
        
        byte state = 1;
        
        if(isInRange && isInPlace)
        {
            state = 3;
        }
        else if (isInRange)
        {
            state = 2;
        }
        
        arduinoSubsystem.setLEDData(state);
    }
}
