package rcx.comm;

import java.io.*;

/**
 * USBOutputStream 
 * @author Dario Laverde
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
class USBOutputStream extends OutputStream
{
    private USBPort port;
    
    USBOutputStream(USBPort usbport) {
	port = usbport;
    }
    
    public synchronized void write(int b) throws IOException {
	port.write(b);
    }
    
    public synchronized void write(byte[] byteArray) throws IOException {
	port.write(byteArray, 0, byteArray.length);
    }
    
    public synchronized void write(byte[] byteArray, int offset, int len) throws IOException {
	port.write(byteArray, offset, len);
    }
}
