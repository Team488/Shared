/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.core.systems;

/**
 *
 * @author Alex
 */
public class BucketCore {
    
    
    //<editor-fold defaultstate="collapsed" desc="Device values">
    private boolean requestSolenoidUp;
    
    /**
     * Determines if the solenoid should be set to raise the bucket (true)
     * or lower (false)
     *
     * @return the value of requestSolenoidUp
     */
    public boolean getRequestSolenoidUp() {
        return requestSolenoidUp;
    }
    
        private boolean deviceBucketUpLimitSwitchPressed;

    /**
     * Get the value of deviceBucketUpLimitSwitchPressed
     *
     * @return the value of deviceBucketUpLimitSwitchPressed
     */
    protected boolean isDeviceBucketUpLimitSwitchPressed() {
        return deviceBucketUpLimitSwitchPressed;
    }

    /**
     * Set the value of deviceBucketUpLimitSwitchPressed
     *
     * @param deviceBucketUpLimitSwitchPressed new value of
     * deviceBucketUpLimitSwitchPressed
     */
    public void setDeviceBucketUpLimitSwitchPressed(boolean deviceBucketUpLimitSwitchIsPressed) {
        this.deviceBucketUpLimitSwitchPressed = deviceBucketUpLimitSwitchIsPressed;
    }
    private boolean deviceBucketDownLimitSwitchPressed;

    /**
     * Get the value of deviceBucketDownLimitSwitchPressed
     *
     * @return the value of deviceBucketDownLimitSwitchPressed
     */
    protected boolean isDeviceBucketDownLimitSwitchPressed() {
        return deviceBucketDownLimitSwitchPressed;
    }

    /**
     * Set the value of deviceBucketDownLimitSwitchPressed
     *
     * @param deviceBucketDownLimitSwitchPressed new value of
     * deviceBucketDownLimitSwitchPressed
     */
    public void setDeviceBucketDownLimitSwitchPressed(boolean deviceBucketDownLimitSwitchIsPressed) {
        this.deviceBucketDownLimitSwitchPressed = deviceBucketDownLimitSwitchIsPressed;
    }
    //</editor-fold>

    //<editor-fold desc="Public methods">
    public void setBucketUp() {
        requestSolenoidUp = true;
    }
    
    public void setBucketDown() {
        requestSolenoidUp = false;
    }
    
    public boolean isBucketDefinitelyUp() {
        return isDeviceBucketUpLimitSwitchPressed();
    }
    
    public boolean isBucketDefinitelyDown() {
        return isDeviceBucketDownLimitSwitchPressed();
    }
    //</editor-fold>

}
