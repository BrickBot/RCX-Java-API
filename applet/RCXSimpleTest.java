import rcx.RCXPort;
import rcx.RCXErrorListener;

public class RCXSimpleTest implements RCXErrorListener {

    public static void main(String[] args) {
        String cmd = null;
        if(args.length>0)
            cmd = args[0];
        new RCXSimpleTest(cmd);
    }

    public RCXSimpleTest(String cmd) {

        RCXPort port = new RCXPort(cmd);
        port.addRCXErrorListener(this);

        // set motor A direction (forwards)
        port.send("e181");
        // turn motor A on
        port.send("2181");

        // play sound
        port.send("5105");

        //delay for a sec
        try { Thread.sleep(1000); } catch(Exception e) { }

        // turn motor A off (float, 2141 is stop)
        port.send("2141");
    }

    public void receivedMessage(byte[] message) {

        // simply convert to String and print the message
        StringBuffer strbuffer = new StringBuffer();
        for(int loop = 0; loop < message.length; loop++) {
            int abyte = (int) message[loop];
            if (abyte < 0) abyte += 256;
            strbuffer.append(Integer.toHexString(abyte) + " ");
        }
        System.out.println("Response: " + strbuffer.toString());
    }

    public void receivedError(String error) {
        System.err.println("Error: " + error);
    }
}
