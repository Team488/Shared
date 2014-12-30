
package org.usfirst.frc.team488.robot;

import java.util.HashMap;
import java.util.Map;

import xbot.arial.AerialRobot;
import xbot.common.BaseRobot;
import xbot.common.actuators.XMotor;
import xbot.common.sensors.XButton;
import xbot.common.sensors.XJoystick;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	BaseRobot baseRobot = new AerialRobot();
	Map<Object, Object> map = new HashMap<Object, Object>();
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	// initialize all the motors
		for(Object item : baseRobot.mechanisms) {
			if(item instanceof XMotor) {
				XMotor motor = (XMotor)item;
				map.put(item, new Talon(motor.channel));
			} else if(item instanceof XJoystick) {
				XJoystick joystick = (XJoystick)item;
				map.put(item, new Joystick(joystick.number));
				
			} else if (item instanceof XButton) {
				XButton button = (XButton)item;
				map.put(item,  new JoystickButton(new Joystick(button.joystick), button.number));
			}
		}
    }

    public void updateValues() {
    	
    	
    	for(Map.Entry<Object, Object> entry : map.entrySet()) {
    		if(entry.getKey() instanceof XMotor) {
    			XMotor motor = (XMotor)entry.getKey();
    			SpeedController controller = (SpeedController)entry.getValue();
				controller.set(motor.value);
    		} else if(entry.getKey() instanceof XJoystick) {
    			XJoystick xJoystick = (XJoystick)entry.getKey();
    			Joystick joystick = (Joystick)entry.getValue();
				xJoystick.x = joystick.getX();
				xJoystick.y = joystick.getY();
    		} else if(entry.getKey() instanceof XButton) {
				XButton xButton = (XButton)entry.getKey();
				JoystickButton button = (JoystickButton)entry.getValue();
				xButton.isPressed = button.get();
			}
    		
    	}
    	
    }
    
    public void updatePeriodic() {
    	updateValues();
    	baseRobot.tick();
    	updateValues();
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	updatePeriodic();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	updatePeriodic();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	updatePeriodic();
    }
    
}
