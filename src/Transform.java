public class Transform
{
	private Matrix4d transformMatrix;
	private Matrix4d tempMatrix;
	private Vector3d position;
	private Vector3d rotation;
	private Vector3d scale;

	public Vector3d getPos() { return position; }
	public Vector3d getRot() { return rotation; }
	public Vector3d getScale() { return scale; }

	public void setPos(Vector3d position) { this.position = position; }
	public void setRot(Vector3d rotation) { this.rotation = rotation; }
	public void setScale(Vector3d scale) { this.scale = scale; }

	public Transform()
	{
		position = new Vector3d(0,0,0);
		rotation = new Vector3d(0,0,0);
		scale = new Vector3d(1,1,1);
		transformMatrix = new Matrix4d();
		tempMatrix = new Matrix4d();
	}

	public Matrix4d calcTransform()
	{
		tempMatrix.initTranslation(position.getX(), position.getY(), position.getZ());
		transformMatrix.initRotation(rotation.getX(), rotation.getY(), rotation.getZ());

		tempMatrix.mul(transformMatrix, transformMatrix);
		return transformMatrix;
	}
}
