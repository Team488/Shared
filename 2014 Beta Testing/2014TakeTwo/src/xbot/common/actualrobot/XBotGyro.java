/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.util.BoundaryException;

/**
 * Use a rate gyro to return the robots heading relative to a starting position.
 * The Gyro class tracks the robots heading based on the starting position. As the robot
 * rotates the new heading is computed by integrating the rate of rotation returned
 * by the sensor. When the class is instantiated, it does a short calibration routine
 * where it samples the gyro while at rest to determine the default offset. This is
 * subtracted from each sample to determine the heading.
 */
public class XBotGyro extends SensorBase implements LiveWindowSendable {
    static final int kOversampleBits = 10;
    static final int kAverageBits = 0;
    static final double kSamplesPerSecond = 50.0;
    static final double kCalibrationSampleTime = 5.0;
    AnalogInput m_analog;
    double m_voltsPerDegreePerSecond;
    double m_offset;
    int m_center;
    boolean m_channelAllocated;
    AccumulatorResult result;
    private PIDSource.PIDSourceParameter m_pidSource;

    /**
     * Initialize the gyro.
     * Calibrate the gyro by running for a number of samples and computing the center value for this
     * part. Then use the center value as the Accumulator center value for subsequent measurements.
     * It's important to make sure that the robot is not moving while the centering calculations are
     * in progress, this is typically done when the robot is first turned on while it's sitting at
     * rest before the competition starts.
     */
    private void initGyro() {
        result = new AccumulatorResult();
        if (m_analog == null) {
            System.out.println("Null m_analog");
        }
        m_analog.setAverageBits(kAverageBits);
        m_analog.setOversampleBits(kOversampleBits);
        double sampleRate = kSamplesPerSecond * (1 << (kAverageBits + kOversampleBits));
        AnalogInput.setGlobalSampleRate(sampleRate);

        Timer.delay(1.0);
        m_analog.initAccumulator();

        Timer.delay(kCalibrationSampleTime);

        m_analog.getAccumulatorOutput(result);

        m_center = (int) ((double)result.value / (double)result.count + .5);

        m_offset = ((double)result.value / (double)result.count) - (double)m_center;

        m_analog.setAccumulatorCenter(m_center);

        m_analog.setAccumulatorDeadband(0); ///< TODO: compute / parameterize this
        m_analog.resetAccumulator();
        
        setPIDSourceParameter(PIDSource.PIDSourceParameter.kAngle);

        //UsageReporting.report(UsageReporting.kResourceType_Gyro, m_analog.getChannel());
        LiveWindow.addSensor("Gyro", m_analog.getChannel(), this);
    }

    /**
     * Gyro constructor with only a channel.
     *
     * Use the default analog module slot.
     *
     * @param channel The analog channel the gyro is connected to.
     */
    public XBotGyro(int channel, double volts) {
        m_voltsPerDegreePerSecond = volts;
        m_analog = new AnalogInput(channel);
        m_channelAllocated = true;
        initGyro();
    }

    /**
     * Gyro constructor with a precreated analog channel object.
     * Use this constructor when the analog channel needs to be shared. There
     * is no reference counting when an AnalogChannel is passed to the gyro.
     * @param channel The AnalogChannel object that the gyro is connected to.
     */
    public XBotGyro(AnalogInput channel, double volts) {
        m_voltsPerDegreePerSecond = volts;
        m_analog = channel;
        if (m_analog == null) {
            System.err.println("Analog channel supplied to Gyro constructor is null");
        } else {
            m_channelAllocated = false;
            initGyro();
        }
    }

    /**
     * Reset the gyro.
     * Resets the gyro to a heading of zero. This can be used if there is significant
     * drift in the gyro and it needs to be recalibrated after it has been running.
     */
    public void reset() {
        if (m_analog != null) {
            m_analog.resetAccumulator();
        }
    }

    /**
     * Delete (free) the accumulator and the analog components used for the gyro.
     */
    public void free() {
        if (m_analog != null && m_channelAllocated) {
            m_analog.free();
        }
        m_analog = null;
    }

    /**
     * Return the actual angle in degrees that the robot is currently facing.
     *
     * The angle is based on the current accumulator value corrected by the oversampling rate, the
     * gyro type and the A/D calibration values.
     * The angle is continuous, that is can go beyond 360 degrees. This make algorithms that wouldn't
     * want to see a discontinuity in the gyro output as it sweeps past 0 on the second time around.
     *
     * @return the current heading of the robot in degrees. This heading is based on integration
     * of the returned rate from the gyro.
     */
    public double getAngle() {
        if (m_analog == null) {
            return 0.0;
        } else {
            m_analog.getAccumulatorOutput(result);

            long value = result.value - (long) (result.count * m_offset);

            double scaledValue = value * 1e-9 * m_analog.getLSBWeight() * (1 << m_analog.getAverageBits()) /
                    (AnalogInput.getGlobalSampleRate() * m_voltsPerDegreePerSecond);

            return scaledValue;
        }
    }
    
    /**
     * Return the rate of rotation of the gyro
     * 
     * The rate is based on the most recent reading of the gyro analog value
     * 
     * @return the current rate in degrees per second
     */
    public double getRate() {      
        if(m_analog == null) {
            return 0.0;
        } else {
            return (m_analog.getAverageValue() - ((double)m_center + m_offset)) * 1e-9 * m_analog.getLSBWeight() 
                    / ((1 << m_analog.getOversampleBits()) * m_voltsPerDegreePerSecond);
        }
    }

    /**
     * Set the gyro type based on the sensitivity.
     * This takes the number of volts/degree/second sensitivity of the gyro and uses it in subsequent
     * calculations to allow the code to work with multiple gyros.
     *
     * @param voltsPerDegreePerSecond The type of gyro specified as the voltage that represents one degree/second.
     */
    public void setSensitivity(double voltsPerDegreePerSecond) {
        m_voltsPerDegreePerSecond = voltsPerDegreePerSecond;
    }
    
    /**
     * Set which parameter of the encoder you are using as a process control variable.
     * The Gyro class supports the rate and angle parameters
     * @param pidSource An enum to select the parameter.
     */
    public void setPIDSourceParameter(PIDSource.PIDSourceParameter pidSource) {
	BoundaryException.assertWithinBounds(pidSource.value, 1, 2);
        m_pidSource = pidSource;
    }

    /*
     * Live Window code, only does anything if live window is activated.
     */
    public String getSmartDashboardType(){
        return "Gyro";
    }
    private ITable m_table;
    
    /**
     * {@inheritDoc}
     */
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }
    
    /**
     * {@inheritDoc}
     */
    public ITable getTable(){
        return m_table;
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateTable() {
        if (m_table != null) {
            m_table.putNumber("Value", getAngle());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void startLiveWindowMode() {}
    
    /**
     * {@inheritDoc}
     */
    public void stopLiveWindowMode() {}
}
