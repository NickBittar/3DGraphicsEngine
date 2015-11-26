/**
 * Created by Nick on 11/25/2015.
 */
public class Axis extends Model
{
    private int scale;
    private int axisCount;
    private int vertexCount;

    public Axis(int scale)
    {
        super("axis", new int[]{scale});
        this.scale = scale;

        axisCount = 3;
        vertexCount = 6;
    }

    public void setScale(int scale)
    {
        resetVertices();
        createAxis(scale);
    }
    public int getScale()
    {
        return scale;
    }

    public int getAxisCount()
    {
        return axisCount;
    }

    public int getVertexCount()
    {
        return vertexCount;
    }

    public void setX(int i, double x)
    {
        Xs.set(i, x);
    }
    public void setY(int i, double y)
    {
        Ys.set(i, y);
    }
    public void setZ(int i, double z)
    {
        Zs.set(i, z);
    }
}
