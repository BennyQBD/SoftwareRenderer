package oldTech;

public class Vector3f_Old
{
	public static final Vector3f_Old UP = new Vector3f_Old(0,1,0);
	public static final Vector3f_Old DOWN = new Vector3f_Old(0,-1,0);
	public static final Vector3f_Old LEFT = new Vector3f_Old(-1,0,0);
	public static final Vector3f_Old RIGHT = new Vector3f_Old(1,0,0);
	public static final Vector3f_Old FORWARD = new Vector3f_Old(0,0,1);
	public static final Vector3f_Old BACK = new Vector3f_Old(0,0,-1);
	public static final Vector3f_Old ZERO = new Vector3f_Old(0,0,0);
	public static final Vector3f_Old ONE = new Vector3f_Old(1,1,1);

	private float x;
	private float y;
	private float z;

	public Vector3f_Old(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

//	public Vector3f(Vector2f r)
//	{
//		this.x = r.getX();
//		this.y = r.getY();
//		this.z = 0;
//	}

	public float lengthSquared()
	{
		return x * x + y * y + z * z;
	}

	public float length()
	{
		return (float)Math.sqrt(lengthSquared());
	}

	public float distance(Vector3f_Old r)
	{
		return this.sub(r).length();
	}

	public float angleBetween(Vector3f_Old r)
	{
		float cosTheta = this.dot(r)/(this.length() * r.length());

		return (float)Math.acos(cosTheta);
	}

	public float dot(Vector3f_Old r)
	{
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}

	public Vector3f_Old cross(Vector3f_Old r)
	{
		float x_ = y * r.getZ() - z * r.getY();
		float y_ = z * r.getX() - x * r.getZ();
		float z_ = x * r.getY() - y * r.getX();

		return new Vector3f_Old(x_, y_, z_);
	}

	public boolean backFaceTest(Vector3f_Old b, Vector3f_Old c)
	{
		//Vector3f line1 = b.sub(this);
		//Vector3f line2 = c.sub(this);

		//Returns true if line1.cross(line2).getZ() > 0

		float x1 = b.getX() - x;//line1.getX();
		float y1 = b.getY() - y;//line1.getY();

		float x2 = c.getX() - x;//line2.getX();
		float y2 = c.getY() - y;//line2.getY();

		return (x1 * y2 - y1 * x2) > 0;
	}

	public Vector3f_Old normalized()
	{
		float length = length();

		return new Vector3f_Old(x / length, y / length, z / length);
	}

//        public Vector3f normalize()
//        {
//                float length = length();
//
//                x /= length;
//                y /= length;
//                z /= length;
//                return this;
//        }

	public Vector3f_Old min(Vector3f_Old r)
	{
		return new Vector3f_Old(Math.min(x, r.getX()), Math.min(y, r.getY()), Math.min(z, r.getZ()));
	}

	public Vector3f_Old max(Vector3f_Old r)
	{
		return new Vector3f_Old(Math.max(x, r.getX()), Math.max(y, r.getY()), Math.max(z, r.getZ()));
	}

	public Vector3f_Old clamp(float maxLength)
	{
		if(lengthSquared() <= maxLength * maxLength)
			return this;

		return this.normalized().mul(maxLength);
	}

	public Vector3f_Old towards(Vector3f_Old r, float amt)
	{
		Vector3f_Old result = r.sub(this);

		if(result.length() < amt)
			return this.add(result);

		return this.add(result.normalized().mul(amt));
	}

	public Vector3f_Old rotate(float angle, Vector3f_Old axis)
	{
		return this.cross(axis.mul((float)Math.sin(Math.toRadians(-angle)))).add(this.mul((float)Math.cos(Math.toRadians(-angle))));
		//return this.rotate(new Quaternion(axis, angle));
	}

//	public Vector3f rotate(Quaternion rotation)
//	{
//		float x1 = rotation.getY() * z - rotation.getZ() * y;
//		float y1 = rotation.getZ() * x - rotation.getX() * z;
//		float z1 = rotation.getX() * y - rotation.getY() * x;
//
//		float x2 = rotation.getW() * x1 + rotation.getY() * z1 - rotation.getZ() * y1;
//		float y2 = rotation.getW() * y1 + rotation.getZ() * x1 - rotation.getX() * z1;
//		float z2 = rotation.getW() * z1 + rotation.getX() * y1 - rotation.getY() * x1;
//
//		return new Vector3f(x + 2.0f * x2, y + 2.0f * y2, z + 2.0f * z2);
//
////                Quaternion conjugate = rotation.conjugate();
////
////                Quaternion w = rotation.mul(this).mul(conjugate);
////
////                return new Vector3f(w.getX(), w.getY(), w.getZ());
//	}

	public Vector3f_Old lerp(Vector3f_Old newVector, float amt)
	{
		return this.sub(newVector).mul(amt).add(newVector);
	}

	public Vector3f_Old add(Vector3f_Old r)
	{
		return new Vector3f_Old(x + r.getX(), y + r.getY(), z + r.getZ());
	}

	public Vector3f_Old add(float r)
	{
		return new Vector3f_Old(x + r, y + r, z + r);
	}

//        public Vector3f addEq(Vector3f r)
//        {
//                x += r.getX();
//                y += r.getY();
//                z += r.getZ();
//                return this;
//        }
//
//        public Vector3f addEq(float r)
//        {
//                x += r;
//                y += r;
//                z += r;
//                return this;
//        }

	public Vector3f_Old sub(Vector3f_Old r)
	{
		return new Vector3f_Old(x - r.getX(), y - r.getY(), z - r.getZ());
	}

	public Vector3f_Old sub(float r)
	{
		return new Vector3f_Old(x - r, y - r, z - r);
	}

//        public Vector3f subEq(Vector3f r)
//        {
//                x -= r.getX();
//                y -= r.getY();
//                z -= r.getZ();
//                return this;
//        }
//
//        public Vector3f subEq(float r)
//        {
//                x -= r;
//                y -= r;
//                z -= r;
//                return this;
//        }

	public Vector3f_Old mul(Vector3f_Old r)
	{
		return new Vector3f_Old(x * r.getX(), y * r.getY(), z * r.getZ());
	}

	public Vector3f_Old mul(float r)
	{
		return new Vector3f_Old(x * r, y * r, z * r);
	}

//        public Vector3f mulEq(Vector3f r)
//        {
//                x *= r.getX();
//                y *= r.getY();
//                z *= r.getZ();
//                return this;
//        }
//
//        public Vector3f mulEq(float r)
//        {
//                x *= r;
//                y *= r;
//                z *= r;
//                return this;
//        }

	public Vector3f_Old div(Vector3f_Old r)
	{
		return new Vector3f_Old(x / r.getX(), y / r.getY(), z / r.getZ());
	}

	public Vector3f_Old div(float r)
	{
		return new Vector3f_Old(x / r, y / r, z / r);
	}

//        public Vector3f divEq(Vector3f r)
//        {
//                x /= r.getX();
//                y /= r.getY();
//                z /= r.getZ();
//                return this;
//        }
//
//        public Vector3f divEq(float r)
//        {
//                x /= r;
//                y /= r;
//                z /= r;
//                return this;
//        }

	public Vector3f_Old abs()
	{
		return new Vector3f_Old(Math.abs(x), Math.abs(y), Math.abs(z));
	}

//	public Quaternion rotationBetween(Vector3f r)
//	{
//		Vector3f temp = this.cross(r);
//		float w = (float)Math.sqrt((this.length() * r.length())) + this.dot(r);
//
//		return new Quaternion(temp.getX(), temp.getY(), temp.getZ(), w);
//	}

	public String toString()
	{
		return "(" + x + " " + y + " " + z + ")";
	}

	public boolean equals(Vector3f_Old r)
	{
		return r.getX() == x && r.getY() == y && r.getZ() == z;
	}

//	public Vector2f getXY()
//	{
//		return new Vector2f(x,y);
//	}
//
//	public Vector2f getXZ()
//	{
//		return new Vector2f(x,z);
//	}
//
//	public Vector2f getYZ()
//	{
//		return new Vector2f(y,z);
//	}
//
//	public Vector2f getYX()
//	{
//		return new Vector2f(y,x);
//	}
//
//	public Vector2f getZX()
//	{
//		return new Vector2f(z,x);
//	}
//
//	public Vector2f getZY()
//	{
//		return new Vector2f(z,y);
//	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	public float getZ()
	{
		return z;
	}

	public void setZ(float z)
	{
		this.z = z;
	}

	public void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
