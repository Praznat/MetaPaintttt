package shapes;

import java.awt.Polygon;

import main.Calc;

public class MetaPolygon extends MetaShape {
	
	protected int reqpoints() {return -1;}
	
	public static MetaShapeFactory createNew() {return new MetaShapeFactory(new MetaPolygon());}
	public static IOFactory<MetaPolygon> getIOFactory() {return new ShapeDefiner<MetaPolygon>(MetaPolygon.class);}

	@Override
	public void setShape() {
		int xy[][] = Calc.getXYMatrixFromPoints(points);
		shape = new Polygon(xy[0], xy[1], xy[0].length);
	}

}
