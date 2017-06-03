package view;

import java.awt.geom.Point2D;

public class Vector2D extends Point2D.Double{

	private static final long serialVersionUID = 1L;
	
	public Vector2D() {
	}

	public Vector2D(Vector2D v) {
		setLocation(v);
	}
	
	public Vector2D(Point2D p) {
		setLocation(p);
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
		setLocation(-getX(), -getY());
		return this;
	}

	public Vector2D normalize() {
		double k = 1 / getLength();
		setLocation(k * getX(), k * getY());
		return this;
	}
	
	public Vector2D add(Vector2D b) {
		setLocation(getX() + b.getX(), getY() + b.getY());
		return this;
	}

	public Vector2D multiply(double b) {
		setLocation(getX() * b, getY() * b);
		return this;
	}
	
	public Vector2D mean(Vector2D b) {
		setLocation(getX() + b.getX() / 2, getY() + b.getY() / 2);
		return this;
	}
	
	public double getAngle() {
		
		double angle = Math.atan(getY() / getX());
		
		if(getX() < 0)
			angle += Math.PI;
		else if(getY() < 0)
			angle += 2*Math.PI;
		
		return angle;
	}
	
	public double angleTo(Vector2D v2) {
		
		double angle = v2.getAngle() - getAngle();
	
		if(angle < 0)
			angle = 2*Math.PI + angle;
		
		return angle;
	}

	public Vector2D setRotation(double angle) {
		setLocation(getLength() * Math.cos(angle),
							getLength() * Math.sin(angle));
		return this;
	}
	
	public Vector2D rotate(double angle) {
		return setRotation(getAngle() + angle);
	}
	
	public String getName() {
		return new String("Vector2D[x=" + getX() + "; y=" + getY() + "]");
	}
	
	public Object clone() {
		return new Vector2D(getX(), getY());
	}
}
