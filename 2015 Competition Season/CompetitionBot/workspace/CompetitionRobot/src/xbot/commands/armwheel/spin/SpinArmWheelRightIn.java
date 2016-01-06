package xbot.commands.armwheel.spin;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ArmWheelSubsystem;

public class SpinArmWheelRightIn extends BaseCommand {
	ArmWheelSubsystem armWheelSubsystem;
	
	private static final Logger log = Logger
			.getLogger(SpinArmWheelRightIn.class);
	
	@Inject
	public SpinArmWheelRightIn(ArmWheelSubsystem armWheelSubsystem){
		this.armWheelSubsystem = armWheelSubsystem;
		this.requires(armWheelSubsystem);
	}
	
	@Override
	public void initialize() {	
		log.info("Initializing");
		armWheelSubsystem.armWheelsPowerRight(1);
	}

	@Override
	public void execute() {
		
	}
	
	@Override
	public void end(){
		armWheelSubsystem.armWheelsPowerRight(0);
	}
}
