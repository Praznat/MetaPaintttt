package main;

import java.awt.Point;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;

import main.MetaObject.MPanel;
import points.MetaPoint;
import shapes.MetaShape;
import variators.Variator;
import GUI.DualSliderUI;


public class Calc {
	
	public static void p(String s) {System.out.println(s);}
	
	public static double roundy(double d, int e) {
		double p = Math.pow(10,e);
		return Math.round(d * p) / p;
	}
	public static int roundy(double d) {
		return (int) Math.round(d);
	}


	public static int sum(int[] x) {
		int sum = 0;
		for (int i = 0; i < x.length; i++) {sum += x[i];}
		return sum;
	}
	public static double max(double[] x) {
		double max = 0;
		for (int i = 0; i < x.length; i++) {if (x[i] > max) {max = x[i];}}
		return max;
	}
	public static int max(int[] x) {
		int max = 0;
		for (int i = 0; i < x.length; i++) {if (x[i] > max) {max = x[i];}}
		return max;
	}
	public static double min(double[] x) {
		double min = Integer.MAX_VALUE;
		for (int i = 0; i < x.length; i++) {if (x[i] < min) {min = x[i];}}
		return min;
	}
	public static int min(int[] x) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < x.length; i++) {if (x[i] < min) {min = x[i];}}
		return min;
	}

	public static int bound(int x, int min, int max) {
		return Math.min(max, Math.max(min, x));
	}


	public static int[] bubbleSort(int[] in) {
		int[] x = new int[in.length];
		System.arraycopy(in, 0, x, 0, in.length);
		boolean moved = true; int tmp;
		while (moved) {
			moved = false;
			for (int i = 0; i < x.length-1; i++) {
				if (x[i]>x[i+1]) {tmp=x[i]; x[i]=x[i+1]; x[i+1]=tmp; moved = true;}
			}
		}
		return x;
	}
	public static int[] cropArray(int[] in, int start, int end) {
		int[] out = new int[end-start];
		System.arraycopy(in, start, out, 0, end-start);
		return out;
	}
	public static int[][] cropArray(int[][] in, int start, int end) {
		int[][] out = new int[end-start][];
		for (int i = 0; i < out.length; i++) {
			out[i] = new int[in[start + i].length];
			for (int j = 0; j < out[i].length; j++) {
				out[i][j] = in[start + i][j];
			}
		}
		return out;
	}

	public static int degreesBetween(Point origin, Point perimeter) {
		double rad = Math.atan2(-(perimeter.y - origin.y), (perimeter.x - origin.x));
		return (int) Math.round(Math.toDegrees(rad));
	}
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	public static double distance(int[] pt1, int[] pt2) {
		return Math.sqrt(Math.pow(pt2[0] - pt1[0], 2) + Math.pow(pt2[1] - pt1[1], 2));
	}
	public static double distance(MetaPoint p1, MetaPoint p2) {
		return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
	}
	public static double distance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
	}

	public static void printArray(Object[] V) {
		for(int i = 0; i < V.length; i++) {System.out.println(V[i].toString());}
	}
	public static void printArray(byte[] V) {
		for(int i = 0; i < V.length; i++) {System.out.println(V[i]);}
	}
	public static void printArrayH(byte[] V) {
		for(int i = 0; i < V.length; i++) {System.out.print(V[i] + "	");}
		System.out.println();
	}
	public static void printArray(int[] V) {
		for(int i = 0; i < V.length; i++) {System.out.println(V[i]);}
	}
	public static void printArrayH(int[] V) {
		for(int i = 0; i < V.length; i++) {System.out.print(V[i] + "	");}
		System.out.println();
	}
	public static void printArray(byte[][] M) {
		for(int i = 0; i < M.length; i++) { for(int j = 0; j < M[i].length; j++) {
			System.out.print(M[i][j] + "	");
		}System.out.println();}
	}
	public static void printArray(String[][] M) {
		for(int i = 0; i < M.length; i++) { for(int j = 0; j < M[i].length; j++) {
			System.out.print(M[i][j] + "	");
		}System.out.println();}
	}
	public static void printArray(int[][] M) {
		for(int i = 0; i < M.length; i++) { for(int j = 0; j < M[i].length; j++) {
			System.out.print(M[i][j] + "	");
		}System.out.println();}
	}
	
	

    public static int[][] transpose (int[][] inM) {
    	int[][] outM = new int[inM[0].length][];  //assumption that all vectors are equal length
    	for (int i = 0; i < outM.length; i++) {
    		outM[i] = new int[inM.length];
    		for (int j = 0; j < outM[i].length; j++) {
    			outM[i][j] = inM[j][i];
    		}
    	}
    	return outM;
    }
    public static String[][] transpose (String[][] inM) {
    	String[][] outM = new String[inM[0].length][];  //assumption that all vectors are equal length
    	for (int i = 0; i < outM.length; i++) {
    		outM[i] = new String[inM.length];
    		for (int j = 0; j < outM[i].length; j++) {
    			outM[i][j] = inM[j][i];
    		}
    	}
    	return outM;
    }

    

	public static int[] combineArrays(int[] v1, int[] v2) {
		int[] v3 = new int[v1.length + v2.length];
		System.arraycopy(v1, 0, v3, 0, v1.length);
		System.arraycopy(v2, 0, v3, v1.length, v2.length);
		return v3;
	}
	public static boolean[] appendToArray(boolean m, boolean[] V) {
		boolean[] VV = new boolean[V.length+1];
		for (int i = 0; i < V.length; i++) {VV[i] = V[i];}
		VV[V.length] = m;
		return VV;
	}
	public static int[] between(int[] xy1, int[] xy2, int N, int D) {
		return new int[] {xy1[0]+(xy2[0]-xy1[0])*N/D, xy1[1]+(xy2[1]-xy1[1])*N/D};
	}
	public static Point pointBetween(Point xy1, Point xy2, int N, int D) {
		return new Point(xy1.x+(xy2.x-xy1.x)*N/D, xy1.y+(xy2.y-xy1.y)*N/D);
	}
	public static int[] offsetArray(int[] V, int off) {
		int[] out = new int[V.length];
		for (int i = 0; i < out.length; i++) {out[i] = V[i] + off;}
		return out;
	}
	
	public static double[] fractal(double R, int L) {
		//R must be 0~1
		int k = 1;   double r;
		double[] F = new double[L];
		while (k < L/2) {
			for(int i = 0; i < k; i++) {
				r = R * (2*Math.random() - 1);
				for (int j = (L/k)/2 - 1; j >= 0 ; j--) {
					F[j + L/k*i] += r*j/((L/k)/2);
					F[j + L/k*i + (L/k)/2] += r - r*j/((L/k)/2);
				}
			}   R = R * R;   k = k * 2;
		}   return F;
	}

	public static int[] copyArray(int[] in) {
		int[] out = new int[in.length];
		System.arraycopy(in, 0, out, 0, in.length);
		return out;
	}

	public static MetaPoint[] addToArray(MetaPoint[] in, MetaPoint newMO) {
		MetaPoint[] out = new MetaPoint[in.length + 1];
		System.arraycopy(in, 0, out, 0, in.length);
		out[in.length] = newMO;
		return out;
	}
	public static Variator[] addToArray(Variator[] in, Variator newMO) {
		Variator[] out = new Variator[in.length + 1];
		System.arraycopy(in, 0, out, 0, in.length);
		out[in.length] = newMO;
		return out;
	}
	public static MetaShape[] addToArray(MetaShape[] in, MetaShape newMO) {
		MetaShape[] out = new MetaShape[in.length + 1];
		System.arraycopy(in, 0, out, 0, in.length);
		out[in.length] = newMO;
		return out;
	}

	public static MetaPoint[] subtractFromArray(MetaPoint[] in, int k) {
		if (k < 0 || k >= in.length) {return in;}
		MetaPoint[] out = new MetaPoint[in.length - 1];
		System.arraycopy(in, 0, 	out, 0, 	k);
		System.arraycopy(in, k + 1, out, k, in.length - k - 1);
		return out;
	}
	public static Variator[] subtractFromArray(Variator[] in, int k) {
		if (k < 0 || k >= in.length) {return in;}
		Variator[] out = new Variator[in.length - 1];
		System.arraycopy(in, 0, out, 0, k);   System.arraycopy(in, k + 1, out, k, in.length - k);
		return out;
	}
	public static MetaShape[] subtractFromArray(MetaShape[] in, int k) {
		if (k < 0 || k >= in.length) {return in;}
		MetaShape[] out = new MetaShape[in.length - 1];
		System.arraycopy(in, 0, out, 0, k);   System.arraycopy(in, k + 1, out, k, in.length - k);
		return out;
	}


	public static int findInVector(Object[] V, Object m) {
		for (int i = 0; i < V.length; i++) {if (V[i].equals(m)) {return i;}}
		return -1;
	}
	
	public static double angleFromWandH(int W, int H) {return Math.atan((double)W / H);} //unfinished

	public static Point getIntersect(Point a0, Point a1, Point b0, Point b1) {
		double x = 0;   double y = 0;
		double aSlope = ((double)(a1.y - a0.y) / (a1.x - a0.x));
		double bSlope = ((double)(b1.y - b0.y) / (b1.x - b0.x));
//		Y - a0.y = aSlope * (X - a0.x)
//		Y - b0.y = bSlope * (X - b0.x)
//		b0.y - a0.y = (aSlope - bSlope) X + bSlope * b0.x - aSlope * a0.x
		x = ((double) (aSlope * a0.x - bSlope * b0.x + b0.y - a0.y)) / (aSlope - bSlope);
		y = aSlope * (x - a0.x) + a0.y;
		return new Point((int) Math.round(x), (int) Math.round(y));
	}

	public static int[][] getXYMatrixFromPoints(MetaPoint[] points) {
		int[][] xy = new int[2][points.length];
		for (int i = 0; i < xy[0].length; i++) {xy[0][i] = points[i].getX();xy[1][i] = points[i].getY();}
		return xy;
	}
	

	public static List<String> readFromFile(String fileName) {
		List<String> result = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str;
			while ((str = in.readLine()) != null) {
				result.add(str);
			}
		}	catch (IOException e) {}
		return result;
	}
	public static void writeToFile(List<String> strgs, String file) {
		try {
			PrintStream out = new PrintStream(new FileOutputStream(file));
			for(String s : strgs) {out.println(s);}
			out.close();
		}	catch (IOException e) {}
		return;
	}
	
	public static interface ParameterCommand<T> {
		public void doit(T i);
	}
	
	public static class MetaSet<T extends MetaObject> implements Iterable<T> {
		private ArrayList<T> members;
		private Class<T> clasz;
		public MetaSet(Class<T> clasz) {
			this.clasz = clasz;
			members = new ArrayList<T>();
		}
		public void moveUp(T obj) {
			int i = members.indexOf(obj);
			if (i == 0) {return;}
			T rplc = members.get(i - 1);
			members.set(i - 1, obj);
			members.set(i, rplc);
		}
		public void moveDown(T obj) {
			int i = members.indexOf(obj);
			if (i == members.size() - 1) {return;}
			T rplc = members.get(i + 1);
			members.set(i + 1, obj);
			members.set(i, rplc);
		}
		public Class<T> getMemberClass() {return clasz;}
		public ArrayList<T> getMembers() {return members;}
		@Override
		public Iterator<T> iterator() {
			return members.iterator();
		}
		@Override
		public String toString() {return members.toString();}
	}
	
	@SuppressWarnings("serial")
	public abstract static class ActionButton extends JButton implements ActionListener {
		public Object relObject;
		public ActionButton(String s) {
			super(s);
			setup();
			this.addActionListener(this);
		}
		public void setup() {}
	}
	
	public static class PlainLabel extends JPanel {
		private JTextPane pane = new JTextPane();
		public PlainLabel() {this("");}
		public PlainLabel(String name) {
			super();
			pane.setText(name);
			pane.setEditable(false);
			pane.setOpaque(false);
			this.add(pane);
		}
		public void setText(String s) {pane.setText(s);}
	}
	
	@SuppressWarnings("serial")
	public static class NamerLabel extends JTextField {
		private MetaObject ref;
		public NamerLabel(MetaObject mo, MPanel spanel) {
			super();
			this.setBorder(null);
			this.setOpaque(false);
			ref = mo;
			if (spanel != null) {
				this.addMouseListener(spanel);
				this.addMouseMotionListener(spanel);
			}
		}
		public void setRef(MetaObject r) {
			for (MouseListener m : this.getMouseListeners()) {this.removeMouseListener(m);}
			for (MouseMotionListener m : this.getMouseMotionListeners()) {this.removeMouseMotionListener(m);}
			ref = r;
			this.addMouseListener(ref.spanel);
			this.addMouseMotionListener(ref.spanel);
			this.setText(r.getName());
		}
	}
	
	public static class StoppingSliderUI extends BasicSliderUI {
		public StoppingSliderUI(JSlider b) {super(b);}
		public void stopAt(StoppingSliderUI stop) {setThumbLocation(thumbRect.x, stop.y());}
		public int y() {return thumbRect.y;}
	}
	
	public static class DualSlider extends JSlider {
	    public DualSlider() {
	        initSlider();
	    }

	    /**
	     * Constructs a RangeSlider with the specified default minimum and maximum 
	     * values.
	     */
	    public DualSlider(int min, int max) {
	        super(min, max);
	        initSlider();
	    }

	    /**
	     * Initializes the slider by setting default properties.
	     */
	    private void initSlider() {
	        setOrientation(VERTICAL);
	    }

	    /**
	     * Overrides the superclass method to install the UI delegate to draw two
	     * thumbs.
	     */
	    @Override
	    public void updateUI() {
	        setUI(new DualSliderUI(this));
	        // Update UI for slider labels.  This must be called after updating the
	        // UI of the slider.  Refer to JSlider.updateUI().
	        updateLabelUIs();
	    }

	    /**
	     * Returns the lower value in the range.
	     */
	    @Override
	    public int getValue() {
	        return super.getValue();
	    }

	    /**
	     * Sets the lower value in the range.
	     */
	    @Override
	    public void setValue(int value) {
	        int oldValue = getValue();
	        if (oldValue == value) {
	            return;
	        }

	        // Compute new value and extent to maintain upper value.
	        int oldExtent = getExtent();
	        int newValue = Math.min(Math.max(getMinimum(), value), oldValue + oldExtent);
	        int newExtent = oldExtent + oldValue - newValue;

	        // Set new value and extent, and fire a single change event.
	        getModel().setRangeProperties(newValue, newExtent, getMinimum(), 
	            getMaximum(), getValueIsAdjusting());
	    }

	    /**
	     * Returns the upper value in the range.
	     */
	    public int getUpperValue() {
	        return getValue() + getExtent();
	    }

	    /**
	     * Sets the upper value in the range.
	     */
	    public void setUpperValue(int value) {
	        // Compute new extent.
	        int lowerValue = getValue();
	        int newExtent = Math.min(Math.max(0, value - lowerValue), getMaximum() - lowerValue);
	        
	        // Set extent to set upper value.
	        setExtent(newExtent);
	    }
	}
	
}







