package xbot.arial;

import xbot.common.BaseRobot;
import xbot.common.actuators.XMotor;
import xbot.common.sensors.XButton;
import xbot.common.sensors.XJoystick;

public class AerialRobot extends BaseRobot {
	XJoystick leftJoystick = new XJoystick(1);
	XJoystick rightJoystick = new XJoystick(2);
	
	XButton trigger = new XButton(1, 1);
	
	XMotor frontLeft = new XMotor(0);
	XMotor frontRight = new XMotor(1);
	XMotor backLeft = new XMotor(2);
	XMotor backRight = new XMotor(3);
	XMotor collector = new XMotor(4);
	
	public AerialRobot() {
		this.mechanisms.add(frontLeft);
		this.mechanisms.add(frontRight);
		this.mechanisms.add(backLeft);
		this.mechanisms.add(backRight);
		
		this.mechanisms.add(collector);
		
		this.mechanisms.add(leftJoystick);
		this.mechanisms.add(rightJoystick);
		this.mechanisms.add(trigger);
		
		this.workers.add(new TriggerCollectorStateMachine(this));
		//this.workers.add(new TankDriveWorker(this));
	}
}
