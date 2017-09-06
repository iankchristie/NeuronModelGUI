
package modelgui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author ianchristie
 */
public class Model implements Serializable
{
    private static final long serialVersionUID = 7526472295622776147L;
    public ArrayList<Modelel> elements;
    public double startT, dT, maxTime;
    public int numTimes;
    
    
    public transient double width;
    public transient double height;
    public transient double size;
    public transient Modelel avatar;
    
    public transient JFrame frame;
    
    public transient JTextField startTField;
    public transient JTextField maxTField;
    public transient JTextField dTField;
    public transient JTextField numTimesField;
    
    public Model(double size, int numElements)
    {
        startT = 0.0;
        dT = .001;
        maxTime = 1.0;
        numTimes = 1;
        this.width =size;
        this.height = size;
        this.size=size;
        this.avatar = new Modelel();
        this.avatar.s = States.avatar;
        elements = new ArrayList<Modelel>();
    }

    public void addElement(Modelel s) 
    {
        elements.add(s);
        s.index = elements.size();
    }

    public void clearElements()
    {
        elements.clear();
    }

    public void unselectAll() 
    {
        for(Modelel m: elements)
        {
            m.selected = false;
        }
    }

    private void print() 
    {
        for(Modelel m: elements)
        {
            System.out.println("Type:\t"+m.type+"\tName:\t"+m.name);
        }
    }
    
    public String ExportTypeMatlab()
    {
        String export = "<STRUCT size=[1 "+String.valueOf(elements.size())+"]  fields={ 'modelel', 'logged' } data=\n";
        for(Modelel m: elements)
        {
            export+= m.exportTypeMatlab();
        }
        export += "/STRUCT>";
        return export;
    }
    
    public String ExportTypeC()
    {
        String export = "";
        for(Modelel m: elements)
        {
            export+= m.exportTypeC(true);
            export+="\n";
        }
        return export;
    }

    void clearAllSpikeTimes() 
    {
        for(Modelel m: elements)
        {
            if(m instanceof SpikeTimeList)
            {
                SpikeTimeList s = (SpikeTimeList) m;
                s.spiketimelist.clear();
            }
        }
    }

    void clearIntSpikeTimes() 
    {
        for(Modelel m: elements)
        {
            if(m instanceof IntFireLeaky)
            {
                SpikeTimeList s = (SpikeTimeList) m;
                s.spiketimelist.clear();
            }
        }
    }

    void openDialog() 
    {
        frame = new JFrame("Element Diaglog");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(250,150);
        frame.setLocationRelativeTo( null ); 
        
        frame.addWindowListener(new WindowAdapter() {
            public void WindowClosing(WindowEvent e) {
                System.out.println("test");
                frame.dispose();
            }
        });
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,2));
        
        //Create Labels
        JLabel startTLabel = new JLabel("Start Time (sec):\t");
        JLabel deltaTLabel = new JLabel("Delta Time: \t");
        JLabel maxTLabel = new JLabel("End Time:\t");
        JLabel numTimesLabel = new JLabel("Number of runs:\t");
        
        //Create Fields
        startTField = new JTextField(20);
        startTField.setText(String.valueOf(this.startT));
        dTField = new JTextField(20);
        dTField.setText(String.valueOf(this.dT));
        maxTField = new JTextField(20);
        maxTField.setText(String.valueOf(this.maxTime));
        numTimesField = new JTextField(20);
        numTimesField.setText(String.valueOf(this.numTimes));
        
        //Add
        panel.add(startTLabel);
        panel.add(startTField);
        panel.add(deltaTLabel);
        panel.add(dTField);
        panel.add(maxTLabel);
        panel.add(maxTField);
        panel.add(numTimesLabel);
        panel.add(numTimesField);
        
        JButton close = new JButton("Save");
        close.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    if(logFields())
                                    {
                                        frame.dispose();
                                    }
                                }
                        });
        frame.add(close,BorderLayout.SOUTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

    }
    
    public boolean logFields()
    {
        try
        {
            this.startT = Double.parseDouble(startTField.getText());
            this.dT = Double.parseDouble(dTField.getText());
            this.maxTime = Double.parseDouble(maxTField.getText());
            this.numTimes = Integer.parseInt(numTimesField.getText());
        }catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "There have been Number Format Excetions.\nPlease check to make sure you have entered in correct formatted data.");
            return false;
        }
        return true;
    }

    public void undoElement() 
    {
        elements.remove(elements.size()-1);
    }

    public void clearClearableSpikeTimes() 
    {
        for(Modelel m: elements)
        {
            if(m instanceof SpikeTimeList)
            {
                SpikeTimeList s = (SpikeTimeList) m;
                if(s.clearSpikes)
                {
                    s.spiketimelist.clear();
                }
            }
        }
    }
}
