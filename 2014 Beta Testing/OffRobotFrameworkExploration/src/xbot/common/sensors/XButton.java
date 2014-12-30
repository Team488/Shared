package xbot.common.sensors;

public class XButton {
	public int joystick; // which joystick
	public int number; // which button
	public boolean isPressed = false;
	
	public XButton(int joystick, int number) {
		this.joystick = joystick;
		this.number = number;
	}
}
