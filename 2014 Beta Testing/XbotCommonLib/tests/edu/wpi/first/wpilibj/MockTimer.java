package edu.wpi.first.wpilibj;

import com.google.inject.Singleton;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Timer.Interface;

@Singleton
public class MockTimer implements Timer.StaticInterface {

	double timeInSeconds;
	
	public void setTimeInSeconds(double time) {
		timeInSeconds = time;
	}
	
	public void advanceTimeInSecondsBy(double time) {
		timeInSeconds += time;
	}
	
	/**
	 * Return the system clock time in seconds. Return the time from the
	 * FPGA hardware clock in seconds since the FPGA started.
	 *
	 * @return Robot running time in seconds.
	 */
	@Override
	public double getFPGATimestamp() {
		// TODO Auto-generated method stub
		return timeInSeconds;
	}

	@Override
	public double getMatchTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delay(double seconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Interface newTimer() {
		// TODO Auto-generated method stub
		return null;
	}
	
}