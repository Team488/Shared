package xbot.commands.distanceSensor;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.RobotMap;
import xbot.common.math.XYPair;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.TranslationalSubsystem;
import xbot.common.controls.Lidar;

@Singleton
public class DriveToPosition extends BaseCommand {
	
	TranslationalSubsystem translate;
	Lidar lidar;
	
	DoubleProperty propDistance;
	
	double position;
	double targetDist;
	
	private boolean finished = false;
	
	private static final Logger log = Logger
			.getLogger(DriveToPosition.class);
		
	@Inject
	public DriveToPosition(TranslationalSubsystem translationalSubsystem)
	{
		this.translate = translationalSubsystem;
		this.requires(translationalSubsystem);
	}
	
	
	public DriveToPosition(RobotMap map, PropertyManager manager)
	{
		propDistance = new DoubleProperty("LIDAR_DISTANCE", 100, manager);
	}

	@Override
	public void initialize()
	{
		log.info("Initializing");
		position = lidar.getDistance();
	}
	
	public void setTargetDistance(int distance) 
	{
		//targetDist = propDistance.get();
		targetDist = distance;
	}
	
	@Override
	public void execute()
	{
		position = lidar.getDistance();
		
		if(position > targetDist)
		{
			translate.translatePowerRobotRelative(new XYPair(0,1)); //forward
		}
		else if(targetDist == position || targetDist < position)
		{	
			translate.translatePowerRobotRelative(new XYPair(0,0)); //stop
			finished = true;
		}
	}
	
	@Override
	public boolean isFinished()
	{
		return finished;
	}
}
