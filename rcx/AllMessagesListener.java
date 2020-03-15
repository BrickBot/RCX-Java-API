package rcx;

import java.util.*;

/**
 * AllMessagesListener - interface to listen to all RCX messages
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public interface AllMessagesListener extends EventListener {

    /** Callback to revieve all messages from RCXPort
     * including those handled by RCXPort 
     * @see RCXPort */
    public void receivedMessage(byte[] message);
}
