package rcx;

import java.util.*;

/** 
 * RCXListener - listens to all messages including errors
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public interface RCXListener extends EventListener {
    public void receivedMessage(byte[] message);
    public void receivedError(String error);
}

