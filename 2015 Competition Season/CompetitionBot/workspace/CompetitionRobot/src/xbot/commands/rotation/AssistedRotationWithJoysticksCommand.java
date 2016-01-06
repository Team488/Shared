package xbot.commands.rotation;

import org.apache.log4j.Logger;

import xbot.OI;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.RotationalSubsystem;

import com.google.inject.Inject;

public class AssistedRotationWithJoysticksCommand extends BaseCommand
{
    OI oi;
    RotationalSubsystem rotation;
    
    private static final Logger log = Logger
			.getLogger(AssistedRotationWithJoysticksCommand.class);
    
    @Inject
    public AssistedRotationWithJoysticksCommand(OI oi, RotationalSubsystem rotation)
    {
        this.oi = oi;
        this.rotation = rotation;
        this.requires(rotation);
    }
    
    @Override
    public void initialize()
    {
    	log.info("Initializing");
        rotation.resetState();
    }

    @Override
    public void execute()
    {
        rotation.humanAssistRotation(oi.rightJoystick.getVector().x);
    }

    @Override
    public void interrupted()
    {
        rotation.resetState();
        super.interrupted();
    }
}
