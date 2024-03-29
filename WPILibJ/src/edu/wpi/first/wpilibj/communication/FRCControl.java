/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.communication;

import com.sun.cldc.jna.Function;
import com.sun.cldc.jna.BlockingFunction;
import com.sun.cldc.jna.Pointer;
import com.sun.cldc.jna.NativeLibrary;
import com.sun.cldc.jna.Structure;
import com.sun.cldc.jna.TaskExecutor;

/**
 * Contains the code necessary to communicate between the robot and the driver station.
 */
public final class FRCControl {

    private static final TaskExecutor taskExecutor = new TaskExecutor("FRCControl Task executor");

    //	int getCommonControlData(FRCCommonControlData *data, int wait_ms);
    private static final BlockingFunction getCommonControlDataFn = NativeLibrary.getDefaultInstance().getBlockingFunction("getCommonControlData");
    
    // 	int getDynamicControlData(UINT8 type, char *dynamicData, INT32 maxLength, int wait_ms);
    private static final BlockingFunction getDynamicControlDataFn = NativeLibrary.getDefaultInstance().getBlockingFunction("getDynamicControlData");
    
    // 	int setStatusDataFloatAsInt(int battery, UINT8 dsDigitalOut, UINT8 updateNumber,
	//                              const char *userDataHigh, int userDataHighLength,
	//                              const char *userDataLow, int userDataLowLength, int wait_ms);
    private static final BlockingFunction setStatusDataFn = NativeLibrary.getDefaultInstance().getBlockingFunction("setStatusDataFloatAsInt");

    // int setErrorData(const char *errors, int errorsLength, int wait_ms);
    private static final BlockingFunction setErrorDataFn = NativeLibrary.getDefaultInstance().getBlockingFunction("setErrorData");

    // int setUserDsLcdData(const char *userDsLcdData, int userDsLcdDataLength, int wait_ms);
    private static final BlockingFunction setUserDsLcdDataFn = NativeLibrary.getDefaultInstance().getBlockingFunction("setUserDsLcdData");

    // int overrideIOConfig(const char *ioConfig, int wait_ms);
    private static final BlockingFunction overrideIOConfigFn = NativeLibrary.getDefaultInstance().getBlockingFunction("overrideIOConfig");

    // void setNewDataSem(SEM_ID);
    private static final BlockingFunction setNewDataSemFn = NativeLibrary.getDefaultInstance().getBlockingFunction("setNewDataSem");

    // void FRC_NetworkCommunication_observeUserProgramStarting(void);
    private static final Function observeUserProgramStartingFn = NativeLibrary.getDefaultInstance().getFunction("FRC_NetworkCommunication_observeUserProgramStarting");

    static {
        getCommonControlDataFn.setTaskExecutor(taskExecutor);
        getDynamicControlDataFn.setTaskExecutor(taskExecutor);
        setStatusDataFn.setTaskExecutor(taskExecutor);
        setErrorDataFn.setTaskExecutor(taskExecutor);
        setUserDsLcdDataFn.setTaskExecutor(taskExecutor);
        setNewDataSemFn.setTaskExecutor(taskExecutor);
        overrideIOConfigFn.setTaskExecutor(taskExecutor);
    }

    /**
     * The size of the IO configuration data
     */
    public static final int IO_CONFIG_DATA_SIZE = 32;
    /**
     * The size of the user control data
     */
    public static final int USER_CONTROL_DATA_SIZE = 936 - IO_CONFIG_DATA_SIZE;
    /**
     * The size of the user status data
     */
    public static final int USER_STATUS_DATA_SIZE = 984;
    /**
     * The size of the user driver station display data
     */
    public static final int USER_DS_LCD_DATA_SIZE = 128;

    private FRCControl() {
    }

    /**
     * A simple 1-element cache that keeps a pointer to native memory around.
     * Works best if repeatedly asked for buffers of same size.
     *
     * WARNING: It's expected that the users of this cache are synchronized
     *
     * @TODO free the cache at shutdown...
     */
    public static class CachedNativeBuffer {

        private Pointer buffer;

        public Pointer getBufferSized(int size) {
            if (buffer == null) {
                buffer = new Pointer(size);
            }
            if (size > buffer.getSize()) {
                buffer.free();
                buffer = new Pointer(size);
            }
            return buffer;
        }

        public void free() {
            if (buffer != null) {
                buffer.free();
                buffer = null;
            }
        }
    } /* CachedNativeBuffer */


    public static abstract class DynamicControlData extends Structure {
    }
    
    private static final CachedNativeBuffer controlDataCache = new CachedNativeBuffer();
    private static final CachedNativeBuffer statusDataCacheHigh = new CachedNativeBuffer();
    private static final CachedNativeBuffer statusDataCacheLow = new CachedNativeBuffer();
    private static final CachedNativeBuffer ioConfigDataCache = new CachedNativeBuffer();

    /**
     * Get the control data from the driver station. The parameter "data"
     * is only updated when the method returns 0.
     *
     * @param data the object to store the results in (out param)
     * @param wait_ms the maximum time to wait
     * @return 0 if new data, 1 if no new data, 2 if access timed out.
     */
    public static int getCommonControlData(FRCCommonControlData data, int wait_ms) {
        int res = getCommonControlDataFn.call2(data.getPointer(), wait_ms);
        if (res == 0) {
            // Copy the FRCControlData from C-accessible memory
            data.read();
        }

        return res;
    }

    /**
     * Get the dynamic control data from the driver station. The parameter 
     * "dynamicData" is only updated when the method returns 0.
     * @param type The type to get.
     * @param dynamicData The array to hold the result in.
     * @param maxLength The maximum length of the data.
     * @param wait_ms The maximum time to wait.
     * @return 0 if new data, 1 if no new data, 2 if access timed out.
     */
    public static int getDynamicControlData(byte type, DynamicControlData dynamicData, int maxLength, int wait_ms) {
        synchronized (controlDataCache) {
            dynamicData.write();
            int res = getDynamicControlDataFn.call4(type, dynamicData.getPointer(), maxLength, wait_ms);
            if (res == 0)
                dynamicData.read();
            return res;
        }
    }

    /**
     * Right new io config data.
     * @param ioConfig The data to write / read
     * @param wait_ms The maximum time to wait.
     * @return 0 if new data, 1 if no new data, 2 if access timed out.
     */
    public static int overrideIOConfig(DynamicControlData ioConfig, int wait_ms) {
        synchronized (ioConfigDataCache) {
            ioConfig.write();
            int res = overrideIOConfigFn.call2(ioConfig.getPointer(), wait_ms);
            if (res == 0)
                ioConfig.read();
            return res;
        }
    }

    /**
     * Set the status data to send to the ds
     *
     * @param battery the battery voltage
     * @param dsDigitalOut value to set the digital outputs on the ds to
     * @param updateNumber unique ID for this update (incrementing)
     * @param userDataHigh additional high-priority user data bytes
     * @param userDataHighLength number of high-priority data bytes
     * @param userDataLow additional low-priority user data bytes
     * @param userDataLowLength number of low-priority data bytes
     * @param wait_ms the timeout
     * @return 0 on success, 1 if userData.length is too big, 2 if semaphore could not be taken in wait_ms.
     */
    public static int setStatusData(double battery, int dsDigitalOut,
            int updateNumber, byte[] userDataHigh, int userDataHighLength,
            byte[] userDataLow, int userDataLowLength, int wait_ms) {
        synchronized (statusDataCacheHigh) {
            //System.out.println("udl " + userData.length);
            // Copy the userdata byte[] to C-accessible memory
            Pointer userDataPtrHigh = statusDataCacheHigh.getBufferSized(userDataHighLength); // new Pointer(userData.length);
            userDataPtrHigh.setBytes(0, userDataHigh, 0, userDataHighLength);
            Pointer userDataPtrLow = statusDataCacheLow.getBufferSized(userDataLowLength); // new Pointer(userData.length);
            userDataPtrLow.setBytes(0, userDataLow, 0, userDataLowLength);

            //System.out.print("Writing ");
            //System.out.print(userData.length);
            //System.out.println(" bytes of status data to network.");

            int res = setStatusDataFn.call8(Float.floatToIntBits((float) battery),
                    dsDigitalOut, updateNumber,
                    userDataPtrHigh.address().toUWord().toPrimitive(), userDataHighLength,
                    userDataPtrLow.address().toUWord().toPrimitive(), userDataLowLength,
                    wait_ms);

            //System.out.println("Data transfered.");
            return res;
        }
    }

    /**
     * Send data to the driver station's error panel
     * @param bytes the byte array containing the properly formatted information for the display
     * @param length the length of the byte array
     * @param timeOut the maximum time to wait
     */
    public static void setErrorData(byte[] bytes, int length, int timeOut) {
        Pointer textPtr = new Pointer(bytes.length);
        textPtr.setBytes(0, bytes, 0, bytes.length);
        setErrorDataFn.call3(textPtr, length, timeOut);
        textPtr.free();
    }

    /**
     * Send data to the driver station's error panel
     * @param textPtr pointer to C byte array containing the properly formatted information for the display
     * @param length the length of the byte array
     * @param timeOut the maximum time to wait
     */
    public static void setErrorData(Pointer textPtr, int length, int timeOut) {
        if (length > textPtr.getSize()) {
            throw new IllegalArgumentException();
        }
        setErrorDataFn.call3(textPtr, length, timeOut);
    }

    /**
     * Send data to the driver station's user panel
     * @param bytes the byte array containing the properly formatted information for the display
     * @param length the length of the byte array
     * @param timeOut the maximum time to wait
     */
    public static void setUserDsLcdData(byte[] bytes, int length, int timeOut) {
        Pointer textPtr = new Pointer(bytes.length);
        textPtr.setBytes(0, bytes, 0, bytes.length);
        setUserDsLcdDataFn.call3(textPtr, length, timeOut);
        textPtr.free();
    }

    /**
     * Set the semaphore for the communications task to use
     * @param sem the semaphore to use
     */
    public static void setNewDataSem(Semaphore sem) {
        if (sem == null || sem.m_semaphore == null) {
            throw new NullPointerException("Null provided for a semaphore");
        }
        setNewDataSemFn.call1(sem.m_semaphore);
    }

    /**
     * Let the DS know that the user is loading a new app.
     */
    public static void observeUserProgramStarting() {
        observeUserProgramStartingFn.call0();
    }
}
