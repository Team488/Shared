package xbot.commands.elevator;

import org.apache.log4j.Logger;

import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ElevatorAcquireNextToteCommand extends CommandGroup {    
    private static final Logger log = Logger
            .getLogger(ElevatorAcquireNextToteCommand.class);
    
    Provider<ElevatorSetTargetLevelCommand> goToHeightCommandProvider;
    
    @Inject
    public ElevatorAcquireNextToteCommand(
            Provider<ElevatorSetTargetLevelCommand> goToHeightCommandProvider,
            PropertyManager propMan) {
        
        this.goToHeightCommandProvider = goToHeightCommandProvider;
        
        addToteLevelTarget(0);
        addToteLevelTarget(1);
    }

    private void addToteLevelTarget(int toteLevel) {
        ElevatorSetTargetLevelCommand command = goToHeightCommandProvider.get();
        command.setToteRelativeHeight(toteLevel);
        this.addSequential(command);
    }
}