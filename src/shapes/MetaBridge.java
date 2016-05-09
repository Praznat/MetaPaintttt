package shapes;

import java.awt.Point;
import java.awt.geom.CubicCurve2D;

import main.Calc;

public class MetaBridge extends MetaShape {
	
	protected int reqpoints() {return 4;}
	
	public static MetaShapeFactory createNew() {return new MetaShapeFactory(new MetaBridge());}
	public static IOFactory<MetaBridge> getIOFactory() {return new ShapeDefiner<MetaBridge>(MetaBridge.class);}
	
	@Override
	public void setShape() {
		if (shape == null) {shape = new CubicCurve2D.Double();}
		Point p0 = points[0].getPoint();
		Point p1 = points[1].getPoint();
		Point p2 = points[2].getPoint();
		Point p3 = points[3].getPoint();
		Point intersect = Calc.getIntersect(p0, p1, p2, p3);
		int minx1, maxx1, miny1, maxy1, minx2, maxx2, miny2, maxy2;
		if (p1.x < p0.x) {
			minx1 = 2*p1.x - p0.x;
			maxx1 = p1.x - (p0.x - p1.x)/3;
		}   else {
			minx1 = p1.x - (p0.x - p1.x)/3;
			maxx1 = 2*p1.x - p0.x;
		}
		if (p1.y < p0.y) {
			miny1 = 2*p1.y - p0.y;
			maxy1 = p1.y - (p0.y - p1.y)/3;
		}   else {
			miny1 = p1.y - (p0.y - p1.y)/3;
			maxy1 = 2*p1.y - p0.y;
		}
		if (p2.x < p3.x) {
			minx2 = 2*p2.x - p3.x;
			maxx2 = p2.x - (p3.x - p2.x)/3;
		}   else {
			minx2 = p2.x - (p3.x - p2.x)/3;
			maxx2 = 2*p2.x - p3.x;
		}
		if (p2.y < p3.y) {
			miny2 = 2*p2.y - p3.y;
			maxy2 = p2.y - (p3.y - p2.y)/3;
		}   else {
			miny2 = p2.y - (p3.y - p2.y)/3;
			maxy2 = 2*p2.y - p3.y;
		}
		Point p1m = Calc.pointBetween(p1, intersect, 1, 2);
		Point p2m = Calc.pointBetween(p2, intersect, 1, 2);
		p1m.x = Calc.bound(p1m.x, minx1, maxx1);
		p1m.y = Calc.bound(p1m.y, miny1, maxy1);
		p2m.x = Calc.bound(p2m.x, minx2, maxx2);
		p2m.y = Calc.bound(p2m.y, miny2, maxy2);
		((CubicCurve2D)shape).setCurve(p1, p1m, p2m, p2);
	}
	
	@Override
	public Point center() {
		int x = 0; int y = 0;
		x += points[1].getX();   y += points[1].getY();
		x += points[2].getX();   y += points[2].getY();
		return new Point(x / 2, y / 2);
	}

}
