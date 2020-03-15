package rcx;

/**
 * Motor - encapsulates access to RCX Motors<br>
 * This class is based on leJOS's class of the same name
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class Motor
{
    public static Motor A, B, C;
    public static Motor[] MOTORS = { A, B, C };

    private char id;
    private short mode = 4;
    private short power = 3;
    private RCXPort port;

    /** creates a motor where id is 'A', 'B' or 'C' for specified port */ 
    public Motor(char id, RCXPort port) {
        this.id = id;
        this.port = port;
    }

    /** sets rcx port to use and creates the static instances A, B, and C */
    public static void setPort(RCXPort port) {
        A=new Motor('A',port);
        B=new Motor('B',port);
        C=new Motor('C',port);
        MOTORS[0]=A;
        MOTORS[1]=B;
        MOTORS[2]=C;
    }

    public final char getId() {
        return id;
    }

    public final void setPower(int p) {
        power = (short) p;
        port.controlMotor(id, mode, power);
    }

    public final void forward() {
        mode = 1;
        port.controlMotor(id, mode, power);
    }
  
    public final boolean isForward() {
        return (mode == 1);
    }

    public final void backward() {
        mode = 2;
        port.controlMotor(id, mode, power);
    }

    public final boolean isBackward() {
        return (mode == 2);
    }

    public final void reverseDirection() {
        if (mode == 1 || mode == 2) {
            mode = (short) (3 - mode);
            port.controlMotor(id, mode, power);
        }
    }

    public final int getPower() {
        return power;	  
    }

    public final boolean isMoving() {
        return (mode == 1 || mode == 2);	  
    }
  
    public final boolean isFloating() {
        return mode == 4;	  
    }

    public final void stop() {
        mode = 3;
        power = 0;
        port.controlMotor(id, mode, (short)7);
    }

    public final boolean isStopped() {
        return (mode == 3);
    }

    /** floats the motor (like stop() but it doesn't brake
     - keeps the wheels spinning freely) */ 
    public final void flt() {
        mode = 4;
        port.controlMotor(id, mode, power);
    }
}
