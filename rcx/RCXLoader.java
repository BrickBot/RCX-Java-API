package rcx;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*; 

/**
 * RCXLoader - sample communications test with lookup table 
 * @author Dario Laverde
 * @version 2.1
 * Copyright 2002 Dario Laverde, under terms of GNU LGPL
 */
public class RCXLoader extends Frame implements ActionListener, WindowListener, RCXListener
{
    private String      portName;
    private RCXPort     rcxPort;
    private Panel       textPanel;
    private Panel       topPanel;
    private TextArea    textArea;
    private TextField   textField;
    private Button      tableButton;
    private Properties  parameters;
    private int inByte;
    private int charPerLine = 48;
    private int lenCount;
    private StringBuffer sbuffer;
    private byte[] byteArray;
    
    public static void main(String[] args) {
    	new RCXLoader();
    }

    public RCXLoader() {
    	super("RCX Loader");
    	
        addWindowListener(this);
        
        topPanel  = new Panel();
        topPanel.setLayout(new BorderLayout());

        tableButton = new Button("table");
        tableButton.addActionListener(this);
        
        textField = new TextField();
        textField.setEditable(false);
        textField.setEnabled(false);
        tableButton.setEnabled(false);
        textField.addActionListener(this);

        textPanel = new Panel();
        textPanel.setLayout(new BorderLayout(5,5));        

        topPanel.add(textField,"Center");
        topPanel.add(tableButton,"East");
        textPanel.add(topPanel,"North");

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier",Font.PLAIN,12));
        textPanel.add(textArea,"Center");

        add(textPanel, "Center");

        textArea.setText("initializing...\n");
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(screen.width/2-370/2,screen.height/2-370/2,370,370);
        setVisible(true);

    	  File f = new File("parameters.txt");
    	  if (!f.exists()) {
    	      f = new File(System.getProperty("user.dir")
                         + System.getProperty("file.separator")
                         + "parameters.txt");
    	  }
    	  if (f.exists()) {
    	      try {
                  FileInputStream fis = new FileInputStream(f);
                  parameters = new Properties();
                  parameters.load(fis);
                  fis.close();
                  portName = parameters.getProperty("port");
    	      } catch (IOException e) {
                e.printStackTrace();
            }
    	  }
        
        rcxPort = new RCXPort(portName);
        rcxPort.addRCXListener(this);

        tableButton.setEnabled(true);        
        
        if(rcxPort.isOpen()) {
            String lasterror = rcxPort.getLastError();
            if(lasterror!=null)
                textArea.append(lasterror+"\n");
            else
                textArea.append("RCXPort ready.\n");
            textField.setEditable(true);
            textField.setEnabled(true);
            textField.requestFocus();            
        }
        else {
            textArea.append("Failed to create RCXPort with "+portName+"\n");            
        }
    }

    public void receivedMessage(byte[] responseArray) {
        if(responseArray==null)
            return;
        for(int loop=0;loop<responseArray.length;loop++) {       
            int newbyte = (int)responseArray[loop];
            if(newbyte<0) newbyte=256+newbyte;
            sbuffer = new StringBuffer(Integer.toHexString(newbyte));
            
            if(sbuffer.length()<2)
                sbuffer.insert(0,'0');
            textArea.append(sbuffer+" ");
            lenCount+=3;
            if(lenCount==charPerLine) {
                lenCount=0;
                textArea.append("\n");
            }
        }
        if(lenCount!=charPerLine)
            textArea.append("\n");        
    }
    public void receivedError(String error) {
        textArea.append(error+"\n");
    }
        
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj==textField) {
            String strInput = textField.getText();
            textField.setText("");
            textArea.append("> "+strInput+"\n");
            byteArray = RCXOpcode.parseString(strInput);
            if(byteArray==null) {
                textArea.append("Error: illegal hex character or length\n");
                return;
            }
            if(rcxPort!=null) {
                if(!rcxPort.write(byteArray)) {
                    textArea.append("Error: writing data to port "+portName+"\n");
                }
            }
        }
        else if(obj==tableButton) {
            RCXOpcode.showTable();
            setLocation(0,getLocation().y);
        }
    }

    public void windowActivated(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowOpened(WindowEvent e) { }
    public void windowClosing(WindowEvent e) {
        if(rcxPort!=null)
        	rcxPort.close();
        System.exit(0);
    }
}
