import javax.swing.*;
import java.awt.*;

/**
 * Created by Nick on 11/25/2015.
 */
public class Panel extends JPanel
{
    View view;

    public Panel(View view)
    {
        this.view = view;
    }
    public void update()
    {
        view.update(getWidth(), getHeight());

    }
    public void paint(Graphics g)
    {
        update();
        view.draw();

        g.drawImage(view.getImage(), 0, 0, null);

        g.setColor(Color.BLACK);
        if(view.getView() == View.VIEW_TOP)
        {
            g.drawString("Top", 3, 13);
        }
        else if(view.getView() == View.VIEW_SIDE)
        {
            g.drawString("Side", 2, 13);
        }
        else if(view.getView() == View.VIEW_FRONT)
        {
            g.drawString("Front", 1, 13);
        }
    }

    public void changeScale(int scale)
    {
        view.changeScale(scale);
    }
}
