/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.fpga.tAccumulator;
import edu.wpi.first.wpilibj.util.AllocationException;
import edu.wpi.first.wpilibj.util.CheckedAllocationException;

/**
 * Analog channel class.
 *
 * Each analog channel is read from hardware as a 12-bit number representing -10V to 10V.
 *
 * Connected to each analog channel is an averaging and oversampling engine.  This engine accumulates
 * the specified ( by setAverageBits() and setOversampleBits() ) number of samples befor returning a new
 * value.  This is not a sliding window average.  The only difference between the oversampled samples and
 * the averaged samples is that the oversampled samples are simply accumulated effectively increasing the
 * resolution, while the averaged samples are divided by the number of samples to retain the resolution,
 * but get more stable values.
 */
public class AnalogChannel extends SensorBase implements PIDSource {

    private static final int kAccumulatorSlot = 1;
    private static Resource channels = new Resource(kAnalogModules * kAnalogChannels);
    private int m_channel;
    private int m_slot;
    private AnalogModule m_module;
    private static final int[] kAccumulatorChannels = {
        1, 2
    };
    private tAccumulator m_accumulator;
    private long m_accumulatorOffset;

    /**
     * Construct an analog channel on the default module.
     *
     * @param channel The channel number to represent.
     */
    public AnalogChannel(final int channel) {
        this(getDefaultAnalogModule(), channel);
    }

    /**
     * Construct an analog channel on a specified module.
     *
     * @param slot The slot that the analog module is plugged into.
     * @param channel The channel number to represent.
     */
    public AnalogChannel(final int slot, final int channel) {
        checkAnalogModule(slot);
        checkAnalogChannel(slot);
        m_channel = channel;
        m_slot = slot;
        m_module = AnalogModule.getInstance(slot);
        try {
            channels.allocate(AnalogModule.slotToIndex(slot) * kAnalogModules + m_channel - 1);
        } catch (CheckedAllocationException e) {
            throw new AllocationException(
                    "Analog channel " + m_channel + " on module " + m_slot + " is already allocated");
        }
        if (channel == 1 || channel == 2) {
            m_accumulator = new tAccumulator((byte) (channel - 1));
            m_accumulatorOffset = 0;
        } else {
            m_accumulator = null;
        }
    }

    /**
     * Channel destructor.
     */
    protected void free() {
        channels.free((AnalogModule.slotToIndex(m_slot) * kAnalogModules + m_channel - 1));
        m_channel = 0;
        m_slot = 0;
//        m_accumulator.Release();
        m_accumulator = null;
        m_accumulatorOffset = 0;
    }

    /**
     * Get the analog module that this channel is on.
     * @return The AnalogModule that this channel is on.
     */
    public AnalogModule getModule() {
        return m_module;
    }

    /**
     * Get a sample straight from this channel on the module.
     * The sample is a 12-bit value representing the -10V to 10V range of the A/D converter in the module.
     * The units are in A/D converter codes.  Use GetVoltage() to get the analog value in calibrated units.
     * @return A sample straight from this channel on the module.
     */
    public int getValue() {
        return m_module.getValue(m_channel);
    }

    /**
     * Get a sample from the output of the oversample and average engine for this channel.
     * The sample is 12-bit + the value configured in SetOversampleBits().
     * The value configured in setAverageBits() will cause this value to be averaged 2**bits number of samples.
     * This is not a sliding window.  The sample will not change until 2**(OversamplBits + AverageBits) samples
     * have been acquired from the module on this channel.
     * Use getAverageVoltage() to get the analog value in calibrated units.
     * @return A sample from the oversample and average engine for this channel.
     */
    public int getAverageValue() {
        return m_module.getAverageValue(m_channel);

    }

    /**
     * Get a scaled sample straight from this channel on the module.
     * The value is scaled to units of Volts using the calibrated scaling data from getLSBWeight() and getOffset().
     * @return A scaled sample straight from this channel on the module.
     */
    public double getVoltage() {
        return m_module.getVoltage(m_channel);
    }

    /**
     * Get a scaled sample from the output of the oversample and average engine for this channel.
     * The value is scaled to units of Volts using the calibrated scaling data from getLSBWeight() and getOffset().
     * Using oversampling will cause this value to be higher resolution, but it will update more slowly.
     * Using averaging will cause this value to be more stable, but it will update more slowly.
     * @return A scaled sample from the output of the oversample and average engine for this channel.
     */
    public double getAverageVoltage() {
        return m_module.getAverageVoltage(m_channel);
    }

    /**
     * Get the factory scaling least significant bit weight constant.
     * The least significant bit weight constant for the channel that was calibrated in
     * manufacturing and stored in an eeprom in the module.
     *
     * Volts = ((LSB_Weight * 1e-9) * raw) - (Offset * 1e-9)
     *
     * @return Least significant bit weight.
     */
    public long getLSBWeight() {
        return m_module.getLSBWeight(m_channel);
    }

    /**
     * Get the factory scaling offset constant.
     * The offset constant for the channel that was calibrated in manufacturing and stored
     * in an eeprom in the module.
     *
     * Volts = ((LSB_Weight * 1e-9) * raw) - (Offset * 1e-9)
     *
     * @return Offset constant.
     */
    public int getOffset() {
        return m_module.getOffset(m_channel);
    }

    /**
     * Get the channel number.
     * @return The channel number.
     */
    public int getChannel() {
        return m_channel;
    }

    /**
     * Get the slot that the analog module is plugged into.
     * @return The slot that the analog module is plugged into.
     */
    public int getSlot() {
        return m_module.getSlot();
    }

    /**
     * Set the number of averaging bits.
     * This sets the number of averaging bits. The actual number of averaged samples is 2**bits.
     * The averaging is done automatically in the FPGA.
     *
     * @param bits The number of averaging bits.
     */
    public void setAverageBits(final int bits) {
        m_module.setAverageBits(m_channel, bits);
    }

    /**
     * Get the number of averaging bits.
     * This gets the number of averaging bits from the FPGA. The actual number of averaged samples is 2**bits.
     * The averaging is done automatically in the FPGA.
     *
     * @return The number of averaging bits.
     */
    public int getAverageBits() {
        return m_module.getAverageBits(m_channel);
    }

    /**
     * Set the number of oversample bits.
     * This sets the number of oversample bits. The actual number of oversampled values is
     * 2**bits. The oversampling is done automatically in the FPGA.
     *
     * @param bits The number of oversample bits.
     */
    public void setOversampleBits(final int bits) {
        m_module.setOversampleBits(m_channel, bits);
    }

    /**
     * Get the number of oversample bits.
     * This gets the number of oversample bits from the FPGA. The actual number of oversampled values is
     * 2**bits. The oversampling is done automatically in the FPGA.
     *
     * @return The number of oversample bits.
     */
    public int getOversampleBits() {
        return m_module.getOversampleBits(m_channel);
    }

    /**
     * Initialize the accumulator.
     */
    public void initAccumulator() {
        if (!isAccumulatorChannel()) {
            throw new AllocationException(
                    "Accumulators are only available on slot " +
                    kAccumulatorSlot + " on channels " + kAccumulatorChannels[0] + "," + kAccumulatorChannels[1]);
        }
        m_accumulatorOffset = 0;
        setAccumulatorCenter(0);
        resetAccumulator();
    }

    /**
     * Set an inital value for the accumulator.
     *
     * This will be added to all values returned to the user.
     * @param initialValue The value that the accumulator should start from when reset.
     */
    public void setAccumulatorInitialValue(long initialValue) {
        m_accumulatorOffset = initialValue;
    }

    /**
     * Resets the accumulator to the initial value.
     */
    public void resetAccumulator() {
        m_accumulator.strobeReset();
    }

    /**
     * Set the center value of the accumulator.
     *
     * The center value is subtracted from each A/D value before it is added to the accumulator. This
     * is used for the center value of devices like gyros and accelerometers to make integration work
     * and to take the device offset into account when integrating.
     *
     * This center value is based on the output of the oversampled and averaged source from channel 1.
     * Because of this, any non-zero oversample bits will affect the size of the value for this field.
     */
    public void setAccumulatorCenter(int center) {
        m_accumulator.writeCenter(center);
    }

    /**
     * Set the accumulator's deadband.
     */
    public void setAccumulatorDeadband(int deadband) {
        m_accumulator.writeDeadband(deadband);
    }

    /**
     * Read the accumulated value.
     *
     * Read the value that has been accumulating on channel 1.
     * The accumulator is attached after the oversample and average engine.
     *
     * @return The 64-bit value accumulated since the last Reset().
     */
    public long getAccumulatorValue() {
        long value = m_accumulator.readOutput_Value() + m_accumulatorOffset;
        return value;
    }

    /**
     * Read the number of accumulated values.
     *
     * Read the count of the accumulated values since the accumulator was last Reset().
     *
     * @return The number of times samples from the channel were accumulated.
     */
    public long getAccumulatorCount() {
        long count = m_accumulator.readOutput_Count();
        return count;
    }

    /**
     * Read the accumulated value and the number of accumulated values atomically.
     *
     * This function reads the value and count from the FPGA atomically.
     * This can be used for averaging.
     *
     * @param result AccumulatorResult object to store the results in.
     */
    public void getAccumulatorOutput(AccumulatorResult result) {
        if (result == null) {
            System.out.println("Null result in getAccumulatorOutput");
        }
        if (m_accumulator == null) {
            System.out.println("Null m_accumulator in getAccumulatorOutput");
        }
        result.value = m_accumulator.readOutput_Value() + m_accumulatorOffset;
        result.count = m_accumulator.readOutput_Count();
    }

    /**
     * Is the channel attached to an accumulator.
     *
     * @return The analog channel is attached to an accumulator.
     */
    public boolean isAccumulatorChannel() {
        if (m_module.getSlot() != kAccumulatorSlot) {
            return false;
        }
        for (int i = 0; i < kAccumulatorChannels.length; i++) {
            if (m_channel == kAccumulatorChannels[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the average value for usee with PIDController
     * @return the average value
     */
    public double pidGet() {
        return getAverageValue();
    }
}
