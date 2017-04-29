package view.drawables;

import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Node extends Drawable{

	private static double radius = 100;
	private double spinSpeed;
	private boolean isConnected;
	private final Color primaryColor;
	
	public Node(int layer, Color color) {

		super(rndSkin(), layer);
		setColor(color);

		spinSpeed = Math.random() * Math.PI/16 - Math.PI/32;
		isConnected = false;
		primaryColor = color;
	}

	public double getRadius() {return radius*getScale().getX();}
	public boolean isConnected() {return isConnected;}
	public Color getPrimaryColor() {return primaryColor;}
	
	public void setSpinSpeed(double speed) {spinSpeed = speed;}
	public void connect() {isConnected = true;}
	public void disconnect() {isConnected = false;}

	public void update(int fps) {
		rotate(spinSpeed);
		
		if(isConnected == true && getColor() != Color.WHITE)
			brightenUp(fps/10);
	}
	
	public static Area rndSkin() {
		Area[] skins = new Area[] {skin1(), skin2(), skin3(), skin4()};
		return skins[(int) (Math.random() * skins.length)];
	}
	
	//Ring mit Mitte
	public static Area skin1() {
		Area shape = new Area(new Ellipse2D.Double(-radius, -radius, radius*2, radius*2));
		shape.subtract(new Area(new Ellipse2D.Double(-radius*2/3, -radius*2/3, radius*4/3, radius*4/3)));
		shape.add(new Area(new Ellipse2D.Double(-radius*2/6, -radius*2/6, radius*2/3, radius*2/3)));
		return shape;
	}
	
	//Ring mit 2 Luecken
	public static Area skin2() {
		Area shape = new Area(new Ellipse2D.Double(-radius, -radius, radius*2, radius*2));
		shape.subtract(new Area(new Ellipse2D.Double(-radius*2/3, -radius*2/3, radius*4/3, radius*4/3)));
		shape.subtract(new Area(new Rectangle2D.Double(-radius, -radius/6, 2*radius, radius/3)));
		return shape;
	}
	
	//Ring mit 2 Luecken und Mitte
	public static Area skin3() {
		Area shape = skin2();
		shape.add(new Area(new Ellipse2D.Double(-radius*1/3, -radius*1/3, radius*2/3, radius*2/3)));
		return shape;
	}
	
	//Ring mit 4 Luecken und Mitte
	public static Area skin4() {
		Area shape = skin2();
		shape.subtract(new Area(new Rectangle2D.Double(-radius/6, -radius, radius/3, 2*radius)));
		shape.add(new Area(new Ellipse2D.Double(-radius*1/3, -radius*1/3, radius*2/3, radius*2/3)));
		return shape;
	}
}
