public class Surface implements Comparable<Surface>
{
	//TEMP VARIABLES!
	private static final Vector3d normal = new Vector3d();
	private static final Vector3d tempColorVector = new Vector3d();
	private static final Vector3d tempColorVector2 = new Vector3d();
	private static final Vector3d lightDirection = new Vector3d(0,0,1);
	//END TEMP VARIABLES!

	private Mesh mesh;
	private int listIndex;
	private double zCenter;
	private int[] scanBuffer;
	private Vector3d[] texCoordScanBuffer;
	private boolean isScanned;
	private boolean isDrawn;
	private int scanMin;
	private int scanMax;

	public Surface(int targetHeight)
	{
		isScanned = true;
		isDrawn = true;
		scanBuffer = new int[targetHeight * 2];
		texCoordScanBuffer = new Vector3d[targetHeight * 2];

		for(int i = 0; i < texCoordScanBuffer.length; i++)
			texCoordScanBuffer[i] = new Vector3d(0,0,0);
	}

	public void init(Mesh mesh, int index)
	{
		this.mesh = mesh;
		this.listIndex = index;
		this.isScanned = false;
		this.isDrawn = false;
		this.scanMin = Integer.MAX_VALUE;
		this.scanMax = Integer.MIN_VALUE;

		this.zCenter = mesh.getFaceCenterZ(index);
	}

	public boolean scanConvert(Bitmap3D target)
	{
		if(isScanned)
			return true;

		fillPolygon(mesh.getScreenSpaceVertices(), mesh.getWorldSpaceVertices(), mesh.getIndices()[listIndex], target);

		isScanned = true;

		return false;
	}

	public int getScanStart(int y)
	{
		return scanBuffer[y * 2];
	}

	public int getScanEnd(int y)
	{
		return scanBuffer[y * 2 + 1];
	}

	private Vector3d getTexCoordScanStart(int y)
	{
		return texCoordScanBuffer[y * 2];
	}

	private Vector3d getTexCoordScanEnd(int y)
	{
		return texCoordScanBuffer[y * 2 + 1];
	}

	public boolean draw(Bitmap3D target, Bitmap texture, double lightAmt, double ambientLight)
	{
		if(isDrawn)
			return true;

		if(scanMin < 0)
			scanMin = 0;
		if(scanMax >= target.getHeight())
			scanMax = target.getHeight() - 1;

		for(int y = scanMin; y <= scanMax; y++)
		{
			int x = getScanStart(y);
			int endX = getScanEnd(y);
			if(endX < 0)
				continue;
			int distance = endX - x;

			Vector3d texCoordStart = getTexCoordScanStart(y);
			Vector3d texCoordEnd = getTexCoordScanEnd(y);

			double texX0 = texCoordStart.getX();
			double texX1 = texCoordEnd.getX();

			double texY0 = texCoordStart.getY();
			double texY1 = texCoordEnd.getY();

			double texXStep = (texX1 - texX0)/distance;
			double texYStep = (texY1 - texY0)/distance;

			for(int i = 0; i < distance; i++)
			{
				int texColor = texture.sample(texX0, texY0);

				tempColorVector.initColor(texColor);
				tempColorVector.mul(lightAmt, tempColorVector2);
				tempColorVector.mul(ambientLight, tempColorVector);
				tempColorVector2.add(tempColorVector, tempColorVector);

				int litTexColor = tempColorVector.toIntColor();

				target.drawPixel(x + i, y, litTexColor);
				texX0 += texXStep;
				texY0 += texYStep;
			}
		}

		isDrawn = true;
		return false;
	}

	private void fillPolygon(Vertex[] vertices, Vertex[] worldSpaceVertices, int[] indices, Bitmap3D target)
	{
//		if(vertices[indices[0]].getPos().backFaceTest(vertices[indices[1]].getPos(), vertices[indices[2]].getPos()))
//			return;

		final double diffuseLightAmt = 0.8;

		//========== TEMP LIGHTING CODE ==========
		normal.initNormal(worldSpaceVertices[indices[0]].getPos(), worldSpaceVertices[indices[1]].getPos(), worldSpaceVertices[indices[2]].getPos());

		double lightAmt = normal.dot(lightDirection) * diffuseLightAmt;
		if(lightAmt < 0)
			lightAmt *= 0;

		//======== END TEMP LIGHTING CODE ========

		int maxY;
		int minY;

		maxY = minY = (int)vertices[indices[0]].getPos().getY();

		for(int i = 1; i < indices.length; i++)
		{
			int currentValue = (int)vertices[indices[i]].getPos().getY();

			if(currentValue > maxY)
				maxY = currentValue;
			else if(currentValue < minY)
				minY = currentValue;
		}
		clearScanBuffer(minY, maxY, target);

		for(int i = 0; i < indices.length - 1; i++)
			drawScanBuffer(vertices[indices[i]], vertices[indices[i + 1]], target);

		drawScanBuffer(vertices[indices[indices.length - 1]], vertices[indices[0]], target);

		this.scanMin = minY;
		this.scanMax = maxY;

		//scanPolygon(minY, maxY, texture, lightAmt, 1 - diffuseLightAmt);
	}

	private void drawScanBuffer(Vertex a, Vertex b, Bitmap3D target)
	{
		int x0 = (int)a.getPos().getX();
		int x1 = (int)b.getPos().getX();
		int y0 = (int)a.getPos().getY();
		int y1 = (int)b.getPos().getY();

		int deltaX = Math.abs(x1 - x0);
		int deltaY = Math.abs(y1 - y0);
		int error = deltaX - deltaY;

		int xStep;
		int yStep;

		if(x0 < x1)
			xStep = 1;
		else
			xStep = -1;

		if(y0 < y1)
			yStep = 1;
		else
			yStep = -1;

		double lineX = b.getPos().getX() - a.getPos().getX();
		double lineY = b.getPos().getY() - a.getPos().getY();

		double lineLength = Math.sqrt(lineX * lineX + lineY * lineY);

		while(true)
		{
			setScanBuffer(x0, y0, a, b, lineLength, target);

			if(x0 == x1 && y0 == y1)
				break;

			int error2 = error * 2;

			if(error2 > -deltaY)
			{
				error -= deltaY;
				x0 += xStep;
			}

			if(x0 == x1 && y0 == y1)
			{
				setScanBuffer(x0, y0, a, b, lineLength, target);
				break;
			}

			if(error2 < deltaX)
			{
				error += deltaX;
				y0 += yStep;
			}
		}
	}

	private void setScanBuffer(int x, int y, Vertex a, Vertex b, double lineLength, Bitmap3D target)
	{
		if(y < 0 || y >= target.getWidth())
			return;

		if(x < 0)
			x = 0;
		if(x > target.getWidth())
			x = target.getWidth();

		int base = y * 2;
		int oldVal1 = scanBuffer[base];
		int oldVal2 = scanBuffer[base + 1];

		if(x < oldVal1)
		{
			int targetValue = target.getScanBuffer(base);

			if(x < targetValue)
			{
				setScanPoints(x, y, a, b, base, lineLength);
				target.setScanBuffer(base, x);
			}
			else
			{
				int maxValue = target.getScanBuffer(base + 1);
				if(x < maxValue)
				{
					int desiredX = target.findXValue(this, x, y, false);

					setScanPoints(desiredX, y, a, b, base, lineLength);
				}
				else
					setScanPoints(x, y, a, b, base, lineLength);
			}
		}
		if(x > oldVal2)
		{
			int targetValue = target.getScanBuffer(base + 1);

			if(x > targetValue)
			{
				setScanPoints(x, y, a, b, base + 1, lineLength);
				target.setScanBuffer(base + 1, x);
			}
			else
			{
				int minValue = target.getScanBuffer(base);
				if(x > minValue)
				{
					int desiredX = target.findXValue(this, x, y, true);

					setScanPoints(desiredX, y, a, b, base + 1, lineLength);
				}
				else
					setScanPoints(x, y, a, b, base + 1, lineLength);
			}
		}
	}

	private void setScanPoints(int x, int y, Vertex a, Vertex b, int location, double lineLength)
	{
		final double CLAMP_MAX = 0.9999999999;

		scanBuffer[location] = x;

		double deltaX = x - (int)a.getPos().getX();
		double deltaY = y - (int)a.getPos().getY();

		double currentDist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		double lerpFactor = currentDist/lineLength;

		double texX = (b.getTexCoord().getX() - a.getTexCoord().getX()) * lerpFactor + a.getTexCoord().getX();
		double texY = (b.getTexCoord().getY() - a.getTexCoord().getY()) * lerpFactor + a.getTexCoord().getY();

		texX = Math.max(Math.min(texX, CLAMP_MAX), 0.0);
		texY = Math.max(Math.min(texY, CLAMP_MAX), 0.0);

		texCoordScanBuffer[location].set(texX, texY, 0);
	}

	private void clearScanBuffer(int yLow, int yHigh, Bitmap3D target)
	{
		if(yLow < 0)
			yLow = 0;
		if(yHigh >= target.getHeight())
			yHigh = target.getHeight() - 1;

		for(int i = yLow * 2; i <= yHigh * 2 + 1; i += 2)
		{
			scanBuffer[i] = Integer.MAX_VALUE;
			scanBuffer[i + 1] = Integer.MIN_VALUE;
		}
	}

	@Override
	public int compareTo(Surface r)
	{
		final double EPSILON = 0.00000000000001;

		final int BEFORE = -1;
		final int AFTER = 1;
		final int EQUAL = 0;

		if(this == r || this.isScanned && r.isScanned)
			return EQUAL;

		if(this.isScanned)
			return AFTER;

		if(r.isScanned)
			return BEFORE;

		if(Math.abs(zCenter - r.zCenter) < EPSILON)
			return EQUAL;

		if(zCenter < r.zCenter)
			return BEFORE;

		return AFTER;
	}
}
