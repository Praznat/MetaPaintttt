package variators;

import java.awt.*;
import java.awt.geom.Area;
import java.util.*;
import java.util.List;

import main.*;
import points.MetaPoint;

public abstract class Variator extends MetaObject {
	public static final int NULL = Integer.MIN_VALUE;
	protected String name;
	protected MetaPoint dependentPoint;
	protected MetaPoint anchorPoint;
	protected List<Gene> genes = new ArrayList<Gene>();

	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.yellow);
		preview(g2d);
	}
	@Override
	public void drawHighlight(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.cyan);
		preview(g2d);
	}
	
	public List<Gene> getGenes() {return genes;}
	
	public void setDependentPoint(MetaPoint p) {
		if (anchorPoint.isDescendedFrom(p)) {return;}
		this.dependentPoint = p;
		p.addVariator(this);
	}
	public void setDependentPoint() {
		ArrayList<MetaPoint> smps = Statics.MP.getTheSpace().getSelectedPoints();
		MetaPoint p = smps.get(smps.size() - 1);
		Calc.p(this+" setting dependent point to " + p.toString());
		setDependentPoint(p);
	}

	public MetaPoint getDependentPoint() {
		return dependentPoint;
	}

	public void setAnchorPoint(MetaPoint p) {
		this.anchorPoint = p;
	}
	public void setAnchorPoint() {
		ArrayList<MetaPoint> smps = Statics.MP.getTheSpace().getSelectedPoints();
		MetaPoint p = smps.get(smps.size() - 1);
		Calc.p(this+" setting anchor point to " + p.toString());
		setAnchorPoint(p);
	}

	public MetaPoint getAnchorPoint() {
		return anchorPoint;
	}
	
	public abstract Point getPhenoPoint();

	public abstract Area getPhenoArea();

	public abstract void preview(Graphics2D g2d);
	
	public abstract void setVInit(Point mousePoint);
	public abstract void setVFinal(Point mousePoint);
	
	public static IOFactory<Variator> getIOFactory() {return new IOFactory<Variator>() {
		public Variator createFromString(String name, String[] in) {
			MetaObject angle = IOManager.CIDMAP.get(in[0]);
			MetaObject dist = IOManager.CIDMAP.get(in[1]);
			MetaObject anchor =  IOManager.CIDMAP.get(in[2]);
			MetaObject dependent =  IOManager.CIDMAP.get(in[3]);
			if (angle == null || dist == null || anchor == null || dependent == null) {return null;}
			Variator v = new SpaceVariator((Gene)angle, (Gene)dist);
			v.setAnchorPoint((MetaPoint)anchor);
			v.setDependentPoint((MetaPoint)dependent);
			v.setName(name);
			return v;
		}
	};}
	
	@Override
	public String saveString() {
		String s = "";
		for (Gene g : genes) {s += g.cid() + IOManager.SEPARATOR;}
		return s + anchorPoint.cid() + IOManager.SEPARATOR + dependentPoint.cid();
	}

}
