import java.applet.*;
import java.awt.*;

public class RCXApplet extends Applet
{
    private RCXControl controlPanel;

    public void init() {
        setBackground(Color.yellow);

        String portName = getParameter("rcxport");

        controlPanel = new RCXControl(portName);
        add(controlPanel);        
    }
}

