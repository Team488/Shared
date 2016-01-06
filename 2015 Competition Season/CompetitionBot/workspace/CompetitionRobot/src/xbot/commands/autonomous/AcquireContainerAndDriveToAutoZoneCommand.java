package xbot.commands.autonomous;

import xbot.commands.CalibrateAllCommand;
import xbot.commands.elevator.ElevatorSetTargetLevelCommand;
import xbot.commands.elevator.ElevatorUpOneLevel;
import xbot.commands.translation.TranslateToPositionCommand;
import xbot.common.properties.PropertyManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AcquireContainerAndDriveToAutoZoneCommand extends CommandGroup {

    @Inject
    public AcquireContainerAndDriveToAutoZoneCommand(
            AutonomousDriveCommandsUtil driveUtil,
            ElevatorSetTargetLevelCommand elevatorSetTargetLevel)
    {
        // pick up container
        // TODO: We need a leve for picking up a container
        elevatorSetTargetLevel.setAbsoluteTargetPositionSetpoint(4);
        this.addSequential(elevatorSetTargetLevel);
        
        // drive into the autozone
        this.addSequential(driveUtil.setUpSimpleDriveToZoneCommands(true));
    }
}
