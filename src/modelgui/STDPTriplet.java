
package modelgui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author ianchristie
 */

public class STDPTriplet extends PlasticityMethod
{
    double tau_plus;
    double tau_minus;
    double tau_x;
    double tau_y;
    double A2_plus;
    double A2_minus;
    double A3_plus;
    double A3_minus;
    
    double T0;
    
    public transient JTextField tau_plusField;
    public transient JTextField tau_minusField;
    public transient JTextField tau_xField;
    public transient JTextField tau_yField;
    public transient JTextField A2_plusField;
    public transient JTextField A2_minusField;
    public transient JTextField A3_plusField;
    public transient JTextField A3_minusField;
    public transient JTextField Gmax_maxField;
    public transient JTextField Gmax_minField;
    public transient JTextField T0Field;
    
    public STDPTriplet()
    {
        tau_plus = 0.01668;
        tau_minus = 0.0337;
        tau_x = 0.101;
        tau_y = 0.125;
        A2_plus = 5e-5;
        A2_minus = 7e-3;

        A3_plus = 6.2e-3;
        A3_minus = 2.3e-4;
    
        T0 = 0;
    }
    
    public double updateGmax(double weight, double time)
    {
        Set<Double> s;
        ArrayList<Double> dT = new ArrayList<Double>();
        ArrayList<Double> triplet_dT = new ArrayList<Double>();

        double doublet = 0.0;
        double triplet = 0.0;
        
        if (pre.approxContains(time)) 
        {
            for (Double d: post.spiketimelist) 
            {
                if(d.doubleValue()<=time) 
                {
                    dT.add(time-d.doubleValue());
                }
            }
            for(Double d: pre.spiketimelist) 
            {
                if(d.doubleValue() < time) 
                {  //strictly less!!!
                    triplet_dT.add(time-d.doubleValue());
                }
            }
            doublet = 0.0;
            for (Double d: dT) 
            {
                doublet += Math.exp(-1*d.doubleValue()/tau_minus);
            }
            triplet = 0.0;
            for (Double d: triplet_dT) 
            {
                triplet += Math.exp(-1*d.doubleValue()/tau_x);
            }
            weight += -1*doublet*(A2_minus+A3_minus*triplet);
            dT.clear();
            triplet_dT.clear();
          }
        
        if(post.approxContains(time))
        {
            for(Double d: pre.spiketimelist)
            {
                if(d.doubleValue() <= time) 
                {
                    dT.add(d.doubleValue() -time);
                }
            }
            for(Double d: post.spiketimelist)
            {
                if(d.doubleValue() < time)
                { //strictly less
                    triplet_dT.add(time- d.doubleValue());
                }
            }
            doublet = 0.0;
            for(Double d: dT) 
            {
                doublet += Math.exp(d.doubleValue() / tau_plus);
            }
            triplet = 0.0;
            for(Double d: triplet_dT) 
            {
                triplet += Math.exp(-1*d.doubleValue() / tau_y);
            }
            weight += doublet * (A2_plus+A3_plus*triplet);
            dT.clear();
            triplet_dT.clear();
        }

        if (weight <= Gmax_max && weight >=Gmax_min) 
        {
            return weight;
        }
        else if(weight > Gmax_max) 
        {
            return Gmax_max;
        }
        else 
        {
            return Gmax_min;
        }
    }
    
    public JPanel getJPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11,2));
        
        JLabel tau_plusLabel = new JLabel("Tau Plus:\t");
        JLabel tau_minusLabel = new JLabel("Tau Minus:\t");
        JLabel tau_xLabel = new JLabel("Tau X:\t");
        JLabel tau_yLabel = new JLabel("Tau Y:\t");
        JLabel A2_plusLabel = new JLabel("A2 Plus:\t");
        JLabel A2_minusLabel = new JLabel("A2 Minus:\t");
        JLabel A3_plusLabel = new JLabel("A3 Plus:\t");
        JLabel A3_minusLabel = new JLabel("A3 Minus:\t");
        JLabel Gmax_maxLabel = new JLabel("Conductance Max:\t");
        JLabel Gmax_minLabel = new JLabel("Conductance Min:\t");
        JLabel T0Label = new JLabel("T0:\t");
        
        tau_plusField = new JTextField(String.valueOf(tau_plus));
        tau_minusField = new JTextField(String.valueOf(tau_minus));
        tau_xField = new JTextField(String.valueOf(tau_x));
        tau_yField = new JTextField(String.valueOf(tau_y));
        A2_plusField = new JTextField(String.valueOf(A2_plus));
        A2_minusField = new JTextField(String.valueOf(A2_minus));
        A3_plusField = new JTextField(String.valueOf(A3_plus));
        A3_minusField = new JTextField(String.valueOf(A3_minus));
        Gmax_maxField = new JTextField(String.valueOf(Gmax_max));
        Gmax_minField = new JTextField(String.valueOf(Gmax_min));
        T0Field = new JTextField(String.valueOf(T0));
        
        panel.add(tau_plusLabel);
        panel.add(tau_plusField);
        panel.add(tau_minusLabel);
        panel.add(tau_minusField);
        panel.add(tau_xLabel);
        panel.add(tau_xField);
        panel.add(tau_yLabel);
        panel.add(tau_yField);
        panel.add(A2_plusLabel);
        panel.add(A2_plusField);
        panel.add(A2_minusLabel);
        panel.add(A2_minusField);
        panel.add(A3_plusLabel);
        panel.add(A3_plusField);
        panel.add(A3_minusLabel);
        panel.add(A3_minusField);
        panel.add(Gmax_maxLabel);
        panel.add(Gmax_maxField);
        panel.add(Gmax_minLabel);
        panel.add(Gmax_minField);
        panel.add(T0Label);
        panel.add(T0Field); 
    
        return panel;
    }
    
    public boolean logFields()
    {
        try
        {
            this.tau_plus = Double.parseDouble(tau_plusField.getText());
            this.tau_minus = Double.parseDouble(tau_minusField.getText());
            this.tau_x = Double.parseDouble(tau_xField.getText());
            this.tau_y = Double.parseDouble(tau_yField.getText());
            this.A2_plus = Double.parseDouble(A2_plusField.getText());
            this.A2_minus = Double.parseDouble(A2_minusField.getText());
            this.A3_plus = Double.parseDouble(A3_plusField.getText());
            this.A3_minus = Double.parseDouble(A3_minusField.getText());
            this.Gmax_max = Double.parseDouble(Gmax_maxField.getText());
            this.Gmax_min = Double.parseDouble(Gmax_minField.getText());
            this.T0 = Double.parseDouble(T0Field.getText());
        }catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "There have been Number Format Excetions in STDP Classic.\nPlease check to make sure you have entered in correct formatted data.");
            return false;
        }
        return true;
    }
}
