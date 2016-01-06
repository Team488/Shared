package xbot.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.OI;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.*;
import xbot.common.math.*;

/**
 * NOTE: Please use individual translate and rotational control commands instead of this one.
 * @author Wasabi
 */
public class DriveWithJoysticksCommand extends BaseCommand {
    OI oi;
    RotationalSubsystem rotation;
    TranslationalSubsystem translation;
    
    private static Logger log = Logger.getLogger(DriveWithJoysticksCommand.class);
    
    @Inject
    public DriveWithJoysticksCommand(OI oi,
            RotationalSubsystem rotation,
            TranslationalSubsystem translation)
    {
        
        this.oi = oi;
        
        this.translation = translation;
        this.requires(translation);
        
        this.rotation = rotation;
        this.requires(rotation);
    }
    
    @Override
    public void initialize()
    {
    	log.info("Initializing");
    }

    @Override
    public void execute()
    {
        rotation.humanAssistRotation(oi.rightJoystick.getVector().x);
        translation.translatePowerRobotRelative(new XYPair(oi.leftJoystick.getVector().x, oi.leftJoystick.getVector().y));
    }

    
}
