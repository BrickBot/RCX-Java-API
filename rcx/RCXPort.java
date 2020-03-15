package rcx;

import java.io.*;
import java.util.*;

/**
 * RCXPort - Encapsulates and manages communications to and from the RCX
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class RCXPort
{
    /** used to display debugging messages */
    public static boolean debug = false;
    /** used to skip sending the initial alive message */
    public static boolean skipalive = false;
    /** used to indicate emulation mode */
    public boolean isEmulation;

    private static int MAXRETRIES = 3;
    private static int BUFFSIZE = 4096;
    private boolean isUSB;
    private boolean isSocket;
    private String portName;
    private OutputStream os;
    private InputStream is;
    private boolean open;
    private byte[] responseArray = new byte[BUFFSIZE];
    private byte[] respArray = new byte[BUFFSIZE];
    private RCXListener rcxListener;
    private AllMessagesListener allmsgListener;
    private RCXErrorListener errorListener;
    private int numread;
    private int numretry;
    private byte[] lastcommandArray;
    private byte[] message;
    private boolean towerError;
    private boolean rcxError;
    private byte inputcommand;    
    private byte lastcommand;
    private String lasterror;
    private int lastreturnval;
    private RCXOpcode opcodes;
    private StreamSwap outin;

    private RCXCommPort port;

    /** creates the port and calls init(null) */
    public RCXPort() {
        init(null);    
    }

    /** creates an instance of RCXOpcode for looking up opcodes, creates
       the static instances of Motor and Sensor, and calls open(port) */
    public void init(String port) {
        open = false;
        opcodes = new RCXOpcode();
        Motor.setPort(this);
        Sensor.setPort(this);
        open(port);
    }

    /** creates the port and calls init(portname) */
    public RCXPort(String portname) {
        init(portname);
    }

    /** adds an RCXListener */
    public void addRCXListener(RCXListener rl) {
        rcxListener = rl;
    }

    /** adds an AllMessagesListener */
    public void addAllMessagesListener(AllMessagesListener rl) {
        allmsgListener = rl;
    }

    /** adds an RCXErrorListener */
    public void addRCXErrorListener(RCXErrorListener rl) {
        errorListener = rl;
    }

    /** sets debug mode on or off */
    public void setDebug(boolean set) {
        debug=set;
    }

    /** creates an instance of the RCXCommPort: RCXUSBPort, RCXSocketPort or RCXSerialPort */
    protected void initPort() {
        if(isUSB) {
            port = new RCXUSBPort();
        } else if(isSocket) {
            port = new RCXSocketPort();
        } else {
            port = new RCXSerialPort();
        }
    }

    /** determines the type of port from the port name<br>
     * (if null will find first available serial port */
    public void open(String port) {
        portName = port;

        // if no port specified - search for serial ports:
        if(portName==null) {
            Vector ports = RCXSerialPort.getAvailableSerialPorts();
            if(ports.size()==0) {
                System.err.print("no available serial ports found -");
                System.err.println(" check if other apps are in conflict");
            }
            // get first available serial port:
            try {
                portName=(String)ports.firstElement();
            } catch(Exception e) {
               portName=null;
               return;
            }
        }
        else if(port.startsWith("LEGO")) {
            isUSB=true;
        }
        else if(port.equals("EMULATION")) {
            isEmulation=true;
            open=true;
        }
        else if(port.startsWith("rcx://")) {
            isSocket=true;
        }
        if(!isEmulation) {
            open();
            if(!skipalive) alive();
        }
    }

    /** sends an alive message to the RCX */
    public void alive() {
        byte[] alivemsg = new byte[1];
    	  alivemsg[0] = (byte)0x10;
    	  write(alivemsg);
    }
    /** actually opens the port and obtains the output and input streams */
    public boolean open() {

        if(open) 
            return true;
           
        initPort();
                
        open = port.open(portName);

        if(open) {
            try {
                os = new BufferedOutputStream(port.getOutputStream(),4096);
                is = new BufferedInputStream(port.getInputStream(),4096);
            } catch (Exception e) {
                port.close();
                return open;
            }
        }

        return open;
    }

    /** closes the streams and the port */
    public void close() {
        if(outin!=null) outin.stop();
    	if (!open || isEmulation)
    	    return;
    	    
    	if (port != null) {
    	    try {
    	    	os.close();
    	    	is.close();
    	    } catch (IOException e) {
        	    System.err.println(e);
    	    }
          port.close();
    	}
    	
    	open = false;
    }

    /** tests to see if port already opened */
    public boolean isOpen() {
        return open;
    }

    /** used to overide the port open status 
     *  (for example, mark as closed when errors occur)
     */
    public void setOpen(boolean set) {
        if(outin!=null) outin.stop();
        outin=null;
        open=set;
    }

    /** gets the OutputStream */
    public OutputStream getOutputStream() {
        return os;
    }

    /** gets the InputStream */
    public InputStream getInputStream() {
        return is;
    }

    /** sets the streams to swap and creates an instance (thread) of StreamSwap 
     * @see RCXServer */
    public void setStreams(InputStream in, OutputStream out) {
        if(outin!=null) outin.stop();
        outin = new StreamSwap(in,out,this);
    }

    /** writes a byte array to the RCX port */
    public synchronized boolean write(byte[] bArray) {
        if(debug)
          System.out.println("port write(): "+ArrayToString(bArray,bArray.length));

        int index=3; int sum=0;

        if(isEmulation) return true;

        if(bArray.length<1)
            return false;

        lasterror=null;

        if(isSocket) {
            try {
                os.write(bArray);
                os.flush();
                numread = is.read(responseArray,0,BUFFSIZE);
                if(debug && numread>0)
                    System.out.println("socket read(): numread = "+numread
                          +" array="+ArrayToString(responseArray,numread));

                byte[] msg = new byte[numread];
                System.arraycopy(responseArray,0,msg,0,numread);

                processMessage(msg);
                if(allmsgListener!=null)
                    allmsgListener.receivedMessage(msg);
                if(rcxListener!=null)
                    rcxListener.receivedMessage(msg);
            } catch(Exception e) {
                if(errorListener!=null)
                    errorListener.receivedError("socket write or read error");
                open = false;
                return false;
            }
            return true;
        }


        byte[] rcxArray = new byte[bArray.length*2+5];

        rcxArray[0]=(byte)0x55;rcxArray[1]=(byte)0xff;rcxArray[2]=(byte)0x00;
        
        if(lastcommand==bArray[0]) {
            if((bArray[0]&(byte)0x08)==0) {
                bArray[0]=(byte)(bArray[0]|(byte)0x08);
            }
            else {
                bArray[0]=(byte)(bArray[0]&(byte)0xf7);
            }
        }

        lastcommand=bArray[0];

        for(int loop=0;loop<bArray.length;loop++) {
            rcxArray[index]=bArray[loop];
            rcxArray[index+1]=(byte)~bArray[loop];
            sum+=rcxArray[index];
            index+=2;
        }

        rcxArray[index]=(byte)sum;
        rcxArray[index+1]=(byte)~sum;

        try {
            os.write(rcxArray);
            os.flush();
        } catch(Exception e) {
            lasterror="error writing to port";
            lastcommandArray=null;
            return false;
        }

        lastcommandArray=rcxArray;

        if(debug)
          System.out.println("wrote out: "+ArrayToString(rcxArray,rcxArray.length));

        processRead();

        return true;
    }

    /** converts string to byte array and send it to RCX */
    public boolean send(String packet) {
        byte[] byteArray;
        byteArray = RCXOpcode.parseString(packet);
        return write(byteArray);
    }

    /** reads and processes messages dispatching where needed
     * and retrying by resending the last command if needed
     */
    private void processRead() {
        int avail=0;
        int echolen=0;
        int numreadsofar=0;

        while (numretry < MAXRETRIES) {
            if(rcxError||towerError) {
                rcxError=false;
                towerError=false;
                try {
                    os.write(lastcommandArray);
                    os.flush();
                } catch(Exception e) {
                    lastcommandArray=null;
                    lasterror="error writing to port";
                    System.err.println("Error: "+lasterror); 
                    if(isSocket) open = false;
                    if(errorListener!=null)
                        errorListener.receivedError(lasterror);
                    if(rcxListener!=null)
                        rcxListener.receivedError(lasterror);
                    break;
                }
            }
            try {
                // don't run 'task only' opcodes
                if(((String)RCXOpcode.Opcodes.get(
                       new Byte((byte)(lastcommand&(byte)0xf7)))).endsWith("C"))
                    break;
                if(!isUSB)
                    if(((lastcommand&(byte)0xf7)==RCXOpcode.GETMEMMAP))
                        try{Thread.sleep(2000);}catch(Exception e){ }
            } catch(Exception e) { break; }
            try {
                if(!isUSB) {
                    numread = is.read(responseArray,0,BUFFSIZE);
                    if(debug && numread>0)
                       System.out.println("processRead(): numread = "+numread
                            +" array="+ArrayToString(responseArray,numread));
                } else {
                    numread=1; numreadsofar=0;
                    while(numread>0) {
                        numread = is.read(respArray,0,BUFFSIZE);
                        if(numread>0) {
                          if(debug)
                            System.out.println(
                              "processRead(): numreadsofar="+numreadsofar
                              +" numread="+numread+" array="
                              +ArrayToString(respArray,numread));
                  System.arraycopy(respArray,0,responseArray,numreadsofar,numread);
                          numreadsofar+=numread;
                        }
                    }
                    numread=numreadsofar;
                }

            } catch(Exception e) {
                lasterror="error reading from port ";
                System.err.println("Error: "+lasterror);
                if(isSocket) open = false;
                if(errorListener!=null)
                    errorListener.receivedError(lasterror);
                if(rcxListener!=null)
                    rcxListener.receivedError(lasterror);
                rcxError=false;
                towerError=false;
                break;
            }

            if(!isUSB)
            {
                if(lastcommandArray.length>numread) {
                    lasterror="error reading message from tower";
                    towerError=true;
                    numretry++;
                    continue;
                }
                if((responseArray[0]!=(byte)0x55&&responseArray[0]!=(byte)0xaa)
                  ||(responseArray[1]!=(byte)0xff)||(responseArray[2]!=(byte)0x00))
                {
                    lasterror="error in message header from tower";                
                    towerError=true;
                    numretry++;
                    continue;
                }

                echolen = lastcommandArray.length;

            } else {
                echolen = 0;
            }

            if(echolen==numread) {
                lasterror="no reponse from RCX";
                rcxError=true;
                numretry++;
                continue;
            }

            if(numread<echolen+7 || (numread-echolen-3)%2!=0) {
                lasterror="error in response length from RCX";
                rcxError=true;
                numretry++;
                continue;
            }
            if((responseArray[echolen]!=(byte)0x55
                  && responseArray[echolen]!=(byte)0xaa)
                   || responseArray[echolen+1]!=(byte)0xff
                   || responseArray[echolen+2]!=(byte)0x00) {
                lasterror="error in response header from RCX";                
                rcxError=true;
                numretry++;
                continue;
            }
            
            message=new byte[(numread-(echolen+3))/2-1];

            int sum=0;
            int loop=0;
            int msgcount=0;
            for(loop=echolen+3;loop<numread;loop+=2) {
                if(responseArray[loop+1]!=~responseArray[loop]) {
                    lasterror="error in response from RCX";  
                    rcxError=true;
                    numretry++;
                    continue;
                }
                if(loop<numread-2) {
                    message[msgcount++] = responseArray[loop];
                    sum+=responseArray[loop];
                }
            }
            if((byte)sum!=responseArray[numread-2]) {
                    lasterror="error in response from RCX";                  
                    rcxError=true;
                    numretry++;
                    continue;
            }
            if(responseArray[echolen+3]!=~lastcommandArray[3])
            {
                lasterror="error - invalid response from RCX";
                rcxError=true;
                numretry++;
                continue;
            }

            if(!rcxError&&!towerError)
            {
                numretry=0;
                if((message[0]&(byte)0x08)==(byte)0x08) {
                    message[0]=(byte)(message[0]&(byte)0xf7);
                }
                processMessage(message);
                if(allmsgListener!=null)
                    allmsgListener.receivedMessage(message);
                if(rcxListener!=null)
                    rcxListener.receivedMessage(message);
                break;
            }

        } //while retries

        if(towerError || rcxError) {
            System.err.println("Error: "+lasterror); 
            if(errorListener!=null)
                errorListener.receivedError(lasterror);
            if(rcxListener!=null)
                rcxListener.receivedError(lasterror);
            message=null;
            numretry=0;
        }

    }

    /** retrieves the last error that occurred
     */
    public String getLastError() {
        return lasterror;
    }   

    /** returns the return value of the last message processed
     * (if it returned a value)
     */
    public int getLastValue() {
        return lastreturnval;
    }   

    /** utility to return a string representation of an array
     * or first part of an array (given a length)
     */
    public static String ArrayToString(byte[] message, int length) {
        StringBuffer strbuffer = new StringBuffer();
        int abyte = 0;        
        for(int loop = 0; loop < length; loop++) {
            abyte = (int) message[loop];
            if (abyte < 0) abyte += 256;
            strbuffer.append(Integer.toHexString(abyte) + " ");
        }
        return strbuffer.toString();
    }

    /** processes messages by converting return values
     * and storing the return value internally until
     * retrieved by getLastValue()
     */
    public void processMessage(byte[] message) {
        int lobyte=0,hibyte=0;
        if (message == null || message.length!=3)
            return;
        try {
            lobyte = (int)message[1];
            hibyte = (int)message[2];
            if(lobyte < 0) lobyte += 256;
            if(hibyte < 0) hibyte += 256;
            lastreturnval = (hibyte<<8);
            lastreturnval = lastreturnval | lobyte;
        } catch(Exception e) {
            if(debug) e.printStackTrace();
        }
    }

    /** sets error message and sends it to the
     * errorListener
     * @see RCXErrorListener */
    public void setError(String msg) {
        if(errorListener!=null)
            errorListener.receivedError(msg);
    }

    /** play system sound (0) */
    public void beep() {
        systemSound(true, 0);
    }

    /** plays system sound (1) */
    public void twoBeeps() {
        systemSound(true, 1);
    }

    /** plays system sound (2) */
    public void beepSequence() {
        systemSound(true, 2);
    }

    /** plays system sound (3) */
    public void beepSequenceUp() {
        systemSound(true, 3);
    }

    /** plays system sound (4) */
    public void buzz() {
        systemSound(true, 4);
    }

    /** plays system sound (5) */
    public void upwardTones() {
        systemSound(true, 5);
    }

    /** plays a system sound (0-5)<br>
     * 0:Blip 1:Beep beep 2:Downward tones 3:Upward tones
     * 4:Low buzz 5:Fast upward tones<br>
     * (note: queued not applied here -
     *    used just for compatiblity with leJOS)
     */
    public void systemSound(boolean queued, int code) {
        byte[] msg = {0x51, 0x0};
        msg[1] = (byte)code;
        write(msg);
    }

  /**
   * Plays a tone, given its frequency and duration.
   * Frequency is audible from about 31 to 2100 Hertz.
   * The duration argument is in hundreds of a seconds and is truncated
   * at 256, so the maximum duration of a tone is 2.56 seconds.
   * @param aFrequency The frequency of the tone in Hertz (Hz).
   * @param aDuration The duration of the tone, in centiseconds.
   * Value is truncated at 256 centiseconds.
   */
    public void playTone(int frequency, int duration) {
        byte[] msg = {0x23, 0x0, 0x0, 0x0};
        int hifreq = frequency >> 8;
        msg[1] = (byte)(frequency & 0xff);
        msg[2] = (byte)hifreq;
        msg[3] = (byte)(duration/10);
        write(msg);
    }

    /**
     * @param aMotor 'A', 'B' or 'C'.
     * @param aMode  1=forward, 2=backward, 3=stop, 4=float
     * @param aPower 0-7
     */
    public void controlMotor(char id, short mode, short power) {
        if(isEmulation) {
            System.out.print("Motor "+id+" is ");
            switch(mode) {
                case 1: System.out.print("moving forward"); break;
                case 2: System.out.print("moving backard"); break;
                case 3: System.out.print("stopping"); break;
                case 4: System.out.print("stopping (floating)"); break;
            }
           System.out.println(".");
        } else {
            byte[] motors = { (byte)0x01, (byte)0x02, (byte)0x04 };
            byte motor=(byte)(id-65);

            byte[] dir =   { (byte)0x80, (byte)0x0 };
            byte[] onoff = { (byte)0x80, (byte)0x80, (byte)0x40, (byte)0x0 };

            if(power>7) power=7; else if(power<0) power=0;
            byte pwr =  (byte)power;

            byte src = (byte)3;

            //set direction:
            if(mode>0&&mode<3) {
                byte[] dircmd = { (byte)0xe1, (byte)(dir[mode-1]|motors[motor]) };
                write(dircmd);
            }
            //set power:
            byte[] pwrcmd = { (byte)0x13, motor, src,(byte)pwr };
            write(pwrcmd);
            byte[] cmd = { (byte)0x21, (byte)(onoff[mode-1]|motors[motor]) };
            write(cmd);
        }
    }

    /** @param sensorId Sensor ID (0..2).
     *  @param requestType 0 = raw value, 1 = canonical value, 2 = boolean value.
     */
    public int readSensorValue(int sensorId, int requestType) {
        byte[] typevals = {(byte)0x12, (byte)0x09, (byte)0x13 };
        if(isEmulation)
            return Sensor.SENSORS[sensorId].emulval;
        byte[] cmd = { (byte)0x12, typevals[requestType], (byte) sensorId };
        write(cmd);
        return getLastValue();
    }

    /** sets the sensorID value and type
     *  (if in emulation mode, sets the value to be read)
     * @see Sensor
     * @see SensorConstants
     */
    public void setSensorValue(int sensorId, int val, int requestType) {
        if(isEmulation) {
            Sensor.SENSORS[sensorId].emulval=val;
            return;           
        }
        byte[] cmd = { (byte)0x32, (byte)sensorId, (byte)val };
        if(requestType==1) {
            write(cmd);
        } else if(requestType==0) {
            cmd[0]=(byte)0x42;
            write(cmd);
        }        
    }

    /** gets the battery power level in volts */
    public float getBatteryPower() {
        byte[] msg = {0x30};
        write(msg);
        return ((float)getLastValue())/1000;
    }
}

/**
 * Used to swap input and output streams for use with RCXServer.
 * @see RCXServer
 */
class StreamSwap implements Runnable, AllMessagesListener {
    RCXPort rcxport;
    Thread swap;
    boolean isRunning;
    InputStream is;
    OutputStream os;
    byte[] array;
    int numread=0;

    StreamSwap(InputStream istr, OutputStream ostr, RCXPort port) {
        isRunning=true;
        os=ostr;
        is=istr;
        rcxport = port;
        array = new byte[4096];
        rcxport.addAllMessagesListener(this);
        swap = new Thread(this);
        swap.start();
    }
    public void run() {
        try {
            while(isRunning) {
                numread=is.read(array);
                if(numread<0) {
                    rcxport.setError("Exception: numread="+numread);
                    break;
                } else {
                    byte[] msg = new byte[numread];
                    System.arraycopy(array,0,msg,0,numread);
                    rcxport.write(msg);
                }
            }
        } catch(Exception e) {
            rcxport.setError("Exception: "+e);
        }
    }
    public void receivedMessage(byte[] msg) {
        try {
            os.write(msg);
            os.flush();
        } catch(Exception e) {
            rcxport.setOpen(false);
        }
    }
    public void stop() {
        isRunning=false;
    }
}

