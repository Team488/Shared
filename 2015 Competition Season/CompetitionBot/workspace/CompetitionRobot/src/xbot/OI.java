package xbot;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.injection.wpi_factories.WPIFactory;
import xbot.common.wpi_extensions.mechanism_wrappers.XJoystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
@Singleton
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
	
	public XJoystick leftJoystick;
	public XJoystick rightJoystick;
	public XJoystick operatorJoystick;
    public XJoystick operatorPanel;
	
	private final int numButtons = 12;
	
	public JoystickButtonManager leftButtons;
	public JoystickButtonManager rightButtons;
	public JoystickButtonManager operatorJoystickButtons;
    public JoystickButtonManager operatorPanelButtons;
	
	@Inject
	public OI(WPIFactory factory) {
		leftJoystick = factory.getJoystick(1);
		rightJoystick = factory.getJoystick(2);
		operatorPanel = factory.getJoystick(3);
		operatorJoystick = factory.getJoystick(4);

        leftJoystick.setYInversion(true);
        rightJoystick.setXInversion(true);
        operatorJoystick.setYInversion(true);
        
        leftButtons = new JoystickButtonManager(numButtons,factory,leftJoystick);
        rightButtons = new JoystickButtonManager(numButtons,factory,rightJoystick );
        operatorJoystickButtons = new JoystickButtonManager(numButtons,factory,operatorJoystick);
        operatorPanelButtons = new JoystickButtonManager(numButtons, factory, operatorPanel);
	} 
}

