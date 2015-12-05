/**
 * Created by Nick on 12/5/2015.
 */
public class Light
{

    Point position;  // Position in world space
    Vector l, v, r, n;      // Light, View, Reflection, Normal Vectors respectively

    double[] I;

    double[][] L;
    private final int RED = 0;
    private final int GREEN = 1;
    private final int BLUE = 2;
    private final int AMBIENT = 0;
    private final int DIFFUSE = 1;
    private final int SPECULAR = 2;

    double alpha;       // Shininess factor

    double Ka, Kd, Ks;

    double a, b, c;     // Constants

    double d;           // Distance between light source and point on surface

    public Light(Point p)
    {
        position = p;

        I = new double[3];

        L = new double[3][3];
        L[RED][AMBIENT] = 1.0;
        L[RED][DIFFUSE] = 0.9;
        L[RED][SPECULAR] = 0.8;
        L[GREEN][AMBIENT] = 0.2;
        L[GREEN][DIFFUSE] = 0.7;
        L[GREEN][SPECULAR] = 0.9;
        L[BLUE][AMBIENT] = 1.0;
        L[BLUE][DIFFUSE] = 0.8;
        L[BLUE][SPECULAR] = 1.0;

        Ka = 0.5;
        Kd = 0.5;
        Ks = 0.5;

        a = 2;
        b = 2;
        c = 2;

        alpha = 10;
    }
    public double[] calculateIntensity(Point p, Vector normal, Point e)
    {
        l = new Vector(position, p);
        n = normal;
        v = new Vector(e, p);
        calculateReflectionVector();
        normalizeVectors();
        double attenuation;
        attenuation = 1/(a + b*d + c*d*d);
        for(int color = 0; color < 3; color++)
        {
            I[color] = attenuation*(Kd*L[color][DIFFUSE]*Math.max(Vector.dotProduct(l, n), 0) + Ks*L[color][SPECULAR]*Math.max(Math.pow(Vector.dotProduct(r, v), 2), 0)) + Ka*L[color][AMBIENT];
        }
        return I;
    }
    private void calculateReflectionVector()
    {
        r = Vector.add(l, Vector.multiplyVector(n, (2 * Vector.dotProduct(l, n) / (Math.pow(n.getMagnitude(), 2)))).neg());
    }
    private void normalizeVectors()
    {
        l.normalize();
        v.normalize();
        r.normalize();
        n.normalize();
    }

}
