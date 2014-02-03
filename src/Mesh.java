public class Mesh implements Comparable<Mesh>
{
	private static final ProfileTimer timer = new ProfileTimer();

	private final Vertex[] vertices;
	private final Vertex[] screenSpaceVertices;
	private final Vertex[] worldSpaceVertices;
	private final int[][] indices;
	private final FaceOrderNode[] faceOrderNodes;

	private Transform transform;

	public Mesh(Vertex[] vertices, int[][] indices)
	{
		this.vertices = vertices;
		this.indices = indices;
		transform = new Transform();
		screenSpaceVertices = new Vertex[vertices.length];
		worldSpaceVertices = new Vertex[vertices.length];
		faceOrderNodes = new FaceOrderNode[indices.length];

		for(int i = 0; i < vertices.length; i++)
		{
			screenSpaceVertices[i] = new Vertex(new Vector3d(0,0,0), new Vector3d(0,0,0));
			worldSpaceVertices[i] = new Vertex(new Vector3d(0,0,0), new Vector3d(0,0,0));

			screenSpaceVertices[i].getTexCoord().set(vertices[i].getTexCoord().getX(),
					vertices[i].getTexCoord().getY(),
					vertices[i].getTexCoord().getZ());

			worldSpaceVertices[i].getTexCoord().set(vertices[i].getTexCoord().getX(),
					vertices[i].getTexCoord().getY(),
					vertices[i].getTexCoord().getZ());
		}

		for(int i = 0; i < faceOrderNodes.length; i++)
			faceOrderNodes[i] = new FaceOrderNode();
	}

	private Matrix4d MVP = new Matrix4d();
	private Matrix4d fullTransform = new Matrix4d();

	public void transformVertices(Camera camera, Matrix4d screenTransform)
	{
		Matrix4d worldTransform = transform.calcTransform();
		Matrix4d projection = camera.getCameraMatrix();

		projection.mul(worldTransform, MVP);

		screenTransform.mul(MVP, fullTransform);

		for(int i = 0; i < vertices.length; i++)
		{
			worldTransform.transformVector(vertices[i].getPos(), 1.0, worldSpaceVertices[i].getPos());
			fullTransform.transformVector(vertices[i].getPos(), 1.0, screenSpaceVertices[i].getPos());
		}

		for(int i = 0; i < indices.length; i++)
		{
			double zCenter = 0;

			for(int j = 0; j < indices[i].length; j++)
				zCenter += screenSpaceVertices[indices[i][j]].getPos().getZ();

			zCenter /= (double)indices[i].length;

			faceOrderNodes[i].update(i, zCenter);
		}
	}

	public double getFaceCenterZ(int index)
	{
		double zCenter = 0;

		for(int j = 0; j < indices[index].length; j++)
			zCenter += screenSpaceVertices[indices[index][j]].getPos().getZ();

		zCenter /= (double)indices[index].length;
		return zCenter;
	}

	public void draw(Bitmap3D target, Bitmap texture)
	{
		QuickX.sort(faceOrderNodes);

		for(int i = 0; i < faceOrderNodes.length; i++)
		{
			target.fillPolygon(screenSpaceVertices, worldSpaceVertices,
					indices[faceOrderNodes[i].getListIndex()], texture);
		}
	}

	public boolean backFaceTest(int faceIndex)
	{
		return screenSpaceVertices[indices[faceIndex][0]].getPos().
				backFaceTest(screenSpaceVertices[indices[faceIndex][1]].getPos(),
						screenSpaceVertices[indices[faceIndex][2]].getPos());
	}

	public Transform getTransform()
	{
		return transform;
	}

	public Vertex[] getScreenSpaceVertices()
	{
		return screenSpaceVertices;
	}

	public Vertex[] getWorldSpaceVertices()
	{
		return worldSpaceVertices;
	}

	public int[][] getIndices()
	{
		return indices;
	}

	@Override
	public int compareTo(Mesh r)
	{
		final double EPSILON = 0.0000001;

		final int BEFORE = -1;
		final int AFTER = 1;
		final int EQUAL = 0;

		if(this == r)
			return EQUAL;

		double thisZ = transform.getPos().getZ();
		double rZ = r.transform.getPos().getZ();

		double dif = Math.abs(thisZ - rZ);

		if(dif < EPSILON)
			return EQUAL;

		if(thisZ < rZ)
			return AFTER;

		return BEFORE;
	}
}
