package tests.drive;

import static org.junit.Assert.*;

import org.junit.*;

import xbot.common.math.XYPair;
import xbot.common.properties.ITableProxy;
import edu.wpi.first.wpilibj.MockEncoder;

public class DriveSubsystemTest extends BaseMotionTest
{
	public DriveSubsystemTest()
    {
        super();
    }

    @Test
	public void testTranslate()
	{		
        tranSystem.translatePowerRobotRelative(new XYPair(1, 0));
		assertEquals(RobotDirection.TranslatingRight, getRobotRelativeMotion()[0]);
		
		tranSystem.translatePowerRobotRelative(new XYPair(-1, 0));
		assertEquals(RobotDirection.TranslatingLeft, getRobotRelativeMotion()[0]);
		
		tranSystem.translatePowerRobotRelative(new XYPair(0, 1));
		assertEquals(RobotDirection.Forward, getRobotRelativeMotion()[0]);
		
		tranSystem.translatePowerRobotRelative(new XYPair(0, -1));
		assertEquals(RobotDirection.Backward, getRobotRelativeMotion()[0]);
		
		tranSystem.translatePowerRobotRelative(new XYPair(0, 0));
		assertEquals(RobotDirection.Stopped, getRobotRelativeMotion()[0]);
		

	}
	
	@Test
	public void testRotation()
	{        
        rotSystem.setRotationalPower(1);
		assertEquals(RobotDirection.TurningLeft, getRobotRelativeMotion()[1]);
		
		rotSystem.setRotationalPower(-1);
		assertEquals(RobotDirection.TurningRight, getRobotRelativeMotion()[1]);

		rotSystem.setRotationalPower(0);
		assertEquals(RobotDirection.Stopped, getRobotRelativeMotion()[1]);
		
		tranSystem.translatePowerRobotRelative(new XYPair(0, 1));
		rotSystem.setRotationalPower(0);
		assertEquals(RobotDirection.MovingButNotRotating, getRobotRelativeMotion()[1]);
		
		tranSystem.translatePowerRobotRelative(new XYPair(0, -1));
		rotSystem.setRotationalPower(0);
		assertEquals(RobotDirection.MovingButNotRotating, getRobotRelativeMotion()[1]);
	}
	
	@Test
	public void testFieldOrientedTranslation()
	{
		setHeading(180);
		tranSystem.translatePowerFieldRelative(new XYPair(0, 1));
		assertEquals(RobotDirection.TranslatingRight, getRobotRelativeMotion()[0]);
		
		setHeading(-90);
        tranSystem.translatePowerFieldRelative(new XYPair(1, 0));
        assertEquals(RobotDirection.TranslatingLeft, getRobotRelativeMotion()[0]);
        
        rotSystem.calibrate();
        tranSystem.translatePowerFieldRelative(new XYPair(0, 1));
        assertEquals(RobotDirection.Forward, getRobotRelativeMotion()[0]);
	}
	
	@Test
	public void testTranslationMeasurement()
	{
	    MockEncoder verticalEncoder1 = ((MockEncoder)map.verticalEncoder1);
	    MockEncoder verticalEncoder2 = ((MockEncoder)map.verticalEncoder2);
	    MockEncoder horizontalEncoder = ((MockEncoder)map.horizontalEncoder);
	    setHeading(90);
	    
	    tranMeasSystem.updateTrackingState();
        assertXYPairEquals(XYPair.ZERO, tranMeasSystem.getPosition());
        assertXYPairEquals(XYPair.ZERO, tranMeasSystem.getVelocityFieldRelative());
	    
	    horizontalEncoder.setDistance(30);
	    tranMeasSystem.updateTrackingState();
	    assertTrue(tranMeasSystem.getPosition().x > 0);
        assertEquals(0, tranMeasSystem.getPosition().y, 0.01);
        
        horizontalEncoder.setDistance(0);
	    tranMeasSystem.updateTrackingState();
        assertXYPairEquals(XYPair.ZERO, tranMeasSystem.getPosition());
	    
	    horizontalEncoder.setDistance(-30);
	    tranMeasSystem.updateTrackingState();
	    assertTrue(tranMeasSystem.getPosition().x < 0);
        assertEquals(0, tranMeasSystem.getPosition().y, 0.01);
        
        horizontalEncoder.setDistance(0);
	    tranMeasSystem.updateTrackingState();
        assertXYPairEquals(XYPair.ZERO, tranMeasSystem.getPosition());
        
	    setHeading(120);
	    verticalEncoder1.setDistance(10);
	    verticalEncoder2.setDistance(-10);
	    tranMeasSystem.updateTrackingState();
        assertXYPairEquals(XYPair.ZERO, tranMeasSystem.getPosition());
        
        verticalEncoder1.setDistance(30);
        verticalEncoder2.setDistance(10);
        tranMeasSystem.updateTrackingState();  
        assertTrue(tranMeasSystem.getPosition().x < 0);
        assertTrue(tranMeasSystem.getPosition().y > 0);
        
        tranMeasSystem.resetState();
        assertXYPairEquals(XYPair.ZERO, tranMeasSystem.getPosition());
	}
	
	@Test
	public void testDriveToPosition()
	{
	    MockEncoder verticalEncoder1 = ((MockEncoder)map.verticalEncoder1);
        MockEncoder verticalEncoder2 = ((MockEncoder)map.verticalEncoder2);
        MockEncoder horizontalEncoder = ((MockEncoder)map.horizontalEncoder);
        
        ITableProxy randomStore = injector.getInstance(ITableProxy.class);
        randomStore.setDouble("Position P", 0.1);
        randomStore.setDouble("Position I", 0.0);
        randomStore.setDouble("Position D", 0.0);
        
        setHeading(90);
	    
        verticalEncoder1.setDistance(0);
        verticalEncoder2.setDistance(0);
        horizontalEncoder.setDistance(0);
        
        tranSystem.goToFieldPositionAbsolute(new XYPair(0, 100));
        assertEquals(RobotDirection.Forward, getRobotRelativeMotion()[0]);
	    
        setHeading(180);
        
        tranSystem.goToFieldPositionAbsolute(new XYPair(-100, 0));
        assertEquals(RobotDirection.Forward, getRobotRelativeMotion()[0]);
        
        setHeading(180);
        
        tranSystem.goToFieldPositionAbsolute(new XYPair(0, 100));
        assertEquals(RobotDirection.TranslatingRight, getRobotRelativeMotion()[0]);
    }	
}
