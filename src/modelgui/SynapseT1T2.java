
package modelgui;

import java.awt.Color;
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

public class SynapseT1T2 extends Synapse
{
    double tau1;
    double tau2;
    
    public transient JTextField tau1Field;
    public transient JTextField tau2Field;
    
    public SynapseT1T2()
    {
        super();
                
        type = "Synapset1t2";
        
        tau1 = 0.001;
        tau2 = 0.050;
    }
    
    public void openDialog(boolean first) 
    {
        super.openDialog(false);
        frame.setTitle("Synapset1t2 Dialog");
        frame.setSize(550,300);
        
        JPanel synapset1t2Tab = new JPanel();
        
        synapset1t2Tab.setLayout(new GridLayout(2,2));
        
        //Create Labels
        JLabel tau1Label = new JLabel("Tau1:\t");
        JLabel tau2Label = new JLabel("Tau2:\t");
        
        //Create Fields
        tau1Field = new JTextField(100);
        tau1Field.setText(String.valueOf(tau1));
        tau2Field = new JTextField(100);
        tau2Field.setText(String.valueOf(tau2));
        
        //Add
        synapset1t2Tab.add(tau1Label);
        synapset1t2Tab.add(tau1Field);
        synapset1t2Tab.add(tau2Label);
        synapset1t2Tab.add(tau2Field);


        
        tabbedPane.addTab("SynapseT1T2", null,
                          synapset1t2Tab,
                          "SynapseT1T2 Attributes"); //tooltip text
        
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
            this.tau1 = Double.parseDouble(tau1Field.getText());
            this.tau2 = Double.parseDouble(tau2Field.getText());
        }catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "There have been Number Format Excetions.\nPlease check to make sure you have entered in correct formatted data.");
            return false;
        }
        return true;
    }
    
    /*
     * <<<STRUCT size=[1 1]  fields={ 'type', 'T', 'dT', 'name', 'V_rev', 'Gmax', 'G', 'tau1', 'tau2', 'pre', 'post', 'Tpast_ignore', 'plasticity_method', 'plasticity_params' } data=
          <<'synapset1t2el'><[0]><[0.0001]><[]><''><[0]><[3e-09]><[0]><[0.001]><[0.05]><[1]><[5]><[1]>>
     /STRUCT>><[]>>
     */
    public String exportTypeMatlab()
    {
        String export = "<<<STRUCT size=[1 1]  fields={ 'type', 'T', 'dT', 'name', 'V_rev', 'Gmax', 'G', 'tau1', 'tau2', 'pre', 'post', 'Tpast_ignore' } data=\n";
        export+="\t<<'synapset1t2el'><["+String.valueOf(T) +"]><["+String.valueOf(dT)+"]><'"+name+"'><["+String.valueOf(V_rev)+"]><["+String.valueOf(Gmax)+"]><["+String.valueOf(G)+"]><["+String.valueOf(tau1)+"]><["+String.valueOf(tau2)+"]><["+String.valueOf(pre.index)+"]><["+String.valueOf(post.index)+"]><["+String.valueOf(Tpast_ignore)+"]>>\n";
        export+="/STRUCT>><[]>>\n";
        return export;
    }
    
    public String exportTypeC(boolean first)
    {
        String s="";
        if (first) {
            s+= "type:\t"+type+"\n";
            first = false;
        }
        s+= super.exportTypeC(first);
        s+= "synapset1t2el:<\n";
        s+= "tau1:\t" + String.valueOf(tau1) + "\n";
        s+= "tau2:\t" + String.valueOf(tau2) + "\n>\n";
        return s;
    }
    
    public void step()
    {
        if(pre == null || post == null) {
            T+=dT;
            System.err.println("No pre or post synaptic terminal");
            return;
        }
    
        ArrayList<Double> deltaT = new ArrayList<Double>();
        Set<Double> vPre = pre.spiketimelist;
        double sum =  0.0;

        if(Tpast_ignore !=0) 
        {
            for (Double rit: vPre) 
            {
                double f = rit.doubleValue();
                if(f > T-Tpast_ignore) 
                {
                    //sanatize for only future
                    if(T-f > 0)
                    {
                        deltaT.add(T-f);
                    }
                }
            }
        }
        else 
        {
            deltaT.addAll(vPre);
        }

        if (!deltaT.isEmpty()) 
        {
            //calculate G
            for (Double f: deltaT) 
            {
                sum += Math.exp(-1*f/tau2)-Math.exp(-1*f/tau1);
            }
            G = Gmax * sum;
        }
        else 
        {
            G = 0.0;
        }

        T+=dT;


        if(plasticity!=null) 
        {
            Gmax = plasticity.updateGmax(Gmax, T);
        }
    }
}
