// Copyright (c) National Instruments 2009.  All Rights Reserved.
// Do Not Edit... this file is generated!

package edu.wpi.first.wpilibj.fpga;

import com.ni.rio.*;

public class tAlarm extends tSystem
{

   public tAlarm()
   {
      super();
   }

   protected void finalize()
   {
      super.finalize();
   }

   public static final int kNumSystems = 1;




//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for TriggerTime
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kAlarm_TriggerTime_Address = 0x8128;

   public static void writeTriggerTime(final long value)
   {
      NiRioSrv.poke32(m_DeviceHandle, kAlarm_TriggerTime_Address, (int)(value), status);
   }
   public static long readTriggerTime()
   {
      return (long)((NiRioSrv.peek32(m_DeviceHandle, kAlarm_TriggerTime_Address, status)) & 0xFFFFFFFFl);
   }

//////////////////////////////////////////////////////////////////////////////////////////////////
// Accessors for Enable
//////////////////////////////////////////////////////////////////////////////////////////////////
   private static final int kAlarm_Enable_Address = 0x8124;

   public static void writeEnable(final boolean value)
   {
      NiRioSrv.poke32(m_DeviceHandle, kAlarm_Enable_Address, (value ? 1 : 0), status);
   }
   public static boolean readEnable()
   {
      return ((NiRioSrv.peek32(m_DeviceHandle, kAlarm_Enable_Address, status)) != 0 ? true : false);
   }




}
