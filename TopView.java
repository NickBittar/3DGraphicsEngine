import javax.swing.*;
import java.awt.*;

public class TopView extends JPanel
{
  Model model;
  public TopView(Model obj)
  {
    model = obj;
  }

    public void update()
    {

    }
  public void paint(Graphics g)
  {
    int w = getWidth();
    int h = getHeight();
      update();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, w, h);
    //Draw grid lines
    g.setColor(Color.lightGray);
    for(int i = 0; i < 10; i++)
    {
       g.drawLine(0, (h*i/10), w, (h*i/10));
       g.drawLine(w*i/10, 0, w*i/10, h);
    }
    g.setColor(Color.RED);
    g.drawLine(0, h / 2, w, h / 2);
    g.setColor(Color.BLUE);
    g.drawLine(w / 2, 0, w / 2, h);

    g.setColor(Color.BLACK);
    for(int i = 0; i < model.ptCount(); i++)
    {
       int x = (int) (Math.round(w/10* model.getX(i) * 1000.0) / 1000.0)+w/2;
       int z = (int) (Math.round(h/10* model.getZ(i) * 1000.0) / 1000.0)+h/2;
       g.fillOval(x-3, z-3, 6, 6);
        for(int j = i; j < model.ptCount(); j++)
        {
            int x1 = (int) (Math.round(w/10* model.getX(i) * 1000.0) / 1000.0)+w/2;
            int z1 = (int) (Math.round(h/10* model.getZ(i) * 1000.0) / 1000.0)+h/2;
            int x2 = (int) (Math.round(w/10* model.getX(j) * 1000.0) / 1000.0)+w/2;
            int z2 = (int) (Math.round(h/10* model.getZ(j) * 1000.0) / 1000.0)+h/2;
            g.drawLine(x1, z1, x2, z2);
        }
    }

    g.setColor(Color.BLACK);
    g.drawString("Top", 4, 16);
  }
}