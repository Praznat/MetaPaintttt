package points;
import java.awt.*;
import java.util.*;

import main.*;
import variators.Variator;


public class MetaPoint extends MetaObject {
	protected int x, y;
	protected Set<Variator> variators;
	
	public MetaPoint(int X, int Y) {
		setX(X);   setY(Y);
		variators = new HashSet<Variator>();
	}
	
	public static IOFactory<MetaPoint> getIOFactory() {return new IOFactory<MetaPoint>() {
		public MetaPoint createFromString(String name, String[] in) {
			MetaPoint mp = new MetaPoint(i(in[0]), i(in[1]));
			mp.setName(name);
			return mp;
		}
	};}
	
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.fillOval(x-2, y-2, 5, 5);
	}
	public void drawSelected(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(x-2, y-2, 5, 5);
	}
	public void drawHighlight(Graphics g) {
		g.setColor(Color.red);
		g.drawOval(x-3, y-3, 7, 7);
	}
	
	public void setX(int x) {this.x = x;}
	public void setX(double x) {setX((int) Math.round(x));}
	public int getX() {return x;}
	public void setY(int y) {this.y = y;}
	public void setY(double y) {setY((int) Math.round(y));}
	public int getY() {return y;}
	public void setPoint(Point P) {x = P.x;   y = P.y;}
	public Point getPoint() {return new Point(x, y);}
	
	@Override
	public String toString() {return "#"+getOrder() + ": " + x + ", " + y;}
	
	public void addVariator(Variator variator) {variators.add(variator);}
	
	public Set<Variator> getVariators() {return variators;}
	
	public boolean isDescendedFrom(MetaPoint point) {
		if (this == point) {return true;}
		Set<Variator> vars = this.getVariators();
		for (Variator v : vars) {
			if (v.getAnchorPoint().isDescendedFrom(point)) {return true;}
		}
		return false;
	}
	
	public void comeToPhenoPoint() {
		System.out.println(System.nanoTime());
		System.out.println(this + " coming to phenopoint");
		int N = variators.size();
		if (N == 0) {return;}
		double sumX = 0;   double sumY = 0;
		Point tmp;
		for (Variator V: variators) {
			tmp = V.getPhenoPoint();
			sumX += tmp.x;   sumY += tmp.y;
		}
		setX(sumX / N);   setY(sumY / N);
	}
	
	public int[] toIntArray() {return new int[] {x, y};}

	@Override
	public String saveString() {
		return x + IOManager.SEPARATOR + y;
	}
	
}
