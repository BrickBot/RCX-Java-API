package rcx;

import java.io.*;

/**
 * RCXCommPort - the interface implemented by all RCX ports
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public interface RCXCommPort
{
    public boolean open(String portName);
    public void close();
    public OutputStream getOutputStream();
    public InputStream getInputStream();
}
