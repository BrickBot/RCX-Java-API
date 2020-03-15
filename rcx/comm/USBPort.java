package rcx.comm;

import java.io.*;
import javax.comm.*;

/**
 * USBPort - implemented by all ports for each platform
 * @author Dario Laverde
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public interface USBPort
{
    public int open(String portname) throws IOException;
    public void close();
    public int available() throws IOException;
    public int read() throws IOException;
    public int read(byte[] byteArray) throws IOException;
    public int read(byte[] byteArray, int offset, int len) throws IOException;
    public void write(int b) throws IOException;
    public void write(byte[] byteArray) throws IOException;
    public void write(byte[] byteArray, int offset, int len) throws IOException;
    public InputStream getInputStream() throws IOException;
    public OutputStream getOutputStream() throws IOException;
    public void enableReceiveThreshold(int i);
    public void disableReceiveThreshold();
    public boolean isReceiveThresholdEnabled();
    public int getReceiveThreshold();
    public void enableReceiveTimeout(int timeout);
    public void disableReceiveTimeout();
    public boolean isReceiveTimeoutEnabled();
    public int getReceiveTimeout();
    public void enableReceiveFraming(int i);
    public void disableReceiveFraming();
    public boolean isReceiveFramingEnabled();
    public int getReceiveFramingByte();
    public void setInputBufferSize(int i);
    public int getInputBufferSize();
    public void setOutputBufferSize(int i);
    public int getOutputBufferSize();
    public String getName();
    public String toString();
}