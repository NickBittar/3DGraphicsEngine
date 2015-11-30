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
        clearVertices();
        
        int scale = 0;
        if(args != null)
        {
            scale = args[0];
        }

        if(shape.equalsIgnoreCase("cube"))
        {
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
            scale *= 2;         // Make it 5 times bigger because it looks better
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
        else
        {
            addPoint(0, 0, 0);
        }
    }

    /**
     * Gets the current amount of vertices in the model.
     *
     * @return The number of vertices in the model.
     */
    public int ptCount()
    {
        return Xs.size();
    }

    /**
     * Provides the X-value of the vertex at the specified index.
     *
     * @param i The index of the point.
     * @return The X value of the point.
     */
    public double getX(int i)
    {
        return Double.parseDouble(Xs.get(i).toString());
    }
    /**
     * Provides the Y-value of the vertex at the specified index.
     *
     * @param i The index of the point.
     * @return The Y value of the point.
     */
    public double getY(int i)
    {
        return Double.parseDouble(Ys.get(i).toString());
    }
    /**
     * Provides the Z-value of the vertex at the specified index.
     *
     * @param i The index of the point.
     * @return The Z value of the point.
     */
    public double getZ(int i)
    {
        return Double.parseDouble(Zs.get(i).toString());
    }

    /**
     * Provides a way of resetting the model's vertices
     */
    public void clearVertices()
    {
        // Reset points
        Xs = new ArrayList();
        Ys = new ArrayList();
        Zs = new ArrayList();
    }

    /**
     * Adds a vertex at the specified location.  This method should be used in groups of three's to specify triangles.
     *
     * @param x The X value of the point.
     * @param y The Y Value of the point.
     * @param z The Z Value of the point.
     */
    public void addPoint(double x, double y, double z)
    {
        Xs.add(x);
        Ys.add(y);
        Zs.add(z);
    }

    /**
     * Provides the center point of the polygon based on the X and Y coordinates in Xs and Ys ArrayLists.
     *
     * @return The Centroid of the polygon
     */
    public void createAxis(int scale)
    {
        addPoint( scale, 0, 0);
        addPoint(-scale, 0, 0);

        addPoint(0,  scale, 0);
        addPoint(0, -scale, 0);

        addPoint(0, 0, scale);
        addPoint(0, 0, -scale);
    }

    /**
     * Provides the center of the model in XYZ space.
     *
     * @return An array containing the [0]X, [1]Y, [2]Z Values of the centroid of the model.
     */
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
     * Scales the model with the specified scaling factors.
     *
     * @param Sx The scaling factor in the X direction.
     * @param Sy The scaling factor in the Y direction.
     * @param Sz The scaling factor in the Z direction.
     */
    public void scale(double Sx, double Sy, double Sz)
    {
        double[] centroid = getCentroid();
        double Cx = centroid[0];
        double Cy = centroid[1];
        double Cz = centroid[2];

        // Translate so centroid is at the origin
        translate(-Cx, -Cy, -Cz);

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
        translate(Cx, Cy, Cz);
    }

    /**
     * Moves the vertices of the model to make the object rotate around its centroid.
     *
     * @param theta The angle of rotation, counter-clockwise, around the X-axis.
     * @param phi   The angle of rotation, counter-clockwise, around the Y-axis.
     * @param omega The angle of rotation, counter-clockwise, around the Z-axis.
     */
    public void rotate(double theta, double phi, double omega)
    {
        double[] centroid = getCentroid();
        double Cx = centroid[0];
        double Cy = centroid[1];
        double Cz = centroid[2];

        rotateAroundPivotPoint(theta, phi, omega, Cx, Cy, Cz);
    }

    /**
     * Rotates the model around a specified point in space.
     *
     * @param theta The angle of rotation, counter-clockwise, around the X-axis.
     * @param phi   The angle of rotation, counter-clockwise, around the Y-axis.
     * @param omega The angle of rotation, counter-clockwise, around the Z-axis.
     * @param Px The pivot point's world-space X-coordinate.
     * @param Py The pivot point's world-space Y-coordinate.
     * @param Pz The pivot point's world-space Z-coordinate.
     */
    public void rotateAroundPivotPoint(double theta, double phi, double omega, double Px, double Py, double Pz)
    {
        theta = Math.toRadians(theta);
        phi = Math.toRadians(phi);
        omega = Math.toRadians(omega);

        // Translate so centroid is at pivot point
        translate( -Px, -Py, -Pz);

        for(int i = 0; i < Xs.size(); i++)
        {
            double x = getX(i);
            double y = getY(i);
            double z = getZ(i);
            double newX = 0;
            double newY = 0;
            double newZ = 0;
            boolean newPoints = false;
            if(theta != 0)
            {
                newY = y*Math.cos(theta) - z*Math.sin(theta);
                newZ = y*Math.sin(theta) + z*Math.cos(theta);
                newX = x;
                y = newY;
                z = newZ;
                newPoints = true;
            }
            if(phi != 0)
            {
                // Rotate Transformation
                newZ = z*Math.cos(phi) - x*Math.sin(phi);
                newX = z*Math.sin(phi) + x*Math.cos(phi);
                newY = y;
                z = newZ;
                x = newX;
                newPoints = true;
            }
            if(omega != 0)
            {
                newX = x*Math.cos(omega) - y*Math.sin(omega);
                newY = x*Math.sin(omega) + y*Math.cos(omega);
                newZ = z;
                newPoints = true;

            }
            if(newPoints)
            {
                Xs.set(i, newX);
                Ys.set(i, newY);
                Zs.set(i, newZ);
            }
        }
        // Inverse translation so centroid is in original position
        translate( Px, Py, Pz );
    }

    /**
     * Moves all the vertices uniformly a specified amount to move the model.
     *
     * @param Tx The distance to move all points on the X-axis.
     * @param Ty The distance to move all points on the Y-axis.
     * @param Tz The distance to move all points on the Z-axis.
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
     * Moves the model so its centroid is exactly at the origin in world-space.
     *
     * @return An array containing the X, Y, and Z values of the distance the model was moved. (to inverse translate)
     */
    public double[] translateToOrigin()
    {
        double[] centroid = getCentroid();
        double Cx = centroid[0];
        double Cy = centroid[1];
        double Cz = centroid[2];
        // Translate so centroid is at the origin
        translate( -Cx, -Cy, -Cz);

        return new double[] {Cx, Cy, Cz};
    }

    /**
     * Moves all the vertices of the model so the model's centroid is exactly at the specified XYZ location.
     *
     * @param x The X position of the centroid of the model.
     * @param y The Y position of the centroid of the model.
     * @param z The Z position of the centroid of the model.
     */
    public void translateToPosition(double x, double y, double z)
    {
        translateToOrigin();
        translate(x, y, z);
    }
}
