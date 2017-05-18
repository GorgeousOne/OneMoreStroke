package view.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import view.window.Camera;

public class Tail extends Drawable{

	private Ball ball;
	private int fHeight;
	private Camera camera;
	
	private Stripe stripe1;
	
	public Tail(int layer, Color color, Ball ball, int fHeight, Camera camera) {
		
		super(null, layer);
		setColor(color);
		
		this.ball = ball;
		
		this.fHeight = fHeight;
		this.camera = camera;
		stripe1 = new Stripe();
	}

	public void update(int fps) {
		stripe1.update();
		if(ball.isSpinning() && !ball.getNode().wasConnected() && !getColor().equals(ball.getNode().getPrimaryColor())) {
			//System.out.println(!ball.getNode().gotTouched());
			courseColorTo(ball.getNode().getPrimaryColor(), fps/5);
		}
	}
	
	@Override
	public void fill(Graphics g) {
		
		if(stripe1.segments.size() < 2)
			return;
		
		ArrayList<Point2D> segments = stripe1.segments;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getColor());
		g2.setStroke(new BasicStroke(fHeight/100f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

		Path2D path = new Path2D.Double();
		path.moveTo(segments.get(0).getX(), segments.get(0).getY());
		
		for(int i = 1; i < segments.size()-3; i++)		
			if(segments.get(i).getY() > camera.getY() - fHeight/2/camera.getZoom() &&
			   segments.get(i).getY() < camera.getY() + fHeight/2/camera.getZoom() ||
			   segments.get(i+1).getY() > camera.getY() - fHeight/2/camera.getZoom() &&
			   segments.get(i+1).getY() < camera.getY() + fHeight/2/camera.getZoom())
				path.lineTo(segments.get(i).getX(), segments.get(i).getY());
			else
				path.moveTo(segments.get(i).getX(), segments.get(i).getY());
			
		g2.draw(path);
	}
	
	public class Stripe {
		
		private ArrayList<Point2D> segments;
		
		public Stripe() {
			segments = new ArrayList<>();
		}
		
		public void update() {
			segments.add(new Point2D.Double(ball.getPos().getX(), ball.getPos().getY()));
		}
	}
}
