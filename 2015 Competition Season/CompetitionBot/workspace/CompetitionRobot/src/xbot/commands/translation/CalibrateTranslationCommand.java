package xbot.commands.translation;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.*;

public class CalibrateTranslationCommand extends BaseCommand
{
    private TranslationalMeasurementSubsystem translation;

    private static Logger log = Logger
            .getLogger(CalibrateTranslationCommand.class);

    @Inject
    public CalibrateTranslationCommand(RotationalSubsystem rotation,
            TranslationalMeasurementSubsystem translation,
            ElevatorSubsystem elevator)
    {
        this.translation = translation;

        this.setRunWhenDisabled(true);

    }

    @Override
    public void initialize()
    {
        translation.resetState();

        log.info("Translation calibrated");
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
