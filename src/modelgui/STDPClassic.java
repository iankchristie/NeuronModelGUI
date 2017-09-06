
package modelgui;

import java.awt.GridLayout;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author ianchristie
 */

public class STDPClassic extends PlasticityMethod
{
    double tau_plus;
    double tau_minus;
    double A_plus;
    double A_minus;
    
    double T0;
    
    public transient JTextField tau_plusField;
    public transient JTextField tau_minusField;
    public transient JTextField A_plusField;
    public transient JTextField A_minusField;
    public transient JTextField Gmax_maxField;
    public transient JTextField Gmax_minField;
    public transient JTextField T0Field;
    
    
    public STDPClassic()
    {
        tau_plus = 0.020;
        tau_minus = 0.020;
        A_plus = 0.005;
        A_minus = 0.005250;

        T0 = 0.0;
    }
    
    public double updateGmax(double weight, double time)
    {
        if (pre.approxContains(time)) 
        {
            for(Double d: post.spiketimelist)
            {
                if(d.doubleValue() <= time)
                {
                    weight += (-1*A_minus*Math.exp((-1*(time - d.doubleValue()))/tau_minus));
                }
            }
        }

        if(post.approxContains(time)) 
        {
            for(Double d: pre.spiketimelist)
            {
                weight += (A_plus*Math.exp((d.doubleValue() - time)/tau_plus));
            }
        }

        if (weight <=Gmax_max && weight >=Gmax_min) {
            return weight;
        }
        else if(weight > Gmax_max) {
            return Gmax_max;
        }
        else {
            return Gmax_min;
        }

    }
    
    public JPanel getJPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7,2));
        
        JLabel tau_plusLabel = new JLabel("Tau Plus:\t");
        JLabel tau_minusLabel = new JLabel("Tau Minus:\t");
        JLabel A_plusLabel = new JLabel("A Plus:\t");
        JLabel A_minusLabel = new JLabel("A Minus:\t");
        JLabel Gmax_maxLabel = new JLabel("Conductance Max:\t");
        JLabel Gmax_minLabel = new JLabel("Conductance Min:\t");
        JLabel T0Label = new JLabel("T0:\t");
        
        tau_plusField = new JTextField(String.valueOf(tau_plus));
        tau_minusField = new JTextField(String.valueOf(tau_minus));
        A_plusField = new JTextField(String.valueOf(A_plus));
        A_minusField = new JTextField(String.valueOf(A_minus));
        Gmax_maxField = new JTextField(String.valueOf(Gmax_max));
        Gmax_minField = new JTextField(String.valueOf(Gmax_min));
        T0Field =  new JTextField(String.valueOf(T0));
        
        panel.add(tau_plusLabel);
        panel.add(tau_plusField);
        panel.add(tau_minusLabel);
        panel.add(tau_minusField);
        panel.add(A_plusLabel);
        panel.add(A_plusField);
        panel.add(A_minusLabel);
        panel.add(A_minusField);
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
            this.A_plus = Double.parseDouble(A_plusField.getText());
            this.A_minus = Double.parseDouble(A_minusField.getText());
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
