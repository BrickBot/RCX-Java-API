package rcx.comm;

import java.io.*;

/**
 * USBInputStream 
 * @author Dario Laverde
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
class USBInputStream extends InputStream
{
    private USBPort port;
    
    USBInputStream(USBPort usbport) {
	port = usbport;
    }
    
    public int read() throws IOException {
	return port.read();
    }
    
    public int read(byte[] byteArray) throws IOException {
	return port.read(byteArray, 0, byteArray.length);
    }
    
    public int read(byte[] byteArray, int offset, int len) throws IOException {
	return port.read(byteArray, offset, len);
    }
    
    public int available() throws IOException {
	return port.available();
    }
}
