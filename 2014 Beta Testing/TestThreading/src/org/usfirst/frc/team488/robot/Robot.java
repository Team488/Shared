
package org.usfirst.frc.team488.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

import java.lang.*;
import java.io.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
	double startTime;
	
	public Robot()
	{
		super();
		startTime = Timer.getFPGATimestamp() * 1000;
	}
	
    private double getMSFromStart() {
        return (Timer.getFPGATimestamp() * 1000) - startTime;
    }    
	
	Thread a = new Thread();
	
	class DoWhile implements Runnable
	{	
		private int num;
		public DoWhile(int num)
		{
			this.num = num;
		}
		
		@Override
		public void run() {
			
			double before = getMSFromStart();
			
			int count = 1;
			while (true)
			{
				count++;
				if (count % 100000000 == 0)
				{
					double after = getMSFromStart();
					double duration = after - before;
					System.out.println("DoWhile" + num + " - 100,000,000 counts in " + duration + "msec");
					before = getMSFromStart();
					count = 1;
				}
			}
		}
	}
	
	class CountOneHz implements Runnable
	{
		int count = 0;
		int num;
		
		public CountOneHz(int num)
		{
			this.num = num;
		}

		@Override
		public void run() {
			while (true)
			{
				double before = getMSFromStart();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				double after = getMSFromStart();
				
				double duration = after-before;
				
				System.out.println("Count1Hz" + num + " - Count: " + count + ", Duration: " + duration);
				count++;
			}
			
		}
		
	}
	
	class HeavyRead implements Runnable
	{
		int num;
		public HeavyRead(int num)
		{
			this.num = num;
		}

		@Override
		public void run() {
			
			while (true)
			{
				double before = getMSFromStart();
				
				// Read a large
				File largeFile = new File("/XbotData/test.txt");
				InputStreamReader reader = null;
				try {
					reader = new InputStreamReader(new FileInputStream(largeFile));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				long fileSize = largeFile.length();
				char[] buffer = new char[(int)fileSize];
				try {
					reader.read(buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				double after = getMSFromStart();
				double duration = after-before;
				
				System.out.println("HeavyRead" + num + " - Took " + duration + " msec to read file.");
			}
		}
		
	}
	
    public void robotInit() {
    	//TestHeavyAndLight();
    	//TestHeavyAndHeavy();
    	//Test3Heavies();
    	TestIOandLight();
    	
    }
    
    private void TestHeavyAndLight()
    {
    	Thread a = new Thread(new DoWhile(1));
    	Thread b = new Thread(new CountOneHz(1));
    	
    	a.start();
    	b.start();
    }
    
    private void TestHeavyAndHeavy()
    {
    	Thread a = new Thread(new DoWhile(1));
    	Thread b = new Thread(new DoWhile(2));
    	
    	a.start();
    	b.start();
    }
    
    private void Test3Heavies()
    {
    	Thread a = new Thread(new DoWhile(1));
    	Thread b = new Thread(new DoWhile(2));
    	Thread c = new Thread(new DoWhile(3));
    	
    	a.start();
    	b.start();
    	c.start();
    }
    
    private void TestIOandLight()
    {
    	Thread a = new Thread(new HeavyRead(1));
    	Thread b = new Thread(new CountOneHz(1));
    	
    	a.start();
    	b.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
