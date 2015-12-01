/**
 * Created by Nick on 11/18/2015.
 */
public class Vector
{
    public double x;
    public double y;
    public double z;
    public Vector(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector(Point a, Point b)
    {
        //From a to b
        this.x = b.getX() - a.getX();
        this.y = b.getY() - a.getY();
        this.z = b.getZ() - a.getZ();
    }
    public Vector(Point p)
    {
        this.x = p.getX();
        this.y = p.getY();
        this.z = p.getZ();
    }
    public Vector()
    {

    }
    public double getMagnitude()
    {
        return Math.sqrt(x*x+y*y+z*z);
    }
    public void setAsCrossProductOf(Vector a, Vector b)
    {
        double x, y, z;

        x = a.y*b.z - b.y*a.z;
        y = a.z*b.x - a.x*b.z;
        z = b.y*a.x - a.y*b.x;

        this.x = x;
        this.y = y;
        this.z = z;
    }
    public static Vector crossProduct(Vector a, Vector b)
    {
        double x, y, z;

        x = a.y*b.z - b.y*a.z;
        y = a.z*b.x - a.x*b.z;
        z = b.y*a.x - a.y*b.x;

        return new Vector(x, y, z);
    }
    public void setAsDivisionOf(Vector a, double b)
    {
        double x, y, z;
        x = a.x/b;
        y = a.y/b;
        z = a.z/b;

        this.x = x;
        this.y = y;
        this.z = z;
    }
    public static Vector divideVectors(Vector a, double b)
    {
        double x, y, z;
        x = a.x/b;
        y = a.y/b;
        z = a.z/b;

        return new Vector(x, y, z);
    }
    public Vector neg()
    {
        return new Vector(-x, -y, -z);
    }
}
