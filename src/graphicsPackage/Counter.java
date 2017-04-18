package graphicsPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;

public class Counter extends Drawable{

	private int score;				//Score des Spielers/Balls
	private double pointDistance;		//Laenge zwischen 2 Score-Punkten
	private Font terminator;		//coole Schriftart, die erst geladen wird
	
	public Counter(int layer) {
		
		super(createShape(), Color.WHITE, layer);
		setVisibility(true);
		pointDistance = 0;
		score = 0;
		
		loadFont("/fonts/terminat.ttf");
	}
	
	public double getPointDistance() {return pointDistance;}
	public int getScore() {return score;}
	
	public void setPointDistance(double distance) {pointDistance = distance;}

	private static Area createShape() {
		return new Area(new Rectangle2D.Double(0, 0, 1, 1));
	}
	
	public void update(Point2D ballPos) {
		if(ballPos.getY() < -(score+1) * pointDistance)
			score++;
	}
	
	@Override
	public void fill(Graphics2D g2, double dx, double dy) {
		g2.setFont(terminator);
		g2.setColor(Color.WHITE);
		g2.drawString(Integer.toString(score), (int) getPos().getX(), (int) getPos().getY());
	}

	public void loadFont(String path) {
		try {
			InputStream stream = getClass().getResourceAsStream(path);
			terminator = Font.createFont(Font.TRUETYPE_FONT, stream);
			terminator = terminator.deriveFont(50f);
			stream.close();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
