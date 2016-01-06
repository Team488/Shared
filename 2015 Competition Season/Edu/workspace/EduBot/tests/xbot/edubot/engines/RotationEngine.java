package xbot.edubot.engines;

public class RotationEngine {

	private double timeStep = 0.1;
	private double currentHeading = 0;
	private double currentVelocity = 0;
	
	private double power = 0;
	
	private double topSpeed = 36;
	
	// Creates a new
	public RotationEngine()
	{
		
	}
	
	public RotationEngine(double timeStep, double currentHeading, double currentVelocity)
	{
		this.timeStep = timeStep;
		this.currentHeading = currentHeading;
		this.currentHeading = currentVelocity;
	}
	
	// Steps the physics simulator forward a timestep
	public void step()
	{
		// Use power to determine new velocity
		double smoothing = 5;
		currentVelocity += timeStep * ((power*topSpeed) - currentVelocity) / smoothing;
		
		// Apply drag to velocity
		// optional step, maybe?
		
		// Use velocity to update orientation
		currentHeading += currentVelocity;
		
		// normalize orientation
		currentHeading = normalizeAngle(currentHeading);
	}
	
	private double normalizeAngle(double angle)
	{
		if (angle > 180)
		{
			angle -= 360;
			return normalizeAngle(angle);
		}
		else if (angle < -180)
		{
			angle += 360;
			return normalizeAngle(angle);
		}
		else
		{
			return angle;
		}
	}
	
	// Accepts powers from -1 to 1
	public void setPower(double power)
	{
		this.power = power;
	}
	
	// Returns the current orientation of the robot.
	public double getOrientation()
	{
		return currentHeading;
	}
	
	public double getVelocity()
	{
		return currentVelocity;
	}
}
