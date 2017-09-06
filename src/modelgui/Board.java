
package modelgui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

/**
 * @author ianchristie
 */
class Board extends JPanel
{
    private Model model = null;
    States state;
    Synapse tempSynapse;
    boolean inhibitory;
    
    public Board(Model m) 
    {
        super();
        this.model = m;
        this.state = States.clear;
        MouseInputListener ml =
                        new CanvasMouseInputListener();
        this.addMouseListener(ml);
        this.addMouseMotionListener(ml);
        inhibitory = false;
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (model==null) return;
        int width = this.getWidth();
        int height = this.getHeight();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height);
        if(state!=States.clear && state!=States.synapset1t2) 
        {
            drawActor(g,model.avatar,Color.GREEN);
        }
        for(Modelel a: model.elements)
        {
            a.drawElement(g,this);
        }
    }
    
    private void drawActor(Graphics g, Modelel a, Color c)
    {
        int theRadius = toViewCoords(a.radius);
        int x = toViewCoords(a.x);
        int y = toViewCoords(a.y);

        if(this.state == States.spiketimelist)
        {
            g.setColor(Color.BLUE);
        }
        else if(this.state == States.intfireleaky)
        {
            g.setColor(Color.RED);
        }
        else
        {
            g.setColor(Color.BLACK);
        }
        g.drawOval(x-theRadius, y-theRadius, 2*theRadius, 2*theRadius);
    }
    
    public int toViewCoords(double x)
    {
        int width = this.getWidth();
        int height = this.getHeight();
        int viewSize = (width<height)?width:height;
        return (int) Math.round(x/model.size*viewSize);
    }
    
    public double toModelCoords(int x)
    {
        int width = this.getWidth();
        int height = this.getHeight();
        int viewSize = (width<height)?width:height;
        return x*model.size/viewSize;
    }
    
    public void checkAndAddElement(double x, double y)
    {
        if(this.state == States.clear)
        {
            model.unselectAll();
            Modelel m = getElement(x,y);
            if(m!=null)
            {
                m.selected = true;
                repaint();
                m.openDialog(true);
            }
        }
        if(this.state == States.spiketimelist)
        {
            SpikeTimeList s = new SpikeTimeList();
            s.x = x;
            s.y = y;
            s.board = this;
            model.addElement(s);
        }
        if(this.state == States.intfireleaky)
        {
            IntFireLeaky s = new IntFireLeaky();
            s.x = x;
            s.y = y;
            s.board = this;
            model.addElement(s);
        }
    }
    
    public void firstSynapse(double x, double y)
    {
        if(this.state == States.synapset1t2)
        {
            SpikeTimeList temp = null;
            try
            {
                temp = (SpikeTimeList) getElement(x,y);
            }
            catch(ClassCastException e) {System.out.println("Class Cast Exception 1");}
            if(temp!=null)
            {
                tempSynapse = new SynapseT1T2();
                tempSynapse.board = this;
                tempSynapse.setPre(temp);
            }
        }
    }
    
    public void secondSynapse(double x, double y)
    {
        if(this.state == States.synapset1t2)
        {
            IntFireLeaky m = null;
            try
            {
                m = (IntFireLeaky) getElement(x,y);
            }
            catch(ClassCastException e) {System.out.println("Class Cast Exception 2");}
            if(m!=null && tempSynapse!=null)
            {
                tempSynapse.setPost(m);
                if(inhibitory)
                {
                    tempSynapse.V_rev = m.V_leak - .005;
                }
                model.addElement(tempSynapse);
                tempSynapse = null;
                inhibitory = false;
            }
        }
    }

    public void clear() 
    {
        model.clearElements();
    }

    private Modelel getElement(double x, double y) 
    {
        for(Modelel m: model.elements)
        {
            if(m.contains(x,y))
            {
                return m;
            }
        }
        return null;
    }
    
    private class CanvasMouseInputListener extends MouseInputAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            Point p = e.getPoint();
            double x = toModelCoords(p.x); 
            double y = toModelCoords(p.y);
            checkAndAddElement(x,y);
        }
        
        @Override
        public void mouseMoved(MouseEvent e)
        {
            Point p = e.getPoint();
            double x = toModelCoords(p.x); 
            double y = toModelCoords(p.y);
            model.avatar.x = x;
            model.avatar.y = y;
            e.getComponent().repaint();		
        }
        
        @Override
        public void mousePressed(MouseEvent e) 
        {
            Point p = e.getPoint();
            double x = toModelCoords(p.x); 
            double y = toModelCoords(p.y);
            firstSynapse(x,y);
            e.getComponent().repaint();	
        }
        
        @Override
        public void mouseReleased(MouseEvent e)
        {
            Point p = e.getPoint();
            double x = toModelCoords(p.x); 
            double y = toModelCoords(p.y);
            secondSynapse(x,y);
            e.getComponent().repaint();	
        }
    }
}
