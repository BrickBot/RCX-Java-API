import rcx.*;

public class Test {

    public static void main(String[] arg) { 
        new Test(arg[0]);
    }

    public Test(String portname) {
        RCXPort port = new RCXPort(portname);
        Motor.A.forward();
        Motor.A.stop();
        port.beep();
        System.out.println("Battery Level = "+port.getBatteryPower()+" volts.");
    }
}
