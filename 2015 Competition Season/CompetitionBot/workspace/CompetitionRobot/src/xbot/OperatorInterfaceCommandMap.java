package xbot;

import xbot.commands.armwheel.spin.SpinArmWheelBothIn;
import xbot.commands.armwheel.spin.SpinArmWheelBothOut;
import xbot.commands.armwheel.spin.SpinArmWheelOrientLeft;
import xbot.commands.armwheel.spin.SpinArmWheelOrientRight;
import xbot.commands.elevator.ElevatorAcquireNextToteCommand;
import xbot.commands.elevator.ElevatorAutoCalibrateCommand;
import xbot.commands.elevator.ElevatorCalibrateEncoderCommand;
import xbot.commands.elevator.ElevatorDownOneLevel;
import xbot.commands.elevator.ElevatorLockOff;
import xbot.commands.elevator.ElevatorLockOn;
import xbot.commands.elevator.ElevatorManualOverrideDown;
import xbot.commands.elevator.ElevatorManualOverrideUp;
import xbot.commands.elevator.ElevatorPIDDisableCommand;
import xbot.commands.elevator.ElevatorSafetyCheckDisableCommand;
import xbot.commands.elevator.ElevatorSetTargetLevelCommand;
import xbot.commands.elevator.ElevatorUpOneLevel;
import xbot.commands.elevator.RegenerateElevatorSetpointsCommand;
import xbot.commands.rotation.CalibrateHeadingCommand;
import xbot.commands.rotation.ManualRotationCommand;
import xbot.commands.rotation.SetTargetHeadingCommand.Target;
import xbot.commands.translation.AutonomousCommand;
import xbot.commands.translation.CalibrateTranslationCommand;
import xbot.commands.translation.FieldRelativePrecisionDriveAndRotateCommand;
import xbot.commands.translation.RobotRelativeTranslateWithJoysticksCommand;
import xbot.common.controls.AnalogHIDButton.AnalogHIDDescription;
import xbot.common.wpi_extensions.mechanism_wrappers.AdvancedJoystickButton;

import com.google.inject.Inject;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OperatorInterfaceCommandMap {
    
    public static final int safetyCheckSwitchChannel = 4;
    public static final int elevatorPIDSwitchChannel = 1;
		
    @Inject
    public void setupOperatorJoystick(
            OI oi, 
            SpinArmWheelBothIn spinArmWheelBothIn,
            SpinArmWheelBothOut spinArmWheelBothOut, 
            ElevatorAutoCalibrateCommand elevatorCalibrate,
            ElevatorAcquireNextToteCommand acquireNextToteCommand,
            ElevatorManualOverrideDown elevatorOverrideDown,
            ElevatorManualOverrideUp elevatorOverrideUp,
            ElevatorSetTargetLevelCommand setElevatorToFirstCarriage,
            ElevatorDownOneLevel downOneLevel,
            ElevatorUpOneLevel upOneLevel,
            ElevatorPIDDisableCommand elevatorPidDisable
            ) {
        oi.operatorJoystickButtons.getifAvailable(1).whenPressed(elevatorCalibrate);
        oi.operatorJoystickButtons.getifAvailable(2).whenPressed(acquireNextToteCommand);
    
        oi.operatorJoystickButtons.getifAvailable(3).whileHeld(spinArmWheelBothIn);
        oi.operatorJoystickButtons.getifAvailable(4).whileHeld(spinArmWheelBothOut);
        
        // Nothing on 3-6
        oi.operatorJoystickButtons.getifAvailable(7).whenPressed(downOneLevel);
        oi.operatorJoystickButtons.getifAvailable(8).whenPressed(upOneLevel);
        // Nothing on 9
        setElevatorToFirstCarriage.setToteRelativeHeight(0);
        oi.operatorJoystickButtons.getifAvailable(10).whenPressed(setElevatorToFirstCarriage);
        oi.operatorJoystickButtons.getifAvailable(11).whileHeld(elevatorOverrideDown);
        oi.operatorJoystickButtons.getifAvailable(12).whileHeld(elevatorOverrideUp);

        // OK UP TO HERE
        
        AnalogHIDDescription alwaysOnManualDescBackward = new AnalogHIDDescription(1, -1d, -0.2d);
        AnalogHIDDescription alwaysOnManualDescForward = new AnalogHIDDescription(1, 0.2d, 1d);
        oi.operatorJoystickButtons.addAnalogButton(alwaysOnManualDescForward);
        oi.operatorJoystickButtons.addAnalogButton(alwaysOnManualDescBackward);
        oi.operatorJoystickButtons.getAnalogIfAvailable(alwaysOnManualDescForward).whenPressed(elevatorPidDisable);
        oi.operatorJoystickButtons.getAnalogIfAvailable(alwaysOnManualDescBackward).whenPressed(elevatorPidDisable);
        
    }
    
    @Inject
    public void setupOperatorPanel(
            OI oi, 
            SpinArmWheelOrientLeft spinArmWheelOrientLeft,
            SpinArmWheelOrientRight spinArmWheelOrientRight,
            ElevatorSafetyCheckDisableCommand elevatorDisableSafetyChecksCommand,
            ManualRotationCommand manualRotationCommand,
            ElevatorManualOverrideDown elevatorOverrideDown,
            ElevatorManualOverrideUp elevatorOverrideUp,
            ElevatorSetTargetLevelCommand setElevatorToFirstCarriage,
            ElevatorDownOneLevel downOneLevel,
            ElevatorUpOneLevel upOneLevel,
            ElevatorPIDDisableCommand elevatorPidDisable
            ) {
        // Hardware OI analog defs
        AnalogHIDDescription auton1Desc = new AnalogHIDDescription(2, -1d, -0.5d);
        AnalogHIDDescription auton2Desc = new AnalogHIDDescription(2, -0.49d, 0.49d);
        AnalogHIDDescription auton3Desc = new AnalogHIDDescription(2, 0.5d, -1d);
        // OK UP TO HERE
        AnalogHIDDescription intakeSpinLeftDesc = new AnalogHIDDescription(1, 0.1d, 1d);
        AnalogHIDDescription intakeSpinRightDesc = new AnalogHIDDescription(1, -1d, -0.1d);
        
        oi.operatorPanelButtons.getifAvailable(elevatorPIDSwitchChannel).whileHeld(elevatorPidDisable);
        
        AdvancedJoystickButton rotationalPidButton = oi.operatorPanelButtons.getifAvailable(2);
        rotationalPidButton.whileHeld(manualRotationCommand);
        // OK UP TO HERE
        oi.operatorPanelButtons.getifAvailable(safetyCheckSwitchChannel).whileHeld(elevatorDisableSafetyChecksCommand);
        
        oi.operatorPanelButtons.getifAvailable(5).whenPressed(setElevatorToFirstCarriage);
        oi.operatorPanelButtons.getifAvailable(6).whenPressed(upOneLevel);
        
        oi.operatorPanelButtons.addAnalogButton(auton1Desc);
        oi.operatorPanelButtons.addAnalogButton(auton2Desc);
        oi.operatorPanelButtons.addAnalogButton(auton3Desc);
        
        oi.operatorPanelButtons.addAnalogButton(intakeSpinLeftDesc);
        oi.operatorPanelButtons.addAnalogButton(intakeSpinRightDesc);
        // OK UP TO HERE
        // Analog autonomous switch
        
        oi.operatorPanelButtons.getAnalogIfAvailable(auton1Desc);//.whileHeld(DoSomething);
        oi.operatorPanelButtons.getAnalogIfAvailable(auton2Desc);//.whileHeld(DoSomething);
        oi.operatorPanelButtons.getAnalogIfAvailable(auton3Desc);//.whileHeld(DoSomething);
        
        // Rotational intake buttons
        oi.operatorPanelButtons.getAnalogIfAvailable(intakeSpinLeftDesc).whileHeld(spinArmWheelOrientLeft);
        oi.operatorPanelButtons.getAnalogIfAvailable(intakeSpinRightDesc).whileHeld(spinArmWheelOrientRight);
        
        oi.operatorPanelButtons.getifAvailable(7).whenPressed(downOneLevel);
        oi.operatorPanelButtons.getifAvailable(8).whileHeld(elevatorOverrideUp);
        oi.operatorPanelButtons.getifAvailable(9).whileHeld(elevatorOverrideDown);
    }
    
    @Inject
    public void setupDriverLeftJoystick(
            OI oi, 
            FieldRelativePrecisionDriveAndRotateCommand precisionDrive,
            ElevatorSetTargetLevelCommand setElevatorToFirstCarriage,
            CommandFactory commandFactory
            ) {
        oi.leftButtons.getifAvailable(1).whenPressed(setElevatorToFirstCarriage); 
        oi.leftButtons.getifAvailable(2).whileHeld(precisionDrive); 
        oi.leftButtons.getifAvailable(3).whenPressed(commandFactory.createSetTargetHeadingCommand(
                Target.LEFT_HUMAN_STATION));

    }
    
    @Inject
    public void setupDriverRightJoystick(
            OI oi, 
            CalibrateHeadingCommand calibrateHeadingCommand,
            RobotRelativeTranslateWithJoysticksCommand robotOrientedDrive,
            CommandFactory commandFactory
            ) {
        oi.rightButtons.getifAvailable(1).whileHeld(robotOrientedDrive);                
        oi.rightButtons.getifAvailable(2).whenPressed(calibrateHeadingCommand);
        
        
        
        oi.rightButtons.getifAvailable(3).whenPressed(
                commandFactory.createSetTargetHeadingCommand(Target.RIGHT_HUMAN_STATION));
    }
	
	@Inject
	public void setupDashboardCommands(
	           CalibrateHeadingCommand calibrateHeadingCommand,
	           CalibrateTranslationCommand calibrateTranslationCommand,
	           AutonomousCommand autoTestCommand,
	           ElevatorSafetyCheckDisableCommand elevatorDisableSafetyChecksCommand,
	           ElevatorPIDDisableCommand elevatorPidSwitchCommand,
	           ElevatorAutoCalibrateCommand elevatorCalibrate,
	           RegenerateElevatorSetpointsCommand genSetpoints,
	           ElevatorCalibrateEncoderCommand elevatorManualEncoderCalibrate,
	           ElevatorLockOn lockOn,
	           ElevatorLockOff lockOff
	           ) {
	    // On-dashboard commands
        SmartDashboard.putData(calibrateHeadingCommand);
        SmartDashboard.putData(calibrateTranslationCommand);
        SmartDashboard.putData(autoTestCommand);
        SmartDashboard.putData("Turn Lock Off", lockOff);
        SmartDashboard.putData("Turn Lock On", lockOn);
        SmartDashboard.putData("Disable Safety Checks", elevatorDisableSafetyChecksCommand);
        SmartDashboard.putData("Disable PID", elevatorPidSwitchCommand);
        SmartDashboard.putData("Run elevator auto-calibration", elevatorCalibrate);
        SmartDashboard.putData("Regenerate elevator setpoints", genSetpoints);
        SmartDashboard.putData("Manually recalibrate encoder to 0 in", elevatorManualEncoderCalibrate);
	}
	
	
}
