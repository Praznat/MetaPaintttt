package shapes;

import java.awt.geom.Ellipse2D;

public class MetaEllipse extends MetaShape {
	
	protected int reqpoints() {return 2;}
	
	public static MetaShapeFactory createNew() {return new MetaShapeFactory(new MetaEllipse());}
	public static IOFactory<MetaEllipse> getIOFactory() {return new ShapeDefiner<MetaEllipse>(MetaEllipse.class);}

	@Override
	public void setShape() {
		if (shape == null) {shape = new Ellipse2D.Double();}
		int x = Math.min(points[0].getX(), points[1].getX());
		int y = Math.min(points[0].getY(), points[1].getY());
		int w = Math.abs(points[0].getX() - points[1].getX());
		int h = Math.abs(points[0].getY() - points[1].getY());
		((Ellipse2D)shape).setFrame(x, y, w, h);
	}


}
