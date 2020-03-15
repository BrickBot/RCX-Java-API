import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class RCXControlApp extends Frame implements WindowListener
{
    private RCXControl controlPanel;

    public static void main(String[] args) {
        if(args.length>0) {
    	    new RCXControlApp(args[0]);
        } else System.out.println("Usage: RCXControlApp portname");
    }

    public RCXControlApp(String portName) {
        super("RCX Control Application");
    	
        addWindowListener(this);
        
        controlPanel = new RCXControl(portName);
        setLayout(new FlowLayout());

        add(controlPanel);
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screen.width/5,screen.height/5,280,150);
        setVisible(true);
    }

    public void windowActivated(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowOpened(WindowEvent e) { }
    public void windowClosing(WindowEvent e) {
        if(controlPanel !=null)
            controlPanel.close();
        System.exit(0);
    }
}
