public class Vertex
{
	private Vector3d pos;
	private Vector3d texCoord;

	public Vertex(Vector3d pos, Vector3d texCoord)
	{
		this.pos = pos;
		this.texCoord = texCoord;
	}

	public Vector3d getPos()
	{
		return pos;
	}

	public Vector3d getTexCoord()
	{
		return texCoord;
	}
}
