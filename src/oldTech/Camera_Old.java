package oldTech;

import java.awt.event.KeyEvent;

public class Camera_Old
{
	public static final Vector3f_Old yAxis = new Vector3f_Old(0,1,0);

	private Vector3f_Old pos;
	private Vector3f_Old forward;
	private Vector3f_Old up;

	public Camera_Old()
	{
		this(new Vector3f_Old(0,0,0));
	}

	public Camera_Old(Vector3f_Old pos)
	{
		this(pos, new Vector3f_Old(0,0,1), new Vector3f_Old(0,1,0));
	}

	public Camera_Old(Vector3f_Old pos, Vector3f_Old forward, Vector3f_Old up)
	{
		this.pos = pos;
		this.forward = forward.normalized();
		this.up = up.normalized();
	}

	//boolean mouseLocked = false;
	//Vector3f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2, 0);
//
//	public void input(Input input)
//	{
//		//float sensitivity = 0.5f;
//		float movAmt = (float)(10 * input.getDelta());
//        float rotAmt = (float)(100 * input.getDelta());
//
////		if(input.getKey(KeyEvent.VK_ESCAPE))
////		{
////			input.setCursor(true);
////			mouseLocked = false;
////		}
////		if(input.getMouse(MouseEvent.BUTTON1))
////		{
////			input.setMousePosition(centerPosition);
////			input.setCursor(false);
////			mouseLocked = true;
////		}
//
//		if(input.getKey(KeyEvent.VK_W))
//			move(getForward(), movAmt);
//		if(input.getKey(KeyEvent.VK_S))
//			move(getForward(), -movAmt);
//		if(input.getKey(KeyEvent.VK_A))
//			move(getLeft(), movAmt);
//		if(input.getKey(KeyEvent.VK_D))
//			move(getRight(), movAmt);
//
////		if(mouseLocked)
////		{
////			int deltaPosX = input.getMouseX() - centerX;
////			int deltaPosY = input.getMouseY() - centerY;
////			//Vector3f deltaPos = input.getMousePosition().sub(centerPosition);
////
////			boolean rotY = deltaPosX != 0;
////			boolean rotX = deltaPosY != 0;
////
////			if(rotY)
////				rotateY((float)deltaPosX * sensitivity);
////			if(rotX)
////				rotateX(-(float)deltaPosY * sensitivity);
////
//////			if(rotY || rotX)
//////				input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
////		}
//
//		if(input.getKey(KeyEvent.VK_UP))
//			rotateX(-rotAmt);
//		if(input.getKey(KeyEvent.VK_DOWN))
//			rotateX(rotAmt);
//		if(input.getKey(KeyEvent.VK_LEFT))
//			rotateY(-rotAmt);
//		if(input.getKey(KeyEvent.VK_RIGHT))
//			rotateY(rotAmt);
//	}

	public void move(Vector3f_Old dir, float amt)
	{
		pos = pos.add(dir.mul(amt));
	}

	public void rotateY(float angle)
	{
		Vector3f_Old Haxis = yAxis.cross(forward).normalized();

		forward = forward.rotate(angle, yAxis).normalized();

		up = forward.cross(Haxis).normalized();
	}

	public void rotateX(float angle)
	{
		Vector3f_Old Haxis = yAxis.cross(forward).normalized();

		forward = forward.rotate(angle, Haxis).normalized();

		up = forward.cross(Haxis).normalized();
	}

	public Vector3f_Old getLeft()
	{
		return forward.cross(up).normalized();
	}

	public Vector3f_Old getRight()
	{
		return up.cross(forward).normalized();
	}

	public Vector3f_Old getPos()
	{
		return pos;
	}

	public void setPos(Vector3f_Old pos)
	{
		this.pos = pos;
	}

	public Vector3f_Old getForward()
	{
		return forward;
	}

	public void setForward(Vector3f_Old forward)
	{
		this.forward = forward;
	}

	public Vector3f_Old getUp()
	{
		return up;
	}

	public void setUp(Vector3f_Old up)
	{
		this.up = up;
	}
}
