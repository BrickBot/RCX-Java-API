import rcx.RCXPort;
import rcx.RCXErrorListener;
import rcx.Motor;
import rcx.Sensor;
import rcx.SensorConstants;

public class RCXTest implements RCXErrorListener {

    private static RCXPort port;
    private static int motor;
    private static int direction;
    private static int power;

    public static void main(String[] args) {
        String portName = null;
        if(args.length>0)
            portName = args[0];
        else if(args.length==0) {
            System.out.println("Usage: RCXTest portname");
            return;
        }

        new RCXTest(portName);
    }

    public RCXTest(String portName) {

        port = new RCXPort(portName);
        port.addRCXErrorListener(this);

        if(portName.equals("EMULATION")) new RCXSensorEmulation();

        System.out.println("battery power left: "
                               +port.getBatteryPower()+" volts");

        Motor.A.forward();

        try { Thread.sleep(500); } catch(Exception e) { }

        Motor.A.backward();

        try { Thread.sleep(500); } catch(Exception e) { }

        Motor.A.stop();

        port.beep();

        Sensor.S1.setTypeAndMode(SensorConstants.SENSOR_TYPE_TOUCH,
                                   SensorConstants.SENSOR_MODE_BOOL);
        Sensor.S2.setTypeAndMode(SensorConstants.SENSOR_TYPE_LIGHT,
                                   SensorConstants.SENSOR_MODE_PCT);
        Sensor.S3.setTypeAndMode(SensorConstants.SENSOR_TYPE_TOUCH,
                                   SensorConstants.SENSOR_MODE_BOOL);

        int s2light,s2prevlight=0;
        boolean s1touch,s3touch,s1prevtouch=true,s3prevtouch=true;

        while(true) {
            s1touch=Sensor.S1.readBooleanValue();
            if(s1touch!=s1prevtouch) {
                s1prevtouch=s1touch;
                System.out.println("reading sensor 1: "+s1touch);
            }
            s2light=Sensor.S2.readValue();
            if(s2light!=s2prevlight) {
                s2prevlight=s2light;
                System.out.println("reading sensor 2: "+s2light);
            }
            s3touch=Sensor.S3.readBooleanValue();
            if(s3touch!=s3prevtouch) {
                s3prevtouch=s3touch;
                System.out.println("reading sensor 3: "+s3touch);
            }
        }
    }

    public void receivedError(String error) {
        System.exit(1);
    }
}
