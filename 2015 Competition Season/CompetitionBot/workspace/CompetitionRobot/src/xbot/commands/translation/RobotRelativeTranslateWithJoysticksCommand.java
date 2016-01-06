package xbot.commands.translation;

import org.apache.log4j.Logger;

import xbot.OI;
import xbot.common.math.XYPair;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.TranslationalSubsystem;

import com.google.inject.Inject;

public class RobotRelativeTranslateWithJoysticksCommand extends BaseCommand
{
    OI oi;
    TranslationalSubsystem translation;
    
    private static final Logger log = Logger
			.getLogger(RobotRelativeTranslateWithJoysticksCommand.class);
    
    @Inject
    public RobotRelativeTranslateWithJoysticksCommand(OI oi, TranslationalSubsystem translation)
    {
        this.oi = oi;
        this.translation = translation;
        this.requires(this.translation);
    }
    
    @Override
    public void initialize()
    {
    	log.info("Initializing");
        this.translation.resetState();
    }

    @Override
    public void execute()
    {
        translation.translatePowerRobotRelative(new XYPair(oi.leftJoystick.getVector().x, oi.leftJoystick.getVector().y));
    }
}
