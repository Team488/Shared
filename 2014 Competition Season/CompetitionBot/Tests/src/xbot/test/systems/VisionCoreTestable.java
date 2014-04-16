/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.test.systems;

import xbot.aerialassist.systems.VisionCore;

/**
 *
 * @author John
 */
public class VisionCoreTestable extends VisionCore {
    
    public void SetImageHeight(double height)
    {
        this.imageHeight.set(height);
    }
    
    public void SetImageWidth(double width)
    {
        this.imageWidth.set(width);
    }
    
    public void SetBallX(double x)
    {
        this.ballX.set(x);
    }
    
    public void SetBallY(double y)
    {
        this.ballY.set(y);
    }
    
    public void SetBlobcount(double count)
    {
        this.blobCount.set(count);
    }
    
}
