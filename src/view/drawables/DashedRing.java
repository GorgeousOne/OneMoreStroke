package view.drawables;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class DashedRing extends Drawable{

	private static double radius = 100;
	
	public DashedRing(int layer, Color color, int segments, double width) {
		super(createShape(width, segments), layer);
		setVisibility(false);
		setColor(color);
	}

	public static Area createShape(double width, int segments) {
		
		double angle = Math.PI/segments;
		
		Area shape = new Area();
		Area segment = new Area(new Rectangle2D.Double(radius - width/2, -radius/2*Math.sin(angle), width, radius*Math.sin(angle)));
		
		AffineTransform rotation = new AffineTransform();
		rotation.rotate(2*angle);
		
		for(double phi = 0; phi < 2*Math.PI; phi += 2*angle) {
			shape.add(segment);
			segment.transform(rotation);
		}
		return shape;
	}
}
