package main;

import java.awt.*;
import java.util.*;
import java.util.List;

import points.MetaPoint;
import shapes.MetaShape;
import variators.Variator;
import GUI.MPFramer;

public class Statics {
	public static final Color PANELDEFAULTCOLOR = new Color(190,190,190);
	public static final Color PANELHIGHLIGHTCOLOR = new Color(250,250,250);
	
	private static List<MetaPoint> chosenMetaPoints;
	private static int step;
	public static ActionState TheActionState = new ActionState();
	public static final int VariatorDenominator = 15;
	public static MPFramer MP;
	private static Command currentAction;
	private static Variator currentVariator;
	private static MetaShape currentShape;
	private static Point currentMousePoint;
	public static Random rand = new Random();
	public static Thread PainterThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while(true) {
//				System.out.println("running");
				MP.getTheSpace().repaint();
			}
		}
	});
	
	public static void setMessage() {setMessage("");}
	public static void setMessage(String S) {MP.getBottomMenu().setLabel(S);}
	public static void setButton(String S, Command C) {MP.getBottomMenu().setButton(S, C);}
	public static void setChosenMetaPoints(List<MetaPoint> mps) {chosenMetaPoints = mps;}
	public static List<MetaPoint> getChosenMetaPoints() {return chosenMetaPoints;}
	public static Variator getCurrentVariator() {return currentVariator;}
	public static void setCurrentVariator(Variator V) {currentVariator = V;}
	public static MetaShape getCurrentShape() {return currentShape;}
	public static void setCurrentShape(MetaShape S) {currentShape = S;}
	public static void setCommand(Command C) {currentAction = C;}
	public static void doCommand() {Calc.p("doing command " + currentAction.toString());   currentAction.doit();}
	public static Command getCommand() {return currentAction;}
	public static int getStep() {return step;}
	public static void incStep() {step++;}
	public static void resetStep() {step = 0;}
	public static Point getCurrentMousePoint() {return currentMousePoint;}
	public static void setCurrentMousePoint(Point p) {currentMousePoint = p;}
	@SuppressWarnings("rawtypes")
	public static Calc.MetaSet getAll(Class clasz) {return MP.getTheSpace().getAllMetas(clasz);}
}
