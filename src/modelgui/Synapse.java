
package modelgui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author ianchristie
 */
class Synapse extends Modelel
{
    SpikeTimeList pre;
    IntFireLeaky post;
    
    PlasticityMethod plasticity;
    STDPClassic stdpc;
    STDPTriplet stdpt;
    
    double x2,y2;
    
    double V_rev;
    double Gmax;
    double G;
    
    double Tpast_ignore = 1;
    boolean inProcess;
    
    public transient JTextField v_revField;
    public transient JTextField gmaxField;
    public transient JTextField gField;
    public transient JTextField tpast_ignoreField;
    public final String[] strings = { "none", "STDP Classic", "STDP Triplet"};
    public transient JComboBox list;
    
    public Synapse()
    {
        super();
        type = "Synapse";
        s = States.synapset1t2;
        
        inProcess = true;
        
        radius = 5;
        
        pre = null;
        post = null;
        
        plasticity = null;
        stdpc = new STDPClassic();
        stdpt = new STDPTriplet();
        
        V_rev = 0.00;
        Gmax =  10e-9;
        G =  0.0;
        Tpast_ignore = 1.0;
    }
    
    public void setPre(SpikeTimeList s)
    {
        pre = s;
        this.x = s.x;
        this.y = s.y;
    }
    
    public void setPost(IntFireLeaky i)
    {
        post = i;
        x2 = i.x;
        y2 = i.y;
        
        i.synapseList.add(this);
        
        inProcess = false;
    }
    
    public boolean contains(double x, double y)
    {   
        return (x <= (((pre.x+post.x)/2)+this.radius) && x>= (((pre.x+post.x)/2)-this.radius) && y <= (((pre.y+post.y)/2)+this.radius) && y >= (((pre.y+post.y)/2)-this.radius));
    }
    
    void drawElement(Graphics g, Board aThis) 
    {
        int theRadius = aThis.toViewCoords(this.radius);
        int x1 = aThis.toViewCoords(pre.x);
        int y1 = aThis.toViewCoords(pre.y);
        
        int _x2 = aThis.toViewCoords(post.x);
        int _y2 = aThis.toViewCoords(post.y);
        
        if(this.selected)
        {
            g.setColor(Color.YELLOW);
        }
        else
        {
            g.setColor(this.color);
        }
        
        g.drawLine(x1,y1, _x2, _y2);
        g.drawString(this.type, (x1+_x2)/2, (y1+_y2)/2);
        g.drawString(this.name, (x1+_x2)/2, (y1+_y2)/2+15);
        
    } 
    
    public void openDialog(boolean first) 
    {
        super.openDialog(false);
        frame.setTitle("Synapset1t2 Dialog");
        
        JPanel synapseTab = new JPanel();
        
        synapseTab.setLayout(new GridLayout(5,2));
        
        //Create Labels
        JLabel v_revLabel = new JLabel("V_rev:\t");
        JLabel gmaxLabel = new JLabel("Conductance max:\t");
        JLabel gLabel = new JLabel("Conductance:\t");
        JLabel tpast_ignoreLabel = new JLabel("Time Past Ignore:\t");
                
        //Create Fields
        v_revField = new JTextField(100);
        v_revField.setText(String.valueOf(V_rev));
        gmaxField = new JTextField(100);
        gmaxField.setText(String.valueOf(Gmax));
        gField = new JTextField(100);
        gField.setText(String.valueOf(G));
        tpast_ignoreField = new JTextField(100);
        tpast_ignoreField.setText(String.valueOf(Tpast_ignore));
        
        //create jcombobox
        list = new JComboBox(strings);
        if(plasticity == null)
        {
            list.setSelectedIndex(0);
        }
        else if (plasticity instanceof STDPClassic)
        {
            list.setSelectedIndex(1);
        }
        else
        {
            list.setSelectedIndex(2);
        }
        list.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    int li = list.getSelectedIndex();
                                    if(li == 0)
                                    {
                                        tabbedPane.setEnabledAt(2,false);
                                        tabbedPane.setEnabledAt(3,false);
                                    }
                                    else if (li == 1)
                                    {
                                        tabbedPane.setEnabledAt(2,true);
                                        tabbedPane.setEnabledAt(3,false);
                                    }
                                    else if (li == 2)
                                    {
                                        tabbedPane.setEnabledAt(2,false);
                                        tabbedPane.setEnabledAt(3,true);
                                    }
                                    else
                                    {
                                        System.out.println("Something messed up");
                                    }
                                }
                        });
        
        //Add
        synapseTab.add(v_revLabel);
        synapseTab.add(v_revField);
        synapseTab.add(gmaxLabel);
        synapseTab.add(gmaxField);
        synapseTab.add(gLabel);
        synapseTab.add(gField);
        synapseTab.add(tpast_ignoreLabel);
        synapseTab.add(tpast_ignoreField);
        synapseTab.add(list);

        JPanel stdpcPanel = stdpc.getJPanel();
        JPanel stdptPanel = stdpt.getJPanel();
        
        
        tabbedPane.addTab("Synapse", null,synapseTab,"Synapse Element Attributes"); //tooltip text
        tabbedPane.addTab("STDP Classic", null,stdpcPanel,"Spike Timing Dependent Plasticity Classic");
        tabbedPane.addTab("STDP Triplet", null,stdptPanel,"Spike Timing Dependent Plasticity Triplet");
       
        if(!(plasticity instanceof STDPClassic))
        {
            tabbedPane.setEnabledAt(2,false);
        }
        if(!(plasticity instanceof STDPTriplet))
        {
            tabbedPane.setEnabledAt(3,false);
        }
        
        if(first)
        {
            endDialog();
        }
    }
    
    public boolean logFields()
    {
        super.logFields();
        try
        {
            this.V_rev = Double.parseDouble(v_revField.getText());
            this.Gmax = Double.parseDouble(gmaxField.getText());
            this.G = Double.parseDouble(gField.getText());
            this.Tpast_ignore = Double.parseDouble(tpast_ignoreField.getText());
            int li = list.getSelectedIndex();
            stdpc.logFields();
            stdpt.logFields();
            if(li == 0)
            {
                plasticity = null;
            }
            else if (li == 1)
            {
                plasticity = stdpc;
                plasticity.pre = this.pre;
                plasticity.post = this.post;
            }
            else if (li == 2)
            {
                plasticity = stdpt;
                plasticity.pre = this.pre;
                plasticity.post = this.post;
            }
            else
            {
                System.out.println("Something messed up");
            }
        }catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "There have been Number Format Excetions.\nPlease check to make sure you have entered in correct formatted data.");
            return false;
        }
        return true;
    }
    
    public String exportTypeC(boolean first)
    {
        String s="";
        if (first) {
            s+= "type:\t"+type+"\n";
            first = false;
        }
        s+= super.exportTypeC(first);
        s+= "synapse:<\n";
        s+= "V_rev:\t" + String.valueOf(V_rev) + "\n";
        s+= "Gmax:\t" + String.valueOf(Gmax) + "\n";
        s+= "G:\t" + String.valueOf(G) + "\n";
        if (pre!=null) {
            s+= "pre:\t" + pre.index + "\n";
        }
        else {
            s+= "pre:\tnone\n";
        }
        if (post!=null) {
            s+= "post:\t" + post.index + "\n";
        }
        else {
            s+= "post:\tnone\n";
        }

        s+= "Tpast_ignore:\t" + String.valueOf(Tpast_ignore) + "\n";
        if (plasticity!=null) 
        {
            s+=plasticity.toString();
        }
        else 
        {
            s+= "plasticity_method:\tnone\n";
        }
        s+=">\n";
        return s;
    }
    
    public void log() 
    {
        if(log)
        {
            logMap.put(T, G);
        }
    }
    
    public void display()
    {
        if(log)
        {
            XYSeries series = new XYSeries("XYGraph");
            for(Map.Entry<Double,Double>  entry: logMap.entrySet()) 
            {
                series.add(entry.getKey(), entry.getValue());
            }
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(series);

            JFreeChart chart = ChartFactory.createXYLineChart(
                name+" Conductance vs. Time",
                "Time",
                "Conductance",
                dataset,
                PlotOrientation.VERTICAL,  // Plot Orientation
                false,                      // Show Legend
                true,                      // Use tooltips
                false                      // Configure chart to generate URLs?
                );

            ChartFrame cFrame = new ChartFrame(name, chart);
            cFrame.setSize(200, 250);
            cFrame.setVisible(true);
        }
    }
}
