package rcx;

import java.io.*; 
import java.util.*;
import javax.comm.*;
import rcx.comm.*;

/**
 * RCXUSBPort - RCXCommPort implementation of a USB port
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class RCXUSBPort implements RCXCommPort
{
    private USBPort usbPort;

    public boolean open(String portName) {

        if(portName==null)
            return false;
  
        usbPort = USBPortFactory.getUSBPort();

        try {
            if(usbPort.open(portName)<0) {
               System.err.println("Error opening USB port: is tower plugged in?");
               return false;
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void close() {
    	  if (usbPort != null) {
            usbPort.close();
    	  }
    }

    public OutputStream getOutputStream() {
        if(usbPort!=null) {
            try {
                return usbPort.getOutputStream();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public InputStream getInputStream() {
        if(usbPort!=null) {
            try {
                return usbPort.getInputStream();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
