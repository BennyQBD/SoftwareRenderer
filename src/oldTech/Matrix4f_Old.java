package oldTech;

public class Matrix4f_Old
{
	private float[][] m;

	public Matrix4f_Old()
	{
		//m = new float[4][4];
		this(new float[4][4]);
	}

	public Matrix4f_Old(float[][] m)
	{
		if(m.length != 4 || m[0].length != 4)
		{
			System.err.println("Invalid 4x4 matrix");
			new Exception().printStackTrace();
			System.exit(1);
		}
		this.m = m;
	}

	public Matrix4f_Old initIdentity()
	{
		m[0][0] = 1;        m[0][1] = 0;        m[0][2] = 0;        m[0][3] = 0;
		m[1][0] = 0;        m[1][1] = 1;        m[1][2] = 0;        m[1][3] = 0;
		m[2][0] = 0;        m[2][1] = 0;        m[2][2] = 1;        m[2][3] = 0;
		m[3][0] = 0;        m[3][1] = 0;        m[3][2] = 0;        m[3][3] = 1;

		return this;
	}

	public Matrix4f_Old initTranslation(float x, float y, float z)
	{
		m[0][0] = 1;        m[0][1] = 0;        m[0][2] = 0;        m[0][3] = x;
		m[1][0] = 0;        m[1][1] = 1;        m[1][2] = 0;        m[1][3] = y;
		m[2][0] = 0;        m[2][1] = 0;        m[2][2] = 1;        m[2][3] = z;
		m[3][0] = 0;        m[3][1] = 0;        m[3][2] = 0;        m[3][3] = 1;

		return this;
	}

	public Matrix4f_Old initRotation(float x, float y, float z)
	{
		Matrix4f_Old rx = new Matrix4f_Old();
		Matrix4f_Old ry = new Matrix4f_Old();
		Matrix4f_Old rz = new Matrix4f_Old();

		x = (float)Math.toRadians(x);
		y = (float)Math.toRadians(y);
		z = (float)Math.toRadians(z);

		rz.m[0][0] = (float)Math.cos(z);rz.m[0][1] = -(float)Math.sin(z);rz.m[0][2] = 0;                                rz.m[0][3] = 0;
		rz.m[1][0] = (float)Math.sin(z);rz.m[1][1] = (float)Math.cos(z);rz.m[1][2] = 0;                                        rz.m[1][3] = 0;
		rz.m[2][0] = 0;                                        rz.m[2][1] = 0;                                        rz.m[2][2] = 1;                                        rz.m[2][3] = 0;
		rz.m[3][0] = 0;                                        rz.m[3][1] = 0;                                        rz.m[3][2] = 0;                                        rz.m[3][3] = 1;

		rx.m[0][0] = 1;                                        rx.m[0][1] = 0;                                        rx.m[0][2] = 0;                                        rx.m[0][3] = 0;
		rx.m[1][0] = 0;                                        rx.m[1][1] = (float)Math.cos(x);rx.m[1][2] = -(float)Math.sin(x);rx.m[1][3] = 0;
		rx.m[2][0] = 0;                                        rx.m[2][1] = (float)Math.sin(x);rx.m[2][2] = (float)Math.cos(x);rx.m[2][3] = 0;
		rx.m[3][0] = 0;                                        rx.m[3][1] = 0;                                        rx.m[3][2] = 0;                                        rx.m[3][3] = 1;

		ry.m[0][0] = (float)Math.cos(y);ry.m[0][1] = 0;                                        ry.m[0][2] = -(float)Math.sin(y);ry.m[0][3] = 0;
		ry.m[1][0] = 0;                                        ry.m[1][1] = 1;                                        ry.m[1][2] = 0;                                        ry.m[1][3] = 0;
		ry.m[2][0] = (float)Math.sin(y);ry.m[2][1] = 0;                                        ry.m[2][2] = (float)Math.cos(y);ry.m[2][3] = 0;
		ry.m[3][0] = 0;                                        ry.m[3][1] = 0;                                        ry.m[3][2] = 0;                                        ry.m[3][3] = 1;

		m = rz.mul(ry.mul(rx)).getM();

		return this;
	}

	public Matrix4f_Old initScale(float x, float y, float z)
	{
		m[0][0] = x;        m[0][1] = 0;        m[0][2] = 0;        m[0][3] = 0;
		m[1][0] = 0;        m[1][1] = y;        m[1][2] = 0;        m[1][3] = 0;
		m[2][0] = 0;        m[2][1] = 0;        m[2][2] = z;        m[2][3] = 0;
		m[3][0] = 0;        m[3][1] = 0;        m[3][2] = 0;        m[3][3] = 1;

		return this;
	}

	public Matrix4f_Old initPerspectiveProjection(float fov, float width, float height, float zNear, float zFar)
	{
		float ar = width/height;
		float tanHalfFOV = (float)Math.tan(Math.toRadians(fov / 2));
		float zRange = zNear - zFar;

		m[0][0] = 1.0f / (tanHalfFOV * ar);        m[0][1] = 0;                                        m[0][2] = 0;        m[0][3] = 0;
		m[1][0] = 0;                                                m[1][1] = 1.0f / tanHalfFOV;        m[1][2] = 0;        m[1][3] = 0;
		m[2][0] = 0;                                                m[2][1] = 0;                                        m[2][2] = (-zNear -zFar)/zRange;        m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;                                                m[3][1] = 0;                                        m[3][2] = 1;        m[3][3] = 0;


		return this;
	}

	public Matrix4f_Old initScreenSpaceTransform(float width, float height)
	{
		float halfWidth = width / 2;
		float halfHeight = height / 2;

		m[0][0] = halfWidth;	m[0][1] = 0;        m[0][2] = 0;        m[0][3] = halfWidth;
		m[1][0] = 0;        m[1][1] = -halfHeight;	m[1][2] = 0;        m[1][3] = halfHeight;
		m[2][0] = 0;        m[2][1] = 0;        m[2][2] = 1;    	m[2][3] = 0;
		m[3][0] = 0;        m[3][1] = 0;        m[3][2] = 0;        m[3][3] = 1;

		return this;
	}

	public Matrix4f_Old initOrtho(float left, float right, float bottom, float top, float near, float far)
	{
		float width = (right - left) / 2;
		float height = (top - bottom) / 2;
		float depth = (far - near) / 2;

		m[0][0] = width;	m[0][1] = 0;        m[0][2] = 0;        m[0][3] = Math.abs(width);
		m[1][0] = 0;        m[1][1] = height;	m[1][2] = 0;        m[1][3] = Math.abs(height);
		m[2][0] = 0;        m[2][1] = 0;        m[2][2] = depth;    m[2][3] = Math.abs(depth);
		m[3][0] = 0;        m[3][1] = 0;        m[3][2] = 0;        m[3][3] = 1;

		return this;
	}

	public Matrix4f_Old initRotation(Vector3f_Old forward, Vector3f_Old up)
	{
		Vector3f_Old f = forward.normalized();

		Vector3f_Old r = up.normalized();
		r = r.cross(f);

		Vector3f_Old u = f.cross(r);

		m[0][0] = r.getX();        m[0][1] = r.getY();        m[0][2] = r.getZ();        m[0][3] = 0;
		m[1][0] = u.getX();        m[1][1] = u.getY();        m[1][2] = u.getZ();        m[1][3] = 0;
		m[2][0] = f.getX();        m[2][1] = f.getY();        m[2][2] = f.getZ();        m[2][3] = 0;
		m[3][0] = 0;                m[3][1] = 0;                m[3][2] = 0;                m[3][3] = 1;

		return this;
	}

	public Matrix4f_Old mul(Matrix4f_Old r)
	{
		Matrix4f_Old res = new Matrix4f_Old();

		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				res.set(i, j, m[i][0] * r.get(0, j) +
						m[i][1] * r.get(1, j) +
						m[i][2] * r.get(2, j) +
						m[i][3] * r.get(3, j));
			}
		}

		return res;
	}

	public Vector3f_Old transform(Vector3f_Old r, float w)
	{
		float x = r.getX() * m[0][0] + r.getY() * m[0][1] + r.getZ() * m[0][2] + w * m[0][3];
		float y = r.getX() * m[1][0] + r.getY() * m[1][1] + r.getZ() * m[1][2] + w * m[1][3];
		float z = r.getX() * m[2][0] + r.getY() * m[2][1] + r.getZ() * m[2][2] + w * m[2][3];

//		float x = r.getX() * m[0][0] + r.GetY() * m[1][0] + r.GetZ() * m[2][0] + w * m[3][0];
//		float y = r.getX() * m[0][1] + r.GetY() * m[1][1] + r.GetZ() * m[2][1] + w * m[3][1];
//		float z = r.GetX() * m[0][2] + r.GetY() * m[1][2] + r.GetZ() * m[2][2] + w * m[3][2];

		return new Vector3f_Old(x,y,z);
	}

	public Vector3f_Old transformDiv(Vector3f_Old r, float w)
	{
		float x = r.getX() * m[0][0] + r.getY() * m[0][1] + r.getZ() * m[0][2] + w * m[0][3];
		float y = r.getX() * m[1][0] + r.getY() * m[1][1] + r.getZ() * m[1][2] + w * m[1][3];
		float z = r.getX() * m[2][0] + r.getY() * m[2][1] + r.getZ() * m[2][2] + w * m[2][3];
		float w_ = r.getX() * m[3][0] + r.getY() * m[3][1] + r.getZ() * m[3][2] + w * m[3][3];

//		float x = r.getX() * m[0][0] + r.GetY() * m[1][0] + r.GetZ() * m[2][0] + w * m[3][0];
//		float y = r.getX() * m[0][1] + r.GetY() * m[1][1] + r.GetZ() * m[2][1] + w * m[3][1];
//		float z = r.GetX() * m[0][2] + r.GetY() * m[1][2] + r.GetZ() * m[2][2] + w * m[3][2];

		return new Vector3f_Old(x / w_, y / w_, z / w_);
	}
//
//	public Quaternion toQuaternion()
//	{
//		float trace = m[0][0] + m[1][1] + m[2][2];
//
//		float w = 1;
//		float x = 0;
//		float y = 0;
//		float z = 0;
//		if( trace > 0 )
//		{// I changed M_EPSILON to 0
//			float s = 0.5f / (float)Math.sqrt(trace+ 1.0f);
//			w = 0.25f / s;
//			x = ( m[1][2] - m[2][1] ) * s;
//			y = ( m[2][0] - m[0][2] ) * s;
//			z = ( m[0][1] - m[1][0] ) * s;
//		}
//		else
//		{
//			if ( m[0][0] > m[1][1] && m[0][0] > m[2][2] )
//			{
//				float s = 2.0f * (float)Math.sqrt( 1.0f + m[0][0] - m[1][1] - m[2][2]);
//				w = (m[1][2] - m[2][1] ) / s;
//				x = 0.25f * s;
//				y = (m[1][0] + m[0][1] ) / s;
//				z = (m[2][0] + m[0][2] ) / s;
//			}
//			else if (m[1][1] > m[2][2])
//			{
//				float s = 2.0f * (float)Math.sqrt( 1.0f + m[1][1] - m[0][0] - m[2][2]);
//				w = (m[2][0] - m[0][2] ) / s;
//				x = (m[1][0] + m[0][1] ) / s;
//				y = 0.25f * s;
//				z = (m[2][1] + m[1][2] ) / s;
//			}
//			else
//			{
//				float s = 2.0f * (float)Math.sqrt( 1.0f + m[2][2] - m[0][0] - m[1][1] );
//				w = (m[0][1] - m[1][0] ) / s;
//				x = (m[2][0] + m[0][2] ) / s;
//				y = (m[1][2] + m[2][1] ) / s;
//				z = 0.25f * s;
//			}
//		}
//
//		return new Quaternion(x,y,z,w);
//	}
//
//	public Quaternion toQuaternionBasic()
//	{
//		float w = (float)(Math.sqrt(1.0 + m[0][0] + m[1][1] + m[2][2]) / 2.0);
//		float w4 = (4.0f * w);
//		float x = (m[1][2] - m[2][1]) / w4 ;
//		float y = (m[2][0] - m[0][2]) / w4 ;
//		float z = (m[0][1] - m[1][0]) / w4 ;
//
//		return new Quaternion(x,y,z,w);
//	}

	public float[][] getM()
	{
		float[][] res = new float[4][4];

		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				res[i][j] = m[i][j];

		return res;
	}

	public float get(int x, int y)
	{
		return m[x][y];
	}

	public void setM(float[][] m)
	{
		this.m = m;
	}

	public void set(int x, int y, float value)
	{
		m[x][y] = value;
	}
}
