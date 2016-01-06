package xbot.commands.elevator;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmPistonSubsystem;
import xbot.subsystems.ElevatorHeightChooserSubsystem;
import xbot.subsystems.ElevatorSubsystem;

public class ElevatorCalibrateEncoderCommand extends BaseCommand {
    ElevatorSubsystem elevator;
    private double currentPosition;
    
    private static final Logger log = Logger
            .getLogger(ElevatorCalibrateEncoderCommand.class);
    
    @Inject
    public ElevatorCalibrateEncoderCommand(ElevatorSubsystem elevator) {
        this.elevator = elevator;
        currentPosition = elevator.getDefaultCalibrationHeight();
    }
    
    public void setCalibrationPosition(double position)
    {
        this.currentPosition = position;
    }
    
    @Override
    public void initialize(){
        log.info("Initializing");
        // Force calibration at current height
        elevator.calibrateEncoder(currentPosition);
    }
    
    @Override
    public void execute() {
        //Intentionally left blank
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
