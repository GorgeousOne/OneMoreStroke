package view.window;

import java.awt.AlphaComposite;
import java.awt.geom.Point2D;

public class Camera extends Point2D.Double{

	private static final long serialVersionUID = 1L;
	private float zoom = 1;
	private float alpha = 1;
	
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
		this.zoom = zoom;
	}
	
	public void zoom(float dZoom) {
		this.zoom += dZoom;
	}
	
	public AlphaComposite getAlpha() {
		return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	}
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public void alphalize(float dAlpha) {
		alpha += dAlpha;
	}
}
