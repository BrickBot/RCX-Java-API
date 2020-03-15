package rcx;

import java.awt.*; 
import java.awt.event.*;
import java.util.*;

/**
 * RCXOpcodes - encapsulates the opcode table with utilities
 * to display a lookup table and convert strings and byte arrays
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class RCXOpcode {
    
    public static Hashtable Opcodes=new Hashtable(55);
    
    public static byte PING             = (byte) 0x10;
    public static byte GETVAL           = (byte) 0x12;
    public static byte SETMOTORPOWER    = (byte) 0x13;
    public static byte SETVAL           = (byte) 0x14;
    public static byte GETVERSIONS      = (byte) 0x15;
    public static byte CALLSUB          = (byte) 0x17;
    public static byte GETMEMMAP        = (byte) 0x20;
    public static byte SETMOTOR         = (byte) 0x21;
    public static byte SETTIME          = (byte) 0x22;
    public static byte PLAYTONE         = (byte) 0x23;
    public static byte ADDVAR           = (byte) 0x24;
    public static byte DOWNLOADTASK     = (byte) 0x25;
    public static byte BRANCHALWAYSNEAR = (byte) 0x27;
    public static byte GETBATTERYPOWER  = (byte) 0x30;
    public static byte SETTRANSMITRANGE = (byte) 0x31;
    public static byte SETSENSORTYPE    = (byte) 0x32;
    public static byte SETDISPLAY       = (byte) 0x33;
    public static byte SUBFROMVAR       = (byte) 0x34;
    public static byte DOWNLOADSUB      = (byte) 0x35;
    public static byte DECLOOPCNTRNEAR  = (byte) 0x37;
    public static byte DELALLTASKS      = (byte) 0x40;
    public static byte SETSENSORMODE    = (byte) 0x42;
    public static byte WAIT             = (byte) 0x43;
    public static byte DIVIDEVAR        = (byte) 0x44;
    public static byte TRANSFERDATA     = (byte) 0x45;
    public static byte STOPALLTASKS     = (byte) 0x50;
    public static byte PLAYSOUND        = (byte) 0x51;
    public static byte SETDATALOGSIZE   = (byte) 0x52;
    public static byte MULTIPLYVAR      = (byte) 0x54;
    public static byte POWEROFF         = (byte) 0x60;
    public static byte DELTASK          = (byte) 0x61;
    public static byte DATALOGNEXT      = (byte) 0x62;
    public static byte SIGNVAR          = (byte) 0x64;
    public static byte DELFIRMWARE      = (byte) 0x65;
    public static byte DELALLSUBS       = (byte) 0x70;
    public static byte STARTTASK        = (byte) 0x71;
    public static byte BRANCHALWAYSFAR  = (byte) 0x72;
    public static byte ABSVAL           = (byte) 0x74;
    public static byte DOWNLOADFIRMWARE = (byte) 0x75;
    public static byte STOPTASK         = (byte) 0x81;
    public static byte SETLOOPCOUNTER   = (byte) 0x82;
    public static byte ANDVAR           = (byte) 0x84;
    public static byte TESTANDBRANCHNEAR= (byte) 0x85;
    public static byte CLEARMESSAGE     = (byte) 0x90;
    public static byte SETPROGRAMNUM    = (byte) 0x91;
    public static byte DECCNTRANDBRANCH = (byte) 0x92;
    public static byte ORVAR            = (byte) 0x94;
    public static byte TESTANDBRANCH    = (byte) 0x95;
    public static byte CLEARTIME        = (byte) 0xa1;
    public static byte UPLOADDATALOG    = (byte) 0xa4;
    public static byte UNLOCKFIRMWARE   = (byte) 0xa5;
    public static byte SETPOWERDOWN     = (byte) 0xb1;
    public static byte SENDMESSAGE      = (byte) 0xb2;
    public static byte DELSUB           = (byte) 0xc1;
    public static byte CLEARSENSOR      = (byte) 0xd1;
    public static byte REMOTECOMMAND    = (byte) 0xd2;
    public static byte SETMOTORDIR      = (byte) 0xe1;
    public static byte SETMESSAGE       = (byte) 0xf7;

    private static Frame opcodeFrame;
    private static TextArea opcodeTextArea;

    public RCXOpcode()
    {
        Opcodes.put(new Byte((byte)0x10),"PING            ,void, void,P");
        Opcodes.put(new Byte((byte)0x12),"GETVAL          ,byte src byte arg, short val,P");
        Opcodes.put(new Byte((byte)0x13),"SETMOTORPOWER   ,byte motors byte src byte arg, void,CP");
        Opcodes.put(new Byte((byte)0x14),"SETVAL          ,byte index byte src byte arg, void,CP");
        Opcodes.put(new Byte((byte)0x15),"GETVERSIONS     ,byte key[5], short rom[2] short firmware[2],P");
        Opcodes.put(new Byte((byte)0x17),"CALLSUB         ,byte subroutine, void,C");
        Opcodes.put(new Byte((byte)0x20),"GETMEMMAP       ,void, short map[94],P");
        Opcodes.put(new Byte((byte)0x21),"SETMOTOR        ,byte code, void,CP");
        Opcodes.put(new Byte((byte)0x22),"SETTIME         ,byte hours byte minutes, void,CP");
        Opcodes.put(new Byte((byte)0x23),"PLAYTONE        ,short frequency byte duration, void,CP");
        Opcodes.put(new Byte((byte)0x24),"ADDVAR          ,byte index byte src short arg, void,CP");
        Opcodes.put(new Byte((byte)0x25),"DOWNLOADTASK    ,byte unknown short task short length, byte error,P");
        Opcodes.put(new Byte((byte)0x27),"BRANCHALWAYSNEAR,byte offset, void,C");
        Opcodes.put(new Byte((byte)0x30),"GETBATTERYPOWER ,void, short millivolts,P");
        Opcodes.put(new Byte((byte)0x31),"SETTRANSMITRANGE,byte range, void,CP");
        Opcodes.put(new Byte((byte)0x32),"SETSENSORTYPE   ,byte sensor byte type, void,CP");
        Opcodes.put(new Byte((byte)0x33),"SETDISPLAY      ,byte src short arg, void,CP");
        Opcodes.put(new Byte((byte)0x34),"SUBFROMVAR      ,byte index byte src short arg, void,CP");
        Opcodes.put(new Byte((byte)0x35),"DOWNLOADSUB     ,byte unknown short subroutine short length, byte error,P");
        Opcodes.put(new Byte((byte)0x40),"DELALLTASKS     ,void, void,CP");
        Opcodes.put(new Byte((byte)0x42),"SETSENSORMODE   ,byte sensor byte code, void,CP");
        Opcodes.put(new Byte((byte)0x43),"WAIT            ,byte src short arg, void,C");
        Opcodes.put(new Byte((byte)0x44),"DIVIDEVAR       ,byte index byte src short arg, void,CP");
        Opcodes.put(new Byte((byte)0x45),"TRANSFERDATA    ,short index short length byte data[length] byte checksum, byte error,P");
        Opcodes.put(new Byte((byte)0x50),"STOPALLTASKS    ,void, void,CP");
        Opcodes.put(new Byte((byte)0x51),"PLAYSOUND       ,byte sound, void,CP");
        Opcodes.put(new Byte((byte)0x52),"SETDATALOGSIZE  ,short size, byte error,CP");
        Opcodes.put(new Byte((byte)0x54),"MULTIPLYVAR     ,byte index byte src short arg, void,CP");
        Opcodes.put(new Byte((byte)0x60),"POWEROFF        ,void, void,CP");
        Opcodes.put(new Byte((byte)0x61),"DELTASK         ,byte task, void,CP");
        Opcodes.put(new Byte((byte)0x62),"DATALOGNEXT     ,byte src byte arg, byte error,CP");
        Opcodes.put(new Byte((byte)0x64),"SIGNVAR         ,byte index byte src short arg, void,CP");
        Opcodes.put(new Byte((byte)0x65),"DELFIRMWARE     ,byte key[5], void,CP");
        Opcodes.put(new Byte((byte)0x70),"DELALLSUBS      ,void, void,CP");
        Opcodes.put(new Byte((byte)0x71),"STARTTASK       ,byte task, void,CP");
        Opcodes.put(new Byte((byte)0x72),"BRANCHALWAYSFAR ,byte offset byte extension, void,C");
        Opcodes.put(new Byte((byte)0x74),"ABSVAL          ,byte index byte src short arg, void,CP");
        Opcodes.put(new Byte((byte)0x75),"DOWNLOADFIRMWARE,short address short checksum byte unknown, void,P");
        Opcodes.put(new Byte((byte)0x81),"STOPTASK        ,byte task, void,CP");
        Opcodes.put(new Byte((byte)0x82),"SETLOOPCOUNTER  ,byte src byte arg, void,C");
        Opcodes.put(new Byte((byte)0x84),"ANDVAR          ,byte index byte src byte arg, void,CP");
        Opcodes.put(new Byte((byte)0x90),"CLEARMESSAGE    ,void, void,C");
        Opcodes.put(new Byte((byte)0x91),"SETPROGRAMNUM   ,byte program, void,CP");
        Opcodes.put(new Byte((byte)0x92),"DECCNTRANDBRANCH,short offset, void,C");
        Opcodes.put(new Byte((byte)0x94),"ORVAR           ,byte index byte src byte arg, void,CP");
        Opcodes.put(new Byte((byte)0x95),"TESTANDBRANCH   ,byte opsrc1 byte src2 short arg1 byte arg2 short offset,void,C");
        Opcodes.put(new Byte((byte)0xa1),"CLEARTIME       ,byte timer, void,CP");
        Opcodes.put(new Byte((byte)0xa4),"UPLOADDATALOG   ,short first short count, dlrec data[length],P");
        Opcodes.put(new Byte((byte)0xa5),"UNLOCKFIRMWARE  ,byte key[5], byte data[25],P");
        Opcodes.put(new Byte((byte)0xb1),"SETPOWERDOWN    ,byte minutes, void,CP");
        Opcodes.put(new Byte((byte)0xb2),"SENDMESSAGE     ,byte src byte arg, void,C");
        Opcodes.put(new Byte((byte)0xc1),"DELSUB          ,byte subroutine, void,CP");
        Opcodes.put(new Byte((byte)0xd1),"CLEARSENSOR     ,byte sensor, void,CP");
        Opcodes.put(new Byte((byte)0xe1),"SETMOTORDIR     ,byte code, void,CP");
        Opcodes.put(new Byte((byte)0xf7),"SETMESSAGE      ,byte message, void,PC");
    }
    
    /** a popup frame window that displays a lookup table of all the opcodes 
     * - can be called from anywhere e.g. RCXOpcode.showTable() */
    public static void showTable()
    {
        if(opcodeFrame!=null)
        {
            opcodeFrame.dispose();
            opcodeFrame=null;
            return;
        }
        opcodeFrame = new Frame("RCX Opcodes Table");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        opcodeFrame.setBounds(screen.width/2-70,0,screen.width/2+70,screen.height);
        opcodeTextArea = new TextArea("   Opcode          ,parameters, response, C=program command P=remote command\n",60,100);
        opcodeTextArea.setFont(new Font("Courier",Font.PLAIN,12));
        opcodeFrame.add(opcodeTextArea);
        Enumeration k = Opcodes.keys();
        for (Enumeration e = Opcodes.elements(); e.hasMoreElements();) {
            String tmp = Integer.toHexString(((Byte)k.nextElement()).intValue());
            tmp = tmp.substring(tmp.length()-2)+" "+(String)e.nextElement()+"\n";
            opcodeTextArea.append(tmp);
        }
        opcodeTextArea.setEditable(false);
        opcodeFrame.setVisible(true);
    }
    
    /** utility to convert a string (can include spaces) to a byte array */
    public static byte[] parseString(String str) {
        byte[] bArray = null;
        str = str.trim();
        
        int index = str.indexOf(' ');
        while(index>=0) {
            str = str.substring(0,index)+str.substring(index+1);
            index = str.indexOf(' ');
        }

        int len = str.length();
        if(len%2!=0)
            return null;
        len = len/2;
        bArray = new byte[len];

        str = str.toUpperCase();
        index=0;
        byte[] tempArray = str.getBytes();
        for(int loop=0; loop<len; loop++)
        {
            if(tempArray[index]>70)
                return null;
            tempArray[index]-=48;
            if(tempArray[index]>16) tempArray[index]-=7;
            if(tempArray[index+1]>70)
                return null;
            tempArray[index+1]-=48;
            if(tempArray[index+1]>16)
                tempArray[index+1]-=7;
            bArray[loop] = (byte)(16 * tempArray[index] + tempArray[index+1]);
            index+=2;
        }
        return bArray;
    }

    /** utility to convert a byte array to a string */
    public static String ArrayToString(byte[] message){
        if (message == null) return null;
        StringBuffer strbuffer = new StringBuffer();
        for(int loop = 0; loop < message.length; loop++) {
            int abyte = (int) message[loop];
            if (abyte < 0) abyte += 256;
            strbuffer.append(Integer.toHexString(abyte) + " ");
        }
        return strbuffer.toString();
    }
}
