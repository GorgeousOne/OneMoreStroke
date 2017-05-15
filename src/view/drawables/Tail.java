package view.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
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
		if(ball.isSpinning() && !getColor().equals(ball.getNode().getPrimaryColor()))
			courseColorTo(ball.getNode().getPrimaryColor(), fps/5);
	}
	
	@Override
	public void fill(Graphics g) {
		
		if(stripe1.segments.size() < 2)
			return;
		
		ArrayList<Point2D> segments = stripe1.segments;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getColor());
		g2.setStroke(new BasicStroke(fHeight/100f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));

		GeneralPath path = new GeneralPath();
		path.moveTo(segments.get(0).getX(), segments.get(0).getY());
		
		for(int i = 1; i < segments.size()-3; i++)		
			if(segments.get(i).getY() > camera.getY() - fHeight/2 &&
			   segments.get(i).getY() < camera.getY() + fHeight/2 ||
			   segments.get(i+1).getY() > camera.getY() - fHeight/2 &&
			   segments.get(i+1).getY() < camera.getY() + fHeight/2)
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
		
		/*public void update2() {
			
			if(segments2.isEmpty())  {
				segments2.add(new Line2D.Double(ball.getPos(), ball.getPos()));
			
			}else {
				Line2D lastSegment = segments2.get(segments2.size()-1);
				lastSegment.setLine(translate(lastSegment.getP1(), ball.getRotation()-Math.PI, 5), 	lastSegment.getP2());
			}
				
		}
		
		private Point2D.Double translate(Point2D p, double angle, double delta) {
			
			return new Point2D.Double(p.getX() + Math.cos(angle) * delta,
									  p.getY() + Math.sin(angle) * delta);
		}*/
	}
}
