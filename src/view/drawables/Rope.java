package view.drawables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Rope extends Drawable{

	private float width;
	private Line2D line;			//ersetzt shape in Oberklasse, da Line2D nur ein Vektor ist, keine Area
	private Point2D ballPos;
	private Color nodeColor;
	private boolean isConnected;

	
	public Rope(int layer, Color color, Point2D ballPos, float width) {
		super(createShape(), layer);
		setVisibility(false);
		setColor(color);
		setPos(ballPos);
		
		this.width = width;
		this.line = createShape();
		this.ballPos = ballPos;
		this.isConnected = false;
	}

	private static Line2D createShape() {
		return new Line2D.Double(0, 0, 0, 0);
	}

	public boolean isConnected() {return isConnected;}
	
	public void connectTo(Node n) {
		line.setLine(ballPos, n.getPos());
		nodeColor = n.getPrimaryColor();
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

		if(!getColor().equals(nodeColor))
			courseColorTo(nodeColor, fps/10);
	}
	
	@Override
	public void fill(Graphics g) {
		
		if(!isVisible())
			return;

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getColor());
		g2.setStroke(new BasicStroke(width));
		g2.draw(line);
	}
}