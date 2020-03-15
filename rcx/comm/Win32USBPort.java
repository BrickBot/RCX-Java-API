package rcx.comm;

import java.io.*;
import javax.comm.*;

/**
 * Win32USBPort - uses JNI to interface to win32usb.dll (see win32usb source)
 * @author Dario Laverde
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class Win32USBPort implements USBPort
{
    static boolean LibLoaded;
    boolean closed;
    private InputStream ins;
    private OutputStream outs;
    int receiveTimeout= 0;
    protected String name;
    
    private native int _open(String port);
    private native int _read();
    private native int _read(byte[] byteArray, int offset, int len);
    private native int _write(int b);
    private native int _write(byte[] byteArray, int offset, int len);
    private native int _close();
    private native int _setReceiveTimeout(int timeout);
    private native int _available();

    public String getName() {
	return name;
    }
    
    public String toString() {
	return name;
    }

    public int open(String portname) throws IOException{

        if(!(portname.startsWith("COM")||portname.startsWith("LPT")))      
            name = "\\\\.\\"+portname;
        else
            return -1;

        if(_open(name)<0)
            return -1;

        closed=false;

        outs = new USBOutputStream(this);
        ins = new USBInputStream(this);

        return 0;
    }
            
    public Win32USBPort() {

        closed=true;

        if(!LibLoaded) {
	    try {
              System.loadLibrary("win32usb");
	    } catch (SecurityException se) {
	        System.err.println("Security Exception loading win32usb.dll: " + se);
                return;
            } catch (UnsatisfiedLinkError ule) {
	        System.err.println("Error loading win32usb.dll: " + ule);
                return;
	    } catch (Exception e) {
	        System.err.println("Exception with win32usb.dll: " + e);
                return;
            }
            LibLoaded=true;
        }
    }

    public int read() throws IOException {
        int ret =_read();
        // alternative to throwing an error natively
        //   (-1 is valid return indicating no bytes)
        if(ret==-2) throw new IOException();
        return ret;
    }

    public void write(int b) throws IOException {
        int ret =_write(b);
        if(ret<1) throw new IOException();
        return;
    }

    public void close() {
        _close();
        closed = true;
    }
     
    /** note: available() currently not supported by win32 driver */
    public int available() throws IOException {
        int ret =  _available();

        if(ret==-1)
//            throw new IOException();
            return 4096;

        return ret;
    }
   
    public int read(byte[] byteArray) throws IOException {
        return read(byteArray, 0, byteArray.length);
    }
  
    public int read(byte[] byteArray, int offset, int len) throws IOException  {

        if(byteArray.length < (offset+len)) 
            throw new ArrayIndexOutOfBoundsException();

       int ret=_read(byteArray,offset,len);

       // alternative to throwing an error natively
       //   (-1 is valid return indicating no bytes)
       if(ret==-2)
		throw new IOException();
        return ret;
    }
    
    public void write(byte[] byteArray) throws IOException {
        write(byteArray, 0, byteArray.length); 
    }      

    public void write(byte[] byteArray, int offset, int len) throws IOException {

        if(byteArray.length < (offset+len)) 
            throw new ArrayIndexOutOfBoundsException();

        if(_write(byteArray,offset,len)<0)
		throw new IOException();
    }

    public InputStream getInputStream() throws IOException {
	if (closed)
	    throw new IllegalStateException("Port Closed");
	return ins;
    }
    
    public OutputStream getOutputStream() throws IOException {
	if (closed)
	    throw new IllegalStateException("Port Closed");
	return outs;
    }
    
    public void enableReceiveThreshold(int i) {
    }

    public void disableReceiveThreshold() {
    }

    public boolean isReceiveThresholdEnabled() {
        return false;
    }

    public int getReceiveThreshold() {
        return -1;
    }

    public void enableReceiveTimeout(int timeout) {
         receiveTimeout = timeout;
         _setReceiveTimeout(receiveTimeout);
    }

    public void disableReceiveTimeout() {
    }

    public boolean isReceiveTimeoutEnabled() {
        return false;
    }

    public int getReceiveTimeout() {
        return receiveTimeout;
    }

    public void enableReceiveFraming(int i) {
    }

    public void disableReceiveFraming() {
    }

    public boolean isReceiveFramingEnabled() {
        return false;
    }

    public int getReceiveFramingByte() {
        return -1;
    }

    public void setInputBufferSize(int i) {
    }

    public int getInputBufferSize() {
        return -1;
    }

    public void setOutputBufferSize(int i) { 
    }

    public int getOutputBufferSize() {
        return -1;
    }

    protected void finalize() throws Throwable {
	close();
    }
}
