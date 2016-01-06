package xbot.subsystems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import xbot.common.logic.Latch;
import xbot.common.logic.Latch.EdgeType;
import xbot.common.math.MathUtils;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.PropertyManager;
import xbot.common.wpi_extensions.BaseSubsystem;

@Singleton
public class ElevatorHeightChooserSubsystem extends BaseSubsystem implements Observer {
	
    private static Logger log = Logger.getLogger(ElevatorHeightChooserSubsystem.class);
    private final PropertyManager propMan;
    
	private ElevatorSubsystem elevator;
	
	private DoubleProperty stepHeight;
	private boolean stepFlag = false;
	
	private boolean heightGoalSetManually = true;
	
	private DoubleProperty elevatorTargetHeight;
	
	private DoubleProperty elevatorHeightTolerance;
	
	private DoubleProperty elevatorMaximumPidPower;	

    private DoubleProperty elevatorPositionDeadband;    
    private DoubleProperty elevatorDeadbandLarge;
    private DoubleProperty elevatorDeadbandSmall;

    private DoubleProperty elevatorMinForkHeight;
    private DoubleProperty forkLowestGroundHeight;
    private DoubleProperty forkHeightPrepareFirstPickup;
    private DoubleProperty forkHeightAtFirstToteLip;
    private DoubleProperty forkDistanceBetweenToteLips;
    private DoubleProperty elevatorMaxForkHeight;
    
    protected int firstTotePositionIndex, lastTotePositionIndex;
    
    public static final String deadbandSmallPropname = "Elevator deadband small (in)";
    public static final String deadbandLargePropname = "Elevator deadband large (in)";

    public static final String forkLowestGroundHeightPropname = "HeightChooser - fork ground-level height (in)";
    public static final String forkHeightPrepareFirstPickupPropname = "HeightChooser - fork prepare first pickup height (in)";
    public static final String forkHeightAtFirstToteLipPropname = "HeightChooser - height at first tote lip (in)";
    public static final String forkDistanceBetweenToteLipsPropname = "HeightChooser - distance between tote lips (in)";
    public static final String forkMaxHeightPropname = "HeightChooser - maximum safe fork height (in)";
    public static final String totePositionBasePropname = "HeightChooser - fork position #";
    
    private ArrayList<DoubleProperty> elevatorAbsoluteHeights;
    private int absoluteHeightIndex = 1;

    private Latch deadbandLatch;
	
    @Inject
	public ElevatorHeightChooserSubsystem(ElevatorSubsystem elevator,
	        PropertyManager propMan)
	{
		log.info("Creating ElevatorHeightChooserSubsystem");
		this.propMan = propMan;
		
		this.elevator = elevator;
		
		stepHeight = propMan.createProperty("StepHeight", 7d);
		
		elevatorTargetHeight = propMan.createProperty("elevatorTargetHeight", 0d);
		elevatorHeightTolerance = propMan.createProperty("elevatorHeightTolerance", 2d);
		elevatorMaximumPidPower = propMan.createProperty("elevatorMaximumPidPower", 0.8);
		
		elevatorDeadbandLarge = propMan.createProperty(deadbandLargePropname, 0.4);
        elevatorDeadbandSmall = propMan.createProperty(deadbandSmallPropname, 0.25);   
        
        forkLowestGroundHeight = propMan.createProperty(forkLowestGroundHeightPropname, elevator.getMinHeight());
        forkHeightPrepareFirstPickup = propMan.createProperty(forkHeightPrepareFirstPickupPropname, elevator.getMinHeight());
        forkHeightAtFirstToteLip = propMan.createProperty(forkHeightAtFirstToteLipPropname, elevator.getMinHeight());
        forkDistanceBetweenToteLips = propMan.createProperty(forkDistanceBetweenToteLipsPropname, 10.5d);
        elevatorMaxForkHeight = propMan.createProperty(forkMaxHeightPropname, elevator.getMaxHeight());
        
        generateElevatorSetpoints();

        elevatorPositionDeadband = 
                propMan.createProperty(
                        "Elevator current position deadband (in)", elevatorDeadbandSmall.get());
		
        deadbandLatch = new Latch(false, EdgeType.Both);
        deadbandLatch.addObserver(this);
	}
    
    @SuppressWarnings("serial")
    public void generateElevatorSetpoints() {
        elevatorAbsoluteHeights = 
                new ArrayList<DoubleProperty>(){{
                    // Add future setpoints here
            }};

            // Next added position index will be the first that will pick up a tote
            firstTotePositionIndex = elevatorAbsoluteHeights.size();
            
            elevatorAbsoluteHeights.add(forkHeightPrepareFirstPickup);
            
            // If we generate more than 20 setpoints, something is probably wrong anyway...
            for(int totePos = 1; totePos <= 20; totePos++) {
                double totePosHeight = forkHeightAtFirstToteLip.get() + totePos * forkDistanceBetweenToteLips.get();
                
                if(totePosHeight >= elevator.getMaxHeight()) {
                    break;
                }
                
                log.info("Dynamically creating height pos " + totePos + " (absolute height " + totePosHeight + ")");
                
                DoubleProperty property = propMan.createProperty(totePositionBasePropname + totePos, totePosHeight);
                property.set(totePosHeight);
                elevatorAbsoluteHeights.add(property);
            }
            
            lastTotePositionIndex = elevatorAbsoluteHeights.size() - 1;
            
            elevatorAbsoluteHeights.add(elevatorMaxForkHeight);
        
    }

    public boolean isInTargetRange()
    {
        double goal = getTargetHeight();
        double currHeight = elevator.updateAndGetHeight();
        double deltaHeight = currHeight - goal;
        
        boolean isOnTarget = Math.abs(deltaHeight) <= getElevatorPositionDeadband();
        deadbandLatch.setValue(isOnTarget);
        
        return isOnTarget;
    }
    
    public double getElevatorPositionDeadband() {
        return elevatorPositionDeadband.get();
    }
	
	public void setStepHeightModifier(boolean onStep){
		stepFlag = onStep;
		setAbsoluteIndex(absoluteHeightIndex);
    }
	
	public void setToteLevel(int toteIndex) {
	    log.info(String.format("Getting height index for tote index #%d", toteIndex));
	    int positionIndex = getIndexForToteLevel(toteIndex);
        setAbsoluteIndex(positionIndex);	    
	}
	
	public int getIndexForToteLevel(int toteRelativeIndex) {
	    return MathUtils.constrainInt(
                toteRelativeIndex + firstTotePositionIndex,
                firstTotePositionIndex,
                lastTotePositionIndex);
	}
	 
    public void setAbsoluteForkPositionIndex(int positionIndex){
    	log.info(String.format("Getting height index #%d", positionIndex));
    	setAbsoluteIndex(positionIndex);
    }
    
    private void setAbsoluteIndex(int index)
    {
        log.info(String.format("Elevator requested to go to fork position index: %d", index));
        absoluteHeightIndex = constrainPosIndex(index);
        
        log.info(String.format("After normalizing, Elevator going to position index: %d", absoluteHeightIndex));
                
        double targetHeight = getHeightForPosIndex(absoluteHeightIndex);
        if (stepFlag)
        {
            targetHeight += stepHeight.get();
        }
        heightGoalSetManually = false;
        setTargetHeight(targetHeight);
    }
    
    private int constrainPosIndex(int index)
    {
        return MathUtils.constrainInt(index, 0, elevatorAbsoluteHeights.size() - 1);
    }
    
    private double getHeightForPosIndex(int posIndex)
    {
        return elevatorAbsoluteHeights.get(constrainPosIndex(posIndex)).get();
    }
    
    public void upOneLevel(){
        log.info("Going up one level");
        
        if (heightGoalSetManually) {
            setAbsoluteIndex(determineNearestAbsoluteIndex() + 1);
        }
        else {
            setAbsoluteIndex(absoluteHeightIndex+1);
        }
    }
    
    public void downOneLevel(){
    	log.info("Going down one level.");
    	
        if (heightGoalSetManually) {
            setAbsoluteIndex(determineNearestAbsoluteIndex() - 1);
        }
        else {
            setAbsoluteIndex(absoluteHeightIndex-1);
        }
    }
    
    public void goToAbsoluteBottom(){
    	log.info("Moving to bottom");
    	setAbsoluteIndex(0);
    }
    
    private int determineNearestAbsoluteIndex() {
        log.info("Finding nearest elevator position");
        double height = elevator.updateAndGetHeight();
        
        ArrayList<DoubleProperty> sorted = (ArrayList<DoubleProperty>) elevatorAbsoluteHeights.clone();
        sorted.sort((a, b) -> (int)(Math.abs(a.get() - height) - Math.abs(b.get() - height)));
        
        return elevatorAbsoluteHeights.indexOf(sorted.get(0));
    }

	public void setTargetHeight(double newTargetHeight) {
	    double constrainedHeight =
	            MathUtils.constrainDouble(newTargetHeight, elevator.getMinHeight(), elevator.getMaxHeight());
	    
	    if(constrainedHeight != newTargetHeight)
	    {
	        log.info("Attempted to set height beyond safe bounds."
	                + " Attempted value: " + newTargetHeight + ", constrained to: " + constrainedHeight);
	    }
	    
	    log.info(String.format("Setting height to %.2f", constrainedHeight));
	    
        this.elevatorTargetHeight.set(constrainedHeight);
	}
	
	public void setTargetHeightToCurrentHeight()
	{
	    heightGoalSetManually = true;
	    setTargetHeight(elevator.updateAndGetHeight());
	}

    public double getTargetHeight() {
        return elevatorTargetHeight.get();
    }
	
	public double getElevatorHeightTolerance() {
		return elevatorHeightTolerance.get();
	}

	public void setElevatorHeightTolerance(double elevatorHeightTolerance) {
		this.elevatorHeightTolerance.set(elevatorHeightTolerance);
	}

	public double getElevatorMaximumPidPower() {
		return elevatorMaximumPidPower.get();
	}

	public void setElevatorMaximumPidPower(double elevatorMaximumPidPower) {
		this.elevatorMaximumPidPower.set(elevatorMaximumPidPower);
	}
	
	@Override
    public void update(Observable sender, Object arg) {
        if(sender.equals(deadbandLatch))
        {
            //Cast arg out as edgetype
            EdgeType edge = (EdgeType)arg;
            
            if (edge == EdgeType.RisingEdge)
            {
                log.info("Elevator has entered deadband. Deadband is increasing to " + elevatorDeadbandLarge.get());
                elevatorPositionDeadband.set(elevatorDeadbandLarge.get());
                log.info("Deadband is now " + elevatorPositionDeadband.get());
            }
            if ( edge == EdgeType.FallingEdge)
            {
                log.info("Elevator has exited deadband. Deadband is decreasing to " + elevatorDeadbandSmall.get());
                elevatorPositionDeadband.set(elevatorDeadbandSmall.get());
                log.info("Deadband is now " + elevatorPositionDeadband.get());
            }
        }
    }
}
