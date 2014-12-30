package edu.wpi.first.wpilibj;

import com.google.inject.Inject;

public class MockSpeedController implements SpeedController {
	public double value;
	public int channel;
	
	public MockSpeedController(int channel) {
		this.channel = channel;
	}

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		value = output;
	}

	@Override
	public double get() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public void set(double speed, byte syncGroup) {
		// TODO Auto-generated method stub
		value = speed;
	}

	@Override
	public void set(double speed) {
		// TODO Auto-generated method stub
		value = speed;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		value = 0;
	}

}
