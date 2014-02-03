public class Vector3d
{
	private double x;
	private double y;
	private double z;

	public double getX() { return x; }
	public double getY() { return y; }
	public double getZ() { return z; }

	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setZ(double z) { this.z = z; }

	public void set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(Vector3d r)
	{
		this.x = r.getX();
		this.y = r.getY();
		this.z = r.getZ();
	}

	public String toString() { return "(" + x + ", " + y + ", " + z + ")"; }

	public Vector3d(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d()
	{
		x = y = z = 0;
	}

	public Vector3d initColor(int color)
	{
		x = ((double)((color & 0x00FF0000) >> 16)/255.0);
		y = ((double)((color & 0x0000FF00) >> 8)/255.0);
		z = ((double)((color & 0x000000FF))/255.0);

		return this;
	}

	public Vector3d initNormal(Vector3d a, Vector3d b, Vector3d c)
	{
//		Vector3d line1 = new Vector3d();
//		Vector3d line2 = new Vector3d();
//
//		b.sub(a, line1);
//		c.sub(a, line2);
//
//		line1.cross(line2, this);
//
//		return this.normalize();
		double line1X = b.getX() - a.getX();
		double line1Y = b.getY() - a.getY();
		double line1Z = b.getZ() - a.getZ();

		double line2X = c.getX() - a.getX();
		double line2Y = c.getY() - a.getY();
		double line2Z = c.getZ() - a.getZ();

		x = (line1Y * line2Z - line1Z * line2Y);
		y = (line1Z * line2X - line1X * line2Z);
		z = (line1X * line2Y - line1Y * line2X);

		return this.normalize();
	}

	public double dot(Vector3d src)
	{
		return x * src.getX() + y * src.getY() + z * src.getZ();
	}

	public double length()
	{
		return Math.sqrt(x * x + y * y + z * z);
	}

	public int toIntColor()
	{
		double normalizedR = x;//Math.max(Math.min(x, 1.0), 0.0);
		double normalizedG = y;//Math.max(Math.min(y, 1.0), 0.0);
		double normalizedB = z;//Math.max(Math.min(z, 1.0), 0.0);

		int r = (int)(normalizedR * 255.0);
		int g = (int)(normalizedG * 255.0);
		int b = (int)(normalizedB * 255.0);

		return ((r << 16) | (g << 8) | (b));
		//return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
	}

	public boolean backFaceTest(Vector3d b, Vector3d c)
	{
		double x1 = b.getX() - x;
		double y1 = b.getY() - y;

		double x2 = c.getX() - x;
		double y2 = c.getY() - y;

		return (x1 * y2 - y1 * x2) > 0;
	}

//	private static final Vector3d lerpTemp = new Vector3d();

	public void lerp(Vector3d endPoint, double amt, Vector3d dest)
	{
		endPoint.sub(this, dest);
		dest.mul(amt, dest);
		this.add(dest, dest);

//		this.mul(1.0 - amt, lerpTemp);
//		src.mul(amt, dest);
//
//		dest.add(lerpTemp, dest);

		//a + (b - a) * amt
		//(1 - amt) * a + amt * b
	}

	public void perspectiveDivide(Vector3d dest)
	{
		div(z, dest);
		//dest.set(x / z, y / z, z / z);
	}

	private static final Vector3d tempLerpStart = new Vector3d();
	private static final Vector3d tempLerpEnd = new Vector3d();
	private static final Vector3d tempLerp = new Vector3d();

	public void perspectiveLerp(Vector3d endPoint, double amt, Vector3d dest)
	{
		this.perspectiveDivide(tempLerpStart);
		endPoint.perspectiveDivide(tempLerpEnd);

		tempLerpStart.lerp(tempLerpEnd, amt, tempLerp);

		double lerpDivide = ((1 - amt) * tempLerpStart.getZ()) + (amt * tempLerpEnd.getZ());

		tempLerp.div(lerpDivide, dest);
	}

	public void cross(Vector3d src, Vector3d dest)
	{
		double x_ = y * src.getZ() - z * src.getY();
		double y_ = z * src.getX() - x * src.getZ();
		double z_ = x * src.getY() - y * src.getX();

		dest.set(x_, y_, z_);
	}

	public Vector3d normalize()
	{
		double length = length();

		set(x / length, y / length, z / length);

		return this;
	}

	public void mul(double src, Vector3d dest)
	{
		dest.set(x * src, y * src, z * src);
	}

	public void div(double src, Vector3d dest)
	{
		dest.set(x / src, y / src, z / src);
	}

	public void add(Vector3d src, Vector3d dest)
	{
		dest.set(x + src.getX(), y + src.getY(), z + src.getZ());
	}

	public void sub(Vector3d src, Vector3d dest)
	{
		dest.set(x - src.getX(), y - src.getY(), z - src.getZ());
	}
}
