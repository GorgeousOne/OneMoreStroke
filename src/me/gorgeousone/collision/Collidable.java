package me.gorgeousone.collision;

import java.awt.geom.Area;

import me.gorgeousone.drawing.Drawable;

public abstract class Collidable extends Drawable {

	private boolean canCollide;
	private boolean hasPhysics;
	
	public Collidable(Area shape) {
		super(shape);
		CollisionObserver.registerCollidable(this);
	}
	
	public boolean canCollide() {
		return canCollide;
	}
	
	public boolean hasPhysics() {
		return hasPhysics;
	}
	
	public void setCanCollide(boolean canCollide) {
		this.canCollide = canCollide;
	}
	
	public void setPhysics(boolean hasPhysics) {
		this.hasPhysics = hasPhysics;
	}
	
	public void onHit(Collidable other, Area intersection) {}
}