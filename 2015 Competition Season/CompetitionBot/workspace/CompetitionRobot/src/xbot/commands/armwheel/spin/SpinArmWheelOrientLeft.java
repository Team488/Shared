package xbot.commands.armwheel.spin;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmPistonSubsystem;
import xbot.subsystems.ArmWheelSubsystem;

public class SpinArmWheelOrientLeft extends BaseCommand {
	ArmWheelSubsystem armWheelSubsystem;
	ArmPistonSubsystem armPistons;
	
	private static final Logger log = Logger
			.getLogger(SpinArmWheelOrientLeft.class);
	
	@Inject
	public SpinArmWheelOrientLeft(
	        ArmWheelSubsystem armWheelSubsystem,
	        ArmPistonSubsystem armPistons){
		this.armWheelSubsystem = armWheelSubsystem;
		this.armPistons = armPistons;
		this.requires(armWheelSubsystem);
		this.requires(armPistons);
	}
	
	@Override
	public void initialize() {	
		log.info("Initializing");
		armWheelSubsystem.armWheelsPowerLeft(-1);
		armWheelSubsystem.armWheelsPowerRight(-1);
		armPistons.pistonStateClose(true);
	}

	@Override
	public void execute() {
		
	}
	
	@Override
	public void end(){
		armWheelSubsystem.armWheelsPowerLeft(0);
		armWheelSubsystem.armWheelsPowerRight(0);
	}
}