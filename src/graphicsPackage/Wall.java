package graphicsPackage;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

public class Wall extends Drawable{
	
	public Wall(int layer, int fWidth, int fHeight) {
		
		super(createShape(fWidth, fHeight), Drawable.NEON_RED, layer);
		setVisibility(true);
	}
	
	private static Area createShape(int fWidth, int fHeight) {
		Area shape =   new Area(new Rectangle(-fWidth/40, 0, fWidth/20, fHeight));
		shape.subtract(new Area(new Rectangle(-fWidth/80, 0, fWidth/80, fHeight)));
		shape.add	  (new Area(new Rectangle(fWidth - fWidth/40, 0, fWidth/20, fHeight)));
		shape.subtract(new Area(new Rectangle(fWidth, 0, fWidth/80, fHeight)));

		return shape;
	}
	
	@Override
	public void fill(Graphics2D g2, double dx, double dy) {
		if(getShape() == null || !isVisible())
			return;
		
		g2.setColor(getColor());
		g2.translate(dx, 0);
  		g2.fill(getShape());
		g2.translate(-dx, 0);
	}
}
