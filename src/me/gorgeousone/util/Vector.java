package me.gorgeousone.util;

public class Vector {
	
	private double x;
	private double y;
	
	public Vector() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector(double x, double z) {
		this.x = x;
		this.y = z;
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double length() {
		return (double) Math.sqrt(x*x + y*y);
	}
	
	public Vector set(double x, double z) {
		this.x = x;
		this.y = z;
		return this;
	}
	
	public Vector setX(double x) {
		this.x = x;
		return this;
	}
	
	public Vector setY(double z) {
		this.y = z;
		return this;
	}
	
	public Vector add(double dx, double dz) {
		x += dx;
		y += dz;
		return this;
	}
	
	public Vector add(Vector vec2) {
		x += vec2.x;
		y += vec2.y;
		return this;
	}
	
	public Vector sub(Vector vec2) {
		x -= vec2.x;
		y -= vec2.y;
		return this;
	}
	
	public Vector mult(double d) {
		x *= d;
		y *= d;
		return this;
	}
	
	public Vector cross(Vector vec2) {
		x *= vec2.y;
		y *= vec2.x;
		return this;
	}
	
	public Vector normalize() {
		mult(1 / length());
		return this;
	}
	
	@Override
	public Vector clone() {
		return new Vector(x, y);
	}
}