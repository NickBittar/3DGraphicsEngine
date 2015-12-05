/**
 * Created by Nick on 11/25/2015.
 */
public class Point
{
    public double x;
    public double y;
    public double z;

    public Point()
    {
        x = 0;
        y = 0;
        z = 0;
    }
    public Point(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public void setX(double x)
    {
        this.x = x;
    }
    public double getX()
    {
        return x;
    }

    public void setY(double y)
    {
        this.y = y;
    }
    public double getY()
    {
        return y;
    }

    public void setZ(double z)
    {
        this.z = z;
    }
    public double getZ()
    {
        return z;
    }

    public void change(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void translate(double Tx, double Ty, double Tz)
    {
        this.x += Tx;
        this.y += Ty;
        this.z += Tz;
    }
    public static double distanceBetween(Point a, Point b)
    {
        double x1, y1, x2, y2;
        double x, y, d;

        x1 = a.getX();
        y1 = a.getY();
        x2 = b.getX();
        y2 = b.getY();

        x = x2-x1;
        y = y2-y1;

        d = Math.sqrt(x*x + y*y);

        return d;
    }
}