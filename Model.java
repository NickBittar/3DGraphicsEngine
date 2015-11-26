import java.util.ArrayList;

/**
 * Created by Nick on 11/18/2015.
 */
public class Model
{
    // Contains the points of the polygon
    public ArrayList Xs;  // X points
    public ArrayList Ys;  // Y points
    public ArrayList Zs;  // Z points

    public Model(String shape, int[] args)
    {
        resetVertices();
        
        int scale = 0;
        if(args != null)
        {
            scale = args[0];
        }

        if(shape.equalsIgnoreCase("cube"))
        {
            /*
            // 4x4x4 Cube
            addPoint(scale, scale, -scale);
            addPoint(-scale, scale, -scale);
            addPoint(-scale, scale, scale);
            addPoint(scale, scale, scale);

            addPoint(-scale, -scale, -scale);
            addPoint(scale, -scale, scale);
            addPoint(-scale, -scale, scale);
            addPoint(scale, -scale, -scale);
            */

            // Top Face
            addPoint( scale,  scale,  scale);
            addPoint( scale,  scale, -scale);
            addPoint(-scale,  scale, -scale);
            addPoint( scale,  scale,  scale);
            addPoint(-scale,  scale, -scale);
            addPoint(-scale,  scale,  scale);

            // Front Face
            addPoint(-scale, -scale,  scale);
            addPoint( scale, -scale,  scale);
            addPoint(-scale,  scale,  scale);
            addPoint( scale, -scale,  scale);
            addPoint( scale,  scale,  scale);
            addPoint(-scale,  scale,  scale);

            // Side Face
            addPoint( scale, -scale,  scale);
            addPoint( scale, -scale, -scale);
            addPoint( scale,  scale,  scale);
            addPoint( scale, -scale, -scale);
            addPoint( scale,  scale, -scale);
            addPoint( scale,  scale,  scale);

            // Neg Top Face
            addPoint( scale, -scale,  scale);
            addPoint( scale, -scale, -scale);
            addPoint(-scale, -scale, -scale);
            addPoint( scale, -scale,  scale);
            addPoint(-scale, -scale, -scale);
            addPoint(-scale, -scale,  scale);

            // Neg Front Face
            addPoint(-scale, -scale, -scale);
            addPoint( scale, -scale, -scale);
            addPoint(-scale,  scale, -scale);
            addPoint( scale, -scale, -scale);
            addPoint( scale,  scale, -scale);
            addPoint(-scale,  scale, -scale);

            // Neg Side Face
            addPoint(-scale, -scale,  scale);
            addPoint(-scale, -scale, -scale);
            addPoint(-scale,  scale,  scale);
            addPoint(-scale, -scale, -scale);
            addPoint(-scale,  scale, -scale);
            addPoint(-scale,  scale,  scale);
        }
        else if(shape.equalsIgnoreCase("axis"))
        {
            scale *= 5;         // Make it 5 times bigger because it looks better
            createAxis(scale);
        }
        else if(shape.equalsIgnoreCase("triangle"))
        {
            addPoint(0, 0, scale);
            addPoint(0, scale, 0);
            addPoint(0, 0, 0);
            addPoint(scale+2, 0, 0);
            addPoint(0, scale, 0);
            addPoint(0, 0, 0);
        }
        else if(shape.equalsIgnoreCase("arrow"))
        {
            addPoint(-1, 0, -1);
            addPoint(-1, 0, 1);
            addPoint(2, 0, 1);
            addPoint(4, 0, 0);
            addPoint(2, 0, -2);
            addPoint(2, 0, -1);

        }
        else if(shape.equalsIgnoreCase("line"))
        {
            addPoint(0,0,0);
            addPoint(1,0,0);
            addPoint(2,0,0);
            addPoint(3,0,0);
            addPoint(4,0,0);
            addPoint(5,0,0);
        }
        else
        {
            addPoint(1, 1, 1);
        }



    }

    public int ptCount()
    {
        return Xs.size();
    }
    public double getX(int i)
    {
        return Double.parseDouble(Xs.get(i).toString());
    }
    public double getY(int i)
    {
        return Double.parseDouble(Ys.get(i).toString());
    }
    public double getZ(int i)
    {
        return Double.parseDouble(Zs.get(i).toString());
    }
    public void resetVertices()
    {
        // Reset points
        Xs = new ArrayList();
        Ys = new ArrayList();
        Zs = new ArrayList();
    }
    public void addPoint(double x, double y, double z)
    {
        Xs.add(x);
        Ys.add(y);
        Zs.add(z);
    }

    /**
     * Provides the center point of the polygon based on the X and Y coordinates in Xs and Ys ArrayLists.
     * @return The Centroid of the polygon
     */
    public void createAxis(int scale)
    {
        addPoint( scale, 0, 0);
        addPoint(-scale, 0, 0);

        addPoint(0,  scale, 0);
        addPoint(0, -scale, 0);

        addPoint(0, 0,  scale);
        addPoint(0, 0, -scale);
    }
    public double[] getCentroid()
    {
        double xSum = 0;
        double ySum = 0;
        double zSum = 0;
        int pointCount = Xs.size();
        // Sums all the X points, and sums all the Y points
        for(int i = 0; i < pointCount; i++)
        {
            xSum += Double.parseDouble(Xs.get(i).toString());
            ySum += Double.parseDouble(Ys.get(i).toString());
            zSum += Double.parseDouble(Zs.get(i).toString());
        }
        // returns the average of the X points and the average of the Y points.
        return new double[] { xSum/pointCount, ySum/pointCount, zSum/pointCount };
    }
    /**
     * Scales the polygon based on the provided scaling factors.
     */
    /*
    private static void scale(double[] arguments)
    {
        double Sx = arguments[0];
        double Sy = arguments[1];
        double Sz = arguments[2];
        double[] centroid = getCentroid();
        double Cx = centroid[0];
        double Cy = centroid[1];
        double Cz = centroid[2];

        // Translate so centroid is at the origin
        translate(new double[] { -Cx, -Cy, -Cz });

        // Scale all points
        for(int i = 0; i < Xs.size(); i++)
        {
            double x = Double.parseDouble(Xs.get(i).toString());
            double y = Double.parseDouble(Ys.get(i).toString());
            double z = Double.parseDouble(Zs.get(i).toString());

            // Scale Transformation
            x *= Sx;
            y *= Sy;
            z *= Sz;

            Xs.set(i, x);
            Ys.set(i, y);
            Zs.set(i, z);
        }

        // Inverse translation so centroid is in original position
        translate(new double[] { Cx, Cy, Cz });
    }
    /**
     * Rotates the polygon around its centroid.
     * @param arguments Contains the value of Theta, the angle the polygon will be rotated by.
     */

    public void rotate(double theta, double phi, double omega)
    {
        theta = Math.toRadians(theta);
        phi = Math.toRadians(phi);
        omega = Math.toRadians(omega);

        double[] centroid = getCentroid();
        double Cx = centroid[0];
        double Cy = centroid[1];
        double Cz = centroid[2];
        // Translate so centroid is at the origin
        translate( -Cx, -Cy, -Cz);
        /*
        *   To rotate...
        *       X = Cos(theta)*X - Sin(theta)*Y;
        *       Y = Sin(theta)*X + Cos(theta)*Y;
        *
        *   rotate around Y axis
        *       Z = Z*cos(theta) - X*sin(theta)
        *       X = Z*sin(theta) + X*cos(theta)
        *       Y = Y
         */

        for(int i = 0; i < Xs.size(); i++)
        {
            double x = getX(i);
            double y = getY(i);
            double z = getZ(i);
            double newX = 0;
            double newY = 0;
            double newZ = 0;
            if(theta != 0)
            {
                newY = y*Math.cos(theta) - z*Math.sin(theta);
                newZ = y*Math.sin(theta) + z*Math.cos(theta);
                newX = x;
                y = newY;
                z = newZ;
            }
            if(phi != 0)
            {
                // Rotate Transformation
                newZ = z*Math.cos(phi) - x*Math.sin(phi);
                newX = z*Math.sin(phi) + x*Math.cos(phi);
                newY = y;
                z = newZ;
                x = newX;
            }
            if(omega != 0)
            {
                newX = x*Math.cos(omega) - y*Math.sin(omega);
                newY = x*Math.sin(omega) + y*Math.cos(omega);
                newZ = z;
            }

            Xs.set(i, newX);
            Ys.set(i, newY);
            Zs.set(i, newZ);
        }
        // Inverse translation so centroid is in original position
        translate( Cx, Cy, Cz );
    }
    /**
     * Moves the polygon a given distance horizontally and vertically.
     */

    public void translate(double Tx, double Ty, double Tz)
    {
        for(int i = 0; i < Xs.size(); i++)
        {
            double x = getX(i);
            double y = getY(i);
            double z = getZ(i);

            // Translate Transformation
            x += Tx;
            y += Ty;
            z += Tz;

            Xs.set(i, x);
            Ys.set(i, y);
            Zs.set(i, z);
        }
    }

    /**
     * Prints out the current X and Y values of the vertices of the polygon.
     */
    /*
    private static void printPolygon()
    {
        System.out.print("(");
        for(int i = 0; i < Xs.size(); i++)
        {
            // Times the value by 1000, and the divides by 1000, to get points to 3 decimal places.
            System.out.print(Math.round(Double.parseDouble(Xs.get(i).toString()) * 1000.0) / 1000.0 + ", " +
                             Math.round(Double.parseDouble(Ys.get(i).toString()) * 1000.0) / 1000.0 + ", " +
                             Math.round(Double.parseDouble(Zs.get(i).toString()) * 1000.0) / 1000.0);
            if(i != Xs.size()-1)
            {
                System.out.print(", ");
            }
        }
        System.out.println(")");
    }
    */
}
