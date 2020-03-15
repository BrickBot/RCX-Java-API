package rcx.comm;

import java.io.*;
import javax.comm.*;

/**
 * USBPortFactory
 * @author Dario Laverde
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class USBPortFactory
{
    /** creates the instance of USBPort that corresponds to the native platform 
     * @return USBPort - returns null if none available for this platform */ 
    public static USBPort getUSBPort() {

        USBPort usbport=null;

        String os = System.getProperty("os.name");

        if(os.startsWith("Win")) {

            usbport = new Win32USBPort(); 

        } else if(os.startsWith("Linux")) {

            usbport = new LinuxUSBPort(); 

        } else if(os.startsWith("Solaris")) {

            // not implemented yet

        } else if(os.startsWith("Mac")) {

            // not implemented yet

        } else {             

            // not implemented yet

        }
        return usbport;
    }
}