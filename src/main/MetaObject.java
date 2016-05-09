package main;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import GUI.Editors;

public abstract class MetaObject implements Comparable<MetaObject> {
	protected static Color paintColor = Color.WHITE;
	
	public static void setPaintColor(Color c) {paintColor = c;}

	protected final SPanel spanel = new SPanel();
	
	@Override
	public int compareTo(MetaObject o) {
		System.out.println("comparing " + o + " and " + this);
		return (int) Math.signum(o.getOrder() - this.getOrder());
	}
	public abstract void draw(Graphics g2d);
	public abstract void drawHighlight(Graphics g);
	
	public String cid() {return IOManager.getMetaCharId(this) + getOrder();}

	public final int getOrder() {return Statics.MP.getTheSpace().getAllMetas(this.getClass()).getMembers().indexOf(this);}
	@SuppressWarnings("unchecked")
	public final void moveUp() {Statics.MP.getTheSpace().getAllMetas(this.getClass()).moveUp(this);}
	@SuppressWarnings("unchecked")
	public final void moveDown() {Statics.MP.getTheSpace().getAllMetas(this.getClass()).moveDown(this);}
	
	public final void setName(String s) {spanel.setNameField(s);}
	public final String getName() {return spanel.nameField.getText();}
	@Override
	public String toString() {return getName();}
	
	public SPanel getSPanel() {return spanel;}
	
	public abstract String saveString();

	protected static int i(String i) {return Integer.parseInt(i);}
	protected static interface IOFactory<T extends MetaObject> {
		public T createFromString(String name, String[] in);
	}

	@SuppressWarnings("serial")
	public class MPanel extends JPanel implements MouseListener, MouseMotionListener {
		protected final JTextField nameField = new Calc.NamerLabel(MetaObject.this, this);
		public MPanel() {addMouseListener(this); addMouseMotionListener(this);}
		public void mouseDragged(MouseEvent arg0) {}
		public void mouseMoved(MouseEvent arg0) {}
		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {
			this.setBackground(Statics.PANELHIGHLIGHTCOLOR);
			Statics.MP.getTheSpace().setHighlightedMeta(MetaObject.this);
			Statics.MP.getTheSpace().repaint();
		}
		public void mouseExited(MouseEvent arg0) {
			this.setBackground(Statics.PANELDEFAULTCOLOR);
			Statics.MP.getTheSpace().setHighlightedMeta(null);
			Statics.MP.getTheSpace().repaint();
		}
		public void mousePressed(MouseEvent arg0) {
			Editors.display(MetaObject.this);
		}
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	@SuppressWarnings("serial")
	public class SPanel extends MPanel {
		public SPanel() {
			super();
			this.setBackground(Statics.PANELDEFAULTCOLOR);
			nameField.setText(MetaObject.this.getClass().toString().substring(MetaObject.this.getClass().toString().indexOf(".")+1));
			add(nameField);
			this.setBorder(BorderFactory.createBevelBorder(0));
			this.setMaximumSize(new Dimension(this.getMaximumSize().width, this.getPreferredSize().height));
		}
		public void setNameField(String s) {nameField.setText(s);}
	}
	
	
	
}
