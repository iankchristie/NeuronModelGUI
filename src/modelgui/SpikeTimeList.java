
package modelgui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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

public class SpikeTimeList extends Modelel
{
    Set<Double> spiketimelist;
    boolean clearSpikes;
    
    public transient JTextField spikeListField;
    public transient JRadioButton clearTrueButton;
    public transient JRadioButton clearFalseButton;
    
    public SpikeTimeList()
    {
        super();
        
        color = Color.BLUE;
        
        type = "SpikeTimeList";
        s = States.spiketimelist;
        spiketimelist = new TreeSet<Double>();
        clearSpikes = false;
    }
    
    public void openDialog(boolean first) 
    {
        super.openDialog(false);
        frame.setTitle("Spike Time List Dialog");
        
        JPanel spikeTimeListTab = new JPanel();
        
        spikeTimeListTab.setLayout(new GridLayout(4,2));
        
        //Create Labels
        JLabel spikeListLabel = new JLabel("Spike Time List:\t");

        
        //Create Fields
        this.spikeListField = new JTextField(100);
        String temp ="";
        for(Double f: spiketimelist)
        {
            temp+=String.valueOf(f);
            temp+=" ";
        }
        spikeListField.setText(temp);
        
        clearTrueButton = new JRadioButton("Clear On");
        clearFalseButton = new JRadioButton("Clear Off");
        if(clearSpikes)
        {
            clearTrueButton.setSelected(true);
        }
        else
        {
            clearFalseButton.setSelected(true);
        }
        ButtonGroup group = new ButtonGroup();
        group.add(clearTrueButton);
        group.add(clearFalseButton);
        
        //Add
        spikeTimeListTab.add(spikeListLabel);
        spikeTimeListTab.add(spikeListField);
        spikeTimeListTab.add(clearTrueButton);
        spikeTimeListTab.add(clearFalseButton);

        
        tabbedPane.addTab("SpikeTimeList", null,
                          spikeTimeListTab,
                          "Spike Time List Element Attributes"); //tooltip text
        
        if(first)
        {
            endDialog();
        }
    }
    
    public boolean logFields()
    {
        super.logFields();
        spiketimelist.clear();
        String temp = spikeListField.getText();
        if(!temp.isEmpty())
        {
            String[] totes = temp.split(" ");
            for(int k = 0; k < totes.length; k++)
            {
                try
                {
                    spiketimelist.add(Double.parseDouble(totes[k]));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, "There have been Number Format Excetions.\nPlease check to make sure you have entered in correct formatted data.");
                    return false;
                }
            }
        }
        if(clearTrueButton.isSelected())
        {
            clearSpikes = true;
        }
        else
        {
            clearSpikes = false;
        }
        return true;
    }
    
    /*
     * <<<STRUCT size=[1 1]  fields={ 'type', 'T', 'dT', 'spiketimes', 'name' } data=
          <<'spiketimelistel'><[0]><[0.0001]><[0.0001]><'cell1'>>
     /STRUCT>><[]>>
     */
    public String exportTypeMatlab()
    {
        String export = "<<<STRUCT size=[1 1]  fields={ 'type', 'T', 'dT', 'spiketimes', 'name', 'x', 'y', 'radius' } data=\n";
        export+="\t<<'spiketimelistel'><["+String.valueOf(T) +"]><["+String.valueOf(dT)+"]><["+spikePrint()+"]><'"+name+"'><["+String.valueOf(x)+"]><["+String.valueOf(y)+"]><["+String.valueOf(radius)+"]>>\n";
        export+="/STRUCT>><[]>>\n";
        return export;
    }

    public String spikePrint() 
    {
        String temp = "";
        for(Double f: spiketimelist)
        {
            temp+=String.valueOf(f);
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
        s+= "spiketimelistel:<\n";
        s+="spiketimelist:\t";
        s+= "["+ spikePrint() +"]\n>\n";
        return s;
    }
    
    public boolean approxContains()
    {
        for(Double d: spiketimelist)
        {
            if(approxEquals(d,T))
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean approxContains(double time) 
    {
        for(Double d: spiketimelist)
        {
            if(approxEquals(d,time))
            {
                return true;
            }
        }
        return false;
    }
    
    public void log() 
    {
        //System.out.println(3);
        if(log)
        {
            if(approxContains())
            {
                logMap.put(T, 1.0);
            }
            else
            {
                logMap.put(T, 0.0);
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
                name+" Spikes vs. Time",
                "Time (sec)",
                "Spikes",
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
