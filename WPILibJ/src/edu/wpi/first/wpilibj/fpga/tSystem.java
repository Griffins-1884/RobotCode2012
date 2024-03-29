package edu.wpi.first.wpilibj.fpga;

import com.ni.rio.*;
import edu.wpi.first.wpilibj.communication.BumARioHandle;

public abstract class tSystem implements ExpectedFPGASignature, DMAChannelDescriptors
{
   protected static int m_DeviceHandle=0;
   private static int m_ReferenceCount=0;
   public static NiRioStatus status = new NiRioStatus();

   private static final String kRIO_DEVICE_NAME = "RIO0";
   private static final int kFPGA_RESET_REGISTER = 0x8102;
   private static final int kFPGA_COMMAND_REGISTER = 0x8104;
   private static final int kFPGA_COMMAND_ENABLE_CLEAR = 4;
   private static final int kFPGA_COMMAND_ENABLE_IN = 2;
   private static final int kFPGA_INTERRUPT_BASE_ADDRESS = 0x8000;
   private static final int kFPGA_SIGNATURE_REGISTER = 0x8108;
   private static final int kMITE_IOPCR_REGISTER = 0x470;
   private static final int kMITE_IOPCR_32BIT = 0xC00231;

   protected tSystem()
   {
      NiRioStatus versionStatus = new NiRioStatus();
      if (m_DeviceHandle == 0)
      {
         // Bum a RIO handle from network communications
         m_DeviceHandle = BumARioHandle.bum(status.getPointer());

         // Check the GUID
         int hwGUID[] = new int[4];
         for(int i=0; i<4; i++)
         {
            NiRioStatus cleanStatus = new NiRioStatus();
            hwGUID[i] = NiRioSrv.peek32(m_DeviceHandle, kFPGA_SIGNATURE_REGISTER, cleanStatus);
            status.setStatus(cleanStatus);
            if (hwGUID[i] != kExpectedFPGASignature[i])
            {
               versionStatus.setStatus(NiRioStatus.kRIOStatusVersionMismatch);
            }
         }
         System.out.print("FPGA Hardware GUID: ");
         printGUID(hwGUID);
         System.out.println("");
         System.out.print("FPGA Software GUID: ");
         printGUID(kExpectedFPGASignature);
         System.out.println("");
      }
      status.setStatus(versionStatus);
   }

   private static void printGUID(int guid[])
   {
      System.out.print("0x");
      for(int i=0; i<4; i++)
      {
         long longVar = guid[i];
         String word = Long.toString(longVar & 0xFFFFFFFFL, 16);
         while (word.length() < 8)
         {
            word = "0" + word;
         }
         System.out.print(word);
      }
   }

   protected void finalize()
   {
   }

   public int[] getFpgaGuid(NiRioStatus status)
   {
		int[] guid = {0,0,0,0};
      if (m_DeviceHandle == 0)
      {
         status.setStatus(NiRioStatus.kRIOStatusInvalidHandle);
         return guid;
      }

      for(int i=0; i<4; i++)
      {
         guid[i] = NiRioSrv.peek32(m_DeviceHandle, kFPGA_SIGNATURE_REGISTER, status);
      }
      return guid;
   }

//   private void configDMA(NiRioStatus status)
//   {
//      for(int i=0; i<kNUM_DMA_CHANNELS; i++)
//      {
//         if (kDMAChannelDescriptors[i].write == 1)
//         {
//            // The FPGA is writing
//            NiRioSrv.configAddFifoInputEx(
//               m_DeviceHandle,
//               kDMAChannelDescriptors[i].channel,
//               kDMAChannelDescriptors[i].baseAddress,
//               kDMAChannelDescriptors[i].depth,
//               kDMA_VERSION,
//               status);
//         }
//         else
//         {
//            // The FPGA is reading
//            NiRioSrv.configAddFifoOutputEx(
//               m_DeviceHandle,
//               kDMAChannelDescriptors[i].channel,
//               kDMAChannelDescriptors[i].baseAddress,
//               kDMAChannelDescriptors[i].depth,
//               kDMA_VERSION,
//               status);
//         }
//      }
//      NiRioSrv.configSet(m_DeviceHandle, 0, status);
//   }

	/**
	 * Releases the native C++ resources held by the tSystem instance.
	 */
	public void Release()
   {
	}

}
