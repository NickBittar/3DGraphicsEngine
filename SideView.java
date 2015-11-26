import javax.swing.*;
import java.awt.*;

public class SideView extends JPanel
{
    Model model;

    public SideView(Model obj)
    {
        model = obj;
    }

    public void paint(Graphics g)
    {
        int w = getWidth();
        int h = getHeight();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        //Draw grid lines
        g.setColor(Color.lightGray);
        for (int i = 0; i < 10; i++)
        {
            g.drawLine(0, (h * i / 10), w, (h * i / 10));
            g.drawLine(w * i / 10, 0, w * i / 10, h);
        }
        g.setColor(Color.BLUE);
        g.drawLine(0, h / 2, w, h / 2);
        g.setColor(Color.GREEN);
        g.drawLine(w / 2, 0, w / 2, h);

        g.setColor(Color.BLACK);
        for (int i = 0; i < model.ptCount(); i++)
        {
            int z = (int) -(Math.round(w / 10 * model.getZ(i) * 1000.0) / 1000.0) + w / 2;
            int y = (int) -(Math.round(h / 10 * model.getY(i) * 1000.0) / 1000.0) + h / 2;
            g.fillOval(z - 3, y - 3, 6, 6);
            for (int j = i; j < model.ptCount(); j++)
            {
                int z1 = (int) -(Math.round(w / 10 * model.getZ(i) * 1000.0) / 1000.0) + w / 2;
                int y1 = (int) -(Math.round(h / 10 * model.getY(i) * 1000.0) / 1000.0) + h / 2;
                int z2 = (int) -(Math.round(w / 10 * model.getZ(j) * 1000.0) / 1000.0) + w / 2;
                int y2 = (int) -(Math.round(h / 10 * model.getY(j) * 1000.0) / 1000.0) + h / 2;
                g.drawLine(z1, y1, z2, y2);
            }
        }

        g.setColor(Color.BLACK);
        g.drawString("Side", 3, 16);
    }
}