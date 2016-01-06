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

public class TranslateToPositionCommand extends BaseCommand
{
    OI oi;
    TranslationalSubsystem translation;
    TranslationalMeasurementSubsystem translationalMeasurement;

    private boolean isFinished = false;

    private double targetXPosition;
    private double targetYPosition;
    private DoubleProperty posTolerance;
    private DoubleProperty velTolerance;

    private static final Logger log = Logger
            .getLogger(TranslateToPositionCommand.class);

    @Inject
    public TranslateToPositionCommand(OI oi,
            TranslationalSubsystem translation,
            TranslationalMeasurementSubsystem translationalMeasurement,
            PropertyManager propMan)
    {
        this.oi = oi;
        this.translation = translation;
        this.translationalMeasurement = translationalMeasurement;
        this.requires(this.translation);

        posTolerance = propMan.createProperty("Target position tolerance", 4d);
        velTolerance = propMan.createProperty("Target velocity tolerance", 1d);
    }
    
    public void setTargetX(double xTarget)
    {
        this.targetXPosition = xTarget;
    }

    public void setTargetY(double yTarget)
    {
        this.targetYPosition = yTarget;
    }

    public void setTarget(XYPair targetPosition)
    {
        setTargetX(targetPosition.x);
        setTargetY(targetPosition.y);
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
        if (!isFinished)
        {
            translation.goToFieldPositionAbsolute(
                    new XYPair(targetXPosition, targetYPosition));
        }
    }

    @Override
    public boolean isFinished()
    {
        log.info("Finished navigating to destination.");
        log.info("Postion: " + translationalMeasurement.getPosition());
        log.info("Velocity: " + translationalMeasurement.getVelocityFieldRelative());
        return isFinished = translation.isAtPositionTarget(
                posTolerance.get(), velTolerance.get());
    }
    
    @Override
    public void end()
    {
        translation.zeroPower();
    }
}
