import java.io.*;
import rcx.comm.Win32USBPort;

public class TestUSB {

    public static void main(String args[]) {

        byte[] testArray = {(byte) 42 };

        Win32USBPort usbPort = new Win32USBPort();

        try {
            if(usbPort.open("LEGOTOWER1")<0) {
               System.err.println("Error opening USB port: is tower plugged in?");
               return;
            }
            OutputStream os = usbPort.getOutputStream();
            InputStream is  = usbPort.getInputStream();

            os.write(testArray);

            os.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
