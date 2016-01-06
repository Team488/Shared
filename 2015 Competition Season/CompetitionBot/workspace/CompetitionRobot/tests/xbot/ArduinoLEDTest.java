package xbot;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import tests.xbot.BaseTestCase;
import xbot.common.math.XYPair;
import xbot.subsystems.ArduinoCommunicationSubsystem;

public class ArduinoLEDTest extends BaseTestCase
{
    private RobotMap map;
    @Before
    public void init()
    {
        map = injector.getInstance(RobotMap.class);
    }
    
    @Test
    public void testLEDs()
    {
        ArduinoCommunicationSubsystem comm = injector.getInstance(ArduinoCommunicationSubsystem.class);
        
        comm.setLEDData(0);
        assertValues(false, false);
        
        comm.setLEDData(1);
        assertValues(true, false);
        
        comm.setLEDData(2);
        assertValues(false, true);
        
        comm.setLEDData(3);
        assertValues(true, true);
    }

    private void assertValues(boolean... b) {
        for(int i = 0; i < b.length; i++)
        {
            int channel = map.ledOutputs[i].getChannel();
            assertTrue(
                    "Digital output " + channel + " should be set to " + b[i],
                    mockRobotIO.getDigital(channel) == b[i]);
        }
    }
}
