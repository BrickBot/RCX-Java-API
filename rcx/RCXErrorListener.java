package rcx;

import java.util.*;

/** 
 * RCXErrorListener - listens to error messages only
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public interface RCXErrorListener extends EventListener {
    public void receivedError(String error);
}
