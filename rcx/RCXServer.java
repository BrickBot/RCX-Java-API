package rcx;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.comm.*;

/**
 * RCXServer - used to receive remote tcp connections to control a local RCX
 * <br>Usage: RCXServer rcxportname [port number]
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class RCXServer implements RCXErrorListener, Runnable
{
    private ServerSocket listenSocket;
    private Socket tcpPort;
    private int portNumber;
    private RCXPort rcxPort;
    private String rcxPortName;
    private boolean waitforRCX=true;
    private boolean waitforClient=true;

    /**
     * The command line driven main for RCXServer.
     * Usage: RCXServer rcxportname [port number]
     * The default port is 174, the port name is mandatory.
     */
    public static void main(String[] args) {
        int portnum = 174; //default
        try {
            if(args.length>1) {
                portnum = Integer.parseInt(args[1]);
            }
            if(args.length>0) {
                RCXServer rcxServer = new RCXServer(args[0],portnum);
                rcxServer.run();
                rcxServer.stop();
            } else {
                System.out.println("Usage: RCXServer rcxportname [port number]");
            }
        } catch(Exception e) {
        }
    }
    
    /**
     * RCXServer is a proxy server for handling remote
     * TCP socket clients to control a local RCX.
     */
    public RCXServer(String rcxport,int portnum) {
        rcxPortName = rcxport;
        portNumber = portnum;
    }

    /**
     * RCXServer runs as a thread looping around
     * a server socket's accept() and handling one
     * client at a time until it disconnects.
     * The client's socket streams are handed to
     * RCXPort and mapped to the local rcx port.
     * @see RCXPort
     */
    public void run() {
        RCXPort.skipalive=true;
        InputStream tcpin;
        OutputStream tcpout;
        boolean loop=true;

        try {
            listenSocket = new ServerSocket(portNumber);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        System.out.println("creating rcx port on "+rcxPortName);
        rcxPort = new RCXPort(rcxPortName);
        rcxPort.addRCXErrorListener(this);

        while(waitforClient) {        
            waitforRCX=true;
            if(!rcxPort.isOpen()) return;
            System.out.println("Listening on port "+portNumber);
            try {
                tcpPort = listenSocket.accept();
                try { System.out.println("accepted connection to "
                        +(tcpPort.getInetAddress()).getHostName());
                } catch(Exception e) { }

                tcpin  = new BufferedInputStream(tcpPort.getInputStream());
                tcpout = new BufferedOutputStream(tcpPort.getOutputStream());

                rcxPort.setStreams(tcpin,tcpout);

                while(waitforRCX) {
                    try { Thread.sleep(100); } catch(Exception e) { }
                }
                try {
                    System.out.println("disconnecting "
                        +(tcpPort.getInetAddress()).getHostName());
                    tcpPort.close();
                } catch(Exception e) { }
            } catch(IOException ioe) {
                try {
                    System.out.println("Exception - disconnecting "
                        +(tcpPort.getInetAddress()).getHostName());
                } catch(Exception e) { }
                try {
                    tcpPort.close();
                } catch(Exception ee) { }
            }
        } //while
    }

    /**
     * This is called to stop the server's thread
     * and gracefully close the sockets.
     */
    public void stop() {
        waitforClient=false;
        waitforRCX=false;
        if (tcpPort!= null) {
            try {
                tcpPort.close();
            } catch(IOException ioe) {
            }
        }
        if (listenSocket!= null) {
            try {
                listenSocket.close();
            } catch(IOException ioe) {
            }
        }
    }

    /**
     * This implements the RCXErrorListener
     * interface to listen and handle errors.
     * It will display the error to stderr 
     * and close the current client connection.
     * @see RCXErrorListener
     */
    public void receivedError(String error) {
        System.err.println("Server got error: "+error);
        waitforRCX=false;
        if (tcpPort!= null) {
            try {
                tcpPort.close();
            } catch(IOException ioe) {
            }
        }
    }
}
