package shapes;

import java.awt.Point;
import java.awt.geom.QuadCurve2D;

public class MetaCurve extends MetaShape {
	
	protected int reqpoints() {return 3;}
	
	public static MetaShapeFactory createNew() {return new MetaShapeFactory(new MetaCurve());}
	public static IOFactory<MetaCurve> getIOFactory() {return new ShapeDefiner<MetaCurve>(MetaCurve.class);}
	
	@Override
	public void setShape() {
		if (shape == null) {shape = new QuadCurve2D.Double();}
		Point z = new Point(2*points[1].getX() - (points[0].getX() + points[2].getX()) / 2, 
				2*points[1].getY() - (points[0].getY() + points[2].getY()) / 2);
		((QuadCurve2D)shape).setCurve(points[0].getPoint(), z, points[2].getPoint());
	}

}
