package rcx;

import java.io.*;
import java.net.*;

/**
 * RCXSocketPort - A TCP Socket RCXCommPort
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class RCXSocketPort implements RCXCommPort
{
    private Socket tcpPort;

    /**
     * Opens and setups a socket port as a
     * RCXCommPort. Returns true if successful
     * false otherwise.
     * @see RCXCommPort
     */
    public boolean open(String portName) {

        if(portName==null)
            return false;

        try {
            String host = getHostName(portName);
            int portnum = getPort(portName);
            tcpPort = new Socket(host,portnum);
            tcpPort.setSoTimeout(0);
        } catch(Exception e) {
            System.err.println("Error opening socket: "+e);
            return false;
        }

        return true;
    }

    /**
     * Closes the socket port
     * @see RCXCommPort
     */
    public void close() {
    	  if (tcpPort!= null) {
            try {
                tcpPort.close();
            } catch(IOException ioe) {
            }
    	  }
    }

    /**
     * Obtains the OutputStream from the socket.
     * @see RCXCommPort
     */
    public OutputStream getOutputStream() {
        if(tcpPort!=null) {
            try {
                return tcpPort.getOutputStream();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Obtains the InputStream from the socket.
     * @see RCXCommPort
     */
    public InputStream getInputStream() {
        if(tcpPort!=null) {
            try {
                return tcpPort.getInputStream();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Obtains the hostname portion from the
     * rcx URL.
     * e.g. "rcx://localhost:174" would return
     * "localhost" (the default anyway - so you
     * could simply pass in "rcx://")
     */
    private String getHostName(String portName) {
        String host = "localhost";
        if(portName.length()<7) return host;
        int pos = portName.lastIndexOf(':');
        if(pos<6) return portName.substring(6);
        return portName.substring(6,pos);
    }

    /**
     * Obtains the port number portion from the
     * rcx URL.
     * e.g. "rcx://localhost:174" would return
     * 174 (the default anyway - so you
     * could simply pass in "rcx://foo")
     */
    private int getPort(String portName) {
        int port = 174;
        if(portName.length()<7) return port;
        int pos = portName.lastIndexOf(':');
        if(pos<6) return port;
        try {
          String portnum=portName.substring(pos);
          port = Integer.parseInt(portnum);
        } catch(Exception e) {
        }
        return port;
    }
}
