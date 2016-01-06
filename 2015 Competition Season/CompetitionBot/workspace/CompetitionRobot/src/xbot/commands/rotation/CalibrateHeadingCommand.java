package xbot.commands.rotation;

import org.apache.log4j.Logger;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.RotationalSubsystem;

import com.google.inject.Inject;

public class CalibrateHeadingCommand extends BaseCommand
{
    RotationalSubsystem rotation;
    
    private static final Logger log = Logger
			.getLogger(CalibrateHeadingCommand.class);
    
    @Inject
    public CalibrateHeadingCommand(RotationalSubsystem rotation)
    {
        this.rotation = rotation;
    }

    @Override
    public void initialize()
    {
    	log.info("Initializing");
        this.rotation.resetState();
        this.rotation.calibrate();
    }

    @Override
    public void execute()
    {
        
    }
    
    @Override
    public boolean isFinished()
    {
        return true;
    }

}
