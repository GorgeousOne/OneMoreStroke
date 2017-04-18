package graphicsPackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Trail extends Drawable{

	private Ball ball;
	private int length;
	
	private Stripe stripe1;
	
	public Trail(int layer, Ball ball, int length) {
		
		super(null, Color.WHITE, layer);
		
		setVisibility(true);
		this.ball = ball;
		this.length = length;
		
		stripe1 = new Stripe();
	}

	public void update(int fps) {
		stripe1.update();
	
		if(ball.isSpinning() && !getColor().equals(ball.getPivot().getPrimaryColor()))
			courseColorTo(ball.getPivot().getPrimaryColor(), fps/5);
	}
	
	@Override
	public void fill(Graphics2D g2, double dx, double dy) {
		
		//if(!isVisible())
		//	return;
		
		if(stripe1.segments.size() < 2)
			return;
		
		ArrayList<Point2D> segments = stripe1.segments;
		
		g2.translate(dx, dy);
		g2.setColor(getColor());

		g2.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND));

		GeneralPath path = new GeneralPath();
		path.moveTo(segments.get(0).getX(), segments.get(0).getY());
		
		for(int i = 1; i < segments.size()-3; i++)
			path.lineTo(segments.get(i).getX(), segments.get(i).getY());
			
		//for(int i = 0; i < stripe1.segments.size()-3; i++)
		//	g2.draw(new Line2D.Double(stripe1.segments.get(i).getX(),	stripe1.segments.get(i).getY(), 
		//							  stripe1.segments.get(i+1).getX(), stripe1.segments.get(i+1).getY()));
		g2.draw(path);
		g2.translate(-dx, -dy);
	}
	
	public class Stripe {
		
		public ArrayList<Point2D> segments;
		
		public Stripe() {
			segments = new ArrayList<>();
		}
		
		public void update() {
			segments.add(new Point2D.Double(ball.getPos().getX(), ball.getPos().getY()));
			
			if(segments.size() > length)
				segments.remove(0);
		}
	}
}
