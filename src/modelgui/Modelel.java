
package modelgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * @author ianchristie
 */

public class Modelel implements Serializable
{
    Color color;
    String name;
    String type;
    States s;
    double T, dT;
    double x,y;
    double radius;
    boolean selected;
    int index;
    boolean log;
    
    Map<Double, Double> logMap;
    
    public transient Board board;
    
    public transient JFrame frame;
    public transient JTabbedPane tabbedPane;
    public transient JTextField nameField;
    public transient JTextField xField;
    public transient JTextField yField;
    public transient JTextField radiusField;
    public transient JRadioButton logTrueButton;
    public transient JRadioButton logFalseButton;
    
    public Modelel()
    {
        color = Color.BLACK;
        name = "";
        type = "";
        x = 0;
        y = 0;
        radius = 10;
        selected = false;
        
        log = false;
        logMap = new TreeMap<Double, Double>();
        
        T= 0;
        dT = .001;
    }
    
    public boolean contains(double x, double y)
    {
        return (x <= this.x+this.radius && x>= this.x - this.radius && y <= this.y + this.radius && y >= this.y - this.radius);
    }

    void drawElement(Graphics g, Board aThis) 
    {
        int theRadius = aThis.toViewCoords(this.radius);
        int x1 = aThis.toViewCoords(this.x);
        int y1 = aThis.toViewCoords(this.y);
        
        if(this.selected)
        {
            g.setColor(Color.YELLOW);
        }
        else
        {
            g.setColor(this.color);
        }
        g.drawOval(x1-theRadius,y1-theRadius, 2*theRadius, 2*theRadius);
        g.drawString(this.type, x1- 40, y1);
        g.drawString(this.name, x1-40, y1+15);
    }

    public void openDialog(boolean first) 
    {
        frame = new JFrame("Element Diaglog");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(350,250);
        frame.setLocationRelativeTo(null);  
        
        tabbedPane = new JTabbedPane();
        
        JPanel modelelTab = new JPanel();
        modelelTab.setLayout(new GridLayout(5,2));
        
        //Create Labels
        JLabel nameLabel = new JLabel("Name:\t");
        JLabel xLabel = new JLabel("X-coordinate: (0<x<100)\t");
        JLabel yLabel = new JLabel("Y-coordinate: (0<x<100)\t");
        JLabel radiusLabel = new JLabel("Radius:\t");
        
        //Create Fields
        nameField = new JTextField(20);
        nameField.setText(String.valueOf(this.name));
        xField = new JTextField(20);
        xField.setText(String.valueOf(this.x));
        yField = new JTextField(20);
        yField.setText(String.valueOf(this.y));
        radiusField = new JTextField(20);
        radiusField.setText(String.valueOf(this.radius));
        logTrueButton = new JRadioButton("Log On");
        logFalseButton = new JRadioButton("Log Off");
        if(log)
        {
            logTrueButton.setSelected(true);
        }
        else
        {
            logFalseButton.setSelected(true);
        }
        ButtonGroup group = new ButtonGroup();
        group.add(logTrueButton);
        group.add(logFalseButton);
        
        //Add
        modelelTab.add(nameLabel);
        modelelTab.add(nameField);
        modelelTab.add(xLabel);
        modelelTab.add(xField);
        modelelTab.add(yLabel);
        modelelTab.add(yField);
        modelelTab.add(radiusLabel);
        modelelTab.add(radiusField);
        modelelTab.add(logTrueButton);
        modelelTab.add(logFalseButton);
        
        tabbedPane.addTab("Modelel", null,
                          modelelTab,
                          "Model Element Attributes"); //tooltip text
        
    }
    
    public boolean logFields()
    {
        this.name = nameField.getText();
        try
        {
            this.x = Double.parseDouble(xField.getText());
            this.y = Double.parseDouble(yField.getText());
            this.radius = Double.parseDouble(radiusField.getText());
            if(logTrueButton.isSelected())
            {
                log = true;
            }
            else
            {
                log = false;
            }
        }catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "There have been Number Format Excetions.\nPlease check to make sure you have entered in correct formatted data.");
            return false;
        }
        return true;
    }
    
    public void endDialog()
    {
        JButton close = new JButton("Save");
        close.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    if(logFields())
                                    {   
                                        frame.dispose();
                                        selected = false;
                                        board.repaint();
                                    }
                                }
                        });
        frame.add(tabbedPane,BorderLayout.CENTER);
        frame.add(close,BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    /*
     * <<<STRUCT size=[1 1]  fields={ 'type', 'T', 'dT', 'name', 'x', 'y', 'radius' } data=
          <<'spiketimelistel'><[0]><[0.0001]><'cell1'><[4]><[55]><[10]>>
     /STRUCT>><[]>>
     */
    public String exportTypeMatlab()
    {
        String export = "<<<STRUCT size=[1 1]  fields={ 'type', 'T', 'dT', 'name', 'x', 'y', 'radius' } data=\n";
        export+="\t<<'modelel'><["+String.valueOf(T) +"]><["+String.valueOf(dT)+"]><'"+name+"'><["+String.valueOf(x)+"]><["+String.valueOf(y)+"]><["+String.valueOf(radius)+"]>>\n";
        export+="/STRUCT>><[]>>\n";
        return export;
    }
    
    public String exportTypeC(boolean first)
    {
        String s = "";
        if (first) {
            s+= "type:\t "+type+"\n";
            first = false;
        }
        s+= "Logging:\t\n";
        s+= "index:\t" + String.valueOf(index) + "\n";
        s+= "modelel:<\n";
        s+= "T:\t" + T + "\n";
        s+= "dT:\t" + dT + "\n";
        s+= "name:\t" + name + "\n>\n";
        return s;
    }
    
    public void step()
    {
        //System.out.println(2);
        this.T += this.dT;
    }

    public void log() 
    {
        if(log)
        {
            logMap.put(T, dT);
        }
    }

    public void display() 
    {
        for(Map.Entry<Double, Double>  entry: logMap.entrySet()) 
        {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }
    
    public boolean approxEquals(double d1, double d2)
    {
        double epsilon = .000000001;
        return Math.abs(d1-d2) <= epsilon;
    }
}
