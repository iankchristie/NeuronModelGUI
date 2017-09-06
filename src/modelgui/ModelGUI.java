
package modelgui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author ianchristie
 */
public class ModelGUI 
{
    private Board board;
    private JLabel status;
    Model model;
    
    public static void main(String[] args) 
    {
        ModelGUI mGUI= new ModelGUI();
    }
    
    public ModelGUI()
    {        
        JFrame frame;
        JMenuBar menuBar;
        JMenu file, actions;
        JMenuItem menuItem;
        JLabel header;
        JPanel buttonPanel;
        JButton spikeTimeListButton;
        JButton intFireLeakyButton;
        JButton synapseT1T2Button;
        JButton inhibitorySynapseButton;
        JButton runButton;
        JButton modelButton;
        
        // first we create the Frame with a border layout
        frame = new JFrame("ModelGUI");
        frame.setSize(900,650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        //make the menubar
        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_A);
        file.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(file);
        
        menuItem = new JMenuItem("Save Type Matlab", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e){
                                    status.setText("saving");
                                    JFileChooser chooser = new JFileChooser();
                                    chooser.showOpenDialog(null);
                                    File f = chooser.getCurrentDirectory();
                                    File newF = new File(f.getAbsoluteFile()+"/NewFile.txt");
                                    PrintWriter writer = null;
                                    try {
                                        writer = new PrintWriter(newF);
                                        writer.println(model.ExportTypeMatlab());
                                        status.setText("saved to: "+newF.getAbsolutePath());
                                    } catch (FileNotFoundException ex) {
                                        JOptionPane.showMessageDialog(null, "Problem writing to " + newF.getAbsoluteFile());
                                    } finally {
                                        writer.close();
                                    }                                        
                                }
                        });
        
        file.add(menuItem);
        
        menuItem = new JMenuItem("Save Type C++", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    status.setText("saving");
                                    JFileChooser chooser = new JFileChooser();
                                    chooser.showOpenDialog(null);
                                    File f = chooser.getCurrentDirectory();
                                    File newF = new File(f.getAbsoluteFile()+"/NewFile.txt");
                                    PrintWriter writer = null;
                                    try {
                                        writer = new PrintWriter(newF);
                                        writer.println(model.ExportTypeC());
                                        status.setText("saved to: "+newF.getAbsolutePath());
                                    } catch (FileNotFoundException ex) {
                                        JOptionPane.showMessageDialog(null, "Problem writing to " + newF.getAbsoluteFile());
                                    } finally {
                                        writer.close();
                                    }
                                }
                        });
        file.add(menuItem);
        
        menuItem = new JMenuItem("Save Type Java Serializable", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    status.setText("saving");
                                    JFileChooser chooser = new JFileChooser();
                                    chooser.showOpenDialog(null);
                                    File f = chooser.getCurrentDirectory();
                                    File newF = new File(f.getAbsoluteFile()+"/NewFile.ser");
                                    FileOutputStream fileOut = null;
                                    try {
                                        fileOut = new FileOutputStream(newF);
                                    } catch (FileNotFoundException ex) {
                                        Logger.getLogger(ModelGUI.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    ObjectOutputStream out = null;
                                        try {
                                            out = new ObjectOutputStream(fileOut);
                                            out.writeObject(model);
                                            status.setText("File saved in "+ newF.getAbsolutePath());
                                        } catch (IOException ex) {
                                            JOptionPane.showMessageDialog(null, "Problem writing to " + newF.getAbsoluteFile());
                                        } finally {
                                            try {
                                                out.close();
                                            } catch (IOException ex) {
                                                Logger.getLogger(ModelGUI.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            try {
                                                fileOut.close();
                                            } catch (IOException ex) {
                                                Logger.getLogger(ModelGUI.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                }
                        });
        file.add(menuItem);
        
        file.addSeparator();
        
        menuItem = new JMenuItem("Import Type Matlab", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e){
                                    status.setText("Importing");
                                    JFileChooser chooser = new JFileChooser();
                                    chooser.showOpenDialog(null);
                                    File f = chooser.getSelectedFile();
                                    Parser p = new Parser(f);
                                    model.elements = p.getModel();
                                }
                        });
        
        file.add(menuItem);
        
        menuItem = new JMenuItem("Import Type Java Serializable", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e){
                                    status.setText("Importing");
                                    JFileChooser chooser = new JFileChooser();
                                    chooser.showOpenDialog(null);
                                    File f = chooser.getSelectedFile();
                                    try
                                    {
                                       FileInputStream fileIn = new FileInputStream(f);
                                       ObjectInputStream in = new ObjectInputStream(fileIn);
                                       Model m = (Model) in.readObject();
                                       model.elements = m.elements;
                                       for(Modelel mo: model.elements)
                                       {
                                           mo.board = board;
                                       }
                                       board.repaint();
                                       in.close();
                                       fileIn.close();
                                    }catch(IOException i) {
                                       i.printStackTrace();
                                       return;
                                    } catch(ClassNotFoundException c) {
                                       System.out.println("Employee class not found");
                                       c.printStackTrace();
                                       return;
                                    }
                                }
                        });
        
        file.add(menuItem);
        
        //actions menu
        actions = new JMenu("Actions");
        actions.setMnemonic(KeyEvent.VK_A);
        actions.getAccessibleContext().setAccessibleDescription(
                "Actions Support");
        menuBar.add(actions);
        
        menuItem = new JMenuItem("Clear All Spike Times", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                        model.clearAllSpikeTimes();
                                        status.setText("Spike Times Cleared");
                                    }
                        });
        actions.add(menuItem);
        
        menuItem = new JMenuItem("Clear IntfireLeaky Spike Times", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                        model.clearIntSpikeTimes();
                                        status.setText("Spike Times Cleared");
                                    }
                        });
        actions.add(menuItem);
        
        menuItem = new JMenuItem("Clear Clearable Spike Times", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                        model.clearClearableSpikeTimes();
                                        status.setText("Spike Times Cleared");
                                    }
                        });
        actions.add(menuItem);
        
        actions.addSeparator();
        
        menuItem = new JMenuItem("Reset Model", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                        model.clearElements();
                                        status.setText("Reset Model");
                                    }
                        });
        actions.add(menuItem);
        
        menuItem = new JMenuItem("Undo element", KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                        model.undoElement();
                                        status.setText("Undo element");
                                        board.repaint();
                                    }
                        });
        actions.add(menuItem);
        
        // next we create the Board and model
        this.model = new Model(100,0);
        board = new Board(model);
        
        // here is the title of the game and the status bar
        header = new JLabel();
        header.setText("Create a Model");
        status = new JLabel("Status:\t");
        
        // Create the buttons and add actions
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3,2));
        
        runButton = new JButton();
        runButton.setText("run");
        runButton.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    status.setText("Running");
                                    Runner r = new Runner(model);
                                    r.run();
                                    status.setText("Done Running");
                                }
                        });
        
        modelButton = new JButton("Model Attributes");
        modelButton.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    status.setText("Model Attributes");
                                    model.openDialog();
                                }
                        });
        
        spikeTimeListButton = new JButton();
        spikeTimeListButton.setText("SpikeTimeList");
        spikeTimeListButton.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    if(board.state != States.spiketimelist)
                                    {
                                        board.state = States.spiketimelist;
                                        status.setText("Spike Time List");
                                    }
                                    else
                                    {
                                        board.state = States.clear;
                                        status.setText("Awaiting Input");
                                    }
                                }
                        });
        
        intFireLeakyButton = new JButton();
        intFireLeakyButton.setText("IntFireLeaky");
        intFireLeakyButton.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    if(board.state != States.intfireleaky)
                                    {
                                        board.state = States.intfireleaky;
                                        status.setText("Integrate and Fire Leaky Neuron");
                                    }
                                    else
                                    {
                                        board.state = States.clear;
                                        status.setText("Awaiting Input");
                                    }
                                }
                        });
        
        synapseT1T2Button = new JButton();
        synapseT1T2Button.setText("Excitatory Synapse");
        synapseT1T2Button.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    if(board.state != States.synapset1t2)
                                    {
                                        board.state = States.synapset1t2;
                                        status.setText("Synapse");
                                    }
                                    else
                                    {
                                        board.state = States.clear;
                                        status.setText("Awaiting Input");
                                    }
                                }
                        });
        
        inhibitorySynapseButton = new JButton();
        inhibitorySynapseButton.setText("Inhibitory \nSynapse");
        inhibitorySynapseButton.addActionListener(
                        new ActionListener(){
                                public void actionPerformed(ActionEvent e){
                                    if(board.state != States.synapset1t2)
                                    {
                                        board.inhibitory = true;
                                        board.state = States.synapset1t2;
                                        status.setText("Synapse");
                                    }
                                    else
                                    {
                                        board.state = States.clear;
                                        status.setText("Awaiting Input");
                                    }
                                }
                        });
        
        // add the buttons to a buttonPanel
        buttonPanel.add(spikeTimeListButton);
        buttonPanel.add(intFireLeakyButton);
        buttonPanel.add(synapseT1T2Button);
        buttonPanel.add(inhibitorySynapseButton);
        buttonPanel.add(runButton);
        buttonPanel.add(modelButton);
        
        // put the frame components together with a border layout
        frame.add(header,BorderLayout.NORTH);
        frame.add(board,BorderLayout.CENTER);
        frame.add(buttonPanel,BorderLayout.EAST);
        frame.add(status,BorderLayout.SOUTH);
        
        frame.setJMenuBar(menuBar);
        
        frame.setVisible(true);
    }
}
