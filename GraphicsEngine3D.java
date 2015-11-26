import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Nick Bittar on 11/8/2015.
 * For CSC 443 - Computer Graphics
 */
public class GraphicsEngine3D extends JPanel implements ActionListener, MouseListener, MouseMotionListener
{
    public static Model model;

    static ThreeDimensionalView view3D;

    static Panel topView;
    static Panel sideView;
    static Panel frontView;

    static int scale;

    public static void main(String[] args)
    {
        model = new Model("cube", new int[] {2});
        view3D = new ThreeDimensionalView(model);
        scale = 10;
        topView = new Panel(new View(model, View.VIEW_TOP, scale));
        sideView = new Panel(new View(model, View.VIEW_SIDE, scale));
        frontView = new Panel(new View(model, View.VIEW_FRONT, scale));

        view3D.setPreferredSize(new Dimension(300, 300));
        topView.setPreferredSize(new Dimension(300, 300));
        sideView.setPreferredSize(new Dimension(300, 300));
        frontView.setPreferredSize(new Dimension(300, 300));

        JFrame frame = new JFrame();                                    // Create Frame
        JPanel container = new JPanel();

        container.setLayout(new GridLayout(2, 3, 2, 2));    // rows=2; cols=3 ; border width=2 ; border height=2
        container.setOpaque(true);
        container.setBackground(Color.BLACK);
        JPanel panel = new GraphicsEngine3D();                      // Create Panel with my engine one it

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("3-D Graphics Engine - Nick Bittar");
        frame.setVisible(true);

        container.add(new JButton("Controls"));
        container.add(view3D);
        container.add(topView);
        container.add(new JButton("Command Box"));
        container.add(sideView);
        container.add(frontView);

        frame.add(container);                                           // Add drawing panel to frame
        frame.pack();
        run();                                                          // Start
    }
    public GraphicsEngine3D()
    {
        // Add Mouse Listeners
        addMouseListener(this);
        addMouseMotionListener(this);
        // Set timer to redraw panel every 16ms for ~60 frames per second to be rendered
        Timer timer = new Timer(16, this);
        timer.start();
    }

    /**
     * Timer event. Fires every 16ms or about 60 times per second
     * @param ev
     */
    public void actionPerformed (ActionEvent ev)
    {
        model.rotate(0, 1, 0);
        view3D.repaint();
        topView.repaint();
        sideView.repaint();
        frontView.repaint();
    }
    public static void run()
    {

    }

    /**
     * Draws to the panel some X and Y Axis and the polygon.
     * @param g The Graphics Object
     */
    public void paint(Graphics g)
    {
    }


    /*
    public int findNearPoint(double x, double y, double threshold)
    {
        double xi, yi;
        for(int i = 0; i < Xs.size(); i++)
        {
            xi = Double.parseDouble(Xs.get(i).toString());
            yi = Double.parseDouble(Ys.get(i).toString());
            if(xi >= x - threshold && xi <= x + threshold &&
               yi >= y - threshold && yi <= y + threshold    )
            {
                return i;
            }
        }
        return -1;
    }*/

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    /**
     * Converts the Window's X Y position to the Cartesian's X Y position (e.g. a 800x800 window position to
     * a 20x20 window (-10 to +10 on each axis) position).
     * @param p The point to be converted
     * @return The converted point
     */
    public double[] windowToCartesian(int[] p)
    {
        int x = p[0];
        int y = p[1];

        double xc = ((x*1.0 - (getWidth()/2))*20)/getWidth();
        double yc = -((y*1.0 - (getHeight()/2))*20)/getHeight();

        return new double[] {xc, yc};
    }
    /**
     * Converts the Cartesian's X Y position to the Window's X Y position given a point on the graph.
     * @param p The point to be converted
     * @return The converted point
     */
    public int[] cartesianToWindow(double[] p)
    {
        double xc = p[0];
        double yc = p[1];
        int w = getWidth();
        int h = getHeight();

        int xw = (int) (Math.round(w/20*xc * 1000.0) / 1000.0)+w/2;
        int yw = (int) -(Math.round(h/20*yc * 1000.0) / 1000.0)+h/2;

        return new int[]{xw, yw};
    }
    /**
     * Converts the Cartesian's X Y position to the Window's X Y position given the index of the point of the polygon.
     * @param i The index of the point to be converted
     * @return The converted point
     */
    public int[] cartesianToWindow(int i)
    {

        int w = getWidth();
        int h = getHeight();

        int xw = (int) (Math.round(w/20* model.getX(i) * 1000.0) / 1000.0)+w/2;
        int yw = (int) -(Math.round(h/20* model.getY(i) * 1000.0) / 1000.0)+h/2;

        return new int[]{xw, yw};
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }
}