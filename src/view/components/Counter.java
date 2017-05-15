package view.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JComponent;

public class Counter extends JComponent{

	private static final long serialVersionUID = 1L;
	
	private int score;					//Score des Spielers/Balls
	private double pointDistance;		//Laenge zwischen 2 Score-Punkten
	
	public Counter(int fWidth) {
		
		pointDistance = 1;
		score = 0;
		
		setFont(loadFont("/res/fonts/terminat.ttf").deriveFont((float) fWidth/10));
		setBounds(0, 0, fWidth/2, getFont().getSize());
	}
	
	public double getPointDistance() {return pointDistance;}
	public int getScore() {return score;}
	
	public void setPointDistance(double distance) {pointDistance = distance;}
	
	public void update(Point2D ballPos) {
		if(ballPos.getY() < -(score+1) * pointDistance)
			score++;
	}
	
	@Override
	public void paintComponent(Graphics g) {
	
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP); 
		g2.setFont(getFont());
		g2.setColor(Color.WHITE);
		g2.drawString(Integer.toString(score), 0, getFont().getSize());
	}

	
	public Font loadFont(String path) {
		Font font = null;

		try {
			InputStream stream = getClass().getResourceAsStream(path);
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
			stream.close();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return font;
	}
}
