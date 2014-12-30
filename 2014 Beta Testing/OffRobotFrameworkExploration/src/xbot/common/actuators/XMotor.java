package xbot.common.actuators;

public class XMotor {
	public int channel;
	public double value = 0;
	
	public XMotor(int channel) {
		this.channel = channel;
	}
}
