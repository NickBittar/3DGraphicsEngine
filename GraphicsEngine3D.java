import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Nick Bittar on 11/8/2015.
 * For CSC 443 - Computer Graphics
 */
public class GraphicsEngine3D extends JPanel implements ActionListener
{
    public static Model model;

    public static ArrayList<Model> models;

    static ThreeDimensionalView view3D;

    static Panel topView;
    static Panel sideView;
    static Panel frontView;

    static int scale;

    static double theta = 0, phi = 0, omega = 0;
    static int fov = 60;

    static JButton resetBtn;
    static JButton updateBtn;

    static JButton moveXBtn;
    static JButton moveYBtn;
    static JButton moveZBtn;
    static JButton moveXNegBtn;
    static JButton moveYNegBtn;
    static JButton moveZNegBtn;
    
    static JTextField RxTxt, RyTxt, RzTxt;
    static JTextField CxTxt, CyTxt, CzTxt;
    static JTextField TxTxt, TyTxt, TzTxt;

    static JButton FOVneg, FOVpos;

    static JButton wireBtn, lightBtn, colorBtn;

    static JButton zoomInBtn, zoomOutBtn;
    static JButton rollLeftBtn, rollRightBtn;


    public static void main(String[] args)
    {
        models = new ArrayList<Model>();
        model = new Model("cube", new int[] {2});
        models.add(model);
        scale = 5;
        view3D = new ThreeDimensionalView(model, scale);
        topView = new Panel(new View(model, View.VIEW_TOP, scale));
        sideView = new Panel(new View(model, View.VIEW_SIDE, scale));
        frontView = new Panel(new View(model, View.VIEW_FRONT, scale));

        view3D.setPreferredSize(new Dimension(300, 300));
        topView.setPreferredSize(new Dimension(300, 300));
        sideView.setPreferredSize(new Dimension(300, 300));
        frontView.setPreferredSize(new Dimension(300, 300));

        JFrame frame = new JFrame();                                    // Create Frame
        JPanel container = new JPanel();

        JPanel controls = new JPanel();
        loadControls(controls);
        JPanel controls2 = new JPanel();
        loadControls2(controls2);

        container.setLayout(new GridLayout(2, 3, 2, 2));    // rows=2; cols=3 ; border width=2 ; border height=2
        container.setOpaque(true);
        container.setBackground(Color.BLACK);
        JPanel panel = new GraphicsEngine3D();                      // Create Panel with my engine one it

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("3-D Graphics Engine - Nick Bittar");
        frame.setSize(900+23, 600+46);
        frame.setVisible(true);

        container.add(controls);
        container.add(view3D);
        container.add(topView);
        container.add(controls2);
        container.add(sideView);
        container.add(frontView);

        frame.add(container);                                           // Add drawing panel to frame
        //frame.pack();
    }
    public GraphicsEngine3D()
    {
        // Add Listeners
        resetBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        moveXBtn.addActionListener(this);
        moveYBtn.addActionListener(this);
        moveZBtn.addActionListener(this);
        moveXNegBtn.addActionListener(this);
        moveYNegBtn.addActionListener(this);
        moveZNegBtn.addActionListener(this);
        FOVneg.addActionListener(this);
        FOVpos.addActionListener(this);
        wireBtn.addActionListener(this);
        lightBtn.addActionListener(this);
        zoomInBtn.addActionListener(this);
        zoomOutBtn.addActionListener(this);
        rollLeftBtn.addActionListener(this);
        rollRightBtn.addActionListener(this);
        colorBtn.addActionListener(this);
        // Set timer to redraw panel every 16ms for ~60 frames per second to be rendered
        Timer timer = new Timer(16, this);
        timer.start();
    }
    public static void loadControls(JPanel controls)
    {
        controls.setLayout(new GridLayout(2, 1, 2, 2));

        JPanel modelCtrls = new JPanel();
        JPanel cameraCtrls = new JPanel();
        JPanel fovCtrls = new JPanel();
        modelCtrls.setLayout(new GridLayout(5, 4, 0, 0));
        cameraCtrls.setLayout(new GridLayout(2, 3, 0, 0));
        fovCtrls.setLayout(new GridLayout(2, 1, 0, 0));

        resetBtn = new JButton("Reset");
        resetBtn.setActionCommand("reset");
        updateBtn = new JButton("Update");
        updateBtn.setActionCommand("update");
        
        moveXBtn = new JButton("+1 X");
        moveYBtn = new JButton("+1 Y");
        moveZBtn = new JButton("+1 Z");
        moveXNegBtn = new JButton("-1 X");
        moveYNegBtn = new JButton("-1 Y");
        moveZNegBtn = new JButton("-1 Z");

        moveXBtn.setActionCommand("move 1 0 0");
        moveYBtn.setActionCommand("move 0 1 0");
        moveZBtn.setActionCommand("move 0 0 1");
        moveXNegBtn.setActionCommand("move -1 0 0");
        moveYNegBtn.setActionCommand("move 0 -1 0");
        moveZNegBtn.setActionCommand("move 0 0 -1");
        
        RxTxt = new JTextField();
        RyTxt = new JTextField();
        RzTxt = new JTextField();

        CxTxt = new JTextField();
        CyTxt = new JTextField();
        CzTxt = new JTextField();

        TxTxt = new JTextField();
        TyTxt = new JTextField();
        TzTxt = new JTextField();

        RxTxt.setActionCommand("rotate");
        RyTxt.setActionCommand("rotate");
        RzTxt.setActionCommand("rotate");

        CxTxt.setActionCommand("position");
        CyTxt.setActionCommand("position");
        CzTxt.setActionCommand("position");

        TxTxt.setActionCommand("translate");
        TyTxt.setActionCommand("translate");
        TzTxt.setActionCommand("translate");

        FOVneg = new JButton("-1 FOV");
        FOVpos = new JButton("+1 FOV");
        FOVneg.setActionCommand("fov -1");
        FOVpos.setActionCommand("fov 1");

        wireBtn = new JButton("Wireframe");
        wireBtn.setActionCommand("wire");
        lightBtn = new JButton("Light");
        lightBtn.setActionCommand("light");
        colorBtn = new JButton("Color");
        colorBtn.setActionCommand("color");

        fovCtrls.add(FOVpos);
        fovCtrls.add(FOVneg);



        modelCtrls.add(new JLabel("Spin"));
        modelCtrls.add(RxTxt);
        modelCtrls.add(RyTxt);
        modelCtrls.add(RzTxt);

        modelCtrls.add(new JLabel("Translate"));
        modelCtrls.add(TxTxt);
        modelCtrls.add(TyTxt);
        modelCtrls.add(TzTxt);

        modelCtrls.add(new JLabel("Move"));
        modelCtrls.add(moveXBtn);
        modelCtrls.add(moveYBtn);
        modelCtrls.add(moveZBtn);

        modelCtrls.add(new JLabel("Move"));
        modelCtrls.add(moveXNegBtn);
        modelCtrls.add(moveYNegBtn);
        modelCtrls.add(moveZNegBtn);

        modelCtrls.add(new JLabel("Position"));
        modelCtrls.add(CxTxt);
        modelCtrls.add(CyTxt);
        modelCtrls.add(CzTxt);

        cameraCtrls.add(resetBtn);
        cameraCtrls.add(updateBtn);
        cameraCtrls.add(fovCtrls);
        cameraCtrls.add(wireBtn);
        cameraCtrls.add(lightBtn);
        cameraCtrls.add(colorBtn);



        controls.add(modelCtrls);
        controls.add(cameraCtrls);
    }
    public static void loadControls2(JPanel controls2)
    {
        controls2.setLayout(new GridLayout(3, 3, 0, 0));

        zoomInBtn = new JButton("Zoom In");
        zoomOutBtn = new JButton("Zoom Out");
        zoomInBtn.setActionCommand("zoom -1");
        zoomOutBtn.setActionCommand("zoom 1");

        rollLeftBtn = new JButton("Roll Left");
        rollRightBtn = new JButton("Roll Right");
        rollLeftBtn.setActionCommand("roll 1 0 -1");
        rollRightBtn.setActionCommand("roll -1 0 1");


        controls2.add(new JButton(""));
        controls2.add(zoomInBtn);
        controls2.add(new JButton(""));

        controls2.add(rollLeftBtn);
        controls2.add(new JButton(""));
        controls2.add(rollRightBtn);

        controls2.add(new JButton(""));
        controls2.add(zoomOutBtn);
        controls2.add(new JButton(""));

    }
    /**
     * Timer event. Fires every 16ms or about 60 times per second
     * @param ev
     */
    public void actionPerformed (ActionEvent ev)
    {
        if(ev.getActionCommand() != null)
        {
            System.out.println(ev.getActionCommand());
            if(ev.getActionCommand().equals("reset"))
            {
                theta = 0;
                phi = 0;
                omega = 0;
                double[] centroid = model.getCentroid();
                double Cx = centroid[0];
                double Cy = centroid[1];
                double Cz = centroid[2];
                // Translate so centroid is at the origin
                model.translate(-Cx, -Cy, -Cz);

            }
            else if(ev.getActionCommand().equals("update"))
            {
                try
                {
                    theta = Double.parseDouble(RxTxt.getText());
                }catch(Exception e){}
                try
                {
                    phi = Double.parseDouble(RyTxt.getText());
                }catch(Exception e){}
                try
                {
                    omega = Double.parseDouble(RzTxt.getText());
                }catch(Exception e){}
                double Tx = 0, Ty = 0, Tz = 0;
                double[] centroid = model.getCentroid();
                double Cx = centroid[0];
                double Cy = centroid[1];
                double Cz = centroid[2];
                try
                {
                    Tx = Double.parseDouble(TxTxt.getText());
                }catch(Exception e){}
                try
                {
                    Ty = Double.parseDouble(TyTxt.getText());
                }catch(Exception e){}
                try
                {
                    Tz = Double.parseDouble(TzTxt.getText());
                }catch(Exception e){}

                try
                {
                    Cx = Double.parseDouble(CxTxt.getText());
                }catch(Exception e){}
                try
                {
                    Cy = Double.parseDouble(CyTxt.getText());
                }catch(Exception e){}
                try
                {
                    Cz = Double.parseDouble(CzTxt.getText());
                }catch(Exception e){}

                model.translateToPosition(Cx, Cy, Cz);
                model.translate(Tx, Ty, Tz);


                CxTxt.setText("");
                CyTxt.setText("");
                CzTxt.setText("");
            }
            else if(ev.getActionCommand().contains("move"))
            {
                String[] cmd = ev.getActionCommand().split(" ");
                model.translate(Integer.parseInt(cmd[1]), Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
            }
            else if(ev.getActionCommand().contains("fov"))
            {
                String[] cmd = ev.getActionCommand().split(" ");
                view3D.changeFOV(Integer.parseInt(cmd[1]));
            }
            else if(ev.getActionCommand().contains("wire"))
            {
                view3D.showWireFrame = !view3D.showWireFrame;
            }
            else if(ev.getActionCommand().contains("light"))
            {
                view3D.applyLighting = !view3D.applyLighting;
            }
            else if(ev.getActionCommand().contains("color"))
            {
                view3D.applyColor = !view3D.applyColor;
            }
            else if(ev.getActionCommand().contains("zoom"))
            {
                String[] cmd = ev.getActionCommand().split(" ");
                scale += Integer.parseInt(cmd[1]);
                if(scale == 0)
                {
                    scale += Integer.parseInt(cmd[1]);
                }
                view3D.changeScale(scale);
                topView.changeScale(scale);
                sideView.changeScale(scale);
                frontView.changeScale(scale);
            }
            else if(ev.getActionCommand().contains("roll"))
            {
                String[] cmd = ev.getActionCommand().split(" ");
                view3D.up.x += Integer.parseInt(cmd[1])/10f;
                view3D.up.z += Integer.parseInt(cmd[3])/10f;
                view3D.updateCamera();
            }
        }
        if(theta != 0 || phi != 0 || omega != 0)
        {
            model.rotate(theta, phi, omega);
        }

        view3D.repaint();
        topView.repaint();
        sideView.repaint();
        frontView.repaint();
    }

}