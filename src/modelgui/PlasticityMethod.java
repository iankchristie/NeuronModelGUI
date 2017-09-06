
package modelgui;

import java.io.Serializable;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author ianchristie
 */

class PlasticityMethod implements Serializable
{
    SpikeTimeList pre;
    SpikeTimeList post;
    double Gmax_max;
    double Gmax_min;
    
    public PlasticityMethod()
    {
        pre = null;
        post = null;
        Gmax_max = Double.MAX_VALUE;
        Gmax_min = Double.MIN_VALUE;
    }
    
    public double updateGmax(double weight, double time)
    {
        return 0.0;
    }
    
    public JPanel getJPanel() 
    {
        JPanel panel = new JPanel();
        return panel;
    }
    
    public boolean logFields()
    {
        return false;
    }
}
