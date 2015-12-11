import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Nick on 11/25/2015.
 */
public class View
{
    public static final int VIEW_TOP = 0;
    public static final int VIEW_SIDE = 1;
    public static final int VIEW_FRONT = 2;

    private int view;

    private Model model;
    private Model projection;
    private int vertexCount;
    private int triangleCount;

    private Axis axis;
    private int axisCount = 3;

    private double[] worldX, worldY, worldZ;

    private int[] screenX, screenY;

    private BufferedImage img;

    private int w, h;

    private int scale;             // How wide of a space to be projected; A scale of 5 give a 10x10 viewing space

    public View(Model obj, int view, int scale)        // View: 0 = top; 1 = side; 2 = front
    {
        model = obj;
        vertexCount = obj.getVertexCount();
        triangleCount = obj.getTriangleCount();
        projection = obj.copy();

        worldX = new double[vertexCount];
        worldY = new double[vertexCount];
        worldZ = new double[vertexCount];

        screenX = new int[vertexCount];
        screenY = new int[vertexCount];

        this.view = view;

        this.scale = scale;

        w = 300;
        h = 300;

        updateAxis(scale);
    }
    
    public void updateAxis(int scale)
    {
        axis = new Axis(scale);
        mapAxisToView();
        mapAxisToScreen();
    }
    private void mapAxisToView()
    {
        double tempX, tempY, tempZ;
        if (view == VIEW_TOP)            // X -> x; Y -> z; Z -> -y
        {
            for (int i = 0; i < axis.getVertexCount(); i++)
            {
                tempX = axis.getX(i);
                tempY = axis.getZ(i);
                tempZ = -axis.getY(i);
                axis.setX(i, tempX);
                axis.setY(i, tempY);
                axis.setZ(i, tempZ);
            }
        }
        else if (view == VIEW_SIDE)      // X -> z; Y -> y; Z -> -x
        {
            for (int i = 0; i < axis.getVertexCount(); i++)
            {
                tempX = axis.getZ(i);
                tempY = axis.getY(i);
                tempZ = -axis.getX(i);
                axis.setX(i, tempX);
                axis.setY(i, tempY);
                axis.setZ(i, tempZ);
            }
        }
        else if (view == VIEW_FRONT)     // X -> x; Y -> y; Z ->  z
        {
            for (int i = 0; i < axis.getVertexCount(); i++)
            {
                tempX = axis.getX(i);
                tempY = axis.getY(i);
                tempZ = axis.getZ(i);
                axis.setX(i, tempX);
                axis.setY(i, tempY);
                axis.setZ(i, tempZ);
            }
        }
    }
    private void mapAxisToScreen()
    {
        for (int i = 0; i < axis.getVertexCount(); i++)
        {
            axis.setX(i, (Math.round(w / (2 * scale) * axis.getX(i))) + (w / 2));
            axis.setY(i, (Math.round(h / (2 * scale) * axis.getY(i))) + (h / 2));
        }
    }
    public void update(int width, int height)
    {
        this.w = width;
        this.h = height;
        updateAxis(scale);
        loadVertices();

        worldToScreen();
    }

    /**
     * Loads the model's vertices into local arrays
     */
    private void loadVertices()
    {
        Point p;
        for (int i = 0; i < vertexCount; i++)
        {
            p = model.getVertex(i);
            worldX[i] = p.getX();
            worldY[i] = p.getY();
            worldZ[i] = p.getZ();
        }
    }

    /**
     * Turns the world points to screen points remapped to the specified view.
     * The Mapping:
     * 0:Top View       X ->  x;    Y ->  z;       ??   Z -> -y     ??
     * 1:Side View      X -> -z;    Y -> -y;       ??   Z ->  x     ??
     * 2:Front View     X ->  x;    Y -> -y;       ??   Z ->  z     ??
     */
    private void worldToScreen()
    {
        Point p;
        if (view == VIEW_TOP)            // X -> x; Y -> z; Z -> -y
        {
            for (int i = 0; i < vertexCount; i++)
            {
                p = model.getVertex(i);
                screenX[i] = (int) (Math.round(w / (2*scale) * p.getX())) + w / 2;
                screenY[i] = (int) (Math.round(h / (2*scale) * p.getZ())) + h / 2;
                projection.setVertex(i, new Point(screenX[i], screenY[i], worldZ[i]));
            }
        }
        else if (view == VIEW_SIDE)      // X -> z; Y -> y; Z -> -x
        {
            for (int i = 0; i < vertexCount; i++)
            {
                p = model.getVertex(i);
                screenX[i] = (int) (Math.round(w / (2*scale) * -p.getZ())) + w / 2;
                screenY[i] = (int) (Math.round(h / (2*scale) * -p.getY())) + h / 2;
                projection.setVertex(i, new Point(screenX[i], screenY[i], worldZ[i]));
            }
        }
        else if (view == VIEW_FRONT)     // X -> x; Y -> y; Z ->  z
        {
            for (int i = 0; i < vertexCount; i++)
            {
                p = model.getVertex(i);
                screenX[i] = (int) (Math.round(w / (2*scale) * p.getX())) + w / 2;
                screenY[i] = (int) (Math.round(h / (2*scale) * -p.getY())) + h / 2;
                projection.setVertex(i, new Point(screenX[i], screenY[i], worldZ[i]));
            }
        }
        projection.retriangulate();
    }

    public void draw()
    {
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        // Color background white
        clear(-1);                  // -1 is rgb code for White

        // Draw Grid Lines
        drawGridLines();

        // Draw Axis
        drawAxis();

        // Draw Projection
        int color = Color.BLACK.getRGB();
        Triangle T;
        Point A, B, C;
        for(int i = 0; i < triangleCount; i++)
        {
            T = projection.getTriangle(i);
            A = T.getA();
            B = T.getB();
            C = T.getC();
            drawLine((int) A.getX(), (int) A.getY(), (int) B.getX(), (int) B.getY(), color);
            drawLine((int) B.getX(), (int) B.getY(), (int) C.getX(), (int) C.getY(), color);
            drawLine((int) C.getX(), (int) C.getY(), (int) A.getX(), (int) A.getY(), color);
        }
        // Fill in surfaces of model

        // Apply Lighting effects
    }
    private void drawAxis()
    {
        for(int i = 0; i < axis.getVertexCount(); i+= 2)
        {
            int color = -1;

            if(view == VIEW_TOP)
            {
                if(i < 2)
                    color = Color.RED.getRGB();
                else if(i < 6)
                    color = Color.BLUE.getRGB();
            }
            else if(view == VIEW_SIDE)
            {
                if(i < 4)
                    color = Color.GREEN.getRGB();
                else if(i < 6)
                    color = Color.BLUE.getRGB();
            }
            else if(view == VIEW_FRONT)
            {
                if (i < 2)
                    color = Color.RED.getRGB();
                else if(i < 4)
                    color = Color.GREEN.getRGB();
            }

            drawLine((int) axis.getX(i), (int) axis.getY(i), (int) axis.getX(i + 1), (int) axis.getY(i + 1), color);
        }
    }
    private void drawGridLines()
    {
        int color = Color.lightGray.getRGB();
        for(int i = 0; i < (2*scale); i++)
        {
            drawLine(0, (h*i/(2*scale)), w, (h*i/(2*scale)), color);
            drawLine((w*i/(2*scale)), 0, (w*i/(2*scale)), h, color);
        }
    }
    private void drawLine(int xi, int yi, int xf, int yf, int color)
    {
        int dx = Math.abs(xf - xi);
        int dy = Math.abs(yf - yi);

        int sx = xi < xf ? 1 : -1;
        int sy = yi < yf ? 1 : -1;

        int err = dx-dy;
        int e2;

        while (true)
        {
            if(xi >= 0 && xi < w && yi >= 0 && yi < h)
            {
                img.setRGB(xi, yi, color);
            }
            if (xi == xf && yi == yf)
                break;

            e2 = 2 * err;
            if (e2 > -dy)
            {
                err = err - dy;
                xi = xi + sx;
            }

            if (e2 < dx)
            {
                err = err + dx;
                yi = yi + sy;
            }
        }
    }
    public void changeScale(int scale)
    {
        this.scale = scale;
        updateAxis(this.scale);
    }
    public BufferedImage getImage()
    {
        return img;
    }

    private void clear(int color)
    {
        // Fill background
        for (int y = 0; y < h; y++)
        {
            for (int x = 0; x < w; x++)
            {
                img.setRGB(x, y, color);
            }
        }
    }

    public int getView()
    {
        return view;
    }
}
