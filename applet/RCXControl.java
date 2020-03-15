import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import rcx.RCXPort;
import rcx.RCXErrorListener;
import rcx.Motor;
import rcx.Sensor;
import rcx.SensorConstants;

public class RCXControl extends Panel implements ActionListener,
                                        RCXErrorListener, Runnable
{
    private String    portName;
    private RCXPort   rcxPort;
    private Thread    thisThread;
    private boolean   isRunning;
    private Panel     topPanel,bottomPanel;
    private TextField sensorField1,sensorField2,sensorField3;
    private Panel   motorPanel1,motorPanel2,motorPanel3;
    private Button  motor1fwd,motor1bwd,motor1stop,motor1float;
    private Button  motor2fwd,motor2bwd,motor2stop,motor2float;
    private Button  motor3fwd,motor3bwd,motor3stop,motor3float;
    private int s1value,s1prevalue,s2value,s2prevalue,s3value,s3prevalue;
    
    public RCXControl(String portname) {

        portName = portname;
    	        
        topPanel  = new Panel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER,4,4));
        bottomPanel = new Panel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
        motorPanel1 = new Panel();
        motorPanel1.setLayout(new BorderLayout(5,5));        
        motorPanel2 = new Panel();
        motorPanel2.setLayout(new BorderLayout(5,5));
        motorPanel3 = new Panel();
        motorPanel3.setLayout(new BorderLayout(5,5));

        sensorField1 = new TextField(9);
        sensorField1.setEditable(false);
        sensorField1.setEnabled(false);
        sensorField2 = new TextField(9);
        sensorField2.setEditable(false);
        sensorField2.setEnabled(false);
        sensorField3 = new TextField(9);
        sensorField3.setEditable(false);
        sensorField3.setEnabled(false);

        motor1fwd = new Button("forward");
        motor1fwd.addActionListener(this);
        motor2fwd = new Button("forward");
        motor2fwd.addActionListener(this);
        motor3fwd = new Button("forward");
        motor3fwd.addActionListener(this);
        motor1bwd = new Button("backward");
        motor1bwd.addActionListener(this);
        motor2bwd = new Button("backward");
        motor2bwd.addActionListener(this);
        motor3bwd = new Button("backward");
        motor3bwd.addActionListener(this);
        motor1stop = new Button("stop");
        motor1stop.addActionListener(this);
        motor2stop = new Button("stop");
        motor2stop.addActionListener(this);
        motor3stop = new Button("stop");
        motor3stop.addActionListener(this);
        motor1float = new Button("float");
        motor1float.addActionListener(this);
        motor2float = new Button("float");
        motor2float.addActionListener(this);
        motor3float = new Button("float");
        motor3float.addActionListener(this);
        
        topPanel.add(sensorField1);
        topPanel.add(sensorField2);
        topPanel.add(sensorField3);

        motorPanel1.add(motor1fwd,"North");
        motorPanel1.add(motor1bwd,"South");
        motorPanel1.add(motor1float,"East");
        motorPanel1.add(motor1stop,"West");
        motorPanel2.add(motor2fwd,"North");
        motorPanel2.add(motor2bwd,"South");
        motorPanel2.add(motor2float,"East");
        motorPanel2.add(motor2stop,"West");
        motorPanel3.add(motor3fwd,"North");
        motorPanel3.add(motor3bwd,"South");
        motorPanel3.add(motor3float,"East");
        motorPanel3.add(motor3stop,"West");

        bottomPanel.add(motorPanel1);
        bottomPanel.add(motorPanel2);
        bottomPanel.add(motorPanel3);

        setLayout(new BorderLayout());
        add(topPanel,"North");
        add(bottomPanel,"South");
        setBackground(Color.yellow);

        thisThread = new Thread(this);
        thisThread.start();
    }

    public void run() {
        rcxPort = new RCXPort(portName);
        rcxPort.addRCXErrorListener(this);

        Sensor.S1.setTypeAndMode(SensorConstants.SENSOR_TYPE_LIGHT,
                                   SensorConstants.SENSOR_MODE_PCT);
        Sensor.S2.setTypeAndMode(SensorConstants.SENSOR_TYPE_LIGHT,
                                   SensorConstants.SENSOR_MODE_PCT);
        Sensor.S3.setTypeAndMode(SensorConstants.SENSOR_TYPE_LIGHT,
                                   SensorConstants.SENSOR_MODE_PCT);
        isRunning=true;

        sensorField1.setText("0");
        sensorField2.setText("0");
        sensorField3.setText("0");

        while(isRunning) {
            // this delay can be adjusted or eliminated
            try{Thread.sleep(1000);}catch(Exception e) { }

            s1value=Sensor.S1.readValue();
            if(s1value!=s1prevalue) {
                s1prevalue=s1value;
                sensorField1.setText(Integer.toString(s1value));
            }
            s2value=Sensor.S2.readValue();
            if(s2value!=s2prevalue) {
                s2prevalue=s2value;
                sensorField2.setText(Integer.toString(s2value));
            }
            s3value=Sensor.S3.readValue();
            if(s3value!=s3prevalue) {
                s3prevalue=s3value;
                sensorField3.setText(Integer.toString(s3value));
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj==motor1fwd) {
            Motor.A.forward();
        }
        else if(obj==motor1bwd) {
            Motor.A.backward();
        }
        else if(obj==motor1stop) {
            Motor.A.stop();
        }
        else if(obj==motor1float) {
            Motor.A.flt();
        }
        else if(obj==motor2fwd) {
            Motor.B.forward();
        }
        else if(obj==motor2bwd) {
            Motor.B.backward();
        }
        else if(obj==motor2stop) {
            Motor.B.stop();
        }
        else if(obj==motor2float) {
            Motor.B.flt();
        }
        else if(obj==motor3fwd) {
            Motor.C.forward();
        }
        else if(obj==motor3bwd) {
            Motor.C.backward();
        }
        else if(obj==motor3stop) {
            Motor.C.stop();
        }
        else if(obj==motor3float) {
            Motor.C.flt();
        }
    }

    public void receivedError(String error) {
        System.err.println("Error: "+error);
        close();
    }
        
    public void close() {
        isRunning=false;
        if(rcxPort!=null)
            rcxPort.close();
    }
}
