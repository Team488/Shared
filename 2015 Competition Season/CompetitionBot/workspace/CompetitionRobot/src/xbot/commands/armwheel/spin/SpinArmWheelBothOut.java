package xbot.commands.armwheel.spin;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmPistonSubsystem;
import xbot.subsystems.ArmWheelSubsystem;

public class SpinArmWheelBothOut extends BaseCommand {
	ArmWheelSubsystem armWheelSubsystem;
	ArmPistonSubsystem armPistons;
	
	private static final Logger log = Logger
			.getLogger(SpinArmWheelBothOut.class);
	
	@Inject
	public SpinArmWheelBothOut(
	        ArmWheelSubsystem armWheelSubsystem){
		this.armWheelSubsystem = armWheelSubsystem;
		this.armPistons = armPistons;
		this.requires(armWheelSubsystem);
	}
	
	@Override
	public void initialize() {	
		log.info("Initializing");
		armWheelSubsystem.armWheelsPowerLeft(armWheelSubsystem.getCollectorPower());
		armWheelSubsystem.armWheelsPowerRight(-armWheelSubsystem.getCollectorPower());
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