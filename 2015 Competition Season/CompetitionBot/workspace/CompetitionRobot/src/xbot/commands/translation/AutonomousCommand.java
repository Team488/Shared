package xbot.commands.translation;

import org.apache.log4j.Logger;

import xbot.OI;
import xbot.commands.CalibrateAllCommand;
import xbot.commands.autonomous.AutonomousDriveCommandsUtil;
import xbot.commands.elevator.ElevatorSetTargetLevelCommand;
import xbot.common.math.XYPair;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.subsystems.AutonomousSubsystem;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonomousCommand extends CommandGroup
{
    AutonomousDriveCommandsUtil driveUtil;

    private static final Logger log = Logger
            .getLogger(AutonomousCommand.class);
        
    private Provider<ElevatorSetTargetLevelCommand> levelProvider;
    private DoubleProperty knownHeightForContainerPickup;
    private DoubleProperty autoCalibrationOffset;

    @Inject
    public AutonomousCommand(
            AutonomousSubsystem autoSub,
            PropertyManager propMan,
            AutonomousDriveCommandsUtil driveUtil,
            CalibrateAllCommand calibrateCommand,
            Provider<ElevatorSetTargetLevelCommand> levelProvider,
            OI oi)
    {
        this.levelProvider = levelProvider;
        this.knownHeightForContainerPickup =
                propMan.createProperty("Elevator starting height for auto container pickup", 17.4d);
        this.autoCalibrationOffset = propMan.createProperty("Autonomous offset from home calibration position", 2d);
        log.info("Creating autonomous command group");
        
        double mode = autoSub.getAutoProgram();
        
        this.driveUtil = driveUtil;
                
        log.info("Detected mode to be: " + mode);
        
        calibrateCommand.setHeightForCalibration(knownHeightForContainerPickup.get() + autoCalibrationOffset.get());
        if (mode == 1) {
            // right, no-op
            calibrateCommand.setDegreesToCalibrateTo(0);
            this.addSequential(calibrateCommand);
        }
        // Face right, pick up container, push tote and rotate to station
        else if (mode == 2) {
            calibrateCommand.setDegreesToCalibrateTo(0);
            this.addSequential(calibrateCommand);
            // pick up container
            this.addSequential(getSetElevatorLevelCommand(3));
            // drive right, towards the human player station
            this.addSequential(driveUtil.getPositionCommand(new XYPair(18, 0)));
            // rotate to station
            // Due to overshoot we can't effectively rotate
            //this.addSequential(driveUtil.getRotationCommand(315));
            log.info("Created auto to face right and go to player station after getting some game pieces");
        }
        // Face drivers, get container, drive away
        else if (mode == 3) {
            // face driver station
            calibrateCommand.setDegreesToCalibrateTo(270);
            calibrateCommand.setHeightForCalibration(knownHeightForContainerPickup.get());
            this.addSequential(calibrateCommand);
            // get container
            this.addSequential(getSetElevatorLevelCommand(3));
            // wait a moment
            this.addSequential(new WaitCommand(1));
            // drive away from driver station
            this.addSequential(driveUtil.setUpSimpleDriveToZoneCommands(false, -66), 5);
            // wait a moment
            this.addSequential(new WaitCommand(1));
            // drop the container
            //this.addSequential(getSetElevatorLevelCommand(0));
            log.info("Created auto to face towards DS and drive away from it");
        }
        else {
            calibrateCommand.setDegreesToCalibrateTo(180);
            this.addSequential(calibrateCommand);
            log.info("Autonomous is disabled, only calibrating in autonomous.");
        }
    }

    private void setupFaceAwayAndDriveForward(AutonomousDriveCommandsUtil driveUtil,
            CalibrateAllCommand calibrateCommand) {
        calibrateCommand.setDegreesToCalibrateTo(90);
        this.addSequential(calibrateCommand);
        this.addSequential(driveUtil.setUpSimpleDriveToZoneCommands(true), 5);
        log.info("Created auto to face away from DS and drive forward");
    }
    
    private Command getSetElevatorLevelCommand(int level)
    {
        ElevatorSetTargetLevelCommand cmd = levelProvider.get();
        cmd.setToteRelativeHeight(level);
        return cmd;
    }
}
