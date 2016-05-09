package variators;

import java.awt.Graphics;
import java.util.ArrayList;

import main.*;


public class Gene extends MetaObject {
	public static final GeneFactory FACTORY = new GeneFactory();
	private final int absMin, absMax;
	private int minval, maxval;
	private double genotype;
	public DependencyRule dependency;
	private ArrayList<Gene> dependencyListeners = new ArrayList<Gene>();
	
	public static final class GeneFactory {
		public Gene create(int min, int max, int amin, int amax) {return create("Gene", min, max, amin, amax);}
		public Gene create(String name, int min, int max, int amin, int amax) {
			Gene result = new Gene(name, min, max, amin, amax);
			Statics.MP.getTheSpace().addMeta(result);
			return result;
		}
	}

	protected Gene(String name, int min, int max, int absMin, int absMax) {
		super();
		this.minval = min;
		this.maxval = max;
		this.absMin = absMin;
		this.absMax = absMax;
		randomizeGenotype();
		this.spanel.setNameField(name);
	}

	public int getAbsMin() {return absMin;}
	public int getAbsMax() {return absMax;}
	
	public int getMinval() {
		return minval;
	}

	public void setMinval(int minval) {
		this.minval = minval;
		for (Gene dependent : dependencyListeners) {dependent.dependency.apply();}
	}

	public int getMaxval() {
		return maxval;
	}

	public void setMaxval(int maxval) {
		this.maxval = maxval;
		for (Gene dependent : dependencyListeners) {dependent.dependency.apply();}
	}

	public int getVal() {
		return minval + (int)(getGenotype() * (maxval-minval)); //use getGenotype() method so can be overriden by dependent genes
	}
	
	protected double getGenotype() {return !isLinked() ? genotype : dependency.getReference().getGenotype();}
	
	public void randomizeGenotype() {genotype = Math.random();}
	
	public void setGenotype(double d) {genotype = d;}
	
	public void setVal(int v) {
		double plc = (double)(v - minval) / (maxval - minval);
		genotype = plc;
	}
	
	public int getSpread() {return maxval - minval;}

	@Override
	public void draw(Graphics g2d) {}

	@Override
	public void drawHighlight(Graphics g) {}

	public boolean isLinked() {return dependency != null;}
	
	public void createDependencyRule(Gene ref) {
		this.dependency = new DependencyRule(ref);
		ref.dependencyListeners.add(this);
	}
	
	public class DependencyRule {
		private final Gene reference;
		private int addConstant = 0;
		private double multFactor = 1;
		private boolean wrapEdges = false;
		public DependencyRule(Gene ref) {this.reference = ref;}
		public void apply() {
			setMaxval(apply(reference.getMaxval(), getAbsMin(), getAbsMax()));
			setMinval(apply(reference.getMinval(), getAbsMin(), getAbsMax()));
		}
		private int apply(double in, int min, int max) {
			int raw = (int) Math.round(in * multFactor + addConstant);
			if (wrapEdges) {
				if (raw > max) {raw -= max - min;}
				else if (raw < min) {raw += max - min;}
			}
			else {
				if (raw > max) {raw = max;}
				else if (raw < min) {raw = min;}
			}
			return raw;
		}
		public int getAddConstant() {return addConstant;}
		public void setAddConstant(int i) {addConstant = i; apply();}
		public double getMultFactor() {return multFactor;}
		public void setMultFactor(double d) {multFactor = d; apply();}
		public boolean getWrapEdges() {return wrapEdges;}
		public void setWrap(boolean wrap) {wrapEdges = wrap; apply();}
		public Gene getReference() {return reference;}
	}
	
	public static IOFactory<Gene> getIOFactory() {return new IOFactory<Gene>() {
		public Gene createFromString(String name, String[] in) {
			Gene g = new Gene(name, i(in[0]), i(in[1]), i(in[2]), i(in[3]));
			if (in.length > 4) { // is linked
				MetaObject dependency = IOManager.CIDMAP.get(in[4]);
				if (dependency == null) {return null;}
				g.createDependencyRule((Gene) dependency);
				g.dependency.setMultFactor(Double.parseDouble(in[5]));
				g.dependency.setAddConstant(i(in[6]));
				g.dependency.setWrap(Boolean.parseBoolean(in[7]));
			}
			return g;
		}
	};}
	
	@Override
	public String saveString() {
		String _ = IOManager.SEPARATOR;
		String s = minval + _ + maxval + _ + absMin + _ + absMax;
		if (isLinked()) {s += _ + dependency.reference.cid() + _ + dependency.multFactor + _ + dependency.addConstant + _ + dependency.wrapEdges;}
		return s;
	}
}
