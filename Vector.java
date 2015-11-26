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
    public Vector neg()
    {
        return new Vector(-x, -y, -z);
    }
}
