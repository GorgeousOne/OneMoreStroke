package me.gorgeousone.core;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class Camera extends Point2D.Double {

	private final float MIN_ZOOM = 0.2f;
	private final float MAX_ZOOM = 5f;
	
	private static final long serialVersionUID = 1L;
	private float zoom = 1;
	
	public Camera() {
		setLocation(0, 0);
	}
		
	public Camera(double x, double y) {
		setLocation(x, y);
	}
	
	public Camera(Point2D p) {
		this.setLocation(p);
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float newZoom) {
		zoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, newZoom));
	}
	
	public void zoom(float dZoom) {
		setZoom(getZoom() + dZoom);
	}
	
	public void applyFocus(Canvas canvas, Graphics2D graphics) {
		
		graphics.translate(canvas.getWidth()/2d, canvas.getHeight()/2d);
		graphics.scale(getZoom(), getZoom());
		graphics.translate(-getX(), -getY());
	}
}