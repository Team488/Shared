package xbot;

import xbot.commands.ReportDebugInfoCommand;
import xbot.commands.SetStatusLEDsCommand;
import xbot.commands.elevator.ElevatorMaintainTargetHeightCommand;
import xbot.commands.rotation.AssistedRotationWithJoysticksCommand;
import xbot.commands.translation.FieldRelativePowerTranslateWithJoysticksCommand;
import xbot.subsystems.ArduinoCommunicationSubsystem;
import xbot.subsystems.DebugInfoSubsystem;
import xbot.subsystems.ElevatorHeightChooserSubsystem;
import xbot.subsystems.ElevatorSubsystem;
import xbot.subsystems.RotationalSubsystem;
import xbot.subsystems.TranslationalSubsystem;

import com.google.inject.Inject;

public class DefaultCommandMap {
	
	@Inject
	public DefaultCommandMap(
	        //Subsystems
	        RotationalSubsystem rotationalDrive,
	        TranslationalSubsystem translationalDrive,
	        ElevatorSubsystem elevator,
	        ElevatorHeightChooserSubsystem elevatorHeightChooser,
	        ArduinoCommunicationSubsystem arduinoComm,
	        //Commands
	        AssistedRotationWithJoysticksCommand rotationalCommand,
	        FieldRelativePowerTranslateWithJoysticksCommand translationalCommand,
	        ElevatorMaintainTargetHeightCommand maintainCommand,
	        SetStatusLEDsCommand ledCommand,
	        //Extras
	        DebugInfoSubsystem infoSubsystem,
	        ReportDebugInfoCommand infoCommand)
	{
		rotationalDrive.setDefaultCommand(rotationalCommand);
		translationalDrive.setDefaultCommand(translationalCommand);
		infoSubsystem.setDefaultCommand(infoCommand);
		elevator.setDefaultCommand(maintainCommand);
		arduinoComm.setDefaultCommand(ledCommand);
	}
}
