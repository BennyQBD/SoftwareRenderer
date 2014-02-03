public class FaceOrderNode implements Comparable<FaceOrderNode>
{
	private int listIndex;
	private double zCenter;

	public int getListIndex()
	{
		return listIndex;
	}

	public void update(int index, double zCenter)
	{
		this.listIndex = index;
		this.zCenter = zCenter;
	}

	@Override
	public int compareTo(FaceOrderNode r)
	{
		final double EPSILON = 0.00000000000001;

		final int BEFORE = -1;
		final int AFTER = 1;
		final int EQUAL = 0;

		if(this == r || Math.abs(zCenter - r.zCenter) < EPSILON)
			return EQUAL;

		if(zCenter < r.zCenter)
			return AFTER;

		return BEFORE;
	}
}
