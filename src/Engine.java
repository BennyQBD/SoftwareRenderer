import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class Engine extends Canvas implements Runnable
{
	private boolean isRunning;
	private double frameTime;
	//private Game game;
	private Input input;
	private Thread thread;
	private BufferedImage img;
	private int[] pixels;
	private Screen screen;
	private double scale;
	private int trueWidth;
	private int trueHeight;

	private MemoryImageSource memoryImageSource;
	private Image vramLocation;

	public Engine(int width, int height, double scale, int frameRate)
	{
		this.scale = scale;
		Dimension size = new Dimension((int)(width * scale), (int)(height * scale));

		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		setSize(size);

		this.isRunning = false;
		this.frameTime = 1.0 / frameRate;
		this.input = new Input();
		this.trueWidth = width;
		this.trueHeight = height;
		this.screen = new Screen(width, height);

//		ColorModel colorModel = new DirectColorModel(32, 0xFF0000, 0x00FF00, 0x0000FF, 0);
//		memoryImageSource = new MemoryImageSource(width, height, colorModel, screen.getPixels(), 0, width);
//		memoryImageSource.setAnimated(true);
//		memoryImageSource.setFullBufferUpdates(true);
//		vramLocation = createImage(memoryImageSource);

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();

		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}

	public JFrame createWindow(String title)
	{
		JFrame frame = new JFrame();
		frame.add(this);
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle(title);
		frame.setVisible(true);

		return frame;
	}

	public void start()
	{
		if(isRunning) return;

		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	public void stop()
	{
		if(!isRunning) return;

		isRunning = false;
		try
		{
			thread.join();
		}
		catch(Exception ex){ex.printStackTrace();}
	}

	public void run()
	{
		isRunning = true;
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();

		//game.init();
		//Starts rendering process immediately
		//render();
		//render();

		while(isRunning)
		{
			boolean render = false;

			long now = System.nanoTime();
			long passedTime = now - lastTime;

			lastTime = now;

			unprocessedSeconds += passedTime / (1000000000.0);

			while(unprocessedSeconds > frameTime)
			{
				render = true;

				input.update(frameTime);
				screen.input(input);
				screen.update(frameTime);

				unprocessedSeconds -= frameTime;
			}

			if(render)
			{
				render();
				//System.out.println((System.nanoTime() - lastTime) / (1000000.0) + " ms");
			}
			else
			{
//				try
//				{
//					Thread.sleep(1);
//				}
//				catch(InterruptedException e)
//				{
//					e.printStackTrace();
//				}
			}
		}
	}

	private static ProfileTimer timer = new ProfileTimer();

	private void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(1);
			return;
		}

		timer.start();
		screen.render();
		timer.stopAndDisplay();

		screen.copyToPixelArray(pixels);
		//memoryImageSource.newPixels();

		Graphics g = bs.getDrawGraphics();
		//g.drawImage(vramLocation, 0, 0, (int)(trueWidth * scale), (int)(trueHeight * scale), null);
		g.drawImage(img, 0, 0, (int)(trueWidth * scale), (int)(trueHeight * scale), null);

		g.dispose();
		bs.show();
	}
}
