import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Nick Bittar on 11/8/2015.
 * For CSC 443 - Computer Graphics
 */
public class TransformationEngine extends JPanel implements ActionListener, MouseListener, MouseMotionListener
{
    // Contains the points of the polygon
    static ArrayList Xs = new ArrayList();  // X points
    static ArrayList Ys = new ArrayList();  // Y points
    static ArrayList Zs = new ArrayList();  // Z points

    // Used to keep track of the cursors starting and ending points when dragging vertices for mouse interaction
    double Xi, Yi, Xf, Yf;
    int selectedPoint = -1;     // The index of the X Y Point being selected
    int cursorX=0, cursorY=0;   // Cursor's coordinates

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();                                    // Create Frame
        JPanel panel = new TransformationEngine();                      // Create Panel with my engine one it
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("2-D Transformation Engine - Display Window");
        frame.setSize(800, 800);                                        // Set window size to 800px by 800px
        frame.setVisible(true);
        frame.add(panel);                                               // Add drawing panel to frame
        run();                                                          // Start
    }
    public TransformationEngine()
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
        repaint();
    }
    public static void run()
    {
        // Get user input
        Scanner k = new Scanner(System.in);
        String input = "";
        String command = "";
        System.out.println("Type \"polygon(x1, y1, x2, y2, ..., xn, yn)\" to get started.");
        // Main input loop
        while(!input.equalsIgnoreCase("Exit"))
        {
            try
            {
                // Get Next input from user
                input = k.nextLine();
                // Check user input
                if (!isValidCommand(input))
                {
                    System.out.println("Invalid Command.");
                    continue;
                }
                command = getCommandType(input);
                if (command == null)
                {
                    System.out.println("Invalid Command!");
                    continue;
                }
                //
                if (Xs.size() < 1 && !command.equalsIgnoreCase("polygon") && !command.equalsIgnoreCase("exit"))
                {
                    System.out.println("You must first specify a polygon");
                    continue;
                }
                // Grab and parse arguments in user's input between parenthesis
                String[] arguments = new String[0];
                if (!command.equalsIgnoreCase("show") && !command.equalsIgnoreCase("exit"))
                {
                    arguments = input.split("\\(")[1].split("\\)")[0].split(",");
                }
                // Run the user selected command and pass the arguments into the corresponding method
                switch (command)
                {
                    case "polygon":
                        createPolygon(arguments);
                        break;
                    case "show":
                        showPolygon();
                        break;
                    case "translate":
                        translatePolygon(arguments);
                        break;
                    case "rotate":
                        rotatePolygon(arguments);
                        break;
                    case "scale":
                        scalePolygon(arguments);
                        break;
                }

                System.out.println("OK");
            }
            catch(Exception e)
            {
                System.out.println("ERROR: " + e.getMessage());
                System.out.println("Exiting.");
                return;
            }
        }

        System.out.println("Goodbye.");
    }

    /**
     * Provides the center point of the polygon based on the X and Y coordinates in Xs and Ys ArrayLists.
     * @return The Centroid of the polygon
     */
    private static double[] getCentroid()
    {
        double xSum = 0;
        double ySum = 0;
        int pointCount = Xs.size();
        // Sums all the X points, and sums all the Y points
        for(int i = 0; i < pointCount; i++)
        {
            xSum += Double.parseDouble(Xs.get(i).toString());
            ySum += Double.parseDouble(Ys.get(i).toString());
        }
        // returns the average of the X points and the average of the Y points.
        return new double[] { xSum/pointCount, ySum/pointCount };
    }

    /**
     * Shears the polygon based on Shx and Shy
     * @param arguments The Shx and Shy shearing values
     */
    private static void shearPolygon(String[] arguments)
    {
        double Shx = Double.parseDouble(arguments[0]);
        double Shy = Double.parseDouble(arguments[1]);
        double x, y, xs, ys;
        for(int i = 0; i < Xs.size(); i++)
        {
            x = Double.parseDouble(Xs.get(i).toString());
            y = Double.parseDouble(Ys.get(i).toString());

            // Shear transformation
            xs = x + Shx*y;
            ys = y + Shy*x;

            Xs.set(i, xs);
            Ys.set(i, ys);
        }
    }

    /**
     * Scales the polygon based on the provided scaling factors.
     * @param arguments Contains Sx and Sy, the scaling values for the X and Y axis.
     */
    private static void scalePolygon(String[] arguments)
    {
        double Sx = Double.parseDouble(arguments[0]);
        double Sy = Double.parseDouble(arguments[1]);
        double[] centroid = getCentroid();
        double Cx = centroid[0];
        double Cy = centroid[1];

        // Translate so centroid is at the origin
        translatePolygon(new String[] { -Cx+"", -Cy+"" });

        // Scale all points
        for(int i = 0; i < Xs.size(); i++)
        {
            double x = Double.parseDouble(Xs.get(i).toString());
            double y = Double.parseDouble(Ys.get(i).toString());

            // Scale Transformation
            x *= Sx;
            y *= Sy;

            Xs.set(i, x);
            Ys.set(i, y);
        }

        // Inverse translation so centroid is in original position
        translatePolygon(new String[] { Cx+"", Cy+"" });
    }

    /**
     * Rotates the polygon around its centroid.
     * @param arguments Contains the value of Theta, the angle the polygon will be rotated by.
     */
    private static void rotatePolygon(String[] arguments)
    {
        double theta = Math.toRadians(Double.parseDouble(arguments[0]));
        double[] centroid = getCentroid();
        double Cx = centroid[0];
        double Cy = centroid[1];
        // Translate so centroid is at the origin
        translatePolygon(new String[] { -Cx+"", -Cy+"" });
        /*
        *   To rotate...
        *       X = Cos(theta)*X - Sin(theta)*Y;
        *       Y = Sin(theta)*X + Cos(theta)*Y;
         */
        for(int i = 0; i < Xs.size(); i++)
        {
            double x = Double.parseDouble(Xs.get(i).toString());
            double y = Double.parseDouble(Ys.get(i).toString());
            double newX;
            double newY;

            // Rotate Transformation
            newX = Math.cos(theta)*x - Math.sin(theta)*y;
            newY = Math.sin(theta)*x + Math.cos(theta)*y;

            Xs.set(i, newX);
            Ys.set(i, newY);
        }
        // Inverse translation so centroid is in original position
        translatePolygon(new String[] { Cx+"", Cy+"" });
    }

    /**
     * Moves the polygon a given distance horizontally and vertically.
     * @param arguments Contains Tx and Ty the translation values.
     */
    private static void translatePolygon(String[] arguments)
    {
        double Tx = Double.parseDouble(arguments[0]);
        double Ty = Double.parseDouble(arguments[1]);
        for(int i = 0; i < Xs.size(); i++)
        {
            double x = Double.parseDouble(Xs.get(i).toString());
            double y = Double.parseDouble(Ys.get(i).toString());

            // Translate Transformation
            x += Tx;
            y += Ty;

            Xs.set(i, x);
            Ys.set(i, y);
        }
    }

    /**
     * Prints out the current X and Y values of the vertices of the polygon.
     */
    private static void showPolygon()
    {
        System.out.print("(");
        for(int i = 0; i < Xs.size(); i++)
        {
            // Times the value by 1000, and the divides by 1000, to get points to 3 decimal places.
            System.out.print(Math.round(Double.parseDouble(Xs.get(i).toString()) * 1000.0) / 1000.0 + ", " +
                             Math.round(Double.parseDouble(Ys.get(i).toString()) * 1000.0) / 1000.0);
            if(i != Xs.size()-1)
            {
                System.out.print(", ");
            }
        }
        System.out.println(")");
    }

    /**
     * Sets the points of the polygon.
     * @param points The points of the polygon to create
     */
    private static void createPolygon(String[] points)
    {
        Xs = new ArrayList();
        Ys = new ArrayList();
        for(int i = 0; i < points.length; i+=2)
        {
            Xs.add(Double.parseDouble(points[i]));
            Ys.add(Double.parseDouble(points[i + 1]));
        }
    }

    /**
     * Parses the input string to decipher what they want to do.
     * @param input The command string by the user
     * @return a String containing the command the user specified
     */
    private static String getCommandType(String input)
    {
        input = input.toLowerCase();
        int argCount;
        switch(input.charAt(0))
        {
            case 'p':
                argCount = input.split(",").length;
                if(argCount%2 == 0 && argCount > 1)
                {
                    return "polygon";
                }
                else
                {
                    System.out.println("Invalid arguments for Polygon!");
                }
                break;
            case 's':
                if(input.equalsIgnoreCase("show"))
                {
                    return "show";
                }
                argCount = input.split(",").length;
                if(argCount == 2)
                {
                    return "scale";
                }
                else
                {
                    System.out.println("Invalid arguments for Scaling!");
                }
                break;
            case 'e':
                if(input.equalsIgnoreCase("exit"))
                {
                    return "exit";
                }
                break;
            case 't':
                argCount = input.split(",").length;
                if(argCount == 2)
                {
                    return "translate";
                }
                else
                {
                    System.out.println("Invalid arguments for Translating!");
                }
                break;
            case 'r':
                if(!input.contains(","))
                {
                    return "rotate";
                }
                else
                {
                    System.out.println("Invalid arguments for Rotating!");
                }
                break;

        }
        return null;
    }

    /**
     * Checks if the input is a valid command
     * @param input The user's input
     * @return True if the input is probably okay, false otherwise.
     */
    private static boolean isValidCommand(String input)
    {
        if ((input.contains("(") && input.contains(")")) || input.equalsIgnoreCase("show") || input.equalsIgnoreCase("exit"))
        {
            String firstLetter = input.charAt(0) + "";
            if (firstLetter.equalsIgnoreCase("p") ||
                firstLetter.equalsIgnoreCase("s") ||
                firstLetter.equalsIgnoreCase("e") ||
                firstLetter.equalsIgnoreCase("t") ||
                firstLetter.equalsIgnoreCase("r"))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Draws to the panel some X and Y Axis and the polygon.
     * @param g The Graphics Object
     */
    public void paint(Graphics g)
    {
        int w = this.getWidth();
        int h = this.getHeight();
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // Clear previous paint
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);

        // Draw X and Y Axis
        g.setColor(Color.BLACK);
        g.drawLine(w/2, 0, w/2, h);
        g.drawLine(0, h/2, w, h/2);

        //Draw tics along axis
        for(int i = 0; i < 20; i++)
        {
            g.drawLine(w/2 - 10, (h*i/20), w/2 + 10, (h*i/20));
            g.drawLine(w*i/20, h/2 -10, w*i/20, h/2 + 10);
        }

        // Draw each of the polygon's lines.
        if(Xs.size() > 0)
        {
            for(int i = 0; i < Xs.size(); i++)
            {
                int x = (int) (Math.round(w/20*Double.parseDouble(Xs.get(i).toString()) * 1000.0) / 1000.0)+w/2;
                int y = (int) -(Math.round(h/20*Double.parseDouble(Ys.get(i).toString()) * 1000.0) / 1000.0)+h/2;
                int xn = (int) (Math.round(w/20*Double.parseDouble(Xs.get((i+1)%(Xs.size())).toString()) * 1000.0) / 1000.0)+w/2;
                int yn = (int) -(Math.round(h/20*Double.parseDouble(Ys.get((i+1)%(Xs.size())).toString()) * 1000.0) / 1000.0)+h/2;
                g.drawLine(x, y, xn, yn);
                g.drawString(""+i, x, y);
            }
        }
        // Highlight selected point
        if(selectedPoint >= 0)
        {
            int[] point = cartesianToWindow(selectedPoint);
            g.fillOval(point[0]-5, point[1]-5, 10, 10);
        }

        // Show cursor range (threshold)
        int[] point = cartesianToWindow(new double[]{1, 0});
        int size = Math.abs(point[1]-point[0]);
        g.drawOval(cursorX - size/2,cursorY - size/2, size, size);
    }

    /**
     * Provides a way of finding a point near the user's cursor for when they want to select a pont.
     * @param x The X value of the cursor's position
     * @param y The Y value of the cursor's position
     * @param threshold The range of their cursor; How far a point can be from the cursor but still be selected
     * @return The index of the point near the cursor's position, if there are multiple, it return the first instance.
     */
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
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON3)
        {
            double[] point = windowToCartesian(new int[]{e.getX(), e.getY()});
            double x = point[0];
            double y = point[1];
            if(findNearPoint(x, y, 1) < 0)
            {
                Xs.add(x);
                Ys.add(y);
            }
        }
        if(e.getButton() == MouseEvent.BUTTON2)
        {
            double[] point = windowToCartesian(new int[]{e.getX(), e.getY()});
            double x = point[0];
            double y = point[1];
            int i = findNearPoint(x, y, 1);
            if(i >= 0)
            {
                Xs.remove(i);
                Ys.remove(i);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            double[] point = windowToCartesian(new int[]{e.getX(), e.getY()});
            Xi = point[0];
            Yi = point[1];

            selectedPoint = findNearPoint(Xi, Yi, 1);

        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            double[] point = windowToCartesian(new int[]{e.getX(), e.getY()});
            Xf = point[0];
            Yf = point[1];
            if(selectedPoint >= 0)
            {
                Xs.set(selectedPoint, Xf);
                Ys.set(selectedPoint, Yf);
            }

        }
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

        int xw = (int) (Math.round(w/20*Double.parseDouble(Xs.get(i).toString()) * 1000.0) / 1000.0)+w/2;
        int yw = (int) -(Math.round(h/20*Double.parseDouble(Ys.get(i).toString()) * 1000.0) / 1000.0)+h/2;

        return new int[]{xw, yw};
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if(SwingUtilities.isLeftMouseButton(e))
        {
            if (selectedPoint >= 0)
            {
                double[] p = windowToCartesian(new int[]{e.getX(), e.getY()});
                double x = p[0];
                double y = p[1];

                Xs.set(selectedPoint, x);
                Ys.set(selectedPoint, y);
            }
        }
        cursorX = e.getX();
        cursorY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        cursorX = e.getX();
        cursorY = e.getY();
    }
}
