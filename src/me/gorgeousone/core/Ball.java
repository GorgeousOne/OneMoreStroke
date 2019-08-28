package me.gorgeousone.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Set;

import me.gorgeousone.collision.Collidable;
import me.gorgeousone.util.Vector;

public class Ball extends Collidable {

	private double speed;
	private Vector speedVec;
	
	private boolean isCirclingAroundNode;
	
	public Ball() {
		super(createShape());
		
		speed = 1;
		speedVec = new Vector(1, 0);
		
		setColor(Color.WHITE);
	}
	
	public void setSpeed(double d) {
		speed = d;
		speedVec.normalize().mult(speed);
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void move() {
		translate(speedVec.getX(), speedVec.getY());
	}
	
	@Override
	public void setRotation(double newRotation) {
		super.setRotation(newRotation);

		speedVec.setX(speed * Math.cos(getRotation()));
		speedVec.setY(speed * Math.sin(getRotation()));
	}
	
	@Override
	public void onHit(Collidable other, Area intersection) {
		
		//literally die
		
	}

	public void enterOrbit(Set<Vector> nodes) {
		
		
	}
	
	public void leaveOrbit() {
		
	}
	
	private static Area createShape() {
		
		Area shape = 	 new Area(new Ellipse2D.Double	(-6, -6, 12, 12));
		shape.subtract	(new Area(new Ellipse2D.Double	(-4, -4,  8,  8)));
		shape.add		(new Area(new Ellipse2D.Double	(-2, -2,  4,  4)));
		shape.add		(new Area(new Rectangle2D.Double(-6, -6,  6, 12)));
		shape.subtract	(new Area(new Rectangle2D.Double(-6, -6,  4, 12)));
		return shape;
	}
	
	@Override
	public void paint(Graphics2D graphics) {
		super.paint(graphics);
	}
}