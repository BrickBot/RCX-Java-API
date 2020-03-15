package rcx;

/** 
 * Sensor - encapsulates access to RCX Sensors<br>
 * This class is based on leJOS's class of the same name<br>
 * <br>
 * Before using a sensor, you should set its mode and type
 * with setTypeAndMode using constants defined in SensorConstants.
 * <p>
 * Example:<p>
 *   Sensor.S1.setTypeAndMode(SensorConstants.SENSOR_TYPE_LIGHT, SensorConstants.SENSOR_MODE_PCT);
 * @see SensorConstants
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class Sensor
{
    public static Sensor S1, S2, S3;
    public static Sensor[] SENSORS = { S1, S2, S3 };

    /** value used for emulation of sensor */
    public int emulval;

    private int sensorId;
    private RCXPort port;

    /** creates a sensor where id is 0, 1 or 2 for the specified RCX port */
    public Sensor(int id, RCXPort port) {
        this.sensorId = id;
        this.port = port;
    }

    /** creates the static instances S1, S2, S3 */
    public static void setPort(RCXPort port) {
        S1 = new Sensor(0,port);
        S2 = new Sensor(1,port);
        S3 = new Sensor(2,port);
        SENSORS[0]=S1;
        SENSORS[1]=S2;
        SENSORS[2]=S3;
    }

    public final int readValue() {
        return port.readSensorValue(sensorId, 1);
    }

    public final int readRawValue() {
        return port.readSensorValue(sensorId, 0);
    }

    public final boolean readBooleanValue() {
        return port.readSensorValue(sensorId, 1) > 0;
    }

    public final int getId() {
        return sensorId;
    }
    
    /** not used (for compatibility with leJOS class of the same name) */
    public final void activate() {
    }

    /** not used (for compatibility with leJOS class of the same name) */
    public final void passivate() {
    }

    /**
     * Sets the sensor's mode and type<br>
     * The default type is LIGHT and the default is PERCENT.
     * (note: mode can be OR'd with slope (0..31) )
     * @param aType 0 = RAW, 1 = TOUCH, 2 = TEMP, 3 = LIGHT, 4 = ROT.
     * @param aMode 0x00 = RAW, 0x20 = BOOL, 0x40 = EDGE, 0x60 = PULSE,
     *   0x80 = PERCENT, 0xA0 = DEGC, 0xC0 = DEGF, 0xE0 = ANGLE
     * @see SensorConstants
     */
    public final void setTypeAndMode(int aType, int aMode) {
        port.setSensorValue(sensorId, aType, 1);
        port.setSensorValue(sensorId, aMode, 0);    
    }

    /**
     * reset canonical sensor value, useful for rotation sensors 
     */
    public final void setPreviousValue(int aValue) {
        port.setSensorValue(sensorId, aValue, 2);    
    }
}
