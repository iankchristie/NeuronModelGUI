
package modelgui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Map;
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

public class IntFireLeaky extends SpikeTimeList
{
    ArrayList<Synapse> synapseList;
    
    double Ie;
    double V_leak;
    double Rm;
    double Taum;
    double Area;
    double V_reset;
    double V_threshold;
    double V;
    double V_spike;
    
    public transient JTextField ieField;
    public transient JTextField vField;
    public transient JTextField v_leakField;
    public transient JTextField rmField;
    public transient JTextField tauField;
    public transient JTextField areaField;
    public transient JTextField v_resetField;
    public transient JTextField v_thresholdField;
    public transient JTextField v_spikeField;
    
    public IntFireLeaky()
    {
        super();
        color = Color.RED;
        
        type = "IntFireLeaky";
        s = States.intfireleaky;
        
        synapseList = new ArrayList<Synapse>();
        
        Ie =  0.0;
        V_leak = -0.075;
        Rm= 10e6;
        Taum= 10e-3;
        Area = 0.1;
        V_reset =  -0.080;
        V_threshold =  -0.055;
        V =  -0.075;
        V_spike =  0.010;
    }
    
    public void openDialog(boolean first) 
    {
        super.openDialog(false);
        frame.setTitle("Integrate and Fire Leaky Dialog");
        frame.setSize(400,300);
        
        JPanel intfireleakyTab = new JPanel();
        intfireleakyTab.setLayout(new GridLayout(9,2));
        
        //Create Labels
        JLabel ieLabel = new JLabel("Electric Current (Amps):\t");
        JLabel vLabel = new JLabel("Voltage (volts):\t");
        JLabel v_leakLabel = new JLabel("Voltage Leak (volts):\t");
        JLabel rmLabel = new JLabel("Input Resistance (ohms):\t");
        JLabel tauLabel = new JLabel("Membrane Time Constant (sec):\t");
        JLabel areaLabel = new JLabel("Area (mm^2):\t");
        JLabel v_resetLabel = new JLabel("Reset Potential (volts):\t");
        JLabel v_thresholdLabel = new JLabel("Threashold Value (volts):\t");
        JLabel v_spikeLabel = new JLabel("Spike Potential (volts):\t");
        
        //Create Fields
        ieField = new JTextField(20);
        ieField.setText(String.valueOf(Ie));
        vField = new JTextField(20);
        vField.setText(String.valueOf(V));
        v_leakField = new JTextField(20);
        v_leakField.setText(String.valueOf(V_leak));
        rmField = new JTextField(20);
        rmField.setText(String.valueOf(Rm));
        tauField = new JTextField(20);
        tauField.setText(String.valueOf(Taum));
        areaField = new JTextField(20);
        areaField.setText(String.valueOf(Area));
        v_resetField = new JTextField(20);
        v_resetField.setText(String.valueOf(V_reset));
        v_thresholdField = new JTextField(20);
        v_thresholdField.setText(String.valueOf(V_threshold));
        v_spikeField = new JTextField(20);
        v_spikeField.setText(String.valueOf(V_spike));
        
        //Add
        intfireleakyTab.add(ieLabel);
        intfireleakyTab.add(ieField);
        intfireleakyTab.add(vLabel);
        intfireleakyTab.add(vField);
        intfireleakyTab.add(v_leakLabel);
        intfireleakyTab.add(v_leakField);
        intfireleakyTab.add(rmLabel);
        intfireleakyTab.add(rmField);
        intfireleakyTab.add(tauLabel);
        intfireleakyTab.add(tauField);
        intfireleakyTab.add(areaLabel);
        intfireleakyTab.add(areaField);
        intfireleakyTab.add(v_resetLabel);
        intfireleakyTab.add(v_resetField);
        intfireleakyTab.add(v_thresholdLabel);
        intfireleakyTab.add(v_thresholdField);
        intfireleakyTab.add(v_spikeLabel);
        intfireleakyTab.add(v_spikeField);
        
        tabbedPane.addTab("IntFireLeaky", null,
                          intfireleakyTab,
                          "Integrate and Fire Leaky Element Attributes"); //tooltip text
        
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
            this.Ie = Double.parseDouble(ieField.getText());
            this.V_leak = Double.parseDouble(v_leakField.getText());
            this.Rm = Double.parseDouble(rmField.getText());
            this.Taum = Double.parseDouble(tauField.getText());
            this.Area = Double.parseDouble(areaField.getText());
            this.V_reset = Double.parseDouble(v_resetField.getText());
            this.V_threshold = Double.parseDouble(v_thresholdField.getText());
            this.V = Double.parseDouble(vField.getText());
            this.V_spike = Double.parseDouble(v_spikeField.getText());
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "There have been Number Format Excetions.\nPlease check to make sure you have entered in correct formatted data.");
            return false;
        }
        return true;
    }
    
    /*
     * <<<STRUCT size=[1 1]  fields={ 'type', 'T', 'dT', 'spiketimes', 'name', 'Ie', 'V_leak', 'Rm', 'Taum', 'Area', 'V_reset', 'V_threshold', 'V', 'synapse_list', 'V_spike' } data=
          <<'intfireleakyel'><[0]><[0.0001]><[]><'output'><[0]><[-0.075]><[10000000]><[0.01]><[0.1]><[-0.08]><[-0.055]><[-0.075]><[6  7  8  9]><[0.01]>>
     /STRUCT>><'T,V'>>
     */
    public String exportTypeMatlab()
    {
        String export = "<<<STRUCT size=[1 1]  fields={ 'type', 'T', 'dT', 'spiketimes', 'name', 'Ie', 'V_leak', 'Rm', 'Taum', 'Area', 'V_reset', 'V_threshold', 'V', 'synapse_list', 'V_spike', 'x', 'y', 'radius' } data=\n";
        export+="\t<<'intfireleakyel'><["+String.valueOf(T) +"]><["+String.valueOf(dT)+"]><["+spikePrint()+"]><'"+name+"'>";
        export+="<["+String.valueOf(Ie) +"]><["+String.valueOf(V_leak)+"]><["+String.valueOf(Rm)+"]><["+String.valueOf(Taum)+"]><["+String.valueOf(Area)+"]><["+String.valueOf(V_reset)+"]><["+String.valueOf(V_threshold)+"]><["+String.valueOf(V)+"]><["+synapsePrint()+"]><["+String.valueOf(V_spike)+"]><["+String.valueOf(x)+"]><["+String.valueOf(y)+"]><["+String.valueOf(radius)+"]>>\n";
        export+="/STRUCT>><[]>>\n";
        return export;
    }

    public String synapsePrint() 
    {
        String temp ="";
        for(Synapse s: synapseList)
        {
            temp+=String.valueOf(s.index);
            temp+=" ";
        }
        return temp;
    }
    
    public String exportTypeC(boolean first)
    {
        String s="";
        if (first) {
            s+= "type:\t"+type+"\n";
            first = false;
        }
        s+= super.exportTypeC(first);
        s+= "intfireleakyel:<\n";
        s+= "Ie:\t" + String.valueOf(Ie) + "\n";
        s+= "V_leak:\t" + String.valueOf(V_leak) + "\n";
        s+= "Rm:\t" + String.valueOf(Rm )+ "\n";
        s+= "Taum:\t" + String.valueOf(Taum) + "\n";
        s+= "Area:\t" + String.valueOf(Area) + "\n";
        s+= "V_reset:\t" + String.valueOf(V_reset) + "\n";
        s+= "V_threshold:\t" + String.valueOf(V_threshold) + "\n";
        s+= "V:\t" + String.valueOf(V) + "\n";
        s+= "V_spike:\t" + String.valueOf(V_spike) + "\n>\n";
        return s;
    }
    
    public void step()
    {
        double dVsyn =  0.0;
        double dVdt =  0.0;
        
        //I we're spiking we need to go back to the resting potential
        if(V >= V_spike) 
        {
            V=V_reset;
        }
        
        //Go through connections and sum input
        for(Synapse s: synapseList) 
        {
            dVsyn -= (Rm/Area) * s.G * (V-(s.V_rev));
        }
        
        dVdt = (-1*(V-V_leak)+dVsyn+Ie*Rm)/Taum;

        V += dVdt * dT;
        T+=dT;
        

        //We're SPIKINGGGG!!!!
        if(V>V_threshold) 
        {
            V = V_spike;
            spiketimelist.add(T);
        }
    }
    
    public void log() 
    {
        if(log)
        {
            if(approxContains())
            {
                logMap.put(T, V_spike);
            }
            else
            {
                logMap.put(T, V);
            }
        }
        
    }
    
    public void display()
    {
        if(log)
        {
            XYSeries series = new XYSeries("XYGraph");
            for(Map.Entry<Double, Double>  entry: logMap.entrySet()) 
            {
                series.add(entry.getKey(), entry.getValue());
            }
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(series);

            JFreeChart chart = ChartFactory.createXYLineChart(
                name+" Voltage vs. Time",
                "Time (sec)",
                "Voltage (V)",
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
