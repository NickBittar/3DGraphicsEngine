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
    public boolean isFacingCamera()
    {
        return getNormal().getZ() < 0;
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

    /**
     * Help with this function from http://www.blackpawn.com/texts/pointinpoly/
     * @param p Point to check if it is within this triangle
     * @return true if p is inside this triangle, false if p is outside of this triangle
     */
    public boolean containsPoint(Point p)
    {
        Vector v0, v1, v2;
        double dot00, dot01, dot02, dot11, dot12;
        double u, v;
        double invDenom;
        Point a, b, c;

        a = points[0];
        b = points[1];
        c = points[2];

        // Get Vectors
        v0 = new Vector(a, c);
        v1 = new Vector(a, b);
        v2 = new Vector(a, p);

        // get dot products
        dot00 = Vector.dotProduct(v0, v0);
        dot01 = Vector.dotProduct(v0, v1);
        dot02 = Vector.dotProduct(v0, v2);
        dot11 = Vector.dotProduct(v1, v1);
        dot12 = Vector.dotProduct(v1, v2);

        // Compute barycentric coordinates
        invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        // Check if point is in triangle
        return (u >= 0) && (v >= 0) && (u + v < 1);
    }
    public double getDepthAt(int x, int y)
    {
        Point A, B, C, P;
        P = new Point(x, y);
        A = points[0];
        B = points[1];
        C = points[2];
        double a, b, c, z;         // The Z values at each point of triangle
        a = A.getZ();
        b = B.getZ();
        c = C.getZ();

        double areaA, areaB, areaC, area;
        area = areaOfTriangle(A, B, C);
        areaA = areaOfTriangle(B, C, P);
        areaB = areaOfTriangle(C, A, P);
        areaC = areaOfTriangle(A, B, P);

        z = a*(areaA/area) + b*(areaB/area) + c*(areaC/area);

        return z;
    }
    public double areaOfTriangle(Point A, Point B, Point C)
    {
        double s, area;
        double a, b, c; // Length of sides
        a = Point.distanceBetween(B, C);
        b = Point.distanceBetween(C, A);
        c = Point.distanceBetween(A, B);
        s = (a + b + c)/2;

        area = Math.sqrt(s*(s-a)*(s-b)*(s-c));

        return area;
    }

}
