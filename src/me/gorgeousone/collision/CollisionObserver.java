package me.gorgeousone.collision;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

public final class CollisionObserver {
	
	private static Set<Collidable> collidables = new HashSet<>();
	
	private CollisionObserver() {}
	
	public static void registerCollidable(Collidable obejct) {
		collidables.add(obejct);
	}
	
	public static void checkForCollisions(Collidable movingObejct) {
		
		if(!movingObejct.canCollide())
			return;
		
		for(Collidable otherObject : collidables) {
			
			if(otherObject == movingObejct || !otherObject.canCollide())
				continue;
			
			if(!boudingBoxesDoIntersect(movingObejct.getShape(), otherObject.getShape()))
				continue;
			
			Area intersection = new Area(movingObejct.getShape());
			intersection.intersect(otherObject.getShape());
			
			if(!intersection.isEmpty()) {
				
				if(movingObejct.hasPhysics())
					movingObejct.onHit(otherObject, intersection);
				
				if(otherObject.hasPhysics())
					otherObject.onHit(movingObejct, intersection);
			}
		}
	}
	
	private static boolean boudingBoxesDoIntersect(Area shape0, Area shape1) {
	
		Rectangle2D bounds0 = shape0.getBounds2D();
		Rectangle2D bounds1 = shape1.getBounds2D();
		
		return 
			bounds1.contains(bounds0.getMinX(), bounds0.getMinY()) ||
			bounds1.contains(bounds0.getMinX(), bounds0.getMaxY()) ||
			bounds1.contains(bounds0.getMaxX(), bounds0.getMaxY()) ||
			bounds1.contains(bounds0.getMaxX(), bounds0.getMinY());
	}
}