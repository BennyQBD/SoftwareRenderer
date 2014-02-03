public class Camera
{
	private Matrix4d projection;

	public Camera(double fov, double aspect, double zNear, double zFar)
	{
		projection = new Matrix4d().initPerspective(fov, aspect, zNear, zFar);
	}

	public Matrix4d getCameraMatrix()
	{
		return projection;
	}
}
