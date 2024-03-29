// Copyright (c) National Instruments 2009.  All Rights Reserved.
// Do Not Edit... this file is generated!

package edu.wpi.first.wpilibj.fpga;

import com.ni.rio.*;

public class tSPI extends tSystem
{

   public tSPI()
   {
      super();
   }

   protected void finalize()
   {
      super.finalize();
   }

   public static final int kNumSystems = 1;




//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for Status
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kStatus_ReceivedDataOverflow_BitfieldMask = 0x00000002;
   private static final int kStatus_ReceivedDataOverflow_BitfieldOffset = 1;
   private static final int kStatus_Idle_BitfieldMask = 0x00000001;
   private static final int kStatus_Idle_BitfieldOffset = 0;
   private static final int kSPI_Status_Address = 0x812C;

   public static int readStatus()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Status_Address, status);
      return (int)(regValue);
   }
   public static boolean readStatus_ReceivedDataOverflow()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Status_Address, status);
      int bitfieldValue = ((regValue & kStatus_ReceivedDataOverflow_BitfieldMask) >>> kStatus_ReceivedDataOverflow_BitfieldOffset);
      return ((bitfieldValue) != 0 ? true : false);
   }
   public static boolean readStatus_Idle()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Status_Address, status);
      int bitfieldValue = ((regValue & kStatus_Idle_BitfieldMask) >>> kStatus_Idle_BitfieldOffset);
      return ((bitfieldValue) != 0 ? true : false);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for ReadReceivedData
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kSPI_ReadReceivedData_Address = 0x813C;

   public static void strobeReadReceivedData()
   {
      NiRioSrv.poke32(m_DeviceHandle, kSPI_ReadReceivedData_Address, 1, status);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for Config
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kConfig_BusBitWidth_BitfieldMask = 0x007F8000;
   private static final int kConfig_BusBitWidth_BitfieldOffset = 15;
   private static final int kConfig_ClockHalfPeriodDelay_BitfieldMask = 0x00007F80;
   private static final int kConfig_ClockHalfPeriodDelay_BitfieldOffset = 7;
   private static final int kConfig_MSBfirst_BitfieldMask = 0x00000040;
   private static final int kConfig_MSBfirst_BitfieldOffset = 6;
   private static final int kConfig_DataOnFalling_BitfieldMask = 0x00000020;
   private static final int kConfig_DataOnFalling_BitfieldOffset = 5;
   private static final int kConfig_LatchFirst_BitfieldMask = 0x00000010;
   private static final int kConfig_LatchFirst_BitfieldOffset = 4;
   private static final int kConfig_LatchLast_BitfieldMask = 0x00000008;
   private static final int kConfig_LatchLast_BitfieldOffset = 3;
   private static final int kConfig_FramePolarity_BitfieldMask = 0x00000004;
   private static final int kConfig_FramePolarity_BitfieldOffset = 2;
   private static final int kConfig_WriteOnly_BitfieldMask = 0x00000002;
   private static final int kConfig_WriteOnly_BitfieldOffset = 1;
   private static final int kConfig_ClockPolarity_BitfieldMask = 0x00000001;
   private static final int kConfig_ClockPolarity_BitfieldOffset = 0;
   private static final int kSPI_Config_Address = 0x8154;

   public static void writeConfig(final int value)
   {
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Config_Address, value, status);
   }
   public static void writeConfig_BusBitWidth(final int value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      regValue &= ~kConfig_BusBitWidth_BitfieldMask;
      regValue |= ((value) << kConfig_BusBitWidth_BitfieldOffset) & kConfig_BusBitWidth_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Config_Address, regValue, status);
   }
   public static void writeConfig_ClockHalfPeriodDelay(final int value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      regValue &= ~kConfig_ClockHalfPeriodDelay_BitfieldMask;
      regValue |= ((value) << kConfig_ClockHalfPeriodDelay_BitfieldOffset) & kConfig_ClockHalfPeriodDelay_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Config_Address, regValue, status);
   }
   public static void writeConfig_MSBfirst(final boolean value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      regValue &= ~kConfig_MSBfirst_BitfieldMask;
      regValue |= (((value ? 1 : 0)) << kConfig_MSBfirst_BitfieldOffset) & kConfig_MSBfirst_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Config_Address, regValue, status);
   }
   public static void writeConfig_DataOnFalling(final boolean value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      regValue &= ~kConfig_DataOnFalling_BitfieldMask;
      regValue |= (((value ? 1 : 0)) << kConfig_DataOnFalling_BitfieldOffset) & kConfig_DataOnFalling_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Config_Address, regValue, status);
   }
   public static void writeConfig_LatchFirst(final boolean value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      regValue &= ~kConfig_LatchFirst_BitfieldMask;
      regValue |= (((value ? 1 : 0)) << kConfig_LatchFirst_BitfieldOffset) & kConfig_LatchFirst_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Config_Address, regValue, status);
   }
   public static void writeConfig_LatchLast(final boolean value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      regValue &= ~kConfig_LatchLast_BitfieldMask;
      regValue |= (((value ? 1 : 0)) << kConfig_LatchLast_BitfieldOffset) & kConfig_LatchLast_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Config_Address, regValue, status);
   }
   public static void writeConfig_FramePolarity(final boolean value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      regValue &= ~kConfig_FramePolarity_BitfieldMask;
      regValue |= (((value ? 1 : 0)) << kConfig_FramePolarity_BitfieldOffset) & kConfig_FramePolarity_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Config_Address, regValue, status);
   }
   public static void writeConfig_WriteOnly(final boolean value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      regValue &= ~kConfig_WriteOnly_BitfieldMask;
      regValue |= (((value ? 1 : 0)) << kConfig_WriteOnly_BitfieldOffset) & kConfig_WriteOnly_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Config_Address, regValue, status);
   }
   public static void writeConfig_ClockPolarity(final boolean value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      regValue &= ~kConfig_ClockPolarity_BitfieldMask;
      regValue |= (((value ? 1 : 0)) << kConfig_ClockPolarity_BitfieldOffset) & kConfig_ClockPolarity_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Config_Address, regValue, status);
   }
   public static int readConfig()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      return (int)(regValue);
   }
   public static short readConfig_BusBitWidth()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      int bitfieldValue = ((regValue & kConfig_BusBitWidth_BitfieldMask) >>> kConfig_BusBitWidth_BitfieldOffset);
      return (short)((bitfieldValue) & 0x000000FF);
   }
   public static short readConfig_ClockHalfPeriodDelay()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      int bitfieldValue = ((regValue & kConfig_ClockHalfPeriodDelay_BitfieldMask) >>> kConfig_ClockHalfPeriodDelay_BitfieldOffset);
      return (short)((bitfieldValue) & 0x000000FF);
   }
   public static boolean readConfig_MSBfirst()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      int bitfieldValue = ((regValue & kConfig_MSBfirst_BitfieldMask) >>> kConfig_MSBfirst_BitfieldOffset);
      return ((bitfieldValue) != 0 ? true : false);
   }
   public static boolean readConfig_DataOnFalling()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      int bitfieldValue = ((regValue & kConfig_DataOnFalling_BitfieldMask) >>> kConfig_DataOnFalling_BitfieldOffset);
      return ((bitfieldValue) != 0 ? true : false);
   }
   public static boolean readConfig_LatchFirst()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      int bitfieldValue = ((regValue & kConfig_LatchFirst_BitfieldMask) >>> kConfig_LatchFirst_BitfieldOffset);
      return ((bitfieldValue) != 0 ? true : false);
   }
   public static boolean readConfig_LatchLast()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      int bitfieldValue = ((regValue & kConfig_LatchLast_BitfieldMask) >>> kConfig_LatchLast_BitfieldOffset);
      return ((bitfieldValue) != 0 ? true : false);
   }
   public static boolean readConfig_FramePolarity()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      int bitfieldValue = ((regValue & kConfig_FramePolarity_BitfieldMask) >>> kConfig_FramePolarity_BitfieldOffset);
      return ((bitfieldValue) != 0 ? true : false);
   }
   public static boolean readConfig_WriteOnly()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      int bitfieldValue = ((regValue & kConfig_WriteOnly_BitfieldMask) >>> kConfig_WriteOnly_BitfieldOffset);
      return ((bitfieldValue) != 0 ? true : false);
   }
   public static boolean readConfig_ClockPolarity()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Config_Address, status);
      int bitfieldValue = ((regValue & kConfig_ClockPolarity_BitfieldMask) >>> kConfig_ClockPolarity_BitfieldOffset);
      return ((bitfieldValue) != 0 ? true : false);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for Reset
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kSPI_Reset_Address = 0x814C;

   public static void strobeReset()
   {
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Reset_Address, 1, status);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for DataToLoad
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kSPI_DataToLoad_Address = 0x8144;

   public static void writeDataToLoad(final long value)
   {
      NiRioSrv.poke32(m_DeviceHandle, kSPI_DataToLoad_Address, (int)(value), status);
   }
   public static long readDataToLoad()
   {
      return (long)((NiRioSrv.peek32(m_DeviceHandle, kSPI_DataToLoad_Address, status)) & 0xFFFFFFFFl);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for ReceivedData
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kSPI_ReceivedData_Address = 0x8138;

   public static long readReceivedData()
   {
      return (long)((NiRioSrv.peek32(m_DeviceHandle, kSPI_ReceivedData_Address, status)) & 0xFFFFFFFFl);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for Channels
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kChannels_SCLK_Channel_BitfieldMask = 0x000F0000;
   private static final int kChannels_SCLK_Channel_BitfieldOffset = 16;
   private static final int kChannels_SCLK_Module_BitfieldMask = 0x00008000;
   private static final int kChannels_SCLK_Module_BitfieldOffset = 15;
   private static final int kChannels_MOSI_Channel_BitfieldMask = 0x00007800;
   private static final int kChannels_MOSI_Channel_BitfieldOffset = 11;
   private static final int kChannels_MOSI_Module_BitfieldMask = 0x00000400;
   private static final int kChannels_MOSI_Module_BitfieldOffset = 10;
   private static final int kChannels_MISO_Channel_BitfieldMask = 0x000003C0;
   private static final int kChannels_MISO_Channel_BitfieldOffset = 6;
   private static final int kChannels_MISO_Module_BitfieldMask = 0x00000020;
   private static final int kChannels_MISO_Module_BitfieldOffset = 5;
   private static final int kChannels_SS_Channel_BitfieldMask = 0x0000001E;
   private static final int kChannels_SS_Channel_BitfieldOffset = 1;
   private static final int kChannels_SS_Module_BitfieldMask = 0x00000001;
   private static final int kChannels_SS_Module_BitfieldOffset = 0;
   private static final int kSPI_Channels_Address = 0x8150;

   public static void writeChannels(final int value)
   {
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Channels_Address, value, status);
   }
   public static void writeChannels_SCLK_Channel(final int value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      regValue &= ~kChannels_SCLK_Channel_BitfieldMask;
      regValue |= ((value) << kChannels_SCLK_Channel_BitfieldOffset) & kChannels_SCLK_Channel_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Channels_Address, regValue, status);
   }
   public static void writeChannels_SCLK_Module(final int value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      regValue &= ~kChannels_SCLK_Module_BitfieldMask;
      regValue |= ((value) << kChannels_SCLK_Module_BitfieldOffset) & kChannels_SCLK_Module_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Channels_Address, regValue, status);
   }
   public static void writeChannels_MOSI_Channel(final int value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      regValue &= ~kChannels_MOSI_Channel_BitfieldMask;
      regValue |= ((value) << kChannels_MOSI_Channel_BitfieldOffset) & kChannels_MOSI_Channel_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Channels_Address, regValue, status);
   }
   public static void writeChannels_MOSI_Module(final int value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      regValue &= ~kChannels_MOSI_Module_BitfieldMask;
      regValue |= ((value) << kChannels_MOSI_Module_BitfieldOffset) & kChannels_MOSI_Module_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Channels_Address, regValue, status);
   }
   public static void writeChannels_MISO_Channel(final int value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      regValue &= ~kChannels_MISO_Channel_BitfieldMask;
      regValue |= ((value) << kChannels_MISO_Channel_BitfieldOffset) & kChannels_MISO_Channel_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Channels_Address, regValue, status);
   }
   public static void writeChannels_MISO_Module(final int value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      regValue &= ~kChannels_MISO_Module_BitfieldMask;
      regValue |= ((value) << kChannels_MISO_Module_BitfieldOffset) & kChannels_MISO_Module_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Channels_Address, regValue, status);
   }
   public static void writeChannels_SS_Channel(final int value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      regValue &= ~kChannels_SS_Channel_BitfieldMask;
      regValue |= ((value) << kChannels_SS_Channel_BitfieldOffset) & kChannels_SS_Channel_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Channels_Address, regValue, status);
   }
   public static void writeChannels_SS_Module(final int value)
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      regValue &= ~kChannels_SS_Module_BitfieldMask;
      regValue |= ((value) << kChannels_SS_Module_BitfieldOffset) & kChannels_SS_Module_BitfieldMask;
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Channels_Address, regValue, status);
   }
   public static int readChannels()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      return (int)(regValue);
   }
   public static byte readChannels_SCLK_Channel()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      int bitfieldValue = ((regValue & kChannels_SCLK_Channel_BitfieldMask) >>> kChannels_SCLK_Channel_BitfieldOffset);
      return (byte)((bitfieldValue) & 0x0000000F);
   }
   public static byte readChannels_SCLK_Module()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      int bitfieldValue = ((regValue & kChannels_SCLK_Module_BitfieldMask) >>> kChannels_SCLK_Module_BitfieldOffset);
      return (byte)((bitfieldValue) & 0x00000001);
   }
   public static byte readChannels_MOSI_Channel()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      int bitfieldValue = ((regValue & kChannels_MOSI_Channel_BitfieldMask) >>> kChannels_MOSI_Channel_BitfieldOffset);
      return (byte)((bitfieldValue) & 0x0000000F);
   }
   public static byte readChannels_MOSI_Module()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      int bitfieldValue = ((regValue & kChannels_MOSI_Module_BitfieldMask) >>> kChannels_MOSI_Module_BitfieldOffset);
      return (byte)((bitfieldValue) & 0x00000001);
   }
   public static byte readChannels_MISO_Channel()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      int bitfieldValue = ((regValue & kChannels_MISO_Channel_BitfieldMask) >>> kChannels_MISO_Channel_BitfieldOffset);
      return (byte)((bitfieldValue) & 0x0000000F);
   }
   public static byte readChannels_MISO_Module()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      int bitfieldValue = ((regValue & kChannels_MISO_Module_BitfieldMask) >>> kChannels_MISO_Module_BitfieldOffset);
      return (byte)((bitfieldValue) & 0x00000001);
   }
   public static byte readChannels_SS_Channel()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      int bitfieldValue = ((regValue & kChannels_SS_Channel_BitfieldMask) >>> kChannels_SS_Channel_BitfieldOffset);
      return (byte)((bitfieldValue) & 0x0000000F);
   }
   public static byte readChannels_SS_Module()
   {
      int regValue = NiRioSrv.peek32(m_DeviceHandle, kSPI_Channels_Address, status);
      int bitfieldValue = ((regValue & kChannels_SS_Module_BitfieldMask) >>> kChannels_SS_Module_BitfieldOffset);
      return (byte)((bitfieldValue) & 0x00000001);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for Load
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kSPI_Load_Address = 0x8148;

   public static void strobeLoad()
   {
      NiRioSrv.poke32(m_DeviceHandle, kSPI_Load_Address, 1, status);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for ReceivedElements
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kSPI_ReceivedElements_Address = 0x8134;

   public static short readReceivedElements()
   {
      return (short)((NiRioSrv.peek32(m_DeviceHandle, kSPI_ReceivedElements_Address, status)) & 0x000001FF);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for ClearReceivedData
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kSPI_ClearReceivedData_Address = 0x8130;

   public static void strobeClearReceivedData()
   {
      NiRioSrv.poke32(m_DeviceHandle, kSPI_ClearReceivedData_Address, 1, status);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for AvailableToLoad
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kSPI_AvailableToLoad_Address = 0x8140;

   public static short readAvailableToLoad()
   {
      return (short)((NiRioSrv.peek32(m_DeviceHandle, kSPI_AvailableToLoad_Address, status)) & 0x000001FF);
   }




}
