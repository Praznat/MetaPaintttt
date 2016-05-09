package main;

import java.awt.*;
import java.awt.geom.Area;
import java.util.*;

import shapes.MetaShape;
import variators.Gene;

public class MetaStructure extends MetaObject {
//	private static final JSlider RSLIDER = new Calc.DualSlider(0, 255);
//	private static final JSlider GSLIDER = new Calc.DualSlider(0, 255);
//	private static final JSlider BSLIDER = new Calc.DualSlider(0, 255);
//	private static final JPanel SLIDERPANEL = new JPanel();
	private static final int DEFAULT_INVISIBLE_PROBABILITY = 10; // expressed as one-in-x chance
	protected static enum PaintStyle {FILL, BORDER, SIDES}
	
	protected Gene red, green, blue, visible;
	protected Color bgColor() {return new Color(red.getVal(), green.getVal(), blue.getVal());}
	
	private PaintStyle paintStyle = PaintStyle.FILL;
	private Collection<MetaShape> shapes = new ArrayList<MetaShape>();
	private Area area = new Area();
	
	public static final Factory FACTORY = new MetaStructureFactory();
	
	protected static class MetaStructureFactory implements Factory {
		@Override
		public void doit() {
			MetaStructure struct = new MetaStructure();
			struct.setColorGenes( Gene.FACTORY.create("Red", 0, 255, 0, 255),
					Gene.FACTORY.create("Green", 0, 255, 0, 255),
					Gene.FACTORY.create("Blue", 0, 255, 0, 255),
					Gene.FACTORY.create("Visible", 0, DEFAULT_INVISIBLE_PROBABILITY, 0, DEFAULT_INVISIBLE_PROBABILITY)
					);
			struct.addShapes(Statics.MP.getTheSpace().getSelectedShapes());
			Statics.MP.getTheSpace().addMeta(struct);
			Statics.MP.getTheSpace().restore();
		}
	}
	
	public void setColorGenes(Gene red, Gene green, Gene blue, Gene visible) {
		this.red = red;   this.green = green;   this.blue = blue;   this.visible = visible;
	}
	public Gene[] getGenes() {return new Gene[] {red, green, blue, visible};}
	public Collection<MetaShape> getShapes() {return shapes;}

	public void setPaintStyle(PaintStyle paintStyle) {this.paintStyle = paintStyle;}
	public PaintStyle getPaintStyle() {return paintStyle;}
	public static PaintStyle getPaintStyle(String s) {
		if ("B".equals(s)) {return PaintStyle.BORDER;}
		else if ("S".equals(s)) {return PaintStyle.SIDES;}
		else {return PaintStyle.FILL;}
	}
	public String getPaintStyleString() {
		switch (getPaintStyle()) {
		case BORDER: return "B";
		case SIDES: return "S";
		default: return "F";
		}
	}
	
	public void addShapes(Collection<MetaShape> s) {
		shapes.addAll(s);
	}
	public void addShape(MetaShape s) {
		shapes.add(s);
	}
	private void setupArea() {area.reset();
		for (MetaShape s : shapes) {if (s.isPositive()) {area.add(s.getArea());}}
		for (MetaShape s : shapes) {if (!s.isPositive())  {area.subtract(s.getArea());}}
	}

	@Override
	public void draw(Graphics g) {
		if (visible.getVal() == 0) {return;}
		setupArea();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(bgColor());
		paintStructure(g2d);
	}
	@Override
	public void drawHighlight(Graphics g) {
		setupArea();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(bgColor().brighter());
		paintStructure(g2d);
	}
	private void paintStructure(Graphics2D g2d) {
		switch (getPaintStyle()) {
		case FILL: g2d.fill(area); break;
		case BORDER: g2d.draw(area); break;
		case SIDES: for (MetaShape ms : shapes) {g2d.draw(ms.getArea());} break;
		}
	}
	
	public static IOFactory<MetaStructure> getIOFactory() {return new IOFactory<MetaStructure>() {
		public MetaStructure createFromString(String name, String[] in) {
			MetaObject red = IOManager.CIDMAP.get(in[0]);
			MetaObject green = IOManager.CIDMAP.get(in[1]);
			MetaObject blue = IOManager.CIDMAP.get(in[2]);
			MetaObject visible = IOManager.CIDMAP.get(in[3]);
			PaintStyle ps = getPaintStyle(in[4]);
			if (red == null || green == null || blue == null) {return null;}
			MetaStructure ms = new MetaStructure();
			ms.setPaintStyle(ps);
			ms.setColorGenes((Gene)red, (Gene)green, (Gene)blue, (Gene)visible);
			for (int i = 5; i < in.length; i++) {
				MetaObject mo = IOManager.CIDMAP.get(in[i]);
				if (mo == null) {return null;}
				ms.addShape((MetaShape) mo);
			}
			return ms;
		}
	};}
	
	@Override
	public String saveString() {
		String _ = IOManager.SEPARATOR;
		String result = red.cid() + _ + green.cid() + _ + blue.cid() + _ + visible.cid() + _ + getPaintStyleString();
		for (MetaShape s : shapes) {result += _ + s.cid();}
		return result;
	}

	@Override
	public String toString() {
		String name = super.toString()+ ": ";
		for (MetaShape ms : shapes) {name += (ms.isPositive() ? "+" : "-") + ms + "; ";}
		return name;
	}
	
}
