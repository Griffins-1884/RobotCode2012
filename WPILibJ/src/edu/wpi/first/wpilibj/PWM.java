/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.fpga.tDIO;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * Class implements the PWM generation in the FPGA.
 * Values supplied as arguments for PWM outputs range from -1.0 to 1.0. They are mapped
 * to the hardware dependent values, in this case 0-255 for the FPGA.
 * Changes are immediately sent to the FPGA, and the update occurs at the next
 * FPGA cycle. There is no delay.
 *
 * As of revision 0.1.4 of the FPGA, the FPGA interprets the 0-255 values as follows:
 *   255 = full "forward"
 *   254 to 129 = linear scaling from "full forward" to "center"
 *   128 = center value
 *   127 to 2 = linear scaling from "center" to "full reverse"
 *   1 = full "reverse"
 *   0 = disabled (i.e. PWM output is held low)
 */
public class PWM extends SensorBase {

    private static Resource allocated = new Resource((tDIO.kNumSystems * kPwmChannels));

    /**
     * Represents the amount to multiply the minimum servo-pulse pwm period by.
     */
    public static class PeriodMultiplier {

        /**
         * The integer value representing this enumeration
         */
        public final int value;
        static final int k1X_val = 1;
        static final int k2X_val = 2;
        static final int k4X_val = 4;
        /**
         * Period Multiplier: don't skip pulses
         */
        public static final PeriodMultiplier k1X = new PeriodMultiplier(k1X_val);
        /**
         * Period Multiplier: skip every other pulse
         */
        public static final PeriodMultiplier k2X = new PeriodMultiplier(k2X_val);
        /**
         * Period Multiplier: skip three out of four pulses
         */
        public static final PeriodMultiplier k4X = new PeriodMultiplier(k4X_val);

        private PeriodMultiplier(int value) {
            this.value = value;
        }
    }
    private int m_channel;
    private DigitalModule m_module;
    /**
     * kDefaultPwmPeriod is "ticks" where each tick is 6.525us
     *
     * - 20ms periods (50 Hz) are the "safest" setting in that this works for all devices
     * - 20ms periods seem to be desirable for Vex Motors
     * - 20ms periods are the specified period for HS-322HD servos, but work reliably down
     *      to 10.0 ms; starting at about 8.5ms, the servo sometimes hums and get hot;
     *      by 5.0ms the hum is nearly continuous
     * - 10ms periods work well for Victor 884
     * - 5ms periods allows higher update rates for Luminary Micro Jaguar speed controllers.
     *      Due to the shipping firmware on the Jaguar, we can't run the update period less
     *      than 5.05 ms.
     *
     * kDefaultPwmPeriod is the 1x period (5.05 ms).  In hardware, the period scaling is implemented as an
     * output squelch to get longer periods for old devices.
     *
     * Set to 5.05 ms period / 6.525us clock = 774
     */
    protected static final int kDefaultPwmPeriod = 774;
    /**
     * kDefaultMinPwmHigh is "ticks" where each tick is 6.525us
     *
     * - There are 128 pwm values less than the center, so...
     * - The minimum output pulse length is 1.5ms - 128 * 6.525us = 0.665ms
     * - 0.665ms / 6.525us per tick = 102
     */
    protected static final int kDefaultMinPwmHigh = 102;
    public static final int kPwmDisabled = 0;
    boolean m_eliminateDeadband;
    int m_maxPwm;
    int m_deadbandMaxPwm;
    int m_centerPwm;
    int m_deadbandMinPwm;
    int m_minPwm;

    /**
     * Initialize PWMs given an module and channel.
     *
     * This method is private and is the common path for all the constructors for creating PWM
     * instances. Checks module and channel value ranges and allocates the appropriate channel.
     * The allocation is only done to help users ensure that they don't double assign channels.
     */
    private void initPWM(final int slot, final int channel) {
        checkPWMModule(slot);
        checkPWMChannel(channel);
        try {
            allocated.allocate(DigitalModule.slotToIndex(slot) * kPwmChannels + channel - 1);
        } catch (CheckedAllocationException e) {
            throw new AllocationException(
                    "PWM channel " + channel + " on module " + slot + " is already allocated");
        }
        m_channel = channel;
        m_module = DigitalModule.getInstance(slot);
        m_module.setPWM(m_channel, kPwmDisabled);
        m_eliminateDeadband = false;
    }

    /**
     * Allocate a PWM given a module and channel.
     * Allocate a PWM using a module and channel number.
     *
     * @param slot The slot the digital module is plugged into.
     * @param channel The PWM channel on the digital module.
     */
    public PWM(final int slot, final int channel) {
        initPWM(slot, channel);
    }

    /**
     * Allocate a PWM in the default module given a channel.
     *
     * Using a default module allocate a PWM given the channel number.  The default module is the first
     * slot numerically in the cRIO chassis.
     *
     * @param channel The PWM channel on the digital module.
     */
    public PWM(final int channel) {
        initPWM(getDefaultDigitalModule(), channel);
    }

    /**
     * Free the PWM channel.
     *
     * Free the resource associated with the PWM channel and set the value to 0.
     */
    protected void free() {
        m_module.setPWM(m_channel, kPwmDisabled);
        allocated.free(DigitalModule.slotToIndex(m_module.getSlot()) * kPwmChannels + m_channel - 1);
    }

    /**
     * Optionally eliminate the deadband from a speed controller.
     * @param eliminateDeadband If true, set the motor curve on the Jaguar to eliminate
     * the deadband in the middle of the range. Otherwise, keep the full range without
     * modifying any values.
     */
    public void enableDeadbandElimination(boolean eliminateDeadband) {
        m_eliminateDeadband = eliminateDeadband;
    }

    /**
     * Set the bounds on the PWM values.
     * This sets the bounds on the PWM values for a particular each type of controller. The values
     * determine the upper and lower speeds as well as the deadband bracket.
     * @param max The Minimum pwm value
     * @param deadbandMax The high end of the deadband range
     * @param center The center speed (off)
     * @param deadbandMin The low end of the deadband range
     * @param min The minimum pwm value
     */
    public void setBounds(final int max, final int deadbandMax, final int center, final int deadbandMin, final int min) {
        m_maxPwm = max;
        m_deadbandMaxPwm = deadbandMax;
        m_centerPwm = center;
        m_deadbandMinPwm = deadbandMin;
        m_minPwm = min;
    }

    /**
     * Set the PWM value based on a position.
     *
     * This is intended to be used by servos.
     *
     * @pre SetMaxPositivePwm() called.
     * @pre SetMinNegativePwm() called.
     *
     * @param pos The position to set the servo between 0.0 and 1.0.
     */
    public void setPosition(double pos) {
        if (pos < 0.0) {
            pos = 0.0;
        } else if (pos > 1.0) {
            pos = 1.0;
        }

        int rawValue;
        // note, need to perform the multiplication below as floating point before converting to int
        rawValue = (int) ((pos * (double)getFullRangeScaleFactor()) + getMinNegativePwm());

        // send the computed pwm value to the FPGA
        setRaw(rawValue);
    }

    /**
     * Get the PWM value in terms of a position.
     *
     * This is intended to be used by servos.
     *
     * @pre SetMaxPositivePwm() called.
     * @pre SetMinNegativePwm() called.
     *
     * @return The position the servo is set to between 0.0 and 1.0.
     */
    public double getPosition() {
        int value = getRaw();
        if (value < getMinNegativePwm()) {
            return 0.0;
        } else if (value > getMaxPositivePwm()) {
            return 1.0;
        } else {
            return (double)(value - getMinNegativePwm()) / (double)getFullRangeScaleFactor();
        }
    }

    /**
     * Set the PWM value based on a speed.
     *
     * This is intended to be used by speed controllers.
     *
     * @pre SetMaxPositivePwm() called.
     * @pre SetMinPositivePwm() called.
     * @pre SetCenterPwm() called.
     * @pre SetMaxNegativePwm() called.
     * @pre SetMinNegativePwm() called.
     *
     * @param speed The speed to set the speed controller between -1.0 and 1.0.
     */
    final void setSpeed(double speed) {
        // clamp speed to be in the range 1.0 >= speed >= -1.0
        if (speed < -1.0) {
            speed = -1.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }

        // calculate the desired output pwm value by scaling the speed appropriately
        int rawValue;
        if (speed == 0.0) {
            rawValue = getCenterPwm();
        } else if (speed > 0.0) {
            rawValue = (int) (speed * ((double)getPositiveScaleFactor()) +
                              ((double)getMinPositivePwm()) + 0.5);
        } else {
            rawValue = (int) (speed * ((double)getNegativeScaleFactor()) +
                              ((double)getMaxNegativePwm()) + 0.5);
        }

        // send the computed pwm value to the FPGA
        setRaw(rawValue);
    }

    /**
     * Get the PWM value in terms of speed.
     *
     * This is intended to be used by speed controllers.
     *
     * @pre SetMaxPositivePwm() called.
     * @pre SetMinPositivePwm() called.
     * @pre SetMaxNegativePwm() called.
     * @pre SetMinNegativePwm() called.
     *
     * @return The most recently set speed between -1.0 and 1.0.
     */
    public double getSpeed() {
        int value = getRaw();
        if (value > getMaxPositivePwm()) {
            return 1.0;
        } else if (value < getMinNegativePwm()) {
            return -1.0;
        } else if (value > getMinPositivePwm()) {
            return (double) (value - getMinPositivePwm()) / (double)getPositiveScaleFactor();
        } else if (value < getMaxNegativePwm()) {
            return (double) (value - getMaxNegativePwm()) / (double)getNegativeScaleFactor();
        } else {
            return 0.0;
        }
    }

    /**
     * Set the PWM value directly to the hardware.
     *
     * Write a raw value to a PWM channel.
     *
     * @param value Raw PWM value.  Range 0 - 255.
     */
    public void setRaw(int value) {
        m_module.setPWM(m_channel, value);
    }

    /**
     * Get the PWM value directly from the hardware.
     *
     * Read a raw value from a PWM channel.
     *
     * @return Raw PWM control value.  Range: 0 - 255.
     */
    public int getRaw() {
        return m_module.getPWM(m_channel);
    }

    /**
     * Slow down the PWM signal for old devices.
     *
     * @param mult The period multiplier to apply to this channel
     */
    public void setPeriodMultiplier(PeriodMultiplier mult) {
        switch (mult.value) {
            case PeriodMultiplier.k4X_val:
                m_module.setPWMPeriodScale(m_channel, 3); // Squelch 3 out of 4 outputs
                break;
            case PeriodMultiplier.k2X_val:
                m_module.setPWMPeriodScale(m_channel, 1); // Squelch 1 out of 2 outputs
                break;
            case PeriodMultiplier.k1X_val:
                m_module.setPWMPeriodScale(m_channel, 0); // Don't squelch any outputs
                break;
            default:
            //Cannot hit this, limited by PeriodMultiplier enum
        }
    }

    private int getMaxPositivePwm() {
        return m_maxPwm;
    }

    ;

    private int getMinPositivePwm() {
        return m_eliminateDeadband ? m_deadbandMaxPwm : m_centerPwm + 1;
    }

    ;

    private int getCenterPwm() {
        return m_centerPwm;
    }

    ;

    private int getMaxNegativePwm() {
        return m_eliminateDeadband ? m_deadbandMinPwm : m_centerPwm - 1;
    }

    ;

    private int getMinNegativePwm() {
        return m_minPwm;
    }

    ;

    private int getPositiveScaleFactor() {
        return getMaxPositivePwm() - getMinPositivePwm();
    } ///< The scale for positive speeds.

    private int getNegativeScaleFactor() {
        return getMaxNegativePwm() - getMinNegativePwm();
    } ///< The scale for negative speeds.

    private int getFullRangeScaleFactor() {
        return getMaxPositivePwm() - getMinNegativePwm();
    } ///< The scale for positions.
}
