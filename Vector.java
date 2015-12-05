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
    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
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
    public static double dotProduct(Vector a, Vector b)
    {
        return a.getX()*b.getX() + a.getY()*b.getY() + a.getZ()*b.getZ();
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
    public static Vector divideVector(Vector v, double a)
    {
        double x, y, z;
        x = v.x/a;
        y = v.y/a;
        z = v.z/a;

        return new Vector(x, y, z);
    }
    public void divideBy(double a)
    {
        x /= a;
        y /= a;
        z /= a;
    }
    public static Vector multiplyVector(Vector v, double a)
    {
        double x, y, z;
        x = a*v.getX();
        y = a*v.getY();
        z = a*v.getZ();
        return new Vector(x, y, z);
    }
    public void multiplyBy(double a)
    {
        x *= a;
        y *= a;
        z *= a;
    }
    public static Vector add(Vector a, Vector b)
    {
        double x, y, z;
        x = a.getX() + b.getX();
        y = a.getY() + b.getY();
        z = a.getZ() + b.getZ();
        return new Vector(x, y, z);
    }
    public static Vector neg(Vector v)
    {
        return new Vector(-v.getX(), -v.getY(), -v.getZ());
    }
    public Vector neg()
    {
        return new Vector(-x, -y, -z);
    }
    public void normalize()
    {
        double length = getMagnitude();
        divideBy(length);
    }
}
