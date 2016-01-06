package xbot.commands.rotation;

import org.apache.log4j.Logger;

import xbot.OI;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.RotationalSubsystem;
import xbot.subsystems.TranslationalSubsystem;

import com.google.inject.Inject;

public class ManualRotationCommand extends BaseCommand {
	
	OI oi;
	RotationalSubsystem rotationalSubsystem;
	
	private static Logger log = Logger.getLogger(ManualRotationCommand.class);
	
	@Inject
	public ManualRotationCommand(RotationalSubsystem rotationalSubsystem, OI oi) {
		
		this.rotationalSubsystem = rotationalSubsystem;
		this.requires(rotationalSubsystem);
		this.oi = oi;
	}
	
	@Override
	public void initialize() {
		log.info("Initializing");		
	}

	@Override
	public void execute() {		
		rotationalSubsystem.setRotationalPower(oi.leftJoystick.getVector().x);		
	}
	
	



}