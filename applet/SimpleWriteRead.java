import java.io.*;
import java.util.*;
import javax.comm.*;
import rcx.comm.USBPort;
import rcx.comm.USBPortFactory;

/**
 * Simple Java Communications API Test Program:
 *
 * This is a simple example of writing and reading from a comm port.
 *
 * Additionally it will illustrate the following:
 *  - Discovering all available serial ports and using the first one
 *    (unless one is specified on the command line)
 *  - How to use the USB port add-on on to the Java Communcations API
 *    using the rcx.comm package.
 *    (requires a specific port name on the command line e.g. LEGOTOWER1 )
 */
public class SimpleWriteRead {

    // statics to allow use in main() which is static 
    static Enumeration portList;
    static CommPortIdentifier portId;
    static boolean useUSB;
    static SerialPort serialPort;
    static USBPort usbPort;
    static OutputStream outputStream;
    static InputStream inputStream;

    // alive msg
    static byte[] testArray1={(byte)0x55,(byte)0xff,(byte)0x0,
                 (byte)0x10,(byte)0xef,(byte)0x10,(byte)0xef};
    // beep msg
    static byte[] testArray2={(byte)0x55,(byte)0xff,(byte)0x00,
       (byte)0x51,(byte)0xae,(byte)0x05,(byte)0xfa,(byte)0x56,(byte)0xa9};


    public static void main(String[] args) {

        useUSB = false;
        String portName = "";
        int numBytes;
        int numread;

        // first, are we using usb or serial?

        if(args.length > 0) {            
            portName = args[0];
            if(portName.startsWith("LEGO"))
                useUSB = true;
        }

        if(!useUSB)
        {               
                           // FIND SERIAL PORT (unless specified):
            if(portName.length()<1) {
             
                // get all avail serial ports -see this method at bottom
                Vector ports = getAvailableSerialPorts(); 

                if(ports.size()==0) { 
                    System.out.print("no available serial ports found - ");
                    System.out.println("check for conflict with another app");
                    return;
                } else {
                  System.out.println("found "+ports.size()+ " avail serial ports");
                }

                portName  = (String)ports.firstElement();
                System.out.println("using first available port: "+portName );
            }
            try {
                portId = CommPortIdentifier.getPortIdentifier(portName);
            } catch (NoSuchPortException e) {
                System.err.println("Error: no such port "+portName);
                return;
            }


                        // SETUP SERIAL PORT:
            try {
                serialPort = (SerialPort)portId.open("SimpleWriteRead", 1000);
            } catch (PortInUseException e) {  
                System.out.println("port already in use");
                return;
            }

            try {
                serialPort.setSerialPortParams(2400,
                      SerialPort.DATABITS_8,
                      SerialPort.STOPBITS_1,
                      SerialPort.PARITY_ODD);
                serialPort.enableReceiveTimeout(30);
                serialPort.enableReceiveThreshold(14);
            } catch (UnsupportedCommOperationException e) {
                e.printStackTrace();
            }

            try {
                outputStream = serialPort.getOutputStream();
                inputStream = serialPort.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        else               // SETUP USB PORT:
        {
            usbPort = USBPortFactory.getUSBPort();

            try {
                if(usbPort.open(portName)<0) {
                   System.err.println("USB port error: is tower plugged in?");
                   return;
                }
                outputStream = usbPort.getOutputStream();
                inputStream  = usbPort.getInputStream();

            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        // now write and read (same way for both types of ports)

        try {

           // write a message out:
            System.out.println("sending 'alive' message... ("
               +testArray1.length+" bytes) "
                 +ArrayToString(testArray1,testArray1.length));

            outputStream.write(testArray1); //alive msg

            byte[] readBuffer = new byte[30];
            numBytes=1; numread=0;

            // read a message in:
            while (numBytes>0) {
                numBytes = inputStream.read(readBuffer,0,30);
                if(numBytes>0)
                    System.out.println("read response to 'alive' message... ("
                            +numBytes+" bytes)"
                              +ArrayToString(readBuffer,numBytes));
                numread+=numBytes;
            }

            if(!useUSB) {
                 // note: serial port tower echoes commands
                if(numread<14) {
                    System.err.println("Error: is the RCX on?");
                    return;
                } else if(numread<1) {
                    System.err.println("serial port error: is tower plugged in?");
                    return;
                }
            } else {
                if(numread<1) {
                    System.err.println("Error: is the RCX on?");
                    return;
                }
            }

            System.out.println("sending 'beep' message... ("
               +testArray2.length+" bytes) "
                 +ArrayToString(testArray2,testArray2.length));

            outputStream.write(testArray2); //beep msg

            numBytes=1; numread=0;
            while (numBytes>0) {
                //msg response
                numBytes = inputStream.read(readBuffer,0,30); 
                if(numBytes>0)
              System.out.println("read response to 'beep' message... ("
                        +numBytes+" bytes) "
                        +ArrayToString(readBuffer,numBytes));
                numread+=numBytes;
            }
                
            // the following could be one call using a interface
            if(!useUSB)
                serialPort.close();
            else
                usbPort.close();  

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Vector getAvailableSerialPorts() {
        CommPortIdentifier pId=null;
        SerialPort sPort=null;
        Enumeration pList=null;
        boolean foundport=false;
        pList = CommPortIdentifier.getPortIdentifiers();
        String port=null;
        Vector ports=new Vector();

        if(!pList.hasMoreElements()) {
            System.err.print("warning: no ports found - "); 
            System.err.println("make sure javax.comm.properties file is found");
            return ports;
        }
        while (pList.hasMoreElements()) {
            pId = (CommPortIdentifier) pList.nextElement();
            if (pId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                foundport=true;
                try {
                    sPort = (SerialPort)pId.open("serialport", 1000);
                } catch (PortInUseException e) {
                    foundport=false;
                } finally {
                    if(sPort!=null) { 
                        try { sPort.close(); } catch(Exception e) {}
                    }
                    if(foundport) {
                        ports.add(pId.getName());
                    }
                }
            }
        }
        return ports;
    }

    public static String ArrayToString(byte[] message, int length) {
        StringBuffer strbuffer = new StringBuffer();
        int abyte = 0;        
        for(int loop = 0; loop < length; loop++) {
            abyte = (int) message[loop];
            if (abyte < 0) abyte += 256;
            strbuffer.append(Integer.toHexString(abyte) + " ");
        }
        return strbuffer.toString();
    }
}
