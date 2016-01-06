package tests.elevator;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import xbot.RobotMap;
import xbot.subsystems.ElevatorHeightChooserSubsystem;

public class ElevatorSubsystemChooserTest extends BaseElevatorTest {
	    
    @Test
    public void testForkSystem() 
    {
        ElevatorHeightChooserSubsystem heightChooser = injector.getInstance(ElevatorHeightChooserSubsystem.class);
        
        setElevatorSensorHeight(40d);
        elevator.updateAndGetHeight();

        heightChooser.goToAbsoluteBottom();
        
        assertEquals(lowestHeight, heightChooser.getTargetHeight(), 0);
        
        heightChooser.setStepHeightModifier(true);
        
        assertEquals(lowestHeight+7, heightChooser.getTargetHeight(), 0);
        
        heightChooser.setStepHeightModifier(false);
        
        heightChooser.setToteLevel(0);
        double zeroHeight = totePosHeights[0];
        assertEquals(zeroHeight, heightChooser.getTargetHeight(), 0);
        
        heightChooser.setAbsoluteForkPositionIndex(8);
        // 55 is max safe height
        assertEquals(elevator.getMaxHeight(), heightChooser.getTargetHeight(), 0);
        
        heightChooser.setStepHeightModifier(false);
        //test for seeing if elevator sets imaginary height values 
        heightChooser.setTargetHeight(49);
        assertEquals(49, heightChooser.getTargetHeight(), 0);
        
        heightChooser.setAbsoluteForkPositionIndex(10);
        heightChooser.downOneLevel();
        assertEquals(totePosHeights[4], heightChooser.getTargetHeight(), 0);
        
        heightChooser.downOneLevel();
        assertEquals(totePosHeights[3], heightChooser.getTargetHeight(), 0);
        
        heightChooser.upOneLevel();
        assertEquals(totePosHeights[4], heightChooser.getTargetHeight(), 0);
        
        heightChooser.goToAbsoluteBottom();
        assertEquals(zeroHeight, heightChooser.getTargetHeight(), 0);
        
        heightChooser.downOneLevel();
        assertEquals(zeroHeight, heightChooser.getTargetHeight(), 0);
    
        heightChooser.downOneLevel();
        assertEquals(zeroHeight, heightChooser.getTargetHeight(), 0);
    }
	
	@Test
	public void testStartupSystem()
	{
	    setElevatorSensorHeight(45);
	    heightChooser.setTargetHeight(elevator.getMinHeight());
	    
	    assertEquals(45, elevator.updateAndGetHeight(), 0.001);
	    assertEquals(elevator.getMinHeight(), heightChooser.getTargetHeight(), 0.001);
	    
	    heightChooser.setTargetHeightToCurrentHeight();
	    
	    assertEquals(45, elevator.updateAndGetHeight(), 0.001);
        assertEquals(45, heightChooser.getTargetHeight(), 0.001);
	}
}

