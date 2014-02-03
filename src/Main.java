public class Main
{
	public static void main(String[] args)
	{
		//Engine engine = new Engine(320, 240, 3, 60);
		//Engine engine = new Engine(640, 480, 1, 60);
		Engine engine = new Engine(800, 600, 1, 60);
		engine.createWindow("Software Rendering");
		engine.start();
	}
}
