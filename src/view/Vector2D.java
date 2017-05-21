package view;

import java.awt.geom.Point2D;
import java.lang.Math;

public class Vector2D extends Point2D.Double{

	private static final long serialVersionUID = 1L;
	
	public Vector2D() {
	}

	public Vector2D(Point2D p) {
		setLocation(p);
	}

	public Vector2D(Vector2D v) {
		setLocation(v);
	}
	
	public Vector2D(double x, double y) {
		setLocation(x, y);
	}

	public Vector2D(float angle, double length) {
		setLocation(length * Math.cos(angle), length * Math.sin(angle));
	}
	
	public double getLength() {
		return Math.sqrt(getX()*getX() + getY()*getY());
	}

	public Vector2D negate() {
		return new Vector2D(-getX(),-getY());
	}

	public Vector2D normalize() {
		double n = 1 / getLength();
		return new Vector2D(getX() * n, getY() * n);
	}
	
	// returns the sum of the given vectors
	public Vector2D add(Vector2D b) {
		return new Vector2D(getX() + b.getX(), getY() + b.getY());
	}

	// returns the product of the given vector and scalar
	public Vector2D multiply(double b) {
		return new Vector2D(getX() * b, getY() * b);
	}
	
	public Vector2D mean(Vector2D b) {
		return new Vector2D(getX() + b.getX() / 2, getY() + b.getY() / 2);
	}
	// The returned angle is in 0 to 2*Math.PI
	public double getAngle() {
		
		double angle = Math.atan(getY() / getX());
		
		if(getX() < 0)
			angle += Math.PI;
		else if(getY() < 0)
			angle += 2*Math.PI;
		
		return angle;
	}
	
	//calclates angle to another vector
	public double angleTo(Vector2D v2) {
		return v2.getAngle() - getAngle();
	}

	public Vector2D setAngle(double angle) {
		return new Vector2D(getLength() * Math.cos(angle), 
							getLength() * Math.sin(angle));
	}
	
	public Vector2D rotate(double angle) {
		return setAngle(getAngle() + angle);
	}
}
