import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ThreeDimensionalView extends JPanel
{
    Model model;
    Model projection;   // The transformed model in 2d screen coordinates
    int vertexCount;
    int triangleCount;

    int w, h;                               // Width and Height of screen/panel

    double H[], AhomogenousVal[];
    double eX, eY, eZ;                      // Camera Location
    double fov;                             // Field of View
    double aspectRatio;                     // Aspect Ratio
    Vector up;                              // Up vector
    Vector l;                               // Look Vector
    double n, f;                            // Near and Far clip planes

    Vector U, V , W;

    double[] viewX, viewY, viewZ;           // View coordinates
    double[] persX, persY, persZ;           // Perspective coordinates
    double[] ndcX, ndcY, ndcZ;              // Normalized Device Coordinates
    int[] screenX, screenY, screenZ;        // Screen coordinates
    
    double[] AviewX, AviewY, AviewZ;        // View coordinates
    double[] ApersX, ApersY, ApersZ;        // Perspective coordinates
    double[] AndcX, AndcY, AndcZ;           // Normalized Device Coordinates
    int[] AscreenX, AscreenY, AscreenZ;     // Screen coordinates

    Model axis;
    int AvertexCount = 6;

    BufferedImage img;
    double[][] zBuffer;

    Light light;

    public ThreeDimensionalView(Model obj)
    {
        model = obj;
        projection = model.copy();
        //vertexCount = model.ptCount();
        triangleCount = model.getTriangleCount();
        vertexCount = model.getVertexCount();
        axis = new Model("axis", new int[]{5});
        light = new Light(new Point(5, 5, 0));
        w = 300;
        h = 300;

        updateCamera();

    }
    public void updateCamera()
    {
        eX = 0;
        eY = 0;
        eZ = -10;
        fov = 60;
        aspectRatio = 1/1;
        up = new Vector(0, 1, 0);
        l = new Vector(1, 1, 1);

        n = 0.1;
        f = 100;

        U = new Vector();
        V = new Vector();
        W = new Vector();

        H = new double[vertexCount];
        AhomogenousVal = new double[AvertexCount];

        viewX = new double[vertexCount];
        viewY = new double[vertexCount];
        viewZ = new double[vertexCount];

        persX = new double[vertexCount];
        persY = new double[vertexCount];
        persZ = new double[vertexCount];

        ndcX = new double[vertexCount];
        ndcY = new double[vertexCount];
        ndcZ = new double[vertexCount];

        screenX = new int[vertexCount];
        screenY = new int[vertexCount];
        screenZ = new int[vertexCount];

        AviewX = new double[AvertexCount];
        AviewY = new double[AvertexCount];
        AviewZ = new double[AvertexCount];

        ApersX = new double[AvertexCount];
        ApersY = new double[AvertexCount];
        ApersZ = new double[AvertexCount];

        AndcX = new double[AvertexCount];
        AndcY = new double[AvertexCount];
        AndcZ = new double[AvertexCount];

        AscreenX = new int[AvertexCount];
        AscreenY = new int[AvertexCount];
        AscreenZ = new int[AvertexCount];

        W.setAsDivisionOf(l.neg(), l.getMagnitude());
        Vector up_X_W = new Vector();
        up_X_W.setAsCrossProductOf(up, W);
        U.setAsDivisionOf(up_X_W, up_X_W.getMagnitude());

        V.setAsCrossProductOf(W, U);
    }
    public void update()
    {
        projection = model.copy();
        for(int i =0; i < vertexCount; i++)
        {
            H[i] = 1;
        }
        for(int i = 0; i < AvertexCount; i++)
        {
            AhomogenousVal[i] = 1;
        }

        applyViewMatrixTransformation();
        applyPerspectiveMatrixTransformation();
        homogenizeToNDC();
        NDCtoScreenCoordinates();
    }
    public void applyViewMatrixTransformation()
    {
        /*
        for(int i = 0; i < vertexCount; i++)
        {
            viewX[i] = model.getX(i)*U.x + model.getY(i)*U.y + model.getZ(i)*U.z + H[i] *(-eX);
            viewY[i] = model.getX(i)*V.x + model.getY(i)*V.y + model.getZ(i)*V.z + H[i] *(-eY);
            viewZ[i] = model.getX(i)*W.x + model.getY(i)*W.y + model.getZ(i)*W.z + H[i] *(-eZ);
            H[i] = 0 + 0 + 0 + H[i] *1;
        }
        */
        Point p;
        double x, y, z, h;

        for(int i = 0; i < vertexCount; i++)
        {
            p = model.getVertex(i);
            x = p.getX();
            y = p.getY();
            z = p.getZ();
            h = H[i];
            viewX[i] = x * U.x + y * U.y + z * U.z + h * (-eX);
            viewY[i] = x * V.x + y * V.y + z * V.z + h * (-eY);
            viewZ[i] = x * W.x + y * W.y + z * W.z + h * (-eZ);
            H[i] = 0 + 0 + 0 + h * 1;

        }
        for(int i = 0; i < AvertexCount; i++)
        {
            AviewX[i] = axis.getX(i)*U.x + axis.getY(i)*U.y + axis.getZ(i)*U.z + AhomogenousVal[i] *(-eX);
            AviewY[i] = axis.getX(i)*V.x + axis.getY(i)*V.y + axis.getZ(i)*V.z + AhomogenousVal[i] *(-eY);
            AviewZ[i] = axis.getX(i)*W.x + axis.getY(i)*W.y + axis.getZ(i)*W.z + AhomogenousVal[i] *(-eZ);
            AhomogenousVal[i] = 0 + 0 + 0 + AhomogenousVal[i] *1;
        }
    }
    public void applyPerspectiveMatrixTransformation()
    {
        for(int i = 0; i < vertexCount; i++)
        {
            persX[i] = viewX[i]*(1/(aspectRatio*Math.tan(Math.toRadians(fov)/2))) + 0 + 0 + 0;
            persY[i] = 0 + viewY[i]*(1/(Math.tan(Math.toRadians(fov)/2))) + 0 + 0;
            persZ[i] = 0 + 0 + viewZ[i]*((n+f)/(n-f)) + H[i] *((2*n*f)/(n-f));
            H[i] = 0 + 0 + viewZ[i]*(-1) + 0;
        }

        for(int i = 0; i < AvertexCount; i++)
        {
            ApersX[i] = AviewX[i]*(1/(aspectRatio*Math.tan(Math.toRadians(fov)/2))) + 0 + 0 + 0;
            ApersY[i] = 0 + AviewY[i]*(1/(Math.tan(Math.toRadians(fov)/2))) + 0 + 0;
            ApersZ[i] = 0 + 0 + AviewZ[i]*((n+f)/(n-f)) + AhomogenousVal[i] *((2*n*f)/(n-f));
            AhomogenousVal[i] = 0 + 0 + AviewZ[i]*(-1) + 0;
        }
    }
    public void homogenizeToNDC()
    {
        for(int i = 0; i < vertexCount; i++)
        {
            ndcX[i] = persX[i]/ H[i];
            ndcY[i] = persY[i]/ H[i];
            ndcZ[i] = persZ[i]/ H[i];
            H[i] /= H[i];
        }
        for(int i = 0; i < AvertexCount; i++)
        {
            AndcX[i] = ApersX[i]/ AhomogenousVal[i];
            AndcY[i] = ApersY[i]/ AhomogenousVal[i];
            AndcZ[i] = ApersZ[i]/ AhomogenousVal[i];
            AhomogenousVal[i] /= AhomogenousVal[i];
        }
    }
    public void NDCtoScreenCoordinates()
    {
        for(int i = 0; i < vertexCount; i++)
        {
            screenX[i] = (int) Math.round(((ndcX[i]+1)*w/2));
            screenY[i] = (int) Math.round(((ndcY[i]+1)*h/2));
            projection.setVertex(i, new Point(screenX[i], screenY[i], ndcZ[i]));
        }
        for(int i = 0; i < AvertexCount; i++)
        {
            AscreenX[i] = (int) Math.round(((AndcX[i]+1)*w/2));
            AscreenY[i] = (int) Math.round(((AndcY[i]+1)*h/2));
        }
        projection.retriangulate();
    }

    public void paint(Graphics g)
    {
        w = getWidth();
        h = getHeight();
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        //zBuffer = new double[w][h];
        int color;
        update();

        color = Color.GRAY.getRGB();
        clear(color);

        for(int i = 0; i < AvertexCount; i+= 2)
        {
            if(i < 2)
            {
                color = Color.RED.getRGB();
            }
            else if(i < 4)
            {
                color = Color.GREEN.getRGB();
            }
            else
            {
                color = Color.BLUE.getRGB();
            }
            drawLine(AscreenX[i], AscreenY[i], AscreenX[i + 1], AscreenY[i + 1], color);
        }
        Triangle T;
        double[] lighting;
        for(int y = 0; y < img.getHeight(); y++)
        {
            for(int x = 0; x < img.getWidth(); x++)
            {
                for(int i = 0; i < projection.getTriangleCount(); i++)
                {
                    T = projection.getTriangle(i);
                    if(T.isFacingCamera() && T.containsPoint(new Point(x, y, 0)))
                    {
                        lighting = light.calculateIntensity(model.getTriangle(i).getCentroid(), model.getTriangle(i).getNormal(), new Point(eX, eY, eZ));
                        //img.setRGB(x, y, new Color(150, 30, 30).getRGB());
                        img.setRGB(x, y, new Color((int)(lighting[0]*255), (int) (lighting[1]*255), (int) (lighting[2]*255)).getRGB());
                        break;
                    }
                }
            }
        }
        color = Color.BLACK.getRGB();
        /*
        for(int i = 0; i < triangleCount; i++)
        {
            T = projection.getTriangle(i);
            if(T.isFacingCamera())
            {
                Point A = T.getA();
                Point B = T.getB();
                Point C = T.getC();
                drawLine((int) A.getX(), (int) A.getY(), (int) B.getX(), (int) B.getY(), color);
                drawLine((int) B.getX(), (int) B.getY(), (int) C.getX(), (int) C.getY(), color);
                drawLine((int) C.getX(), (int) C.getY(), (int) A.getX(), (int) A.getY(), color);
            }
        }
        */
        g.drawImage(img, 0, 0, null);
        g.setColor(Color.BLACK);
        g.drawString("3D", 4, 16);
    }
    private void drawLine(int xi, int yi,int xf, int yf, int color)
    {
        int x = xi;
        int y = yi;
        int dx = Math.abs(xf - x);
        int dy = Math.abs(yf - y);

        int sx = x < xf ? 1 : -1;
        int sy = y < yf ? 1 : -1;

        int err = dx-dy;
        int e2;

        while (true)
        {
            if(x >= 0 && x < w && y >= 0 && y < h)
            {
                img.setRGB(x, y, color);
            }
            if (x == xf && y == yf)
                break;

            e2 = 2 * err;
            if (e2 > -dy)
            {
                err = err - dy;
                x = x + sx;
            }

            if (e2 < dx)
            {
                err = err + dx;
                y = y + sy;
            }
        }
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

    public void changeFOV(int fov)
    {
        this.fov += fov;
        // Thanks BearishMushroom
        if(this.fov > 120)
        {
            this.fov = 120;
        }
        else if(this.fov < 20)
        {
            this.fov = 20;
        }
    }
}