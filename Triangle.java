/**
 * Created by Nick on 11/30/2015.
 */
public class Triangle
{
    Point[] points;
    public Triangle(Point A, Point B, Point C)
    {
        points = new Point[3];
        points[0] = A;
        points[1] = B;
        points[2] = C;
    }
    public Vector getNormal()
    {
        Vector a = new Vector(points[0], points[1]);
        Vector b = new Vector(points[0], points[2]);

        return Vector.crossProduct(a, b);
    }
    public Point[] getPoints()
    {
        return points;
    }
    public Point getA()
    {
        return points[0];
    }
    public Point getB()
    {
        return points[1];
    }
    public Point getC()
    {
        return points[2];
    }
}
