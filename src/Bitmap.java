import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bitmap
{
	private final int width;
	private final int height;
	private final int[] pixels;

	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public int[] getPixels() { return pixels; }

	public Bitmap(String fileName)
	{
		int width_ = 0;
		int height_ = 0;
		int[] pixels_ = null;

		try
		{
			BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));

			width_ = image.getWidth();
			height_ = image.getHeight();

			pixels_ = new int[width_ * height_];
			image.getRGB(0, 0, width_, height_, pixels_, 0, width_);
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

		width = width_;
		height = height_;
		pixels = pixels_;
	}

	public Bitmap(int width, int height)
	{
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}

	public Bitmap fill(int color)
	{
		for(int i = 0; i < width * height; i++)
			pixels[i] = color;

		return this;
	}

	public void drawPixel(int x, int y, int color)
	{
		pixels[x + y * width] = color;
	}

	public void copyToPixelArray(int[] dest)
	{
		System.arraycopy(pixels, 0, dest, 0, width * height);
	}

	public int sample(double x, double y)
	{
		int xPos = (int)(x * width);
		int yPos = (int)(y * height);

		return pixels[xPos + yPos * width];
	}

	public void blit(Bitmap bitmap, int xOffset,int yOffset)
	{
		for(int y = 0; y <  bitmap.height; y++)
		{
			int yPix = y + yOffset;

			if (yPix < 0 || yPix >= height) continue;

			for(int x = 0; x <  bitmap.width; x++)
			{
				int xPix = x + xOffset;

				if (xPix < 0 || xPix >= width) continue;

				pixels[xPix + yPix * width] = bitmap.pixels[x + y * bitmap.width];
			}
		}
	}

	public void blitRange(Bitmap bitmap, double xStart, double yStart, double xEnd, double yEnd)
	{
		int xMin = (int)(xStart * width);
		int yMin = (int)(yStart * height);
		int xMax = (int)(xEnd * width);
		int yMax = (int)(yEnd * height);

		for(int y = yMin; y < yMax; y++)
		{
			double yAmt = (double)(y - yMin)/(double)(yMax - yMin);

			for(int x = xMin; x < xMax; x++)
			{
				double xAmt = (double)(x - xMin)/(double)(xMax - xMin);

				pixels[x + y * width] = bitmap.sample(xAmt, yAmt);
			}
		}
	}

	public void drawLine(Vector3d a, Vector3d b, int color)
	{
		int x0 = (int)a.getX();
		int x1 = (int)b.getX();
		int y0 = (int)a.getY();
		int y1 = (int)b.getY();

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

		while(true)
		{
			drawPixel(x0, y0, color);

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
				drawPixel(x0, y0, color);
				break;
			}

			if(error2 < deltaX)
			{
				error += deltaX;
				y0 += yStep;
			}
		}
	}
}
