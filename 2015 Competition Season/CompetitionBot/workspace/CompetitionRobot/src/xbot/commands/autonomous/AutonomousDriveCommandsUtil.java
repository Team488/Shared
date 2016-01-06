package xbot.commands.autonomous;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.wpi.first.wpilibj.command.Command;
import xbot.commands.elevator.ElevatorCalibrateEncoderCommand;
import xbot.commands.rotation.SetTargetHeadingCommand;
import xbot.commands.translation.TranslateToPositionCommand;
import xbot.common.math.XYPair;

public class AutonomousDriveCommandsUtil {

    final Provider<TranslateToPositionCommand> positionCommandProvider;
    final Provider<SetTargetHeadingCommand> rotationCommandProvider;
    final Provider<ElevatorCalibrateEncoderCommand> elevatorCalibrateCommandProvider;
    
    @Inject
    public AutonomousDriveCommandsUtil(
            Provider<TranslateToPositionCommand> positionCommandProvider,
            Provider<SetTargetHeadingCommand> rotationCommandProvider,
            final Provider<ElevatorCalibrateEncoderCommand> elevatorCalibrateCommandProvider) {
        this.positionCommandProvider = positionCommandProvider;
        this.rotationCommandProvider = rotationCommandProvider;
        this.elevatorCalibrateCommandProvider = elevatorCalibrateCommandProvider;
    }

    public Command setUpCalibrateToKnownPointCommand(double knownCurrentPosition)
    {
        ElevatorCalibrateEncoderCommand calibrateCommand = elevatorCalibrateCommandProvider.get();
        calibrateCommand.setCalibrationPosition(knownCurrentPosition);
        return calibrateCommand;
    }
    
    public Command setUpSimpleDriveToZoneCommands(boolean forward) {
        return setUpSimpleDriveToZoneCommands(forward, -4d * 12d);
    }
    
    public Command setUpSimpleDriveToZoneCommands(boolean forward, double distance) {
        double autoZoneY = distance;

        if (!forward) {
            autoZoneY *= -1;
        }

        return getPositionCommand(new XYPair(0, autoZoneY));
    }

    public Command getPositionCommand(XYPair position) {
        TranslateToPositionCommand command = positionCommandProvider.get();
        command.setTarget(position);
        return command;
    }
    
    public Command getRotationCommand(double orientation)
    {
        SetTargetHeadingCommand cmd = rotationCommandProvider.get();
        cmd.setTargetHeading(orientation);
        return cmd;
        
    }
}
