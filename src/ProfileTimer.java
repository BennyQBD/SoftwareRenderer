public class ProfileTimer
{
	private long startTime;

	public void start()
	{
		startTime = System.nanoTime();
	}

	public double stop()
	{
		return (double)(System.nanoTime() - startTime)/1000000.0;
	}

	public void stopAndDisplay()
	{
		System.out.println(stop() + " ms");
	}
}
