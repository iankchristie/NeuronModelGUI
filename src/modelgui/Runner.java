
package modelgui;

/**
 * @author ianchristie
 */

public class Runner 
{
    Model model;
    
    public Runner(Model m)
    {
        model = m;
    }
    
    public void run()
    {
        for(int i = 0; i < model.numTimes; i++)
        {
            for(Modelel m: model.elements)
            {
                m.T = model.startT;
                m.dT = model.dT;
            }
            for(double k = model.startT; k < model.maxTime; k+=model.dT)
            {
                for(Modelel m: model.elements)
                {
                    m.step();
                    m.log();
                }
            }
            for(Modelel m: model.elements)
            {
                m.display();
            }
            model.clearClearableSpikeTimes();
        }
    }
}
