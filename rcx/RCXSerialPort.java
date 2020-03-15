package rcx;

import java.io.*; 
import java.util.*;
import javax.comm.*;

/**
 * RCXSerialPort - represents a serial port
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class RCXSerialPort implements RCXCommPort
{
    private static int TIMEOUT = 3000;
    private SerialPort sPort;

    /** opens the serial port 
     * @see RCXCommPort */
    public boolean open(String portName) {

        if(portName==null)
            return false;

        CommPortIdentifier portId;
        try {
            portId = CommPortIdentifier.getPortIdentifier(portName);
        } catch (NoSuchPortException e) {
            return false;
        }
        try {
            sPort = (SerialPort)portId.open("RCXPort", 1000);
        } catch (PortInUseException e) {
            return false;
        }

    	try {
    	    sPort.setSerialPortParams(2400,
    	                              SerialPort.DATABITS_8,
    	                              SerialPort.STOPBITS_1,
                                    SerialPort.PARITY_ODD );
          sPort.enableReceiveTimeout(TIMEOUT);
          sPort.enableReceiveThreshold(14);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    return false;
    	}
        return true;
    }

    public void close() {
    	  if (sPort != null) {
            sPort.close();
    	  }
    }

    public OutputStream getOutputStream() {
        if(sPort!=null) {
            try {
                return sPort.getOutputStream();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public InputStream getInputStream() {
        if(sPort!=null) {
            try {
                return sPort.getInputStream();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * returns a vector of Strings consisting of available serial port names
     */
    public static Vector getAvailableSerialPorts() {
        CommPortIdentifier pId=null;
        SerialPort sPort=null;
        Enumeration pList=null;
        boolean foundserialport=false;
        pList = CommPortIdentifier.getPortIdentifiers();
        Vector ports=new Vector();

        if(!pList.hasMoreElements()) {
          System.err.println(
           "warning: no ports found - make sure javax.comm.properties file is found");
            return ports;
        }
        while (pList.hasMoreElements()) {
            pId = (CommPortIdentifier) pList.nextElement();
            if (pId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                foundserialport=true;
                try {
                    sPort = (SerialPort)pId.open("serialport", 1000);
                } catch (PortInUseException e) {
                    foundserialport=false;
                } finally {
                    if(sPort!=null) { 
                        try { sPort.close(); } catch(Exception e) {}
                    }
                    if(foundserialport) {
                        ports.addElement(pId.getName());
                    }
                }
            }
        }
        return ports;
    }
}
