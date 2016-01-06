package edu.wpi.first.wpilibj;

public class MockDigitalInput implements xbot.common.wpi_extensions.mechanism_wrappers.XDigitalInput {

	protected boolean value;
	protected final int channel;
	
	public MockDigitalInput(int channel) {
	    this.channel = channel;
	}
	
	public void set_value(boolean value) {
		this.value = value;
	}
	
	public boolean get() {
		return value;
	}
	
	public int getChannel() {
	    return this.channel;
	}

}
