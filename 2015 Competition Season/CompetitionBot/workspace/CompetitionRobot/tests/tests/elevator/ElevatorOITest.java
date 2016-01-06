package tests.elevator;

import org.junit.Test;

import xbot.OI;
import xbot.commands.elevator.ElevatorUpOneLevel;
import xbot.common.wpi_extensions.XScheduler;
import edu.wpi.first.wpilibj.MockJoystick;

public class ElevatorOITest extends BaseElevatorTest {

    @Test
    public void TestJoystickDown() {
        XScheduler xScheduler = this.injector.getInstance(XScheduler.class);
        setElevatorSensorHeight(40);
        heightChooser.setTargetHeight(40);
        
        // Run default command
        xScheduler.run();
        xScheduler.run();
        xScheduler.run();
        
        checkElevatorState(Zone.Equals, 0, true);
        MockJoystick operatorStick = (MockJoystick)(injector.getInstance(OI.class).operatorJoystick);
        // Move joystick down
        
        operatorStick.setY(-1);
        // Run default command
        xScheduler.run();
        xScheduler.run();
        xScheduler.run();
        
        checkElevatorState(Zone.Below, 0, false);
    }
    
    @Test
    public void TestJoystickUp() {
        
        MockJoystick operatorStick = (MockJoystick)(injector.getInstance(OI.class).operatorJoystick);
        setElevatorSensorHeight(40);
        heightChooser.setTargetHeight(40);
        
        XScheduler xScheduler = this.injector.getInstance(XScheduler.class);
        // Run default command
        xScheduler.run();
        xScheduler.run();
        xScheduler.run();
        
        checkElevatorState(Zone.Equals, 0, true);
        // Move joystick up
        
        operatorStick.setY(1);
        
        // Run default command
        xScheduler.run();
        xScheduler.run();
        xScheduler.run();
        
        checkElevatorState(Zone.Above, 0, false);
    }
}
