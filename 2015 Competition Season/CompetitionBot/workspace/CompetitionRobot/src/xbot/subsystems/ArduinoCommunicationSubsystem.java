package xbot.subsystems;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.wpi.first.wpilibj.DigitalOutput;
import xbot.RobotMap;
import xbot.common.wpi_extensions.BaseSubsystem;
import xbot.common.wpi_extensions.mechanism_wrappers.XDigitalOutput;
import xbot.common.wpi_extensions.mechanism_wrappers.XSolenoid;

@Singleton
public class ArduinoCommunicationSubsystem extends BaseSubsystem {

    private static final Logger log = Logger.getLogger(ArduinoCommunicationSubsystem.class);
    
    private XDigitalOutput[] outputPins;

    @Inject
    public ArduinoCommunicationSubsystem(RobotMap map) {
        this.outputPins = map.ledOutputs;
    }
    
    public void setLEDData(byte data)
    {
        setLEDData((int)data);
    }
    
    public void setLEDData(int data)
	{
        if(data >> outputPins.length != 0)
        {
            log.warn("LED data lost (not enough pins!)");
        }
        
	    for(int i = 0; i < outputPins.length; i++)
	    {
	        outputPins[i].set((data & (1 << i)) != 0);
	    }
	}
}
