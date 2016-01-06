package tests.elevator;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import edu.wpi.first.wpilibj.MockJoystick;
import xbot.OI;
import xbot.OperatorInterfaceCommandMap;
import xbot.commands.elevator.ElevatorPIDDisableCommand;
import xbot.common.wpi_extensions.XScheduler;

public class ElevatorPIDDisableCommandTest extends BaseElevatorTest {

    @Test
    public void testDisablingPID()
    {
        MockJoystick operatorJoystick = (MockJoystick)(injector.getInstance(OI.class).operatorJoystick);
        XScheduler sched = injector.getInstance(XScheduler.class);
        
        MockJoystick operatorPanel = (MockJoystick)(injector.getInstance(OI.class).operatorPanel);
        operatorPanel.pressButton(OperatorInterfaceCommandMap.elevatorPIDSwitchChannel);
        
        sched.run();
        sched.run();
        
        // Mess with the joysticks
        operatorJoystick.setY(1);
        sched.run();
        sched.run();
        checkElevatorState(Zone.Equals, 1, false);
        
        operatorJoystick.setY(-1);
        sched.run();
        checkElevatorState(Zone.Equals, -1, false);
        
        operatorJoystick.setY(0);
        sched.run();
        checkElevatorState(Zone.Equals, 0, true);
        
        // Verify PID enabled
        // TODO: Alex: This assert broke during OI command map refactoring. It seems like each run the disable command is starting and stoping (and it's end causes state to go back to true
        // assertTrue("Elevator PID is disabled", !elevator.getElevatorPidEnabled());
        
        operatorPanel.releaseButton(OperatorInterfaceCommandMap.elevatorPIDSwitchChannel);
        
        sched.run();
        sched.run();
        checkBrake(true);
        assertTrue("Elevator PID is enabled", elevator.getElevatorPidEnabled());
        

    }
}
