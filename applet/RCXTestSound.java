import rcx.RCXPort;
import rcx.RCXErrorListener;
import rcx.Motor;
import rcx.Sensor;
import rcx.SensorConstants;

public class RCXTestSound implements RCXErrorListener {

    private static RCXPort port;
    private static int motor;
    private static int direction;
    private static int power;

    public static void main(String[] args) {
        String portName = null;
        int freq = 31;
        int dur = 255;

        if(args.length>0)
            portName = args[0];
        else if(args.length==0) {
            System.out.println("Usage: RCXTest portname");
            return;
        }

        try {
            if(args.length>1)
                freq = Integer.parseInt(args[1]);
            if(args.length>2)
                dur = Integer.parseInt(args[2]);
        } catch(Exception e) {
        }

        new RCXTestSound(portName,freq,dur);
    }

    public RCXTestSound(String portName, int freq, int dur) {

        port = new RCXPort(portName);
        port.addRCXErrorListener(this);

        System.out.println("battery power left: "
                               +port.getBatteryPower()+" volts");

//        port.beep();
//        port.twoBeeps();
//        port.beepSequence();
//        port.beepSequenceUp();
//        port.buzz();
//        port.upwardTones();

        port.playTone(freq,dur);

//          for(int loop=3000;loop>30;loop-=25) {
//              port.playTone(loop,freq);
//          }
    }

    public void receivedError(String error) {
        System.exit(1);
    }
}
