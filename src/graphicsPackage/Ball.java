package graphicsPackage;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Ball extends Drawable{

	private static int LEFT = -1;		//gibt die Drehrichtung von Ball im Orbit um einen Pivot an.
	private static int RIGHT = 1;		//je nach Richtung werden all Drehungen mit -1/+1 multipliziert -> keine Abfragen mehr
	
	private static double radius = 1;	//Radius fuer Ball, eig Groesse wird mit setScale() gemacht
	private double speed;				//Geschwindigkeit in Pixeln, mit der sich Ball pro Frame vorbewegt
	private Point2D camera;				//Position, die dem Ball gewisser Massen folgt
	private int fWidth, fHeight;

	private boolean isInOrbit;		//ist Leertaste gedrueckt und bald wird ein Pivot umkreist?
	private boolean isSpinning;			//Ball dreht sich um einen Pivot (in seinem Orbit)?
	private boolean leftInWall;			//Ball ist beim Orbit leaven in der Wand, somit vorerst geschuetzt?
	private boolean hasCrashed;			//Ball kolliediert nach Spielregeln mit anderem Objekt?

	private Pivot lastPivot;			//haelt fest, um welchen Pivot sich Ball gerade dreht/zuletzt drehte
	private int spinDirection;			//Vorfaktor, der bestimmt, ob Ball nach links/rechts dreht
	private double spinRadius;			//Radius von Ball zu seinem Pivot in einem Orbit
	private double spinAddition;		//Grad, um die sich Ball jedes Mal in einem Orbit drehen wird
	
	//wenn Ball ausserhalb Spielfelds einen Orbit leavt, soll Player ein wenig Zeit zum neu Orbit Joinen bekommen
	//Variable haelt fest, wann Orbit geleavt wurde zum Vergleichen bei nachfolgenden Crashs
	private Long crashTimer;
	private int crashBuffer;				//Zeit, die Spieler zum neu Joinen hat
	
	public Ball(int layer, int fWidth, int fHeight) {

		super(createShape(), Color.WHITE, layer);
		
		setPos(fWidth/2, 0);
		setRotation(Math.PI * 3/2);
		setScale(fWidth/40.0);
		setVisibility(true);
		
		this.fWidth = fWidth;
		this.fHeight = fHeight;
		
		speed = 0;
		isInOrbit = false;
		isSpinning = false;
		hasCrashed = false;
		lastPivot = null;
		crashTimer = new Long(0);
		crashBuffer = 400;
		
		camera = new Point2D.Double(getPos().getX(), getPos().getY() - fHeight/6);
	}

	private static Area createShape() {
		Area shape = 	 new Area(new Ellipse2D.Double	(-radius, -radius, 2*radius, 2*radius));
		shape.subtract	(new Area(new Ellipse2D.Double	(-radius*2/3, -radius*2/3, radius * 4/3, radius * 4/3)));
		shape.add		(new Area(new Ellipse2D.Double	(-radius/3, -radius/3, radius*2/3, radius*2/3)));
		shape.add		(new Area(new Rectangle2D.Double(-radius, -radius, radius, 2*radius)));
		shape.subtract	(new Area(new Rectangle2D.Double(-radius, -radius, radius*2/3, 2*radius)));
		return shape;
	}
	
	public double getSpeed() {return speed;}
	public Pivot getPivot() {return lastPivot;}
	public Point2D getCamera() {return camera;}

	public boolean isInOrbit() {return isInOrbit;}
	public boolean isSpinning() {return isSpinning;}
	public boolean hasCrashed() {return hasCrashed;}

	public double getSpinRadius() {
		if(isSpinning)
			return spinRadius;
		else
			return 0;
	}
	
	public void setSpeed(double speed) {this.speed = speed;}
	
	public void enterOrbit(ArrayList<Pivot> visPivots) {
		
		if(isInOrbit == true || visPivots.isEmpty())
			return;
		/* proportion Abstand zu Pivot(hypotenuse) & rotation zu Richtung zu Pivot(alpha) 
		 * in gleiches Verhaeltnis -> proportion ist max. 2*fWidth gross(das waere ganz weit weg, entgegen Blickrichtung)
		 */
		double alpha, hypotenuse, opposite, proportion;
		double nextProportion = 2*fWidth;//, nextAlpha = Math.PI;	
		Pivot nextPivot = null;
		
		for(Pivot p : visPivots) {
			
			alpha = getInternalAngle(getAngleTo(p.getPos()) - getRotation());
			hypotenuse = getPos().distance(p.getPos());
			opposite = Math.sin(alpha) * hypotenuse;

			//Pivot darf nicht zu weit weg sein oder Ball bei Eintritt in Orbit mit ihm	 crashen
			if(opposite < radius*getScale() + p.getRadius())
				continue;
			
			proportion = hypotenuse + alpha * (fWidth / Math.PI);

			if(lastPivot == null || proportion < nextProportion) {
				nextPivot = p;
				//nextAlpha = alpha;
				nextProportion = proportion;
			}
		}
		
		if(nextPivot == null) // && lastPivot != null)
			nextPivot = lastPivot;
		
		if(nextPivot != null) {

			isInOrbit = true;
			lastPivot = nextPivot;
			//teste, ob Pivot lings/rechts von Ball ist -> setzte Drehrichtung
			double angleToPivot = getAngleTo(lastPivot.getPos()) - getRotation();
			if(angleToPivot > -Math.PI && angleToPivot < 0||
			   angleToPivot > Math.PI && angleToPivot < 2*Math.PI)
				spinDirection = LEFT;
			else
				spinDirection = RIGHT;
		}
	}

	public void leaveOrbit(ArrayList<Drawable> solids) {
		isInOrbit = false;
		isSpinning = false;
		lastPivot.disconnect();
		
		//Ball soll beim Leaven in einer Wall sicher sein
		for(Drawable d : solids)
			if(d instanceof Wall && intersects(d.getShape()))
				leftInWall = true;
		
		//wenn Ball ausserhalb Spielfelds Orbit leavt, wird crashTimer geupdated
		if(getPos().getX() < 40 - radius/2 || getPos().getX() > fWidth - 40)		//40 ist die Dicke der Mauer, hab grad keine Variable dafuer
			crashTimer = System.currentTimeMillis();
	}
	
	
	public void update(ArrayList<Drawable> solids) {
		
		if(isCrashing(solids)) {
			hasCrashed = true;
			return;
		}
		
		if(isInOrbit) {
			
			if(isSpinning)
				spin();

			else {
				double angleToPivot = getAngleTo(lastPivot.getPos()) - getRotation();
		
				//wenn Pivot sich noch noch vor Ball befindet
				if(angleToPivot > -Math.PI * 5/2 && angleToPivot < -Math.PI * 3/2 ||
				   angleToPivot > -Math.PI / 2 	 && angleToPivot <  Math.PI/2 ||
				   angleToPivot >  Math.PI * 3/2 && angleToPivot <  Math.PI * 5/2) {
					move(speed);					//-> geradeaus bis Ball wirklich parallel zu Pivot fliegt
				
				//ansonsten leite noetige Schritte zum Drehen ein
				}else {
					isSpinning  = true;
					spinRadius = getPos().distance(lastPivot.getPos());
					
					//cos Alpha(Winkel zum Pivot) = Hypotenuse(spinRadius) / Gegenkathete(speed/2)
					//180° - Alpha, da sich Ball nur um Gegenwinkel von berechnetem Winkel dreht
					//Ball kommt schraeg aus einer Richtung, geht schraeg in andere Richtung -> 2xAlpha
					spinAddition = Math.PI - 2*(Math.acos(speed/2 / spinRadius));
					setRotation(getAngleTo(lastPivot.getPos()) - spinDirection * (Math.PI/2));
					//da sich Ball immer nur um spinAddition drehen wird, muss Ausgangsposition wie spinAddition/2 sein
					//nicht 90° aka senkrecht zum Pivot
					rotate(-spinDirection * spinAddition/2);
					//drehe Pivot auf Winkelgeschwindigkeit von Ball an
					lastPivot.setSpinSpeed(spinDirection * 2*Math.asin(speed/2 / spinRadius));
					//lasse Pivot sich weiss faerben
					lastPivot.connect();
					
				}
			}
			
		}else
			move(speed);
		
		moveCamera();
	}
	
	//bewegt Ball um angegebene Distanz in Blickrichtung (rotation)
	public void move(double distance) {
		setPos(getPos().getX() + Math.cos(getRotation()) * distance,
			   getPos().getY() + Math.sin(getRotation()) * distance);
	}
	
	//koordiniert die Bewegung in Pivot Orbit
	public void spin() {
		//drehe Ball minimal im Kreis: je nach Drehrichtung (*1/*-1)
		rotate(spinAddition * spinDirection);
		move(speed);
	}
	
	private void moveCamera() {
		double posX, posY;
		
		posX = fWidth/2 + (getPos().getX() - fWidth/2) / 2;
		posY = getPos().getY() - fHeight/6;
		camera.setLocation(posX, posY);
	}
	
	//ueberpruefe, ob nach Spielregeln Crash vorliegt
	private boolean isCrashing(ArrayList<Drawable> solids) {
		
		if(leftInWall == true && crashTimer + crashBuffer < System.currentTimeMillis())
			leftInWall = false;
		
		//gehe all soliden Objekte durch
		for(Drawable d : solids) {
			//nur wenn es Ueberschneidungen der Shapes gibt
			if(!intersects(d.getShape()))
				continue;
			//wenn Ball geschuetzt crashe trotzdem an allen Pivots ausser dem Umkreisten
			if(d instanceof Pivot && !d.equals(lastPivot))
				return true;
			//andere Objekte sind, wenn Ball in Orbit, nicht wichtig
			if(isInOrbit())
				continue;
			//Wenn Ball eine Orbit in einer Wand leavt, ist er darin sicher
			if(d instanceof Wall && leftInWall == false)
				return true;
			//wenn Ball nicht geschuetzt ist -> alles crash
			if(crashTimer + crashBuffer < System.currentTimeMillis())
				return true;
			
			//Waende sind im geschuetzten Modus fein raus
		}	
		
		//wenn nicht in einem Orbit und Schutzzeit abgelaufen
		if(!isInOrbit && crashTimer + crashBuffer < System.currentTimeMillis())
			//und Ball ausserhalb des Spielfelds -> crash
			if(getPos().getX() < -40 + radius/2 || getPos().getX() > fWidth + 40 - radius/2)
				return true;
		
		return false;
	}
	
	//ueberpruefe ob Ball anderes Shape schneidet -> BOOOM!
	private boolean intersects(Shape shapeB) {
		Area areaA = new Area(getShape());
		areaA.intersect(new Area(shapeB));
		
		return !areaA.isEmpty();
	}
	
	//berechnet Winkel von eigenem Mittelpunkt zu 2. Punkt
	private double getAngleTo(Point2D p2) {
		double deltaX = p2.getX() - getPos().getX();
		double deltaY = p2.getY() - getPos().getY();
		double angle = Math.atan(deltaY / deltaX);
		
		if(deltaX < 0)			//Tangens-Korrektur
			angle += Math.PI;
		else if(deltaY < 0)
			angle += 2*Math.PI;
		
		return angle;
	}
		
		//mache aus Parameter einen Winkel >0° und <180° wie ein Innenwinkel ines Dreiecks
	private double getInternalAngle(double angle) {
		double internalAngle = angle;
		
		if(angle < 0)
			internalAngle *= -1;
		if(angle > Math.PI)
			internalAngle = 2*Math.PI - internalAngle;
		
		return internalAngle;
	}
}
