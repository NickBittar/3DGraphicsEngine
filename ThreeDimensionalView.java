import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ThreeDimensionalView extends JPanel
{
    Model model;
    int vertexCount;

    int w, h;                               // Width and Height of screen/panel

    double homogenousVal[], AhomogenousVal[];
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

    public ThreeDimensionalView(Model obj)
    {
        model = obj;
        vertexCount = model.ptCount();
        axis = new Model("axis", new int[]{5});
        w = 300;
        h = 300;
    }

    public void update()
    {

        homogenousVal = new double[vertexCount];
        for(int i =0; i < vertexCount; i++)
        {
            homogenousVal[i] = 1;
        }
        AhomogenousVal = new double[AvertexCount];
        for(int i = 0; i < AvertexCount; i++)
        {
            AhomogenousVal[i] = 1;
        }
        eX = 0;
        eY = 0;
        eZ = -20;
        fov = 60;
        aspectRatio = 1/1;
        up = new Vector(0, 1, 0);
        l = new Vector(10, 10, 10);

        n = 1;
        f = 10;

        U = new Vector();
        V = new Vector();
        W = new Vector();

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

        applyViewMatrixTransformation();
        applyPerspectiveMatrixTransformation();
        homogenizeToNDC();
        NDCtoScreenCoordinates();
    }
    public void applyViewMatrixTransformation()
    {
        for(int i = 0; i < vertexCount; i++)
        {
            viewX[i] = model.getX(i)*U.x + model.getY(i)*U.y + model.getZ(i)*U.z + homogenousVal[i] *(-eX);
            viewY[i] = model.getX(i)*V.x + model.getY(i)*V.y + model.getZ(i)*V.z + homogenousVal[i] *(-eY);
            viewZ[i] = model.getX(i)*W.x + model.getY(i)*W.y + model.getZ(i)*W.z + homogenousVal[i] *(-eZ);
            homogenousVal[i] = 0 + 0 + 0 + homogenousVal[i] *1;
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
            persZ[i] = 0 + 0 + viewZ[i]*((n+f)/(n-f)) + homogenousVal[i] *((2*n*f)/(n-f));
            homogenousVal[i] = 0 + 0 + viewZ[i]*(-1) + 0;
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
            ndcX[i] = persX[i]/ homogenousVal[i];
            ndcY[i] = persY[i]/ homogenousVal[i];
            ndcZ[i] = persZ[i]/ homogenousVal[i];
            homogenousVal[i] /= homogenousVal[i];
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
        }
        for(int i = 0; i < AvertexCount; i++)
        {
            AscreenX[i] = (int) Math.round(((AndcX[i]+1)*w/2));
            AscreenY[i] = (int) Math.round(((AndcY[i]+1)*h/2));
        }
    }

    public void paint(Graphics g)
    {
        w = getWidth();
        h = getHeight();
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
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

        color = Color.BLACK.getRGB();
        for(int i = 0; i < vertexCount; i+= 3)
        {
            /*
            g.fillOval(screenX[i]- 2, screenY[i] - 2, 4, 4);
            g.fillOval(screenX[i+1]- 2, screenY[i+1] - 2, 4, 4);
            g.fillOval(screenX[i+2]- 2, screenY[i+2] - 2, 4, 4);
            g.drawLine(screenX[i], screenY[i], screenX[i+1], screenY[i+1]);
            g.drawLine(screenX[i+1], screenY[i+1], screenX[i+2], screenY[i+2]);
            g.drawLine(screenX[i+2], screenY[i+2], screenX[i], screenY[i]);
            */
            drawLine(screenX[i], screenY[i], screenX[i+1], screenY[i+1], color);
            drawLine(screenX[i+1], screenY[i+1], screenX[i+2], screenY[i+2], color);
            drawLine(screenX[i+2], screenY[i+2], screenX[i], screenY[i], color);
        }

        g.drawImage(img, 0, 0, null);
        g.setColor(Color.BLACK);
        g.drawString("3D", 4, 16);
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
}