package view.drawables;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

public class Drawable {

	public static final Color BLUE = new Color(36, 140, 240);
	public static final Color YELLOW = new Color(255, 200, 0);
	public static final Color LIGHT_BLUE = new Color(50, 200, 255);
	public static final Color RED = new Color(255, 50, 150);
	public static final Color GREEN = new Color(125, 174, 51);
	public static final Color ORANGE = new Color(246, 130, 34);
	public static final Color PURPLE = new Color(157, 63, 255);
	
	private static Color[] NEON_COLORS = {BLUE, YELLOW, LIGHT_BLUE, RED, 
										  GREEN, ORANGE, PURPLE};
	
	protected Shape shape;
	private Color color;
	private int layer;
	private Point2D scale;
	private boolean isVisible;

	private Point2D pos;
	private double rotation;
	
	public Drawable(Shape shape, int layer) {
		this.shape = shape;
		this.layer = layer;
		this.color = Color.BLACK;
		this.scale = new Point2D.Float(1f, 1f);
		this.isVisible = true;

		this.rotation = 0;
		this.pos = new Point2D.Double(0, 0);
	}
	
	//Getter
	
	//berechnet Shape neu nach Position, Scale, etc.
	public Area getShape() {
		AffineTransform at = new AffineTransform();
		at.rotate(rotation, pos.getX(), pos.getY());

		at.translate(pos.getX(), pos.getY());
		at.scale(scale.getX(), scale.getY());	//scale nur hier

		Area shape = new Area(this.shape);
		shape.transform(at);
		return shape;
	}
	
	public Color getColor() {return color;}
	public int getLayer() {return layer;}
	public Point2D getScale() {return (Point2D) scale.clone();}
	public boolean isVisible() {return isVisible;}
	public Point2D getPos() {return pos;}
	public double getRotation() {return rotation;}

	//Setter
	public void setShape(Shape shape) {this.shape = shape;}
	public void setColor(Color color) {this.color = color;}
	
	public void setScale(double sx, double sy) {scale.setLocation(sx, sy);}
	public void setVisible(boolean visible) {this.isVisible = visible;}

	public void setPos(Point2D pos) {this.pos = pos;}
	public void setPos(double posX, double posY) {pos.setLocation(posX, posY);}
	
	public void translate(double dX, double dY) {pos.setLocation(dX, dY);}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
		//Blickrichting im Bereich von 0° - 360° macht das doch alles ein bisl uebersichtlicher
		if(getRotation() > 2*Math.PI)
			this.rotation -= 2*Math.PI;
		if(getRotation() < 0)
			this.rotation += 2*Math.PI;
	}

	public void rotate(double rotation) {
		this.rotation += rotation;
		
		if(getRotation() > 2*Math.PI)
			this.rotation -= 2*Math.PI;
		if(getRotation() < 0)
			this.rotation += 2*Math.PI;
	}
	
	public void fill(Graphics g) {

		if(shape == null || !isVisible)
			return;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
  		g2.fill(getShape());
	}
	
	public void brightenUp(int speed) {
		if(speed <= 0)
			return;
		
		int dRed = color.getRed() <= 255 - speed? speed : 255 - color.getRed();
		int dGreen = color.getGreen() <= 255 - speed? speed : 255 - color.getGreen();
		int dBlue = color.getBlue() <= 255 - speed? speed : 255 - color.getBlue();

		color = new Color(color.getRed() + dRed, color.getGreen() + dGreen, color.getBlue() + dBlue);
	}
	
	public void courseColorTo(Color newColor, int speed) {
		if(speed <= 0)
			return;
		
		int dRed = newColor.getRed() - color.getRed();
		int dGreen = newColor.getGreen() - color.getGreen();
		int dBlue = newColor.getBlue() - color.getBlue();
		
		int newRed = (int) (Math.abs(dRed) > speed? Math.signum(dRed) * speed : dRed);
		int newGreen = (int) (Math.abs(dGreen) > speed? Math.signum(dGreen) * speed : dGreen);
		int newBlue = (int) (Math.abs(dBlue) > speed? Math.signum(dBlue) * speed : dBlue);
		
		color = new Color(color.getRed() + newRed, color.getGreen() + newGreen, color.getBlue() + newBlue);
	}
	
	public static Color rndNeonColor() {
		return NEON_COLORS[(int) (Math.random() * NEON_COLORS.length)];
	}
	
	public static Color nextNeonColor(Color lastColor) {
		for(int i = 0; i < NEON_COLORS.length; i++)
			if(lastColor.equals(NEON_COLORS[i]) && i+1 < NEON_COLORS.length)
				return NEON_COLORS[i+1];
		return NEON_COLORS[0];
	}
	
}
