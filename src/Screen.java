public class Screen extends Bitmap3D
{
	private Bitmap test;
	private Bitmap background;
	private Camera camera;

	public Screen(int width, int height)
	{
		super(width, height);

		test = new Bitmap(width/4, height/4).fill(0xFF0000);
		background = new Bitmap(width, height).fill(0x000000);
		camera = new Camera(Math.toRadians(70.0), (double)getWidth()/(double)getHeight(), 0.01, 1000.0);
	}

	double offsetCounter;

	public void input(Input input)
	{
		//camera.input(input);
	}

	public void update(double delta)
	{
		offsetCounter += delta;
	}

//	private void testRenderCode()
//	{
//		final Vector3f pointA = new Vector3f(100, 120, 0);
//		final Vector3f pointB = new Vector3f(175, 175, 0);
//		final Vector3f pointC = new Vector3f(200, 100, 0);
////		final Vector3f pointD = new Vector2i(100, 200);
////
////		fillTriangle(pointA, pointB, pointC, 0xFF0000);
//
//		final Vector3f[] tri = new Vector3f[] { pointA, pointB, pointC };
//
//		final Vector3f topLeft = new Vector3f(100, 100, 0);
//		final Vector3f topRight = new Vector3f(200, 100, 0);
//		final Vector3f bottomRight = new Vector3f(200, 200, 0);
//		final Vector3f bottomLeft = new Vector3f(100, 200, 0);
//
//		final Vector3f[] quad = new Vector3f[] { topLeft, topRight, bottomRight, bottomLeft };
//
////		fillTriangle(topLeft, topRight, bottomRight, 0x0000FF);
////		fillTriangle(topLeft, bottomRight, bottomLeft, 0xFF0000);
////		fillPolygon(tri1, 0x0000FF);
////		fillPolygon(tri2, 0xFF0000);
//
//		fillPolygon(quad, 0xFF0000);
//		fillPolygon(tri, 0x0000FF);
//
//		//renderCount++;
//
//		//scanLine(pointD, 100, 0xFF00FF);
//
////		int xOffset = (int)(Math.sin(offsetCounter) * (getWidth() / 2) + getWidth() / 2);
////
////		blit(test, xOffset, 0);
//	}

//	Transform triangleTransform = new Transform();
//	Transform floorTransform = new Transform();
//
//	public void transformVertices(Vector3f[] vertices, Matrix4f transform)
//	{
//		for(int i = 0; i < vertices.length; i++)
//			vertices[i] = transform.transformDiv(vertices[i], 1.0f);
//	}
//
//	public void transformVerticesWithoutDiv(Vector3f[] vertices, Matrix4f transform)
//	{
//		for(int i = 0; i < vertices.length; i++)
//			vertices[i] = transform.transform(vertices[i], 1.0f);
//	}

	private static Vertex[] testPoly = new Vertex[]
			{
					new Vertex(new Vector3d(-0.5, -0.5, 0), new Vector3d(0,1,0)),
					new Vertex(new Vector3d(0, 0.5, 0), new Vector3d(0.5,0,0)),
					new Vertex(new Vector3d(0.5, -0.5, 0), new Vector3d(1,1,0))
			};

	private static int[][] testIndices = new int[][] {{ 2, 1, 0 }};

//	private static Vertex[] cubeMesh = new Vertex[]
//			{
//					new Vertex(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0,0,0)),
//					new Vertex(new Vector3d(-0.5, 0.5, -0.5), new Vector3d(0,1,0)),
//					new Vertex(new Vector3d(0.5, 0.5, -0.5), new Vector3d(1,1,0)),
//					new Vertex(new Vector3d(0.5, -0.5, -0.5), new Vector3d(1,0,0)),
//
//					new Vertex(new Vector3d(-0.5, -0.5, 0.5), new Vector3d(0,0,0)),
//					new Vertex(new Vector3d(-0.5, 0.5, 0.5), new Vector3d(0,1,0)),
//					new Vertex(new Vector3d(0.5, 0.5, 0.5), new Vector3d(1,1,0)),
//					new Vertex(new Vector3d(0.5, -0.5, 0.5), new Vector3d(1,0,0)),
//			};
//
//	private static int[][] cubeIndices = new int[][]
//			{
////					{ 0, 1, 2, 3 },
////					{ 7, 6, 5, 4 },
////
////					{ 0, 4, 5, 1 },
////					{ 2, 6, 7, 3 },
////
////					{ 5, 6, 2, 1 },
////					{ 3, 7, 4, 0 },
//
//					{ 3, 2, 1, 0 },
//					{ 4, 5, 6, 7 },
//
//					{ 1, 5, 4, 0 },
//					{ 3, 7, 6, 2 },
//
//					{ 1, 2, 6, 5 },
//					{ 0, 4, 7, 3 },
//			};

	private static Vertex[] cubeMesh = new Vertex[]
			{
					new Vertex(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0,0,0)),
					new Vertex(new Vector3d(-0.5, 0.5, -0.5), new Vector3d(0,1,0)),
					new Vertex(new Vector3d(0.5, 0.5, -0.5), new Vector3d(1,1,0)),
					new Vertex(new Vector3d(0.5, -0.5, -0.5), new Vector3d(1,0,0)),

					new Vertex(new Vector3d(-0.5, -0.5, 0.5), new Vector3d(0,0,0)),
					new Vertex(new Vector3d(-0.5, 0.5, 0.5), new Vector3d(0,1,0)),
					new Vertex(new Vector3d(0.5, 0.5, 0.5), new Vector3d(1,1,0)),
					new Vertex(new Vector3d(0.5, -0.5, 0.5), new Vector3d(1,0,0)),

					new Vertex(new Vector3d(-0.5, 0.5, -0.5), new Vector3d(0,0,0)),
					new Vertex(new Vector3d(-0.5, 0.5, 0.5), new Vector3d(0,1,0)),
					new Vertex(new Vector3d(-0.5, -0.5, 0.5), new Vector3d(1,1,0)),
					new Vertex(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(1,0,0)),

					new Vertex(new Vector3d(0.5, 0.5, -0.5), new Vector3d(0,0,0)),
					new Vertex(new Vector3d(0.5, 0.5, 0.5), new Vector3d(0,1,0)),
					new Vertex(new Vector3d(0.5, -0.5, 0.5), new Vector3d(1,1,0)),
					new Vertex(new Vector3d(0.5, -0.5, -0.5), new Vector3d(1,0,0)),

					new Vertex(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0,0,0)),
					new Vertex(new Vector3d(-0.5, -0.5, 0.5), new Vector3d(0,1,0)),
					new Vertex(new Vector3d(0.5, -0.5, 0.5), new Vector3d(1,1,0)),
					new Vertex(new Vector3d(0.5, -0.5, -0.5), new Vector3d(1,0,0)),

					new Vertex(new Vector3d(-0.5, 0.5, -0.5), new Vector3d(0,0,0)),
					new Vertex(new Vector3d(-0.5, 0.5, 0.5), new Vector3d(0,1,0)),
					new Vertex(new Vector3d(0.5, 0.5, 0.5), new Vector3d(1,1,0)),
					new Vertex(new Vector3d(0.5, 0.5, -0.5), new Vector3d(1,0,0)),

//					new Vertex(new Vector3d(-0.5, 0.5, -0.5), new Vector3d(0,0,0)),
//					new Vertex(new Vector3d(0.5, 0.5, -0.5), new Vector3d(0,1,0)),
//					new Vertex(new Vector3d(-0.5, 0.5, 0.5), new Vector3d(1,1,0)),
//					new Vertex(new Vector3d(0.5, 0.5, 0.5), new Vector3d(1,0,0)),

//					new Vertex(new Vector3d(-0.5, -0.5, -0.5), new Vector3d(0,0,0)),
//					new Vertex(new Vector3d(0.5, -0.5, -0.5), new Vector3d(0,1,0)),
//					new Vertex(new Vector3d(-0.5, -0.5, 0.5), new Vector3d(1,1,0)),
//					new Vertex(new Vector3d(0.5, -0.5, 0.5), new Vector3d(1,0,0)),

			};

	private static int[][] cubeIndices = new int[][]
			{
//					{ 0, 1, 2, 3 },
//					{ 7, 6, 5, 4 },
//
//					{ 0, 4, 5, 1 },
//					{ 2, 6, 7, 3 },
//
//					{ 5, 6, 2, 1 },
//					{ 3, 7, 4, 0 },

					{ 3, 2, 1, 0 },
					{ 4, 5, 6, 7 },

					{ 8, 9, 10, 11 },
					{ 15, 14, 13, 12 },

					{ 16, 17, 18, 19 },
					{ 23, 22, 21, 20 },

//					{ 1, 5, 4, 0 },
//					{ 3, 7, 6, 2 },
//
//					{ 1, 2, 6, 5 },
//					{ 0, 4, 7, 3 },
			};

	private Mesh cube = new Mesh(cubeMesh, cubeIndices);
	private Mesh cube1 = new Mesh(cubeMesh, cubeIndices);
	private Mesh cube2 = new Mesh(cubeMesh, cubeIndices);
	private Mesh cube3 = new Mesh(cubeMesh, cubeIndices);
	private Mesh cube4 = new Mesh(cubeMesh, cubeIndices);
	private Mesh cube5 = new Mesh(cubeMesh, cubeIndices);
	private Mesh triangle = new Mesh(testPoly, testIndices);

	private Vector3d red = new Vector3d(1, 0, 0);
	private Vector3d white = new Vector3d(1, 1, 1);

	private Mesh[] meshList = new Mesh[] { cube };
	private Mesh[] meshList2 = new Mesh[] { cube2, cube3, cube4, cube5 };

	private Bitmap bricks = new Bitmap("bricks.jpg");
	private Bitmap testImage = new Bitmap("test.png");

	private static Vertex[] triSquareVert = new Vertex[]
			{
					new Vertex(new Vector3d(-0.5, -0.5, 0), new Vector3d(0,0,0)),
					new Vertex(new Vector3d(-0.5, 0.5, 0), new Vector3d(0,1,0)),
					new Vertex(new Vector3d(0.5, 0.5, -0.5), new Vector3d(1,1,0)),
					new Vertex(new Vector3d(0.5, -0.5, -0.5), new Vector3d(1,0,0)),

					new Vertex(new Vector3d(-1.5, -0.5, -0.5), new Vector3d(0,0,0)),
					new Vertex(new Vector3d(-1.5, 0.5, -0.5), new Vector3d(0,1,0)),
					new Vertex(new Vector3d(-0.5, 0.5, 0.0), new Vector3d(1,1,0)),
					new Vertex(new Vector3d(-0.5, -0.5, 0.0), new Vector3d(1,0,0)),
			};

	private static int[][] triSquareVertIndex = new int[][] {
			{3, 2, 1, 0}, {0, 1, 2, 3},
			{7, 6, 5, 4}, {4, 5, 6, 7}};

	private Mesh triSquare = new Mesh(triSquareVert, triSquareVertIndex);

	private Mesh[] meshList3 = new Mesh[] { triSquare };

	public void render()
	{
		blit(background, 0, 0);
		//blitRange(bricks, 0, 0, 1.0, 1.0);

		double zDist = 2.0;
		double zDist1 = 2.0;
		double zDist2 = 10.0;
		double zDist3 = 3.5;
		double zDist4 = 8.7;
		double zDist5 = 5.6;

		double sinOff = Math.sin(offsetCounter);
		double cosOff = Math.cos(offsetCounter);

		double sinOff2 = Math.sin(offsetCounter * 2);
		double cosOff2 = Math.cos(offsetCounter * 2);


		cube1.getTransform().getPos().set(0,0, zDist1);
		cube2.getTransform().getPos().set(sinOff2 * 1, cosOff2 * 4, cosOff * 5 + zDist2);
		cube3.getTransform().getPos().set(cosOff * 2, sinOff * 1, zDist3 + Math.abs(sinOff2 * 2));
		cube4.getTransform().getPos().set(sinOff * 3, cosOff * 3, zDist4 - cosOff2 * 3);
		cube5.getTransform().getPos().set(sinOff * -2, sinOff2 * -2, zDist5 + sinOff);

		for(Mesh mesh : meshList2)
				mesh.getTransform().getRot().set(offsetCounter, offsetCounter, offsetCounter);

		cube.getTransform().getPos().set(0, 0, zDist);
		cube.getTransform().getRot().set(0, 0, offsetCounter + Math.PI / 2.0 - 0.1);
		triangle.getTransform().getPos().set(0, 0, zDist);
		triangle.getTransform().getRot().set(0, 0, offsetCounter);

		triSquare.getTransform().getPos().setZ(zDist + 1);
		triSquare.getTransform().getRot().set(0, offsetCounter, 0);

		rasterize(meshList2, camera, bricks);

//		rasterizeMesh(triangle, camera, red);
//		rasterizeMesh(cube, camera, white);
	}
}
