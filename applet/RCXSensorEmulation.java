import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import rcx.Sensor;

public class RCXSensorEmulation implements Runnable
{
    private Thread    thisThread;
    private boolean   isRunning;
    
    public RCXSensorEmulation() {
    	thisThread = new Thread(this);
        thisThread.start();      
    }

    public void run() {
        isRunning=true;
        while(isRunning) {
            try {Thread.sleep(1000);} catch(Exception e) { }
            switch((int)(Math.random()*5)+1) {
                case 1: Sensor.S1.setPreviousValue((int)(Math.random()*1024)+1);
                    break;
                case 2: Sensor.S2.setPreviousValue((int)(Math.random()*1024)+1);
                    break;
                case 3: Sensor.S3.setPreviousValue((int)(Math.random()*1024)+1);
                    break;
                case 4: try {Thread.sleep(3000);} catch(Exception e) { }
                    break;
            }
        }
    }

    public void stop() {
        isRunning=false;
    }
}

