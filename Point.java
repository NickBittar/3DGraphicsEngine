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
}