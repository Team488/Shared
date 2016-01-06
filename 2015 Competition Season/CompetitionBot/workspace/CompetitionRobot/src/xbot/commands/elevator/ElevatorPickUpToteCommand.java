package xbot.commands.elevator;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmPistonSubsystem;
import xbot.subsystems.ElevatorHeightChooserSubsystem;
import xbot.subsystems.ElevatorSubsystem;

public class ElevatorPickUpToteCommand extends BaseCommand {
    
    ElevatorHeightChooserSubsystem elevatorHeightChooserSubsystem;
    ElevatorSubsystem elevatorSubsystem;
    
    private double goalHeight;
    private double rampingPower;
    
    private DoubleProperty rampingPowerRate;
    
    private static final Logger log = Logger
            .getLogger(ElevatorPickUpToteCommand.class);
    
    @Inject
    public ElevatorPickUpToteCommand(
            ElevatorHeightChooserSubsystem elevatorHeightChooserSubsystem,
            ElevatorSubsystem elevatorSubsystem,
            PropertyManager propMan){
        
        this.elevatorHeightChooserSubsystem = elevatorHeightChooserSubsystem;
        this.elevatorSubsystem = elevatorSubsystem;
        this.requires(elevatorHeightChooserSubsystem);
        this.requires(elevatorSubsystem);
        
        rampingPowerRate = propMan.createProperty("Ramping Power Rate", 0.01);
    }
    
    @Override
    public void initialize(){
        log.info("Initializing");
        
        // Get current height
        double currentHeight = elevatorSubsystem.updateAndGetHeight();
        goalHeight = currentHeight + 5;
        rampingPower = 0;
    }
    
    @Override
    public void execute(){
        rampingPower += rampingPowerRate.get();
        elevatorSubsystem.setManualPower(rampingPower);
    }
    
    @Override
    public boolean isFinished() {
        return elevatorSubsystem.updateAndGetHeight() > goalHeight; 
    }
    
    @Override
    public void end() {
        elevatorHeightChooserSubsystem.upOneLevel();
    }

}
