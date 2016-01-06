package xbot.commands.translation;

import org.apache.log4j.Logger;

import xbot.OI;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.TranslationalMeasurementSubsystem;
import xbot.subsystems.TranslationalSubsystem;

import com.google.inject.Inject;

public class FieldRelativeVelocityTranslateWithJoysticksCommand extends BaseCommand
{
    OI oi;
    TranslationalSubsystem translation;
    TranslationalMeasurementSubsystem translationalMeasurement;
    
    private DoubleProperty maxVelocityX;
    private DoubleProperty maxVelocityY;
    
    private static final Logger log = Logger
			.getLogger(FieldRelativeVelocityTranslateWithJoysticksCommand.class);
    
    @Inject
    public FieldRelativeVelocityTranslateWithJoysticksCommand(
            OI oi,
            TranslationalSubsystem translation, 
            TranslationalMeasurementSubsystem translationalMeasurement,
            PropertyManager propMan)
    {
        this.oi = oi;
        this.translation = translation;
        this.translationalMeasurement = translationalMeasurement;
        this.requires(this.translation);
        
        maxVelocityX = propMan.createProperty("Maximum X velocity", 120d);
        maxVelocityY = propMan.createProperty("Maximum Y velocity", 120d);
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
        XYPair joystickPower = new XYPair(oi.leftJoystick.getVector().x, oi.leftJoystick.getVector().y);
        joystickPower.scale(maxVelocityX.get(), maxVelocityY.get());
        translation.translateVelocityFieldRelative(joystickPower);
        
    }
}
