/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.systems;

import xbot.common.CommonTools;
import xbot.common.Loggable;
import xbot.common.properties.DoubleProperty;

/**
 * This system reads vision data from the network tables (presumably coming from Roborealm) and formats
 * it for use by workers.
 * @author John
 */
public class VisionCore extends Loggable {
    
    /**
     *
     */
    protected DoubleProperty ballX = new DoubleProperty("COG_X", 0.0);
    /**
     *
     */
    protected DoubleProperty ballY = new DoubleProperty("COG_Y", 0.0);
    /**
     *
     */
    protected DoubleProperty imageHeight = new DoubleProperty("IMAGE_HEIGHT", 1.0);
    /**
     *
     */
    protected DoubleProperty imageWidth = new DoubleProperty("IMAGE_WIDTH", 2.0);
    /**
     *
     */
    public DoubleProperty blobCount = new DoubleProperty("BLOB_COUNT", 0.0);
    /**
     *
     */
    protected DoubleProperty area = new DoubleProperty("AREA", 0.0);
    /**
     * 
     */
    protected DoubleProperty prayTime = new DoubleProperty("PrayTime", .33);
    
    /**
     * 
     */
    protected DoubleProperty minRedArea = new DoubleProperty("MinRedAreaInPercent", 10.0);
    
    /**
     *
     */
    protected double lastTimeWeSawABall = 0;
    
    /**
     *
     */
    public VisionCore()
    {
        super("VisionCore");
    }
    
    /**
     * Calculates how far, relatively, the ball is from the center of the image.
     * @return  [-1..1]. First is X, second is Y.
     */
    public double[] GetBallDisplacementFromCenter()
    {
        // If there is no ball, don't freak out
        if (blobCount.get() == 0)
        {
            Important("Robot does not see a ball");
            double timeSinceBall = CommonTools.Get().time.GetElapsedMSecFromMarker(lastTimeWeSawABall);
            
            if (timeSinceBall > (prayTime.get() * 1000))
            {
                return new double[] { 0, 0} ;
            }
            
        }
        
        lastTimeWeSawABall = CommonTools.Get().time.GetMarker();
        
        double[] midXandY = GetImageMiddle();
        double[] ballXandY = GetBallCoordinate();
        
        // Now, figure out how far the ball is displaced. For now, we will assume a ball coming in from
        // above is the normal way that a ball will arrive.
        
        // Positive X displacement means you need to go right
        double xDisplacement = ballXandY[0] - midXandY[0];
        
        // Positive Y displacement means you need to... go backwards?
        // I guess it should be negated in order to match up with everything else.
        double yDisplacement = -(ballXandY[1] - midXandY[1]);
        
        // In theory, we may not want to go backwards. Here's some optional code
        // to try and not go the wrong way.
        /*if (yDisplacement < 0)
        {
            yDisplacement = 0;
        }*/
        
        // displacement needs to be scaled to a -1, 1 environment
        double scaledXdisplacement = xDisplacement / midXandY[0];
        double scaledYdisplacement = yDisplacement / midXandY[1];
        
        return new double[] { scaledXdisplacement, scaledYdisplacement};        
    }
    
    private double[] GetImageMiddle()
    {
        if ((imageWidth.get() <= 0 ) || (imageHeight.get() <= 0))
        {
            Warning("ImageWidth and ImageHeight were: " 
                    + imageWidth.get() + " and " + imageHeight.get() 
                    +  " respectively. These are not good values.");
            
            return new double[] { 1, 1};
        }
        
        double[] midXandY = new double[2];
        midXandY[0] = imageWidth.get() / 2;
        midXandY[1] = imageHeight.get() / 2;
        
        return midXandY;
    }
    
    private double[] GetBallCoordinate()
    {
        if ((ballX.get() <= 0 ) || (ballY.get() <= 0))
        {            
            Warning("ballX and ballY were: " 
                    + ballX.get() + " and " + ballY.get() 
                    +  " respectively. These are not good values.");
            
            return new double[] { 1, 1};
        }
        
        double[] ballXandY = new double[2];
        ballXandY[0] = ballX.get();
        ballXandY[1] = ballY.get();
        
        return ballXandY;
    }
    
    public boolean isGoalHot()
    {
        if (blobCount.get() >= 1)
        {
            return true;
        }
        return false;
    }
    
    public boolean isRedDetected()
    {
        double percentRed = getPercentOfAreaRed();
        double minArea = minRedArea.get();
        
        if (percentRed >= minArea)
        {
            return true;
        }
        return false;
    }
    
    public double getTotalPixels()
    {
        return imageWidth.get() * imageHeight.get();
    }
    
    public double getPercentOfAreaRed()
    {
        double totalPixels = getTotalPixels();
        
        if (totalPixels > 1)
        {
            return (area.get() / totalPixels) * 100;
        }
        {
            return 0;
        }
    }
    
    public void SET_RED_AREA_TEST_ONLY(double percent)
    {
        imageHeight.set(10);
        imageWidth.set(10);
        area.set(percent);
    }
    
    public void SET_DETECT_HOT_TEST_ONLY(boolean value)
    {
        if (value)
        {
            SET_RED_AREA_TEST_ONLY(minRedArea.get() + 1);
            blobCount.set(1);
        }
        else
        {
            SET_RED_AREA_TEST_ONLY(minRedArea.get() - 1);
            blobCount.set(0);
        }
    }
    
}
