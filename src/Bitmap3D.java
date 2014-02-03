import java.util.ArrayList;
import java.util.Collections;

public class Bitmap3D extends Bitmap
{
	private static final ProfileTimer timer = new ProfileTimer();

	private final int[] scanBuffer;
	private final Vector3d[] texCoordScanBuffer;
	private final Matrix4d screenTransform;

	private final ArrayList<Surface> surfaces;

	public Bitmap3D(int width, int height)
	{
		super(width, height);
		scanBuffer = new int[height * 2];
		texCoordScanBuffer = new Vector3d[height * 2];
		screenTransform = new Matrix4d().initScreenTransform(width, height);

		for(int i = 0; i < texCoordScanBuffer.length; i++)
			texCoordScanBuffer[i] = new Vector3d(0,0,0);

		surfaces = new ArrayList<Surface>();
	}

	public void initSurface(Mesh mesh, int listIndex, int surfaceNumber)
	{
		if(surfaceNumber >= surfaces.size())
			surfaces.add(new Surface(getHeight()));

		surfaces.get(surfaceNumber).init(mesh, listIndex);
	}

	public void rasterize(Mesh[] meshList, Camera camera, Bitmap texture)
	{
//		for(Mesh mesh : meshList)
//			mesh.transformVertices(camera, screenTransform);
//
//		int surfaceNumber = 0;
//
//		for(Mesh mesh : meshList)
//		{
//			for(int i = 0; i < mesh.getIndices().length; i++)
//			{
//				if(mesh.backFaceTest(i))
//					continue;
//
//				initSurface(mesh, i, surfaceNumber);
//				surfaceNumber++;
//			}
//		}
//
//		clearScanBuffer(0, getHeight());
//		Collections.sort(surfaces);
//
//		for(Surface surface : surfaces)
//		{
//			if(surface.scanConvert(this))
//				break;
//		}
//
//		for(Surface surface : surfaces)
//		{
//			if(surface.draw(this, texture, 1, 0))
//				break;
//		}

		QuickX.sort(meshList);

		for(Mesh mesh : meshList)
		{
			mesh.transformVertices(camera, screenTransform);
			mesh.draw(this, texture);
		}
	}

	public int findXValue(Surface target, int x, int y, boolean returnMax)
	{
		for(Surface surface : surfaces)
		{
			if(surface == target)
				break;

			int potentialXStart = surface.getScanStart(y);
			int potentialXEnd = surface.getScanEnd(y);

			if(x > potentialXStart && x < potentialXEnd)
			{
				if(returnMax)
					return potentialXStart;
				else
					return potentialXEnd;
			}
		}

		return x;
	}

	public int getScanBuffer(int location)
	{
		return scanBuffer[location];
	}

	public void setScanBuffer(int location, int value)
	{
		scanBuffer[location] = value;
	}

	private static Vector3d lightDirection = new Vector3d(0,0,1);
	private static Vector3d normal = new Vector3d();

	private static Vector3d tempColorVector = new Vector3d();
	private static Vector3d tempColorVector2 = new Vector3d();

	public void fillPolygon(Vertex[] vertices, Vertex[] worldSpaceVertices, int[] indices, Bitmap texture)
	{
		if(vertices[indices[0]].getPos().backFaceTest(vertices[indices[1]].getPos(), vertices[indices[2]].getPos()))
			return;

		final double diffuseLightAmt = 0.8;

		//========== TEMP LIGHTING CODE ==========
		//System.out.println(points[indices[0]] + ", " +  points[indices[1]] + ", " + points[indices[2]]);
		normal.initNormal(worldSpaceVertices[indices[0]].getPos(), worldSpaceVertices[indices[1]].getPos(), worldSpaceVertices[indices[2]].getPos());

		double lightAmt = normal.dot(lightDirection) * diffuseLightAmt;
		if(lightAmt < 0)
			lightAmt *= 0;
//
//		color.mul(lightAmt, tempColorVector);
//		color.mul(0.2, tempColorVector2);
//		tempColorVector.add(tempColorVector2, tempColorVector);

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
		clearScanBuffer(minY, maxY);

		for(int i = 0; i < indices.length - 1; i++)
			drawScanBuffer(vertices[indices[i]], vertices[indices[i + 1]]);

		drawScanBuffer(vertices[indices[indices.length - 1]], vertices[indices[0]]);

		scanPolygon(minY, maxY, texture, lightAmt, 1 - diffuseLightAmt);


		//========== DEBUG CODE ==========
//		for(int i = 0; i < indices.length - 1; i++)
//			drawLine(points[indices[i]], points[indices[i + 1]], ~color);
//
//		drawLine(points[indices[indices.length - 1]], points[indices[0]], ~color);
		//======== END DEBUG CODE ========

//		int maxY;
//		int minY;
//		int minIndexL;
//		int minIndexR;
//		int maxIndex;
//
//		minIndexL = maxIndex = 0;
//		maxY = minY = (int)vertices[indices[0]].getPos().getY();
//
//		for(int i = 1; i < indices.length; i++)
//		{
//			if((int)vertices[indices[i]].getPos().getY() > maxY)
//				maxY = (int)vertices[indices[maxIndex = i]].getPos().getY();
//			else if((int)vertices[indices[i]].getPos().getY() < minY)
//				minY = (int)vertices[indices[minIndexL = i]].getPos().getY();
//		}
//
//		if(minY == maxY)
//			return;
//
//		minIndexR = minIndexL;
//
//		while((int)vertices[indices[minIndexR]].getPos().getY() == minY)
//			minIndexR = (minIndexR + 1) % indices.length;
//		minIndexR = (minIndexR - 1 + indices.length) % indices.length;
//
//		while((int)vertices[indices[minIndexL]].getPos().getY() == minY)
//			minIndexL = (minIndexL - 1 + indices.length) % indices.length;
//		minIndexL = (minIndexL + 1) % indices.length;
//
//		int leftEdgeDir = -1;
//		boolean topIsFlat = (int)vertices[indices[minIndexL]].getPos().getX() != (int)vertices[indices[minIndexR]].getPos().getX();
//
//		if(topIsFlat)
//		{
//			if((int)vertices[indices[minIndexL]].getPos().getX() > (int)vertices[indices[minIndexR]].getPos().getX())
//			{
//				leftEdgeDir = 1;
//				int temp = minIndexL;
//				minIndexL = minIndexR;
//				minIndexR = temp;
//			}
//		}
//		else
//		{
//			int nextIndex = (minIndexR + 1) % indices.length;
//			int previousIndex = (minIndexL - 1 + indices.length) % indices.length;
//
//			int deltaXN = (int)vertices[indices[nextIndex]].getPos().getX() - (int)vertices[indices[minIndexL]].getPos().getX();
//			int deltaYN = (int)vertices[indices[nextIndex]].getPos().getY() - (int)vertices[indices[minIndexL]].getPos().getY();
//			int deltaXP = (int)vertices[indices[previousIndex]].getPos().getX() - (int)vertices[indices[minIndexL]].getPos().getX();
//			int deltaYP = (int)vertices[indices[previousIndex]].getPos().getY() - (int)vertices[indices[minIndexL]].getPos().getY();
//
//			if(((long)deltaXN * deltaYP - (long)deltaYN * deltaXP) < 0L)
//			{
//				leftEdgeDir = 1;
//				int temp = minIndexL;
//				minIndexL = minIndexR;
//				minIndexR = temp;
//			}
//		}
//
//		int numScanLines = maxY - minY - 1;
//		if(topIsFlat)
//			numScanLines++;
//
//		if(numScanLines <= 0)
//			return;
//
//		int scanStartY = minY - 1;
//		if(topIsFlat)
//			scanStartY++;
//
//		clearScanBuffer(scanStartY, scanStartY + numScanLines);
//
//		//TODO: Perform scan conversion and scan line drawing
//
//		int previousIndex;
//		int currentIndex;
//		previousIndex = currentIndex = minIndexL;
//
//		boolean skipFirst = !topIsFlat;
//
//		do
//		{
//			if(leftEdgeDir > 0)
//				currentIndex = (currentIndex + 1) % indices.length;
//			else
//				currentIndex = (currentIndex - 1 + indices.length) % indices.length;
//
//			drawScanBuffer(vertices[indices[previousIndex]], vertices[indices[currentIndex]]);
//
//			skipFirst = false;
//		} while(currentIndex != maxIndex);
//
//		previousIndex = currentIndex = minIndexR;
//
//		do
//		{
//			if(leftEdgeDir <= 0)
//				currentIndex = (currentIndex + 1) % indices.length;
//			else
//				currentIndex = (currentIndex - 1 + indices.length) % indices.length;
//
//			drawScanBuffer(vertices[indices[previousIndex]], vertices[indices[currentIndex]]);
//
//			skipFirst = false;
//		} while(currentIndex != maxIndex);
//
//		scanPolygon(minY, maxY, texture, lightAmt, 1 - diffuseLightAmt);
	}

	private void scanPolygon(int yLow, int yHigh, Bitmap texture, double lightAmt, double ambientLight)
	{
		if(yLow < 0)
			yLow = 0;
		if(yHigh >= getHeight())
			yHigh = getHeight() - 1;

		for(int y = yLow; y <= yHigh; y++)
		{
			int x = getScanStart(y);
			int distance = getScanEnd(y) - x;

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

				drawPixel(x + i, y, litTexColor);
				texX0 += texXStep;
				texY0 += texYStep;
			}
		}
	}

	private void clearScanBuffer(int yLow, int yHigh)
	{
		if(yLow < 0)
			yLow = 0;
		if(yHigh >= getHeight())
			yHigh = getHeight() - 1;

		for(int i = yLow * 2; i <= yHigh * 2 + 1; i += 2)
		{
			scanBuffer[i] = Integer.MAX_VALUE;
			scanBuffer[i + 1] = Integer.MIN_VALUE;
		}
	}

	private void setScanPoints(int x, int y, Vertex a, Vertex b, int location, double lineLength, double texX0, double texY0)
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

		//if(texX0 > 1 || texX0 < 0 || texY0 > 1 || texY0 < 0)
		//if(Math.abs(texX - texX0) > EPSILON || Math.abs(texY - texY0) > EPSILON)
			//System.out.println("( " + texX0 + ", " + texY0 + "), (" + texX + ", " + texY + ")");

		texCoordScanBuffer[location].set(texX, texY, 0);

//		double aZ = 1.0/a.getPos().getZ();
//		double bZ = 1.0/b.getPos().getZ();
//		double texCoordZ = aZ + (bZ - aZ) * lerpFactor;
//
//		texCoordScanBuffer[location].setZ(texCoordZ);
	}

	private void setScanBuffer(int x, int y, Vertex a, Vertex b, double lineLength, double texX0, double texY0)
	//private void setScanBuffer(int x, int y, double texX, double texY, double texZ)
	{
		if(y < 0 || y >= getHeight())
			return;

		if(x < 0)
			x = 0;
		if(x > getWidth())
			x = getWidth();

		int base = y * 2;
		int oldVal1 = scanBuffer[base];
		int oldVal2 = scanBuffer[base + 1];

		if(x < oldVal1)
		{
//			scanBuffer[base] = x;
//			texCoordScanBuffer[base].set(texX, texY, texZ);
			setScanPoints(x, y, a, b, base, lineLength, texX0, texY0);
		}
		if(x > oldVal2)
		{
//			scanBuffer[base + 1] = x;
//			texCoordScanBuffer[base + 1].set(texX, texY, texZ);
			setScanPoints(x, y, a, b, base + 1, lineLength, texX0, texY0);
		}
	}

	private int getScanStart(int y)
	{
		return scanBuffer[y * 2];
	}

	private int getScanEnd(int y)
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

//	private double interpolateZ(int x, int y, Vector3d a, Vector3d b, double lineXYLength)
//	{
//		double z0 = 1.0 / a.getZ();
//		double z1 = 1.0 / b.getZ();
//
//		double currentX = (double)x - a.getX();
//		double currentY = (double)y - a.getY();
//
//		double currentLength = Math.sqrt(currentX * currentX + currentY * currentY);
//
//		double lerpFactor = currentLength/lineXYLength;
//
//		return (z1 - z0) * lerpFactor + z0;
//	}

	private void drawTexCoords(Vertex a, Vertex b)
	{
		//double texDX = (b.getTexCoord().getX() - a.getTexCoord().getX());

		if(a.getPos().getY() > b.getPos().getY())
		{
			Vertex temp = a;
			a = b;
			b = temp;
		}

		int y0 = (int)a.getPos().getY();
		int y1 = (int)b.getPos().getY();
		int x0 = (int)a.getPos().getX();
		int x1 = (int)b.getPos().getX();

		double texX0 = a.getTexCoord().getX();
		double texX1 = b.getTexCoord().getX();
		double texY0 = a.getTexCoord().getY();
		double texY1 = b.getTexCoord().getY();

		double texXStep = (texX1 - texX0)/Math.abs(y1 - y0);
		double texYStep = (texY1 - texY0)/Math.abs(x1 - x0);

		int deltaNear = scanBuffer[y0 * 2] - x0;
		int deltaFar = scanBuffer[y0 * 2 + 1] - x0;

		int scanAdditive = 0;

		if(deltaFar < deltaNear)
			scanAdditive = 0;

		int deltaY = Math.abs(y1 - y0);

		//texCoordScanBuffer[y0 * 2 + scanAdditive].set(texX0, texY0, 0);
		for(int y = y0; y < y1; y++)
		{
			double lerpFactor = (double)y/deltaY;

			double texX = (texX1 - texX0) * lerpFactor + texX0;
			double texY = (texY1 - texY0) * lerpFactor + texY0;

			//texX0 += texXStep;
			//texY0 += texYStep * (scanBuffer[y * 2 + scanAdditive] - scanBuffer[(y - 1) * 2 + scanAdditive]);
			texCoordScanBuffer[y * 2 + scanAdditive].set(texX, texY, 0);
		}
	}

	private void drawScanBuffer(Vertex a, Vertex b)
	{
//		if(a.getPos().getY() > b.getPos().getY())
//		{
//			Vertex temp = a;
//			a = b;
//			b = temp;
//		}
//
//		if(a.getPos().getX() > b.getPos().getX())
//		{
//			Vertex temp = a;
//			a = b;
//			b = temp;
//		}

		int x0 = (int)a.getPos().getX();
		int x1 = (int)b.getPos().getX();
		int y0 = (int)a.getPos().getY();
		int y1 = (int)b.getPos().getY();

//		double z0 = 1.0 / a.getPos().getZ();
//		double z1 = 1.0 / b.getPos().getZ();

		double texX0 = a.getTexCoord().getX();
		double texX1 = b.getTexCoord().getX();
		double texY0 = a.getTexCoord().getY();
		double texY1 = b.getTexCoord().getY();

		int deltaX = Math.abs(x1 - x0);
		int deltaY = Math.abs(y1 - y0);
		int error = deltaX - deltaY;

		double texXStepDelta = Math.abs(b.getPos().getX() - a.getPos().getX());
		double texYStepDelta = Math.abs(b.getPos().getY() - a.getPos().getY());

		double texXStep = (texX1 - texX0)/texXStepDelta;
		double texYStep = (texY1 - texY0)/texYStepDelta;
		//double zStep = (z0 - z1) / deltaX;

//		if(texXStep < 1)
//			System.out.println(texXStep);

//		System.out.println((texXStep * deltaX)/(texX1 - texX0));

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

		//TEMP CODE:
		double lineX = b.getPos().getX() - a.getPos().getX();
		double lineY = b.getPos().getY() - a.getPos().getY();

		double lineLength = Math.sqrt(lineX * lineX + lineY * lineY);
		//END TEMP CODE

		while(true)
		{
//			double oneOverZ = interpolateZ(x0, y0, a.getPos(), b.getPos(), lineLength);
//			setScanBuffer(x0, y0, texX0 * oneOverZ, texY0 * oneOverZ, oneOverZ);
//			setScanBuffer(x0, y0, texX0, texY0, 0);
			setScanBuffer(x0, y0, a, b, lineLength, texX0, texY0);

			if(x0 == x1 && y0 == y1)
				break;

			int error2 = error * 2;

			if(error2 > -deltaY)
			{
				error -= deltaY;
				x0 += xStep;
				texX0 += texXStep;
			}

			if(x0 == x1 && y0 == y1)
			{
//				oneOverZ = interpolateZ(x0, y0, a.getPos(), b.getPos(), lineLength);
//				setScanBuffer(x0, y0, texX0 * oneOverZ, texY0 * oneOverZ, oneOverZ);
//				setScanBuffer(x0, y0, texX0, texY0, 0);
				setScanBuffer(x0, y0, a, b, lineLength, texX0, texY0);
				break;
			}

			if(error2 < deltaX)
			{
				error += deltaX;
				y0 += yStep;
				texY0 += texYStep;
			}
		}
	}
}
