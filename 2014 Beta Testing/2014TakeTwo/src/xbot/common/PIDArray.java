/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common;

/**
 *
 * @author Alex
 */
public class PIDArray {
    
    private PID[] pidArray;
    
    /**
     *
     * @param size
     */
    public PIDArray(int size) {
        pidArray = new PID[size];
        for(int j = 0; j < size; j++) {
            pidArray[j] = new PID();
        }
    }
    
    /**
     * Default dimension count of 2 (common for x,y pairs)
     */
    public PIDArray() {
        int size = 2;
        pidArray = new PID[size];
        for(int j = 0; j < size; j++) {
            pidArray[j] = new PID();
        }
    }
    
    /**
     *
     */
    public void reset() {
        for(int j = 0; j < pidArray.length; j++) {
            pidArray[j].reset();
        }
    }
    
    /**
     *
     * @param sensorGoal
     * @param sensorCurrent
     * @param p
     * @param i
     * @param d
     * @return
     */
    public double[] calculate(double[] sensorGoal, double[] sensorCurrent, 
            double p, double i, double d) {
        if(sensorGoal.length != pidArray.length || sensorCurrent.length != pidArray.length) {
            throw new RuntimeException("Length of array inputs must match size passed to constructor");
        }
        double[] result = new double[pidArray.length];
        
        for(int j = 0; j < pidArray.length; j++) {
            result[j] = pidArray[j].calculate(sensorGoal[j], sensorCurrent[j], p, d, d);
        }
        
        return result;
    }
    
    /**
     *
     * @param absoluteTolerance
     * @return
     */
    public boolean isOnTarget(double absoluteTolerance) {
        boolean result = true;
        for(int j = 0; j < pidArray.length; j++) {
            result = result && pidArray[j].isOnTarget(absoluteTolerance);
        }
        return result;
    }
}
