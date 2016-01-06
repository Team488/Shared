package tests.elevator;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.wpi.first.wpilibj.command.Scheduler;
import xbot.RobotMap;
import xbot.commands.elevator.ElevatorMaintainTargetHeightCommand;
import xbot.commands.elevator.ElevatorManualOverrideDown;
import xbot.commands.elevator.ElevatorManualOverrideUp;
import xbot.common.wpi_extensions.XScheduler;

public class ElevatorManualTests extends BaseElevatorTest {

    @Test
    public void testManualUp()
    {
        ElevatorManualOverrideUp goUp = injector.getInstance(ElevatorManualOverrideUp.class);
        
        // Elevator is at its midpoint
        mockRobotIO.setAnalogVoltage(RobotMap.elevatorAbsoluteHeightAnalogChannel, 2.5);
        
        checkElevatorPower(Zone.Equals, 0);
        checkBrake(true);
        
        goUp.initialize();
        goUp.execute();
        
        checkElevatorPower(Zone.Above, 0);
        checkBrake(false);
    }
    
    @Test
    public void testManualDown()
    {
        ElevatorManualOverrideDown goDown = injector.getInstance(ElevatorManualOverrideDown.class);
        
        // Elevator is at its midpoint
        setElevatorSensorHeight(30);
        
        checkElevatorPower(Zone.Equals, 0);
        checkBrake(true);
        
        goDown.initialize();
        goDown.execute();
        
        checkElevatorPower(Zone.Below, 0);
        checkBrake(false);
    }
    
    @Ignore("For some reason, WhileHeld instead of WhenActive breaks this. There is no reason for this.")
    // Notably, it works perfectly well on the robot - but using WhenActive doesn't work on the robot.
    // Mystery...
    @Test
    public void testMaintainerIntoManual()
    {
        ElevatorManualOverrideUp goUp = 
                injector.getInstance(ElevatorManualOverrideUp.class);
        ElevatorMaintainTargetHeightCommand maintain = 
                injector.getInstance(ElevatorMaintainTargetHeightCommand.class);
        
        heightChooser.setTargetHeight(0);
        
        XScheduler xScheduler = this.injector.getInstance(XScheduler.class);
        // Run default command
        xScheduler.run();
        xScheduler.run();
        xScheduler.run();
        
        checkElevatorPower(Zone.Equals, 0);
        checkBrake(true);
        
        goUp.start();
        
        xScheduler.run();
        xScheduler.run();
        
        checkElevatorPower(Zone.Above, 0);
        checkBrake(false);
        
        goUp.cancel();
        
        xScheduler.run();
        xScheduler.run();
        
        checkElevatorPower(Zone.Equals, 0);
        checkBrake(true);
    }
    
    @Test
    public void testAfterManualUpOneLevelWorks()
    {
        assertTrue("This test assumes carriage 3 height is near the middle", totePosHeights[2] > 15);
        assertTrue("This test assumes carriage 2 height is a little below 3",
                totePosHeights[2] - totePosHeights[1] > 10);
        
        setElevatorSensorHeight(10);
        heightChooser.setTargetHeightToCurrentHeight();
        
        elevator.setManualPower(1);
        setElevatorSensorHeight(totePosHeights[2] - 2);
        
        heightChooser.upOneLevel();       
        
        assertEquals("Should have chosen carriage 4", totePosHeights[3], heightChooser.getTargetHeight(), 0.001);
    }
    
    @Test
    public void testAfterManualDownOneLevelWorks() {
        
        assertTrue("This test assumes carriage 3 height is near the middle", totePosHeights[2] > 15);
        assertTrue("This test assumes carriage 2 height is a little below 3", totePosHeights[2] - totePosHeights[1] > 10);
        
        setElevatorSensorHeight(10);
        heightChooser.setTargetHeightToCurrentHeight();
        
        elevator.setManualPower(1);
        setElevatorSensorHeight(totePosHeights[2] - 5);
        
        heightChooser.downOneLevel();
        
        assertEquals("Should have chosen carriage 1", totePosHeights[0], heightChooser.getTargetHeight(), 0.001);
    }
    
    @Test
    public void testAfterManualToteSelectionWorksBoundaryConditions()
    {
        setElevatorSensorHeight(10);
        heightChooser.setTargetHeightToCurrentHeight();
        
        setElevatorSensorHeight(-10);
        heightChooser.setTargetHeightToCurrentHeight();
        
        heightChooser.upOneLevel();       
        assertEquals("Should have chosen second-lowest point", totePosHeights[1], heightChooser.getTargetHeight(), 0.001);
        
        setElevatorSensorHeight(1000);
        heightChooser.setTargetHeightToCurrentHeight();
        
        heightChooser.upOneLevel();       
        assertEquals("Should have chosen highest point", highestHeight, heightChooser.getTargetHeight(), 0.001);
        
        setElevatorSensorHeight(-10);
        heightChooser.setTargetHeightToCurrentHeight();
        
        heightChooser.downOneLevel();       
        assertEquals("Should have chosen lowest point", lowestHeight, heightChooser.getTargetHeight(), 0.001);
        
        setElevatorSensorHeight(1000);
        heightChooser.setTargetHeightToCurrentHeight();
        
        heightChooser.downOneLevel();       
        assertEquals("Should have chosen second-highest point", totePosHeights[4], heightChooser.getTargetHeight(), 0.001);
        
    }
}
