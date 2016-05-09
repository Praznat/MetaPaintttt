package shapes;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

import main.*;
import points.MetaPoint;


public abstract class MetaShape extends MetaObject {
	
	protected MetaPoint[] points = new MetaPoint[] {};
	protected Shape shape;
	protected boolean positive = true;

	protected abstract int reqpoints();
	//protected abstract void currentShape();
	
	protected static class MetaShapeFactory implements Factory {
		private MetaShape MS;
		public MetaShapeFactory(MetaShape ms) {MS = ms;}
		public void doit() {
			Statics.setCurrentShape(MS);
			final MetaShape subject = Statics.getCurrentShape();
			switch(Statics.getStep()) {
			case 0: 
				Statics.setMessage("Choose " + (subject.reqpoints() > 0 ? subject.reqpoints() : "") + " points");
				AbstractCommands.selectPoints(new Command() {public void doit() {
					Statics.incStep();
					subject.addMetaPoints(Statics.MP.getTheSpace().getSelectedPoints());
					MetaShapeFactory.this.doit();
				}}, subject.reqpoints());
				break;
			case 1:
				Statics.MP.getTheSpace().addMeta(subject);
				Statics.MP.getTheSpace().restore();
				return;
			default: break;
			}
		}
	}
	
	public void setPositive(boolean b) {positive = b;}
	public boolean isPositive() {return positive;}
	
	public MetaPoint[] getPoints() {return points;}
	
	protected void addMetaPoints(ArrayList<MetaPoint> selectedPoints) {
		points = new MetaPoint[selectedPoints.size()];
		for (int i = 0; i < points.length; i++) {points[i] = selectedPoints.get(i);}
	}
	
	public boolean contains(MetaPoint p) {
		for (MetaPoint mp : points) {
			if (p == mp) {return true;}
		}   return false;
	}
	public Point center() {
		int x = 0; int y = 0;
		for (MetaPoint p : points) {
			x += p.getX();   y += p.getY();
		}
		return new Point(x / points.length, y / points.length);
	}
	
	protected boolean pointNCorrect() {return points.length == reqpoints();}

	@Override
	public void draw(Graphics g) {
//		if (!pointNCorrect()) {return;}
//		setShape();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.black);
		//g2d.fill(shape);
		g2d.draw(shape);
	}
	
	public void drawHighlight(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.red);
		g2d.draw(shape);
	}
	public void drawSelected(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.blue);
		g2d.draw(shape);
	}

	public abstract void setShape();
	public Shape getShape() {return shape;}
	public Area getArea() {return new Area(shape);}
	
	public static class ShapeDefiner<T extends MetaShape> implements IOFactory<T> {
		private Class<T> clasz;
		public ShapeDefiner(Class<T> clasz) {this.clasz = clasz;}
		public T createFromString(String name, String[] in) {
			T ms = null; try {ms = clasz.newInstance();} catch (Exception e) {}
			ArrayList<MetaPoint> points = new ArrayList<MetaPoint>();
			for (int i = 1; i < in.length; i++) {
				MetaObject mo = IOManager.CIDMAP.get(in[i]);
				if (mo == null) {return null;} else {points.add((MetaPoint)mo);}
			}
			ms.setPositive(in[0].equals("+"));
			ms.addMetaPoints(points);
			ms.setName(name);
			return ms;
		}
	}
	
	@Override
	public String saveString() {
		String result = (positive ? "+" : "-");
		for (MetaPoint p : points) {result += IOManager.SEPARATOR + p.cid();}
		return result;
	}

}
