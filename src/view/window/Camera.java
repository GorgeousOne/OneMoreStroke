package view.window;

import java.awt.geom.Point2D;

public class Camera extends Point2D.Double{

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

	public void setZoom(float zoom) {
		this.zoom = Math.max(0, Math.min(10, zoom));
	}
	
	public void zoom(float dZoom) {
		if(zoom + dZoom > 0.2 && zoom + dZoom < 5)
			this.zoom += dZoom;
	}
}
