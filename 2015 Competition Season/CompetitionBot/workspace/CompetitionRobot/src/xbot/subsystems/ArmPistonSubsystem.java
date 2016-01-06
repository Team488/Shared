package xbot.subsystems;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.RobotMap;
import xbot.common.wpi_extensions.BaseSubsystem;
import xbot.common.wpi_extensions.mechanism_wrappers.XSolenoid;

@Singleton
public class ArmPistonSubsystem extends BaseSubsystem {

	public XSolenoid pistonArmLeftOpenClose;
	public XSolenoid pistonArmRightOpenClose;
	public boolean intakePosition;
	
	private static final Logger log = Logger
            .getLogger(ArmPistonSubsystem.class);

	@Inject
	public ArmPistonSubsystem(RobotMap map) {
		this.pistonArmLeftOpenClose = map.intakeArmLeftSolenoid;
		this.pistonArmRightOpenClose = map.intakeArmRightSolenoid;
		intakePosition = true;
	}
	
	public void pistonStateClose(boolean state) {
	    log.info("Setting piston state to: " + state);
	    intakePosition = state;
		pistonArmLeftOpenClose.set(state);
	}
}
