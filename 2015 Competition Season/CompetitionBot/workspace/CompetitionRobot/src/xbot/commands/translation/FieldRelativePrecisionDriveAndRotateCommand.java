package xbot.commands.translation;

import org.apache.log4j.Logger;

import xbot.OI;
import xbot.common.math.XYPair;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.RotationalSubsystem;
import xbot.subsystems.TranslationalSubsystem;

import com.google.inject.Inject;

public class FieldRelativePrecisionDriveAndRotateCommand extends BaseCommand
{
    OI oi;
    TranslationalSubsystem translation;
    RotationalSubsystem rotation;
    
    private static final double precisionFactor = 0.5d;
    
    private static final Logger log = Logger
			.getLogger(FieldRelativePrecisionDriveAndRotateCommand.class);
    
    @Inject
    public FieldRelativePrecisionDriveAndRotateCommand(OI oi,
            TranslationalSubsystem translation,
            RotationalSubsystem rotation)
    {
        this.oi = oi;
        this.translation = translation;
        this.rotation = rotation;
        this.requires(this.translation);
        this.requires(this.rotation);
    }
    
    @Override
    public void initialize()
    {
    	log.info("Initializing");
    }

    @Override
    public void execute()
    {
        translation.translatePowerFieldRelative(oi.leftJoystick.getVector().scale(precisionFactor));
        rotation.humanAssistRotation(oi.rightJoystick.getVector().x * precisionFactor);
    }
    
    @Override
    public void interrupted()
    {
        super.interrupted();
    }
}
