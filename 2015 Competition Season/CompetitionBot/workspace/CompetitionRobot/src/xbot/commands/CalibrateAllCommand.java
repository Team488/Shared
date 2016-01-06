package xbot.commands;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.*;

public class CalibrateAllCommand extends BaseCommand
{
    private RotationalSubsystem rotation;
    private TranslationalMeasurementSubsystem translation;
    private ElevatorSubsystem elevator;
    private ElevatorHeightChooserSubsystem heightChooser;
    
    CalibrateAllCommand calibrateAllCommand;
    
    private double degreesToCalibrateTo = 90;
    private double knownElevatorCalibrationHeight;
    
    private static Logger log = Logger.getLogger(CalibrateAllCommand.class);
    
    @Inject
    public CalibrateAllCommand(RotationalSubsystem rotation,
            TranslationalMeasurementSubsystem translation,
            ElevatorSubsystem elevator,
            ElevatorHeightChooserSubsystem heightChooser)
    {
        this.rotation = rotation;
        this.translation = translation;
        this.elevator = elevator;
        this.heightChooser = heightChooser;
        
        knownElevatorCalibrationHeight = elevator.getDefaultCalibrationHeight();
    }
    
    public void setDegreesToCalibrateTo(double degrees)
    {
        degreesToCalibrateTo = degrees;
    }
    
    public void setHeightForCalibration(double height)
    {
        knownElevatorCalibrationHeight = height;
    }
    
    @Override
    public void initialize()
    {
        log.info("Initializing");
        rotation.calibrate(degreesToCalibrateTo);
        translation.resetState();
        elevator.calibrateEncoder(knownElevatorCalibrationHeight);
        heightChooser.setTargetHeightToCurrentHeight();
        elevator.setElevatorPidEnabled(true);
        
        log.info("All systems calibrated.");
    }

    @Override
    public void execute()
    {
        
    }
    
	@Override
	public boolean isFinished() {
		return true;
	}
    

}
