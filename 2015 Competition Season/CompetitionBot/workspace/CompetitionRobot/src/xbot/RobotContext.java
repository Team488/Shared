package xbot;

import com.google.inject.Injector;

import xbot.common.properties.ITableProxy;
import xbot.common.properties.PermanentStorage;

import xbot.subsystems.*;

public class RobotContext {
	
	public static void initializeRobotSystems(Injector injector)
	{
        injector.getInstance(PermanentStorage.class);
        injector.getInstance(ITableProxy.class);
        
		injector.getInstance(RobotMap.class);
		injector.getInstance(DriveSubsystem.class);
		injector.getInstance(DebugInfoSubsystem.class);
		injector.getInstance(OI.class);

        injector.getInstance(VisionSubsystem.class);
        injector.getInstance(ArmPistonSubsystem.class);
        injector.getInstance(ArmWheelSubsystem.class);
        injector.getInstance(ElevatorSubsystem.class);
		injector.getInstance(DriveSubsystem.class);
		injector.getInstance(DebugInfoSubsystem.class);
		injector.getInstance(AutonomousSubsystem.class);
		
		injector.getInstance(DefaultCommandMap.class);
		injector.getInstance(OperatorInterfaceCommandMap.class);
	}
}
