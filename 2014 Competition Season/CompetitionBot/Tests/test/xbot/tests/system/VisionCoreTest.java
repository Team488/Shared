/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import xbot.test.common.BaseTest;
import xbot.test.systems.VisionCoreTestable;
import static org.junit.Assert.*;

/**
 *
 * @author John
 */
public class VisionCoreTest extends BaseTest {
    
    private VisionCoreTestable testableVision;
    
    public VisionCoreTest() {
        
    }
    
    @Before
    public void setUp() {
        testableVision = new VisionCoreTestable();
    }
    
    @After
    public void tearDown() {
    }
     
     @Test
     public void TestBallMiddle() {
         testableVision.SetImageWidth(200);
         testableVision.SetImageHeight(120);
         testableVision.SetBallX(100);
         testableVision.SetBallY(60);
         testableVision.SetBlobcount(1);
         
         checkResult(0, 0);
     }
     
     @Test
     public void TestBallTop()
     {
         testableVision.SetImageWidth(200);
         testableVision.SetImageHeight(120);
         testableVision.SetBallX(100);
         testableVision.SetBallY(120);
         testableVision.SetBlobcount(1);
         
         checkResult(0, -1);
     }
     
     @Test
     public void TestBallRight()
     {
        testableVision.SetImageWidth(200);
         testableVision.SetImageHeight(120);
         testableVision.SetBallX(200);
         testableVision.SetBallY(60);
         testableVision.SetBlobcount(1);
         
         checkResult(1, 0); 
     }
     
     @Test
     public void TestBallLostTop() throws InterruptedException
     {
         testableVision.SetImageWidth(200);
         testableVision.SetImageHeight(120);
         testableVision.SetBallX(100);
         testableVision.SetBallY(120);
         testableVision.SetBlobcount(1);
         
         checkResult(0, -1);
         
         testableVision.SetBlobcount(0);
         checkResult(0, -1);
         
         mockTime.incrementTime(100);
         checkResult(0, -1);
         
         mockTime.incrementTime(1000);
         checkResult(0, 0);
     }
     
     private void checkResult(double expectedX, double expectedY)
     {
         double[] result = testableVision.GetBallDisplacementFromCenter();
         assertEquals("X displacement", expectedX, result[0], 0.001);
         assertEquals("Y displacement", expectedY, result[1], 0.001);
     }
}
