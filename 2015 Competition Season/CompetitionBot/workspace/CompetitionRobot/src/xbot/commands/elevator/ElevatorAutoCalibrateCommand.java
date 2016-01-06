package xbot.commands.elevator;

import org.apache.log4j.Logger;

import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;

import com.google.inject.Inject;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ElevatorAutoCalibrateCommand extends CommandGroup {    
    private static final Logger log = Logger
            .getLogger(ElevatorAutoCalibrateCommand.class);
    
    private DoubleProperty calibrateHeightPosition;
    
    @Inject
    public ElevatorAutoCalibrateCommand(
            ElevatorMoveUntilAtHomeCommand moveDownCommand,
            PropertyManager propMan) {
                
        // Timeout at 2s
        this.addSequential(moveDownCommand, 5d);
    }
}