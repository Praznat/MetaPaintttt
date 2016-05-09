package shapes;
import java.awt.Graphics;

import points.MetaPoint;



public interface ShapeType {
	
	public void setToMetaPoints(MetaPoint[] pts);
	public void draw(Graphics g, boolean finish);
	public void fill(Graphics g);
}
