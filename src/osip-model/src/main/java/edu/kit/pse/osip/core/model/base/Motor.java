package edu.kit.pse.osip.core.model.base;

import edu.kit.pse.osip.core.SimulationConstants;
import java.util.Observable;

/**
 * This is the motor which is installed in the mixtank.
 * 
 * @author David Kahles
 * @version 1.0
 */
public class Motor extends Observable {
    /**
     * The current rpm.
     */
    private int rpm;
    /**
     * The initial rpm.
     */
    private int initialRpm;

    /**
     * Constructs a new Motor.
     * 
     * @param initialRpm The initial motor speed which is also set when the motor gets reset.
     */
    public Motor(int initialRpm) {
        this.initialRpm = initialRpm;
    }

    /**
     * Sets the RPM of the motor.
     * 
     * @throws IllegalArgumentException if rpm is negative or greater than SimulationConstants.MAX_MOTOR_SPEED.
     * @param rpm The target RPM.
     */
    public synchronized void setRPM(int rpm) {
        if (rpm < 0 || rpm > SimulationConstants.MAX_MOTOR_SPEED) {
            throw new IllegalArgumentException("Motor RPM needs to be in range 0 to 3000");
        }
        this.rpm = rpm;
        setChanged();
        notifyObservers();
    }
    /**
     * Gets the RPM.
     * 
     * @return the RPM.
     */
    public int getRPM() {
        return rpm;
    }

    /**
     * Resets the Motor to @see initialRpm.
     */
    public synchronized void reset() {
        setRPM(initialRpm);
    }
}
