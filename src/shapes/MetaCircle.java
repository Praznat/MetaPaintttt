package shapes;

import java.awt.geom.Ellipse2D;

public class MetaCircle extends MetaShape {
	
	protected int reqpoints() {return 2;}
	
	public static MetaShapeFactory createNew() {return new MetaShapeFactory(new MetaCircle());}
	public static IOFactory<MetaCircle> getIOFactory() {return new ShapeDefiner<MetaCircle>(MetaCircle.class);}
	
	@Override
	public void setShape() {
		if (shape == null) {shape = new Ellipse2D.Double();}
		double rad = Math.sqrt(Math.pow((points[1].getX() - points[0].getX()), 2) +
				Math.pow((points[1].getY() - points[0].getY()), 2));
		double x = points[0].getX() - rad;
		double y = points[0].getY() - rad;
		((Ellipse2D)shape).setFrame(x, y, 2*rad, 2*rad);
	}


}
