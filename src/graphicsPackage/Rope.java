package graphicsPackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Rope extends Drawable{

	private float width;
	private Line2D line;			//ersetzt shape in Oberklasse, da Line2D nur ein Vektor ist, keine Area
	private Point2D ballPos;
	private Color pivotColor;
	private boolean isConnected;

	
	public Rope(int layer, Point2D ballPos, float width) {
		super(createShape(), Color.WHITE, layer);
		
		setPos(ballPos);
		this.width = width;
		this.line = createShape();
		this.ballPos = ballPos;
		this.isConnected = false;
	}

	private static Line2D createShape() {
		return new Line2D.Double(0, 0, 100, 100);
	}

	public boolean isConnected() {return isConnected;}
	
	public void connectTo(Pivot p) {
		line.setLine(ballPos, p.getPos());
		pivotColor = p.getPrimaryColor();
		isConnected = true;
		setVisibility(true);
	}
	
	public void disconnect() {
		isConnected = false;
		setVisibility(false);
	}
	
	public void update(int fps) {
		if(!isConnected)
			return;
		
		line.setLine(ballPos, line.getP2());

		if(!getColor().equals(pivotColor))
			courseColorTo(pivotColor, fps/10);
	}
	
	@Override
	public void fill(Graphics2D g2, double dx, double dy) {
		
		if(!isVisible())
			return;
		
		g2.translate(dx, dy);
		g2.setColor(getColor());
		g2.setStroke(new BasicStroke(width));
		g2.draw(line);
		
		g2.translate(-dx, -dy);

	}
}