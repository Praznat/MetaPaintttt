package main;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JPanel;

import points.MetaPoint;
import shapes.MetaShape;
import variators.*;

@SuppressWarnings("serial")
public class Space extends JPanel implements MouseListener, MouseMotionListener {
	protected boolean drawBuildersOK = true;
	public void setDrawBuildersOK(boolean b) {drawBuildersOK = b;}
	public final int HEIGHT, WIDTH;
	private Point mousePoint;
	
	private MetaPoint HighlightedPoint;
	private ArrayList<MetaPoint> SelectedPoints = new ArrayList<MetaPoint>();
	public MetaShape HighlightedShape;
	private ArrayList<MetaShape> SelectedShapes = new ArrayList<MetaShape>();
	public Variator HighlightedVariator;
	public MetaStructure HighlightedStructure;

	private Calc.MetaSet<MetaPoint> allPoints = new Calc.MetaSet<MetaPoint>(MetaPoint.class);
	private Calc.MetaSet<MetaShape> allShapes = new Calc.MetaSet<MetaShape>(MetaShape.class);
	private Calc.MetaSet<Variator> allVariators = new Calc.MetaSet<Variator>(Variator.class);
	private Calc.MetaSet<MetaStructure> allStructures = new Calc.MetaSet<MetaStructure>(MetaStructure.class);
	private Calc.MetaSet<Gene> allGenes = new Calc.MetaSet<Gene>(Gene.class);
	private MetaObject highlightedMeta, editingMeta;
	
	public Space(int w, int h) {
		super();
		WIDTH = w;   HEIGHT = h;
		mousePoint = new Point();
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public void forcePaint() {
		repaint();
		paintImmediately(0, 0, getWidth(), getHeight());
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for(MetaShape S : allShapes) {S.setShape();}
		if (drawBuildersOK) {
			for(Variator V : allVariators) {V.draw(g);}
			for(MetaPoint P : allPoints) {P.draw(g);}
			for(MetaShape S : allShapes) {S.draw(g);}
		}
		for(MetaStructure S : allStructures) {S.draw(g);}
		for(MetaPoint P : SelectedPoints) {P.drawSelected(g);}
		for(MetaShape S : SelectedShapes) {S.drawSelected(g);}
		if (HighlightedPoint != null) {HighlightedPoint.drawHighlight(g);}
		if (HighlightedShape != null) {HighlightedShape.drawHighlight(g);}
		if (highlightedMeta != null) {highlightedMeta.drawHighlight(g);}
		if (Statics.getCurrentVariator() != null && Statics.TheActionState.getState()==ActionState.SETVARIATION) {
			Statics.getCurrentVariator().draw(g);
		}
	}
	public void comeAllToPhenoPoint() {
		for(MetaPoint P : allPoints) {P.comeToPhenoPoint();}
	}
	
	public Calc.MetaSet getAllMetas(Class clasz) {
		if (MetaPoint.class.isAssignableFrom(clasz)) {return allPoints;}
		if (MetaShape.class.isAssignableFrom(clasz)) {return allShapes;}
		if (Variator.class.isAssignableFrom(clasz)) {return allVariators;}
		if (MetaStructure.class.isAssignableFrom(clasz)) {return allStructures;}
		if (Gene.class.isAssignableFrom(clasz)) {return allGenes;}
		else {return null;}
	}
//	public void setHighlighted(MetaObject obj) {
//		if (MetaPoint.class.isAssignableFrom(obj.getClass())) {HighlightedPoint = (MetaPoint) obj;}
//		else if (MetaShape.class.isAssignableFrom(obj.getClass())) {HighlightedShape = (MetaShape) obj;}
//		else if (Variator.class.isAssignableFrom(obj.getClass())) {HighlightedVariator = (Variator) obj;}
//		else if (MetaStructure.class.isAssignableFrom(obj.getClass())) {HighlightedStructure = (MetaStructure) obj;}
//	}

	public void addMeta(MetaObject obj) {
		getAllMetas(obj.getClass()).getMembers().add(obj);
	}
	public void removeMeta(MetaObject obj) {
		getAllMetas(obj.getClass()).getMembers().remove(obj);
	}

	public void setHighlightedMeta(MetaObject obj) {highlightedMeta = obj;}
	public void setEditingMeta(MetaObject obj) {editingMeta = obj;}
	
	private void addSelectedPoints(MetaPoint P) {
		SelectedPoints.add(P);
		//int k = Calc.findInVector(SelectedPoints, P);
		//if (k != -1) {SelectedPoints = (MetaPoint[]) Calc.subtractFromArray(SelectedPoints, k);}
		//else {SelectedPoints =  Calc.addToArray(SelectedPoints, P);}
		//Calc.printArray(SelectedPoints);
	}
	private void addSelectedShapes(MetaShape s) {SelectedShapes.add(s);}
	public ArrayList<MetaPoint> getSelectedPoints() {return SelectedPoints;}
	public ArrayList<MetaShape> getSelectedShapes() {return SelectedShapes;}

	public void moveUp(MetaObject obj) {getAllMetas(obj.getClass()).moveUp(obj);}
	public void moveDown(MetaObject obj) {getAllMetas(obj.getClass()).moveDown(obj);}
	
//	public void movePointUp(int p) {moveInArray(AllPoints, p, true);}
//	public void movePointDown(int p) {moveInArray(AllPoints, p, false);}
//	public void moveVariatorUp(int p) {moveInArray(AllVariators, p, true);}
//	public void moveVariatorDown(int p) {moveInArray(AllVariators, p, false);}
//	public void moveShapeUp(int p) {moveInArray(AllShapes, p, true);}
//	public void moveShapeDown(int p) {moveInArray(AllShapes, p, false);}


	public MetaPoint nearestPoint(int x, int y) {
		double mindist = Double.MAX_VALUE; MetaPoint result = null;
		for (MetaPoint p : allPoints.getMembers()) {
			double dist = Math.sqrt(Math.pow(x - p.getX(),2) + Math.pow(y - p.getY(),2));
			if (dist < mindist) {mindist = dist; result = p;}
		}
		return result;
	}
	public MetaShape nearestShape(int x, int y) {
		double mindist = Double.MAX_VALUE; Point c;
		MetaShape result = null;
		for (MetaShape ms : allShapes.getMembers()) {
			c = ms.center();
			double dist = Math.sqrt(Math.pow(x - c.x,2) + Math.pow(y - c.y,2));
			if (dist < mindist) {mindist = dist; result = ms;}
		}
		return result;
	}
	
	public void orderAllTheDamnPointsCorrectly() {
		MetaPoint lastPoint; boolean changed = true;
		while (changed) {
			lastPoint = null; changed = false;
			for (MetaPoint mp : allPoints) {
				if (lastPoint != null && lastPoint.isDescendedFrom(mp)) {
					System.out.println(lastPoint + " switched with " + mp);
					lastPoint.moveDown();
					changed = true;
					break;
				}
				lastPoint = mp;
			}
		}
	}
	
	
	public void restore() {
		Statics.TheActionState.setState(ActionState.DEFAULT);
		Statics.setMessage();
		HighlightedPoint = null;
		HighlightedShape = null;
		SelectedPoints.clear();
		SelectedShapes.clear();
		Statics.resetStep();
		Statics.MP.getRightMenu().showSelectionPanels();
		forcePaint();
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		int mx = e.getX();   int my = e.getY();
		mousePoint.setLocation(mx, my);
		switch (Statics.TheActionState.getState()) {
		case ActionState.SETVARIATION: Statics.getCurrentVariator().setVFinal(mousePoint); break;
		default: break;
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		int mx = e.getX();   int my = e.getY();
		mousePoint.setLocation(mx, my);
		switch (Statics.TheActionState.getState()) {
		case ActionState.CHOOSEPOINT: HighlightedPoint = nearestPoint(mx, my); break;
		case ActionState.CHOOSESHAPE: HighlightedShape = nearestShape(mx, my); break;
		default: break;
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mx = e.getX();   int my = e.getY();
		switch (Statics.TheActionState.getState()) {
		case ActionState.DEFAULT: break;
		case ActionState.NEWPOINT: addMeta(new MetaPoint(mx, my)); Statics.MP.getRightMenu().showSelectionPanels(); break;
		case ActionState.CHOOSEPOINT: addSelectedPoints(HighlightedPoint); Statics.doCommand(); break;
		case ActionState.CHOOSESHAPE: addSelectedShapes(HighlightedShape); break;
		default: break;
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX();   int my = e.getY();
		mousePoint.setLocation(mx, my);
		switch (Statics.TheActionState.getState()) {
		case ActionState.SETVARIATION: Statics.getCurrentVariator().setVInit(mousePoint); break;
		default: break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
