package edu.wpi.first.wpilibj;

public class MockJoystick extends GenericHID {
	double x = 0;
	double y = 0;
	
	public MockJoystick() {

	}

	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}

	@Override
	public double getX(Hand hand) {
		return this.x;
	}

	@Override
	public double getY(Hand hand) {
		return this.y;
	}

	@Override
	public double getZ(Hand hand) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public double getTwist() {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public double getThrottle() {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public double getRawAxis(int which) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean getTrigger(Hand hand) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean getTop(Hand hand) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean getBumper(Hand hand) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public boolean getRawButton(int button) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public int getPOV(int arg0) {
		throw new RuntimeException("Not yet implemented");
	}	
}
