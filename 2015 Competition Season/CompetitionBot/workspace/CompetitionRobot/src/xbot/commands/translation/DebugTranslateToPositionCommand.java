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

public class DebugTranslateToPositionCommand extends BaseCommand
{
    OI oi;
    TranslationalSubsystem translation;
    TranslationalMeasurementSubsystem translationalMeasurement;

    private DoubleProperty targetXPosition;
    private DoubleProperty targetYPosition;

    private static final Logger log = Logger
            .getLogger(DebugTranslateToPositionCommand.class);

    @Inject
    public DebugTranslateToPositionCommand(OI oi,
            TranslationalSubsystem translation,
            TranslationalMeasurementSubsystem translationalMeasurement,
            PropertyManager propMan)
    {
        this.oi = oi;
        this.translation = translation;
        this.translationalMeasurement = translationalMeasurement;
        this.requires(this.translation);

        targetXPosition = propMan.createProperty("Test target X field position", 0d);
        targetYPosition = propMan.createProperty("Test target Y field position", 0d);
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
        translation.goToFieldPositionAbsolute(new XYPair(targetXPosition.get(),
                targetYPosition.get()));
    }
}
