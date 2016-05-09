package variators;

import java.awt.*;
import java.awt.geom.*;

import main.*;

public class SpaceVariator extends Variator {

	public Gene angle, distance;
	private int lastVAngle;


	public static SpaceVariatorFactory createNew() {return new SpaceVariatorFactory(new SpaceVariator());}
	
	public static class SpaceVariatorFactory implements Factory {
		private Variator SV;
		public SpaceVariatorFactory(Variator sv) {SV = sv;}
		@Override
		public void doit() {
			Statics.setCurrentVariator(SV);
			SpaceVariator subject = (SpaceVariator)Statics.getCurrentVariator();
			switch(Statics.getStep()) {
			case 0: 
				Statics.setMessage("Choose anchor point");
				AbstractCommands.selectPoints(this, 1);
				break;
			case 1: 
				subject.setAnchorPoint();
				Statics.setMessage("Choose dependent point");
				AbstractCommands.selectPoints(this, 2);
				break;
			case 2:
				subject.setDependentPoint();
				if (subject.getDependentPoint() == null) {
					Statics.setMessage("ERROR: VARIATOR CIRCLE");
					Statics.MP.getTheSpace().restore(); return;
				}
				Statics.setMessage("Highlight variation span");
				Statics.MP.getTheSpace().setEditingMeta(subject);
				AbstractCommands.setVariance(this);
				break;
			case 3:
				subject.fixAngle();
				Statics.MP.getTheSpace().addMeta(subject);
				Statics.MP.getTheSpace().restore();
				return;
			default: break;
			}
			Statics.incStep();
		}
	}

	protected SpaceVariator() {
		this(Gene.FACTORY.create("Angle", 160, 190, 0, 360),
			 Gene.FACTORY.create("Distance", 20, 50, 0, Statics.MP.spaceHypotenuseSize()));
	}
	protected SpaceVariator(Gene angle, Gene distance) {
		this.angle = angle;   this.distance = distance;
		genes.clear();
		genes.add(angle);   genes.add(distance);
	}

	public Point getPhenoPoint() {
		int dist = distance.getVal();
		double ang = Math.toRadians(angle.getVal());
		int X = (int) Math.round(getAnchorPoint().getX() + Math.cos(ang) * dist);
		int Y = (int) Math.round(getAnchorPoint().getY() - Math.sin(ang) * dist);
		return new Point(X, Y);
	}

	public Area getPhenoArea() {
		Arc2D big = new Arc2D.Double();
		Arc2D small = new Arc2D.Double();
		big.setArcByCenter(getAnchorPoint().getX(), getAnchorPoint().getY(), distance.getMaxval(),
				angle.getMinval(), angle.getSpread(), Arc2D.PIE);
		small.setArcByCenter(getAnchorPoint().getX(), getAnchorPoint().getY(), distance.getMinval(),
				angle.getMinval(), angle.getSpread(), Arc2D.PIE);
		Area A = new Area(big);   A.subtract(new Area(small));
		return A;
	}

	@Override
	public void setVInit(Point P) {
		int ang = Calc.degreesBetween(getAnchorPoint().getPoint(), P);
		int dist = (int) Math.round(Calc.distance(getAnchorPoint().getPoint(), P));
		angle.setMinval(ang);			angle.setMaxval(ang);
		distance.setMinval(dist);	distance.setMaxval(dist);
		lastVAngle = ang;
	}
	@Override
	public void setVFinal(Point P) {
		int ang = Calc.degreesBetween(getAnchorPoint().getPoint(), P);
		if (Math.abs(ang - lastVAngle) > 180) {ang = (ang-lastVAngle >= 0 ? ang -360 : ang +360);}
		int dist = (int) Math.round(Calc.distance(getAnchorPoint().getPoint(), P));
		if (ang < angle.getMinval()) {angle.setMinval(ang);}
		else if (ang > angle.getMaxval()) {angle.setMaxval(ang);}
		if (dist < distance.getMinval()) {distance.setMinval(dist);}
		else if (dist > distance.getMaxval()) {distance.setMaxval(dist);}
		lastVAngle = ang;
	}
	
	protected void fixAngle() {
		while (angle.getMaxval() - angle.getMinval() > 360) {angle.setMaxval(angle.getMaxval() - 360);}
	}

	@Override
	public void preview(Graphics2D g2d) {
		Area A = getPhenoArea();
		g2d.fill(A);
		g2d.setPaint(Color.blue);
		Point p = getPhenoPoint();
		g2d.drawOval(p.x-1, p.y-1, 3, 3);
	}
	
	
}
