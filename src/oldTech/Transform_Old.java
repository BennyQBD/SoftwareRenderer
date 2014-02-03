package oldTech;

public class Transform_Old
{
	private static Camera_Old camera;

	private static float zNear;
	private static float zFar;
	private static float width;
	private static float height;
	private static float fov;

	private Vector3f_Old translation;
	private Vector3f_Old rotation;
	private Vector3f_Old scale;

	public Transform_Old()
	{
		translation = new Vector3f_Old(0,0,0);
		rotation = new Vector3f_Old(0,0,0);
		scale = new Vector3f_Old(1,1,1);
	}

	public Matrix4f_Old getTransformation()
	{
		Matrix4f_Old translationMatrix = new Matrix4f_Old().initTranslation(translation.getX(), translation.getY(), translation.getZ());
		Matrix4f_Old rotationMatrix = new Matrix4f_Old().initRotation(rotation.getX(), rotation.getY(), rotation.getZ());
		Matrix4f_Old scaleMatrix = new Matrix4f_Old().initScale(scale.getX(), scale.getY(), scale.getZ());

		return translationMatrix.mul(rotationMatrix.mul(scaleMatrix));
	}

	public Matrix4f_Old getProjectedTransformation()
	{
		Matrix4f_Old transformationMatrix = getTransformation();
		Matrix4f_Old projectionMatrix = new Matrix4f_Old().initPerspectiveProjection(fov, width, height, zNear, zFar);
		Matrix4f_Old cameraRotation = new Matrix4f_Old().initRotation(camera.getForward(), camera.getUp());
		Matrix4f_Old cameraTranslation = new Matrix4f_Old().initTranslation(-camera.getPos().getX(), -camera.getPos().getY(), -camera.getPos().getZ());

		return projectionMatrix.mul(cameraRotation.mul(cameraTranslation.mul(transformationMatrix)));
	}

	public Vector3f_Old getTranslation()
	{
		return translation;
	}

	public static void setProjection(float fov, float width, float height, float zNear, float zFar)
	{
		Transform_Old.fov = fov;
		Transform_Old.width = width;
		Transform_Old.height = height;
		Transform_Old.zNear = zNear;
		Transform_Old.zFar = zFar;
	}

	public void setTranslation(Vector3f_Old translation)
	{
		this.translation = translation;
	}

	public void setTranslation(float x, float y, float z)
	{
		this.translation = new Vector3f_Old(x, y, z);
	}

	public Vector3f_Old getRotation()
	{
		return rotation;
	}

	public void setRotation(Vector3f_Old rotation)
	{
		this.rotation = rotation;
	}

	public void setRotation(float x, float y, float z)
	{
		this.rotation = new Vector3f_Old(x, y, z);
	}

	public Vector3f_Old getScale()
	{
		return scale;
	}

	public void setScale(Vector3f_Old scale)
	{
		this.scale = scale;
	}

	public void setScale(float x, float y, float z)
	{
		this.scale = new Vector3f_Old(x, y, z);
	}

	public static Camera_Old getCamera()
	{
		return camera;
	}

	public static void setCamera(Camera_Old camera)
	{
		Transform_Old.camera = camera;
	}
}
