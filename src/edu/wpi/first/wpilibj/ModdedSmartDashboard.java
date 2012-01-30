/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj;

/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
import edu.wpi.first.wpilibj.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * hax for ModdedSmartDashboard to support image overlays
 * Send robot parameters to a flexible GUI interface running on a laptop.
 * @author pmalmsten
 */
public class ModdedSmartDashboard implements IDashboard {

	public static final int MAX_FIELDS = 127;
	public static final int BUFFER_SIZE = 768;
	// Error codes
	public static final int SUCCESS = 0;
	public static final int ERROR_BUFFER_FULL = -1;
	public static final int ERROR_TYPE_MISMATCH = -2;
	public static final int ERROR_TOO_MANY_FIELDS = -3;
	public static final int ERROR_STRING_TOO_LONG = -4;
	// Data type codes
	protected static final byte BYTE_TYPE = 0;
	protected static final byte CHAR_TYPE = 1;
	protected static final byte INT_TYPE = 2;
	protected static final byte LONG_TYPE = 3;
	protected static final byte SHORT_TYPE = 4;
	protected static final byte FLOAT_TYPE = 5;
	protected static final byte DOUBLE_TYPE = 6;
	protected static final byte STRING_UTF16_TYPE = 7;
	protected static final byte BOOL_TYPE = 8;
	protected static final byte STRING_UTF8_TYPE = 9;
	protected static final byte OVERLAY_ELEMENT = 10;
	protected static final int REANNOUNCEMENT_INTERVAL_MS = 5000;
	protected final static Hashtable field_types = new Hashtable();
	protected final static Hashtable field_ids = new Hashtable();
	private static ModdedSmartDashboard instance = null;
	private static boolean initialized = false;
	protected final Object m_monitor;
	private ByteArrayOutputStream m_byteBuffer;
	private DataOutputStream m_dataOutput;
	private long m_nextReannouncementTime;
	private boolean m_blockingIO = false;

	public static final String OVERLAY_FIELD = "__overlay";
	public static final String OVERLAY_DATA = "__overlay_data";

	private ModdedSmartDashboard(Object monitor) {
		m_monitor = monitor;
		m_byteBuffer = new ByteArrayOutputStream(BUFFER_SIZE);
		m_dataOutput = new DataOutputStream(m_byteBuffer);
		m_nextReannouncementTime = System.currentTimeMillis() + REANNOUNCEMENT_INTERVAL_MS;
	}

	/**
	 * Initializes the ModdedSmartDashboard by injecting it into the DriverStation
	 * class such that it is polled for data instead of the default
	 * Dashboard instance.
	 *
	 * This must be called before using any static method of
	 * ModdedSmartDashboard for the data to be sent across the network.
	 */
	public static void init() {
		field_types.clear();
		field_ids.clear();
		instance = new ModdedSmartDashboard(DriverStation.getInstance().getStatusDataMonitor());
		DriverStation.getInstance().setDashboardPackerToUseHigh(getInstance());
		initialized = true;
	}

	/**
	 * Send the given byte value to the client as the field with the given name.
	 * @param value The value to be displayed on the client.
	 * @param name The name of the field.
	 * @return An integer status code.
	 */
	public static int log(byte value, String name) {
		if (!initialized) {
			init();
		}

		int retcode = SUCCESS;
		ModdedSmartDashboard dash = getInstance();

		synchronized (dash.m_monitor) {
			try {
				if ((retcode = dash.announceIfNewField(BYTE_TYPE, name)) != SUCCESS) {
					return retcode;
				}

				byte dataLen = 1;
				if (dash.checkBufferFull(SmartDashboardPacketFactory.getUpdateLength(dataLen))) {
					return ERROR_BUFFER_FULL;
				}

				dash.fieldUpdatePrefix(name, dataLen);
				dash.m_dataOutput.writeByte(value);

				dash.periodicallyReannounce();
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
			DriverStation.getInstance().incrementUpdateNumber();
		}
		return retcode;
	}

	/**
	 * Send the given UTF-16 char value to the client as the field with the given name.
	 * @param value The value to be displayed on the client.
	 * @param name The name of the field.
	 * @return An integer status code.
	 */
	public static int log(char value, String name) {
		if (!initialized) {
			init();
		}

		int retcode = SUCCESS;
		ModdedSmartDashboard dash = getInstance();

		synchronized (dash.m_monitor) {
			try {
				if ((retcode = dash.announceIfNewField(CHAR_TYPE, name)) != SUCCESS) {
					return retcode;
				}

				byte dataLen = 2;
				if (dash.checkBufferFull(SmartDashboardPacketFactory.getUpdateLength(dataLen))) {
					return ERROR_BUFFER_FULL;
				}

				dash.fieldUpdatePrefix(name, dataLen);
				dash.m_dataOutput.writeChar(value);

				dash.periodicallyReannounce();
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
			DriverStation.getInstance().incrementUpdateNumber();
		}
		return retcode;
	}

	/**
	 * Sends the given int value to the client as the field with the given name.
	 * @param value The value to send.
	 * @param name The name of the field.
	 * @return An integer status code.
	 */
	public static int log(int value, String name) {
		if (!initialized) {
			init();
		}

		int retcode = SUCCESS;
		ModdedSmartDashboard dash = getInstance();

		synchronized (dash.m_monitor) {
			try {
				if ((retcode = dash.announceIfNewField(INT_TYPE, name)) != SUCCESS) {
					return retcode;
				}

				byte dataLen = 4;
				if (dash.checkBufferFull(SmartDashboardPacketFactory.getUpdateLength(dataLen))) {
					return ERROR_BUFFER_FULL;
				}

				dash.fieldUpdatePrefix(name, dataLen);
				dash.m_dataOutput.writeInt(value);

				dash.periodicallyReannounce();
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
			DriverStation.getInstance().incrementUpdateNumber();
		}
		return retcode;
	}

	/**
	 * Sends the given long value to the client as the field with the given name.
	 * @param value The value to send.
	 * @param name The name of the field.
	 * @return An integer status code.
	 */
	public static int log(long value, String name) {
		if (!initialized) {
			init();
		}

		int retcode = SUCCESS;
		ModdedSmartDashboard dash = getInstance();

		synchronized (dash.m_monitor) {
			try {
				if ((retcode = dash.announceIfNewField(LONG_TYPE, name)) != SUCCESS) {
					return retcode;
				}

				byte dataLen = 8;
				if (dash.checkBufferFull(SmartDashboardPacketFactory.getUpdateLength(dataLen))) {
					return ERROR_BUFFER_FULL;
				}

				dash.fieldUpdatePrefix(name, dataLen);
				dash.m_dataOutput.writeLong(value);

				dash.periodicallyReannounce();
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
			DriverStation.getInstance().incrementUpdateNumber();
		}
		return retcode;
	}

	/**
	 * Sends the given short value to the client as the field with the given name.
	 * @param value The value to send.
	 * @param name The name of the field.
	 * @return An integer status code.
	 */
	public static int log(short value, String name) {
		if (!initialized) {
			init();
		}

		int retcode = SUCCESS;
		ModdedSmartDashboard dash = getInstance();

		synchronized (dash.m_monitor) {
			try {
				if ((retcode = dash.announceIfNewField(SHORT_TYPE, name)) != SUCCESS) {
					return retcode;
				}

				byte dataLen = 2;
				if (dash.checkBufferFull(SmartDashboardPacketFactory.getUpdateLength(dataLen))) {
					return ERROR_BUFFER_FULL;
				}

				dash.fieldUpdatePrefix(name, dataLen);
				dash.m_dataOutput.writeShort(value);

				dash.periodicallyReannounce();
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
			DriverStation.getInstance().incrementUpdateNumber();
		}
		return retcode;
	}

	/**
	 * Sends the given float value to the client as the field with the given name.
	 * @param value The value to send.
	 * @param name The name of the field.
	 * @return An integer status code.
	 */
	public static int log(float value, String name) {
		if (!initialized) {
			init();
		}

		int retcode = SUCCESS;
		ModdedSmartDashboard dash = getInstance();

		synchronized (dash.m_monitor) {
			try {
				if ((dash.announceIfNewField(FLOAT_TYPE, name)) != SUCCESS) {
					return retcode;
				}

				byte dataLen = 4;
				if (dash.checkBufferFull(SmartDashboardPacketFactory.getUpdateLength(dataLen))) {
					return ERROR_BUFFER_FULL;
				}

				dash.fieldUpdatePrefix(name, dataLen);
				dash.m_dataOutput.writeFloat(value);

				dash.periodicallyReannounce();
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
			DriverStation.getInstance().incrementUpdateNumber();
		}
		return retcode;
	}

	/**
	 * Sends the given double value to the client as the field with the given name.
	 * @param value The value to send.
	 * @param name The name of the field.
	 * @return An integer status code.
	 */
	public static int log(double value, String name) {
		if (!initialized) {
			init();
		}

		int retcode = SUCCESS;
		ModdedSmartDashboard dash = getInstance();

		synchronized (dash.m_monitor) {
			try {
				if ((retcode = dash.announceIfNewField(DOUBLE_TYPE, name)) != SUCCESS) {
					return retcode;
				}

				byte dataLen = 8;
				if (dash.checkBufferFull(SmartDashboardPacketFactory.getUpdateLength(dataLen))) {
					return ERROR_BUFFER_FULL;
				}

				dash.fieldUpdatePrefix(name, dataLen);
				dash.m_dataOutput.writeDouble(value);

				dash.periodicallyReannounce();
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
			DriverStation.getInstance().incrementUpdateNumber();
		}
		return retcode;
	}

	/**
	 * Computes the UTF-8 length per DataInput. Unicode code points greater than
	 * 0x7FF (including supplementary chars and their surrogate pair encodings)
	 * are not currently supported.
	 */
	static int utfLength(String s) {
		int sLength = s.length();
		int utfLength = 0;
		for (int i = 0; i < sLength; ++i) {
			char c = s.charAt(i);
			if (c == 0 || c >= 0x80) {
				++utfLength;
			}
			++utfLength;
		}
		return utfLength;
	}

	/**
	 * Sends the given String value to the client as the field with the given name.
	 * @param value The value to send. This may be at most 63 characters in length.
	 * @param name The name of the field.
	 * @return An integer status code.
	 */
	public static int log(String value, String name) {
		int dataLen = utfLength(value) + 2;
		// Data length field is sent as a single byte
		// String lengths must be restricted accordingly
		if (dataLen > 127) {
			return ERROR_STRING_TOO_LONG;
		}

		if (!initialized) {
			init();
		}

		int retcode = SUCCESS;
		ModdedSmartDashboard dash = getInstance();

		synchronized (dash.m_monitor) {
			try {
				if ((retcode = dash.announceIfNewField(STRING_UTF8_TYPE, name)) != SUCCESS) {
					return retcode;
				}

				if (dash.checkBufferFull(SmartDashboardPacketFactory.getUpdateLength((byte) dataLen))) {
					return ERROR_BUFFER_FULL;
				}

				dash.fieldUpdatePrefix(name, (byte) dataLen);
				dash.m_dataOutput.writeUTF(value);

				dash.periodicallyReannounce();
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
			DriverStation.getInstance().incrementUpdateNumber();
		}
		return retcode;
	}

	/**
	 * Sends the given boolean value to the client as the field with the given name.
	 * @param value The value to send.
	 * @param name The name of the field.
	 * @return An integer status code.
	 */
	public static int log(boolean value, String name) {
		if (!initialized) {
			init();
		}

		int retcode = SUCCESS;
		ModdedSmartDashboard dash = getInstance();

		synchronized (dash.m_monitor) {
			try {
				if ((retcode = dash.announceIfNewField(BOOL_TYPE, name)) != SUCCESS) {
					return retcode;
				}

				byte dataLen = 1;
				if (dash.checkBufferFull(SmartDashboardPacketFactory.getUpdateLength(dataLen))) {
					return ERROR_BUFFER_FULL;
				}

				dash.fieldUpdatePrefix(name, dataLen);
				dash.m_dataOutput.writeBoolean(value);

				dash.periodicallyReannounce();
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
			DriverStation.getInstance().incrementUpdateNumber();
		}
		return retcode;
	}

	public static int overlayStart() {
		return log(true, OVERLAY_FIELD);
	}

	/**
	 * Sends new image overlay data to the dashboard. Position should be for
	 * the center X as reported via the NIVision api, and is converted to
	 * standard screen coords on the client side.  The bounding box will be
	 * drawn in black.
	 * @param x x position of the particle
	 * @param y y position of the particle
	 * @param width width of the particle
	 * @param height height of the particle
	 * @return a status code
	 */
	public static int overlay(int x, int y, int width, int height){
		return overlay(x, y, width, height, getColorCode(255, 0, 0));
	}

	/**
	 * Sends new image overlay data to the dashboard. Position should be for
	 * the center X as reported via the NIVision api, and is converted to
	 * standard screen coords on the client side.  The bounding box will be
	 * drawn in the color specified.
	 * @param x x position of the particle
	 * @param y y position of the particle
	 * @param width width of the particle
	 * @param height height of the particle
	 * @param color The color of the particle, as specified in the single int
	 *				constructor of <code>Color</code>.
	 * @return a status code
	 */
	public static int overlay(int x, int y, int width, int height, int color) {
		if (!initialized) {
			init();
		}

		int retcode = SUCCESS;
		ModdedSmartDashboard dash = getInstance();

		synchronized (dash.m_monitor) {
			try {
				if ((retcode = dash.announceIfNewField(OVERLAY_ELEMENT, OVERLAY_DATA)) != SUCCESS) {
					return retcode;
				}

				byte dataLen = 20; /* 4bytes * 5 numbers (4 for the two
				                    * coordinate points, one for the color */
				if (dash.checkBufferFull(SmartDashboardPacketFactory.getUpdateLength(dataLen))) {
					return ERROR_BUFFER_FULL;
				}

				dash.fieldUpdatePrefix(OVERLAY_DATA, dataLen);
				dash.m_dataOutput.writeInt(x);
				dash.m_dataOutput.writeInt(y);
				dash.m_dataOutput.writeInt(width);
				dash.m_dataOutput.writeInt(height);
				dash.m_dataOutput.writeInt(color);

				dash.periodicallyReannounce();
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
			DriverStation.getInstance().incrementUpdateNumber();
		}
		return retcode;
	}

	public static int overlay(int x, int y, int width, int height, int r, int g, int b) {
		return ModdedSmartDashboard.overlay(x, y, width, height,
				ModdedSmartDashboard.getColorCode(r, g, b));
	}

	public static int overlayEnd() {
		return log(false, OVERLAY_FIELD);
	}

	/**
	 * Informs the client that the given profile should be used.
	 *
	 * This has not yet been implemented on the client!
	 *
	 * @param name The name of the client profile which should be used.
	 */
	public static int useProfile(String name) {
		if (!initialized) {
			init();
		}

		synchronized (getInstance().m_monitor) {
			try {
				if (getInstance().checkBufferFull(SmartDashboardPacketFactory.getAnnounceProfileLength(name))) {
					return ERROR_BUFFER_FULL;
				}

				SmartDashboardPacketFactory.announceProfile(getInstance().m_dataOutput, name);
			} catch (IOException ex) {
				ex.printStackTrace();
				return ERROR_BUFFER_FULL;
			}
		}
		return SUCCESS;
	}

	/**
	 * Provides a brief clarification of the given error code.
	 *
	 * This is most advantageous when its output is sent to a print statement.
	 *
	 * @param code The error code to diagnose.
	 * @return A String containing a brief description of the error encountered.
	 */
	public static String diagnoseErrorCode(int code) {
		switch (code) {
			case SUCCESS:
				return "Not an error.";
			case ERROR_BUFFER_FULL:
				return "An error occurred attempting to queue your data "
						+ "for sending. Please try to send less data.";
			case ERROR_TOO_MANY_FIELDS:
				return "ModdedSmartDashboard cannot track that many fields at this time. "
						+ "Please restrict the number of fields you create to " + MAX_FIELDS + ".";
			case ERROR_TYPE_MISMATCH:
				return "You prevously logged a field with the same name with a different "
						+ "type. This is not supported; A field first logged as a particular type must always "
						+ "be logged as that type.";
			default:
				return "Unrecognized error code: " + code;
		}
	}

	/**
	 * Sets whether the dashboard should ensure whether all data is eventually
	 * sent to a client by temporarily blocking a user's code.
	 *
	 * When false (the default value), a call to log() may return ERROR_BUFFER_FULL
	 * if a significant amount of data is logged quickly before the buffer can flush
	 * itself. Data is lost when this occurs. By using blocking I/O, data will
	 * not be lost if the buffer fills, but a user's program will stop running until
	 * room is available in the transmission buffer. This may cause adverse effects
	 * in real-time code.
	 *
	 * @param value Determines whether blocking I/O is used. Defaults to false.
	 */
	public static void useBlockingIO(boolean value) {
		getInstance().m_blockingIO = value;
	}

	/**
	 * If the given field has not been seen before, this method will announce it.
	 * Additionally, some sanity checking is done.
	 * @param type The type of the field.
	 * @param name The name of the field.
	 * @return A code representing success or failure.
	 * @throws IOException
	 */
	private int announceIfNewField(byte type, String name) throws IOException {
		if (checkBufferFull(SmartDashboardPacketFactory.getAnnounceLength(name))) {
			return ERROR_BUFFER_FULL;
		}

		// Field does not already exist
		if (!field_ids.containsKey(name)) {
			if (field_ids.size() >= MAX_FIELDS) {
				return ERROR_TOO_MANY_FIELDS;
			}

			field_ids.put(name, new Byte((byte) field_ids.size()));
			field_types.put(name, new Byte(type));
			SmartDashboardPacketFactory.announce(m_dataOutput, name, type, ((Byte) field_ids.get(name)).byteValue());
		} else {
			// Field already exists
			if (((Byte) field_types.get(name)).byteValue() != type) {
				return ERROR_TYPE_MISMATCH;
			}
		}

		return SUCCESS;
	}

	/**
	 * Periodically re-announces all tracked fields.
	 * @throws IOException
	 */
	private void periodicallyReannounce() throws IOException {
		if (System.currentTimeMillis() >= m_nextReannouncementTime) {
			Enumeration e = field_ids.keys();
			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				byte id = ((Byte) field_ids.get(name)).byteValue();
				byte type = ((Byte) field_types.get(name)).byteValue();

				if (!checkBufferFull(SmartDashboardPacketFactory.getAnnounceLength(name))) {
					SmartDashboardPacketFactory.announce(m_dataOutput, name, type, id);
				}
			}

			m_nextReannouncementTime += REANNOUNCEMENT_INTERVAL_MS;
		}
	}

	/**
	 * Creates a frame header in the output data stream which indicates that
	 * a particular field should be updated with the data to follow. The data
	 * must be added to the stream by the calling method after calling this
	 * routine or inconsistencies will result!
	 * @param name The name of the field to update.
	 * @param len The length of the data to follow this prefix.
	 * @throws IOException
	 */
	private void fieldUpdatePrefix(String name, byte len) throws IOException {
		SmartDashboardPacketFactory.updatePrefix(m_dataOutput, ((Byte) field_ids.get(name)).byteValue(), len);
	}

	/**
	 * Determines whether the output stream buffer is full.
	 *
	 * If blocking IO is enabled, this function will block until space is
	 * available, and will always return false.
	 *
	 * @param requestedBytes The number of bytes which a user would like to write.
	 * @return Boolean value of whether the buffer is full or not.
	 * @throws InterruptedException
	 */
	private boolean checkBufferFull(int requestedBytes) {
		synchronized (m_monitor) {
			while ((m_byteBuffer.size() + requestedBytes) > BUFFER_SIZE) {
				if (!m_blockingIO) {
					return true;
				}

				try {
					m_monitor.wait();
				} catch (InterruptedException ex) {
				}
			}
		}

		return false;
	}

	/**
	 * Gets the singleton instance of ModdedSmartDashboard.
	 * @return An instance of ModdedSmartDashboard.
	 */
	private static ModdedSmartDashboard getInstance() {
		return instance;

	}

	/**	Shifts the red into bits 16-23, green into  bits 8-15,
	 * blue into bits 0-7.
	 * @param r the red value.
	 * @param g the green value.
	 * @param b the blue value.
	 */
	protected static int getColorCode(int r, int g, int b) {
		return (r) * 65536 + (g) * 256 + b;  /*	Shifts the red into bits 16-23,
											  *	g into  bits 8-15, and blue
		                                      * into bits 0-7*/
	}

	public byte[] getBytes() {
		synchronized (m_monitor) {
			return m_byteBuffer.toByteArray();
		}
	}

	public int getBytesLength() {
		synchronized (m_monitor) {
			return m_byteBuffer.size();
		}
	}

	public void flush() {
		synchronized (m_monitor) {
			m_byteBuffer.reset();
			m_monitor.notifyAll();
		}
	}

	//public class Color {
	//	public static final int RED = 0;
	//	public static final int
	//}
}
