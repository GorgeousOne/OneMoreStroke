package me.gorgeousone.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

public class Drawable {

	private Shape shape;
	private Area transformedShape;
	
	private Color color;
	private Point2D scale;
	private boolean isVisible;

	private Point2D pos;
	private double rotation;
	
	private boolean transformChanged;
	
	public Drawable(Area shape) {
		
		this.shape = shape;
		this.color = Color.BLACK;
		this.scale = new Point2D.Float(1f, 1f);
		this.isVisible = true;

		this.rotation = 0;
		this.pos = new Point2D.Double(0, 0);
	}
	
	public Area getShape() {

		if(transformChanged)
			updateShape();
		
		return transformedShape;
	}

	public void setShape(Shape newShape) {
		shape = newShape;
		transformChanged = true;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color newColor) {
		color = newColor;
		transformChanged = true;
	}
	
	public Point2D getScale() {
		return (Point2D) scale.clone();
	}
	
	public void setScale(double sx, double sy) {
		scale.setLocation(sx, sy);
		transformChanged = true;
	}
	
	public Point2D getPos() {
		return pos;
	}

	public void setPos(double posX, double posY) {
		pos.setLocation(posX, posY);
		transformChanged = true;
	}

	public void setPos(Point2D newPos) {
		setPos(newPos.getX(), newPos.getY());
	}

	public void translate(double dx, double dy) {
		setPos(pos.getX() + dx, pos.getY() + dy);
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double newRotation) {
		
		rotation = newRotation % (2*Math.PI);
		
		if(Math.abs(rotation) > Math.PI)
			rotation = -Math.signum(rotation) * (2*Math.PI - Math.abs(rotation)); 
		
		transformChanged = true;
	}

	public void rotate(double angle) {
		setRotation(getRotation() + angle);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean b) {
		isVisible = b;
	}
	
	public void paint(Graphics2D graphics) {
		
		if(shape == null || !isVisible)
			return;
		
		graphics.setColor(color);
  		graphics.fill(getShape());
	}
	
//	public void brightenUp(int speed) {
//		
//		int dRed = color.getRed() <= 255 - speed? speed : 255 - color.getRed();
//		int dGreen = color.getGreen() <= 255 - speed? speed : 255 - color.getGreen();
//		int dBlue = color.getBlue() <= 255 - speed? speed : 255 - color.getBlue();
//
//		color = new Color(color.getRed() + dRed, color.getGreen() + dGreen, color.getBlue() + dBlue);
//	}
	
	private void updateShape() {
		
		AffineTransform transform = new AffineTransform();
		
		transform.translate(pos.getX(), pos.getY());
		transform.rotate(rotation);
		transform.scale(scale.getX(), scale.getY());

		transformedShape = new Area(shape);
		transformedShape.transform(transform);
		
		transformChanged = false;
	}
	
//	public void courseColorTo(Color newColor, int speed) {
//		
//		if(speed < 1)
//			return;
//		
//		int dRed = newColor.getRed() - color.getRed();
//		int dGreen = newColor.getGreen() - color.getGreen();
//		int dBlue = newColor.getBlue() - color.getBlue();
//		
//		int newRed = (int) (Math.abs(dRed) > speed ? Math.signum(dRed) * speed : dRed);
//		int newGreen = (int) (Math.abs(dGreen) > speed ? Math.signum(dGreen) * speed : dGreen);
//		int newBlue = (int) (Math.abs(dBlue) > speed ? Math.signum(dBlue) * speed : dBlue);
//		
//		color = new Color(color.getRed() + newRed, color.getGreen() + newGreen, color.getBlue() + newBlue);
//	}
}
