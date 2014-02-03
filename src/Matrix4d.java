public class Matrix4d
{
	private double[][] m;

	public void set(int i, int j, double value)
	{
		m[i][j] = value;
	}

	public void copy(Matrix4d r)
	{
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				m[i][j] = r.m[i][j];
			}
		}
	}

	public Matrix4d()
	{
		m = new double[4][4];
	}

	public Matrix4d(double[][] m)
	{
		assert(m.length == 4 && m[0].length == 4);
		this.m = m;
	}

	public Matrix4d initIdentity()
	{
		m[0][0] = 1.0; m[0][1] = 0.0; m[0][2] = 0.0; m[0][3] = 0.0;
		m[1][0] = 0.0; m[1][1] = 1.0; m[1][2] = 0.0; m[1][3] = 0.0;
		m[2][0] = 0.0; m[2][1] = 0.0; m[2][2] = 1.0; m[2][3] = 0.0;
		m[3][0] = 0.0; m[3][1] = 0.0; m[3][2] = 0.0; m[3][3] = 1.0;

		return this;
	}

	public Matrix4d initScreenTransform(int width, int height)
	{
		int halfWidth = width / 2;
		int halfHeight = height / 2;

		m[0][0] = halfWidth;	m[0][1] = 0.0;			m[0][2] = 0.0; m[0][3] = halfWidth;
		m[1][0] = 0.0;			m[1][1] = -halfHeight;	m[1][2] = 0.0; m[1][3] = halfHeight;
		m[2][0] = 0.0;			m[2][1] = 0.0;			m[2][2] = 1.0; m[2][3] = 0.0;
		m[3][0] = 0.0;			m[3][1] = 0.0;			m[3][2] = 0.0; m[3][3] = 1.0;

		return this;
	}

	public Matrix4d initPerspective(double fov, double aspect, double zNear, double zFar)
	{
		double tanHalfFOV = Math.tan(fov / 2.0);

		//NOTE: Currently perspective divide is not performed on Z.
		//Should perspective divide be performed on z, proper mapping can be done with:
		double zw = (-2 * zNear * zFar)/(zFar - zNear);
		double zz = 1 + (2 * zNear)/(zFar - zNear);

		//OR
//		double zRange = zNear - zFar;
//		double zw = 2 * zFar * zNear / zRange;
//		double zz = (-zNear -zFar)/zRange;

		//NOTE: "Improper" perspective mapping (for when no perspective divide is performed on Z):
//		double zz = 2/(zFar-zNear);
//		double zw = -1;

		m[0][0] = 1.0 / (tanHalfFOV * aspect); m[0][1] = 0;                m[0][2] = 0;  m[0][3] = 0;
		m[1][0] = 0;                           m[1][1] = 1.0 / tanHalfFOV; m[1][2] = 0;  m[1][3] = 0;
		m[2][0] = 0;                           m[2][1] = 0;                m[2][2] = zz; m[2][3] = zw;
		m[3][0] = 0;                           m[3][1] = 0;                m[3][2] = 1;  m[3][3] = 0;

		return this;
	}

	public Matrix4d initTranslation(double x, double y, double z)
	{
		m[0][0] = 1.0; m[0][1] = 0.0; m[0][2] = 0.0; m[0][3] = x;
		m[1][0] = 0.0; m[1][1] = 1.0; m[1][2] = 0.0; m[1][3] = y;
		m[2][0] = 0.0; m[2][1] = 0.0; m[2][2] = 1.0; m[2][3] = z;
		m[3][0] = 0.0; m[3][1] = 0.0; m[3][2] = 0.0; m[3][3] = 1.0;

		return this;
	}

	private static Matrix4d rx = new Matrix4d();
	private static Matrix4d ry = new Matrix4d();
	private static Matrix4d rz = new Matrix4d();

	public Matrix4d initRotation(double x, double y, double z)
	{
		double sinX = Math.sin(x);
		double sinY = Math.sin(y);
		double sinZ = Math.sin(z);
		double cosX = Math.cos(x);
		double cosY = Math.cos(y);
		double cosZ = Math.cos(z);

//		Matrix4d rx = new Matrix4d();
//		Matrix4d ry = new Matrix4d();
//		Matrix4d rz = new Matrix4d();

		rz.m[0][0] = cosZ; rz.m[0][1] = -sinZ; rz.m[0][2] = 0; rz.m[0][3] = 0;
		rz.m[1][0] = sinZ; rz.m[1][1] = cosZ;  rz.m[1][2] = 0; rz.m[1][3] = 0;
		rz.m[2][0] = 0;    rz.m[2][1] = 0;     rz.m[2][2] = 1; rz.m[2][3] = 0;
		rz.m[3][0] = 0;    rz.m[3][1] = 0;     rz.m[3][2] = 0; rz.m[3][3] = 1;

		rx.m[0][0] = 1; rx.m[0][1] = 0;    rx.m[0][2] = 0;     rx.m[0][3] = 0;
		rx.m[1][0] = 0; rx.m[1][1] = cosX; rx.m[1][2] = -sinX; rx.m[1][3] = 0;
		rx.m[2][0] = 0; rx.m[2][1] = sinX; rx.m[2][2] = cosX;  rx.m[2][3] = 0;
		rx.m[3][0] = 0; rx.m[3][1] = 0;    rx.m[3][2] = 0;     rx.m[3][3] = 1;

		ry.m[0][0] = cosY; ry.m[0][1] = 0; ry.m[0][2] = -sinY; ry.m[0][3] = 0;
		ry.m[1][0] = 0;    ry.m[1][1] = 1; ry.m[1][2] = 0;     ry.m[1][3] = 0;
		ry.m[2][0] = sinY; ry.m[2][1] = 0; ry.m[2][2] = cosY;  ry.m[2][3] = 0;
		ry.m[3][0] = 0;    ry.m[3][1] = 0; ry.m[3][2] = 0;     ry.m[3][3] = 1;

		ry.mul(rx, ry);
		rz.mul(ry, this);

		return this;//rz;//.mul2(ry.mul2(rz));//rz;
	}

	public void transformVector(Vector3d src, double srcW, Vector3d dest)
	{
		double resX = m[0][0] * src.getX() + m[0][1] * src.getY() + m[0][2] * src.getZ() + m[0][3] * srcW;
		double resY = m[1][0] * src.getX() + m[1][1] * src.getY() + m[1][2] * src.getZ() + m[1][3] * srcW;
		double resZ = m[2][0] * src.getX() + m[2][1] * src.getY() + m[2][2] * src.getZ() + m[2][3] * srcW;
		double resW = m[3][0] * src.getX() + m[3][1] * src.getY() + m[3][2] * src.getZ() + m[3][3] * srcW;

		resX /= resW;
		resY /= resW;
		resZ /= resW;

		dest.set(resX, resY, resZ);
	}

	public void mul(Matrix4d src, Matrix4d dest)
	{
		double m00 = m[0][0] * src.m[0][0] + m[0][1] * src.m[1][0] + m[0][2] * src.m[2][0] + m[0][3] * src.m[3][0];
		double m01 = m[0][0] * src.m[0][1] + m[0][1] * src.m[1][1] + m[0][2] * src.m[2][1] + m[0][3] * src.m[3][1];
		double m02 = m[0][0] * src.m[0][2] + m[0][1] * src.m[1][2] + m[0][2] * src.m[2][2] + m[0][3] * src.m[3][2];
		double m03 = m[0][0] * src.m[0][3] + m[0][1] * src.m[1][3] + m[0][2] * src.m[2][3] + m[0][3] * src.m[3][3];

		double m10 = m[1][0] * src.m[0][0] + m[1][1] * src.m[1][0] + m[1][2] * src.m[2][0] + m[1][3] * src.m[3][0];
		double m11 = m[1][0] * src.m[0][1] + m[1][1] * src.m[1][1] + m[1][2] * src.m[2][1] + m[1][3] * src.m[3][1];
		double m12 = m[1][0] * src.m[0][2] + m[1][1] * src.m[1][2] + m[1][2] * src.m[2][2] + m[1][3] * src.m[3][2];
		double m13 = m[1][0] * src.m[0][3] + m[1][1] * src.m[1][3] + m[1][2] * src.m[2][3] + m[1][3] * src.m[3][3];

		double m20 = m[2][0] * src.m[0][0] + m[2][1] * src.m[1][0] + m[2][2] * src.m[2][0] + m[2][3] * src.m[3][0];
		double m21 = m[2][0] * src.m[0][1] + m[2][1] * src.m[1][1] + m[2][2] * src.m[2][1] + m[2][3] * src.m[3][1];
		double m22 = m[2][0] * src.m[0][2] + m[2][1] * src.m[1][2] + m[2][2] * src.m[2][2] + m[2][3] * src.m[3][2];
		double m23 = m[2][0] * src.m[0][3] + m[2][1] * src.m[1][3] + m[2][2] * src.m[2][3] + m[2][3] * src.m[3][3];

		double m30 = m[3][0] * src.m[0][0] + m[3][1] * src.m[1][0] + m[3][2] * src.m[2][0] + m[3][3] * src.m[3][0];
		double m31 = m[3][0] * src.m[0][1] + m[3][1] * src.m[1][1] + m[3][2] * src.m[2][1] + m[3][3] * src.m[3][1];
		double m32 = m[3][0] * src.m[0][2] + m[3][1] * src.m[1][2] + m[3][2] * src.m[2][2] + m[3][3] * src.m[3][2];
		double m33 = m[3][0] * src.m[0][3] + m[3][1] * src.m[1][3] + m[3][2] * src.m[2][3] + m[3][3] * src.m[3][3];

		dest.m[0][0] = m00; dest.m[0][1] = m01; dest.m[0][2] = m02; dest.m[0][3] = m03;
		dest.m[1][0] = m10; dest.m[1][1] = m11; dest.m[1][2] = m12; dest.m[1][3] = m13;
		dest.m[2][0] = m20; dest.m[2][1] = m21; dest.m[2][2] = m22; dest.m[2][3] = m23;
		dest.m[3][0] = m30; dest.m[3][1] = m31; dest.m[3][2] = m32; dest.m[3][3] = m33;
//		for(int i = 0; i < 4; i++)
//		{
//			for(int j = 0; j < 4; j++)
//			{
//				dest.m[i][j] = 0;
//				for(int k = 0; k < 4; k++)
//					dest.m[i][j] += m[i][k] * src.m[k][j];
//			}
//		}
	}

//	private static final double PROJECTION_RATIO = -2.0;
//	private static final double SCREEN_WIDTH = 320;
//	private static final double SCREEN_HEIGHT = 240;
//
//	public void transformAndProjectPolygon(Vector3d[] src, Vector3d[] dest)
//	{
//		for(int i = 0; i < src.length; i++)
//		{
//			transformVector(src[i], 1.0, dest[i], false);
//
//			int projectedX = (int)(dest[i].getX()/dest[i].getZ()
//				* PROJECTION_RATIO * (SCREEN_WIDTH / 2.0) + 0.5) + (int)(SCREEN_WIDTH/2.0);
//			int projectedY = (int)(dest[i].getX()/dest[i].getZ()
//					* -PROJECTION_RATIO * (SCREEN_WIDTH / 2.0) + 0.5) + (int)(SCREEN_HEIGHT/2.0);
//
//			dest[i].setX(projectedX);
//			dest[i].setY(projectedY);
//   	}
//	}
}
