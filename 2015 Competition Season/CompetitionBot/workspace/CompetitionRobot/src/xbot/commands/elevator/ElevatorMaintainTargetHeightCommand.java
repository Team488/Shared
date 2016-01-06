package xbot.commands.elevator;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import xbot.RobotMap;
import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.math.PIDManager;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseCommand;
import xbot.subsystems.ElevatorHeightChooserSubsystem;
import xbot.subsystems.ElevatorSensorSubsystem;
import xbot.subsystems.ElevatorSubsystem;

public class ElevatorMaintainTargetHeightCommand extends BaseCommand implements Observer
{
    private static Logger log = Logger.getLogger(ElevatorMaintainTargetHeightCommand.class);
    
    private PIDManager heightReacher;
    
    private PIDManager speedReacher;
    private double oldHeight = 0;
    private long oldNano = 0;
    
    private ElevatorSubsystem elevator;
    private ElevatorSensorSubsystem elevatorSensors;
    private ElevatorHeightChooserSubsystem elevatorHeightChooser;
    
    private ArrayList<Double> powerHistory;
    public static final String elevatorAveragingWindowPropname = "Elevator power samples to average";
    private DoubleProperty averagingWindowSize;
    private Latch powerLatch;
    private double elevatorSmoothedPower = 0;
    
    private DoubleProperty speedFactor;
    private DoubleProperty speedLimit;
       
    @Inject
    public ElevatorMaintainTargetHeightCommand(
            ElevatorSubsystem elevator,
            ElevatorSensorSubsystem elevatorSensors,
            RobotMap map, 
            PropertyManager propMan,
            ElevatorHeightChooserSubsystem elevatorHeightChooser)
    {
        log.info("Creating ElevatorMaintainTargetHeightCommand");
                
        powerHistory = new ArrayList<Double>();
        averagingWindowSize = propMan.createProperty(elevatorAveragingWindowPropname, 1.0);
        speedFactor = propMan.createProperty("Elevator Speed Factor", 10d);
        speedLimit = propMan.createProperty("Elevator Speed Limit (in)", 10d);
        powerLatch = new Latch(false, EdgeType.Both);
        
        powerLatch.addObserver(this);
        
        heightReacher = new PIDManager("Elevator", propMan, 0.4d, 0d, 1.0d);
        speedReacher = new PIDManager("ElevatorSpeed", propMan, 0.001d, 0.003d, 0.001d);
        
        this.elevator = elevator;
        this.elevatorHeightChooser = elevatorHeightChooser;
        this.elevatorSensors = elevatorSensors;
        this.requires(elevator);
    }
    
    @Override
    public void initialize()
    {
        log.info("Initializing");
        
    }
    
    private double getAveragePower()
    {
        return elevatorSmoothedPower;
    }

    private void storeElevatorPowerHistory(double currentPower)
    {
        powerLatch.setValue(currentPower > 0);
        
        // if current power is negative, don't bother storing
        if (currentPower < 0) {
            elevatorSmoothedPower = currentPower;
            return;
        }
        
        // Basic idea - keep a history of positive powers, reset on negative powers.
        double windowSize = averagingWindowSize.get();
        
        // add newest power, remove oldest if too long
        powerHistory.add(currentPower);
        if (powerHistory.size() > windowSize)
        {
            powerHistory.remove(0);
        }
        
        // Do the averaging math
        double total = 0;
        for (double power : powerHistory)
        {
            total += power;
        }
        if (windowSize > 0)
        {
            total /= windowSize;
        }
        
        elevatorSmoothedPower = total;
    }
    
    @Override
    public void execute() {
    	
        if (!shouldWeUsePid()) {
            elevator.setAutomaticPower(0);
            return;
        }

        double elevatorPower;
        boolean isLocked = false;
        
        if (elevatorHeightChooser.isInTargetRange()) {
            elevatorPower = 0;
            isLocked = true;
        }
        else {

            double goal = elevatorHeightChooser.getTargetHeight();
            double currHeight = elevator.updateAndGetHeight();
            
            double elevatorSpeedInInches = getElevatorSpeed(currHeight);
            double positionalIntent = getPositionalIntent(goal, currHeight);
            
            // We now scale this intent into the speed system and calculate.
            
            elevatorPower = getSpeedIntent(elevatorSpeedInInches, positionalIntent);
            
            elevatorPower = limitMaximumOutputPower(elevatorPower);
            
            elevatorPower = reducePowerIfTipping(elevatorPower);
            
            elevatorPower = smoothPositivePowers(elevatorPower);
        }

        elevator.setLocked(isLocked);        
        elevator.updateStallTracking();
        elevator.setAutomaticPower(elevatorPower);
    }

    private double smoothPositivePowers(double elevatorPower) {
        storeElevatorPowerHistory(elevatorPower);
        elevatorPower = getAveragePower();
        return elevatorPower;
    }

    private double reducePowerIfTipping(double elevatorPower) {
        if(elevatorSensors.isTiltInDangerZone()) {
            elevatorPower = 0d;
            powerHistory.clear();
        }
        return elevatorPower;
    }

    private double limitMaximumOutputPower(double elevatorPower) {
        // 1.0 power on the elevator is terrifyingly fast.
        double maxPidPower = elevatorHeightChooser
                .getElevatorMaximumPidPower();
        double minPower = -maxPidPower;
        
        elevatorPower = MathUtils.constrainDouble(
                elevatorPower,
                minPower,
                maxPidPower);
        return elevatorPower;
    }

    private double getSpeedIntent(double elevatorSpeedInInches, double positionalIntent) {
        double speedGoal = positionalIntent * speedFactor.get();
        // We limit the overall velocity of the system
        speedGoal = MathUtils.constrainDouble(speedGoal, -speedLimit.get(), speedLimit.get());
        double speedIntent = speedReacher.calculate(speedGoal, elevatorSpeedInInches);
        return speedIntent;
    }

    private double getPositionalIntent(double goal, double currHeight) {
        // If our current is 0, and our goal is 5, this will produce a negative delta - 
        // but that's okay, since the goal of the PID is to drive the "error" (in this
        // case the delta) down to 0. Seeing a negative error, it will apply positive
        // power. Positive power will move the elevator up, thus reducing the error.
        
        double heightError = currHeight - goal;
        double positionalIntent = heightReacher.calculate(0, heightError);
        return positionalIntent;
    }

    private double getElevatorSpeed(double currHeight) {
        //assuming running every 20ms
        double deltaHeight = currHeight - oldHeight;
        oldHeight = currHeight;
        
        // The very first calculation will be broken. Not sure how to best handle that.
        long currentNano = System.nanoTime();
        long deltaNano = currentNano - oldNano;
        oldNano = currentNano;
        
        // I suspect that proper speed calculation is VERY important. As a result, we need
        // to have a good sense of time.
        double elevatorSpeedInInches = deltaHeight/((double)deltaNano / 1000000000);
        return elevatorSpeedInInches;
    }

    private boolean shouldWeUsePid() {
        return elevator.getElevatorPidEnabled() && elevator.getIsEncoderCalibrated();
    }

    @Override
    public void update(Observable o, Object arg) {
        
        EdgeType edge = (EdgeType)arg;
        
        if (o.equals(powerLatch))
        {
            if (edge == EdgeType.RisingEdge)
            {
                log.debug("Resetting averaging window for elevator power");
                // We just got our first positive power. Clear the array.
                powerHistory.clear();
                // Add a bunch of historical zeroes
                for (int i = 0; i < (int)averagingWindowSize.get(); i++)
                {
                    powerHistory.add(0d);
                }
            }
        }
    }
}
