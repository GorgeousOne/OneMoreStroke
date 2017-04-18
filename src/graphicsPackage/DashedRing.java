package graphicsPackage;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class DashedRing extends Drawable{

	private static double radius = 100;
	
	public DashedRing(int layer, int segments, double width) {
		super(createShape(width, Math.PI/segments), Drawable.NEON_ORANGE, layer);
	}

	public static Area createShape(double width, double angle) {
		
		double innerRadius = radius - width/2;		//Ring, Haelfte von width kleiner als Ring mit radius
		double outerRadius = radius + width/2;		//Ring, Haelfte von width groesser als Ring mit radius
		
		//Ein Ring, ganz normal
		Area shape =   new Area(new Ellipse2D.Double(-outerRadius, -outerRadius, 2*outerRadius, 2*outerRadius));
		shape.subtract(new Area(new Ellipse2D.Double(-innerRadius, -innerRadius, 2*innerRadius, 2*innerRadius)));
		
		//AffineTransform um ein Rechteck zu drehen
		AffineTransform rotation = new AffineTransform();
		rotation.rotate(2*angle, 0, 0);
		//Rechteck, das die Luecken erzeigen wird
		Area bar = new Area(new Rectangle2D.Double(-outerRadius, -outerRadius * Math.sin(angle)/2,
														  width,  outerRadius * Math.sin(angle)));
		//Lueckenerzeugung durch Iteration
		for (double phi = 0; phi < 2*Math.PI; phi += 2*angle) {
			bar.transform(rotation);
			shape.subtract((new Area(bar)));
		}
		return shape;
	}
}
