package GUI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import points.MetaPoint;
import shapes.MetaShape;

import variators.*;

import main.*;
import main.Calc.ParameterCommand;

public class Editors {

	private static final BlankPane BLANKPANE = new BlankPane();
	private static final PointPane POINTPANE = new PointPane();
	private static final GenePane GENEPANE = new GenePane();
	private static final LinkedGenePane LINKEDGENEPANE = new LinkedGenePane();
	private static final CompositePane COMPOSITEPANE = new CompositePane();
	
	private static final AllEditorPane CARDHOLDER = new AllEditorPane(BLANKPANE, POINTPANE, GENEPANE, LINKEDGENEPANE, COMPOSITEPANE);
	
	private static final Stack<MetaObject> history = new Stack<MetaObject>();
	private static final Stack<MetaObject> forwards = new Stack<MetaObject>();
	
	public static void display(MetaObject o) {display(o, true, true);}
	private static void display(MetaObject o, boolean storeInHistory, boolean clearForwards) {
		if (o instanceof Gene) {
			if (((Gene) o).isLinked()) {CARDHOLDER.display(LINKEDGENEPANE); LINKEDGENEPANE.display(o);}
			else {CARDHOLDER.display(GENEPANE); GENEPANE.display(o);}
		}
		else if (o instanceof MetaPoint && ((MetaPoint) o).getVariators().isEmpty()) {CARDHOLDER.display(POINTPANE); POINTPANE.display(o);}
		else {CARDHOLDER.display(COMPOSITEPANE); COMPOSITEPANE.display(o);}
		if (storeInHistory) {history.push(o);}
		if (clearForwards) {forwards.removeAllElements();}
	}
	public static void attachTo(JPanel parent) {parent.add(CARDHOLDER);}
	
	@SuppressWarnings("serial")
	private static abstract class EditorPane extends JPanel {
		protected abstract String desc();
		protected MetaObject currentMetaObject;
		private final Calc.NamerLabel namePart = new Calc.NamerLabel(currentMetaObject, null);
		private final JPanel nameContainer = new JPanel();
		private final JPanel header = new JPanel();
		protected EditorPane() {
			super();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			nameContainer.add(namePart);
			header.setLayout(new BorderLayout());
			header.add(nameContainer, BorderLayout.NORTH);
			header.add(new Calc.ActionButton("<") {
				public void actionPerformed(ActionEvent e) {
					if (history.size() > 1) {forwards.push(history.pop()); Editors.display(history.peek(), false, false);}
				}}, BorderLayout.WEST);
			header.add(new Calc.ActionButton(">") {
				public void actionPerformed(ActionEvent e) {
					if (!forwards.empty()) {Editors.display(forwards.pop(), true, false);}
				}}, BorderLayout.EAST);
			header.setMaximumSize(namePart.getPreferredSize());
			this.add(header);
		}
		protected void display(MetaObject mo) {
			currentMetaObject = mo;
			namePart.setRef(mo);
			header.setMaximumSize(header.getPreferredSize());
			namePart.setPreferredSize(new Dimension(this.getSize().width, namePart.getPreferredSize().height));
		}
	}

	@SuppressWarnings("serial")
	private static final class BlankPane extends EditorPane {
		@Override
		protected String desc() {return "BLANK";}
	}
	
	@SuppressWarnings("serial")
	private static final class GenePane extends EditorPane {
		private static Gene linkSeeker;
		@Override
		protected String desc() {return "GENE";}
		private Calc.ActionButton dependButton = new Calc.ActionButton("DEPEND") {
			public void actionPerformed(ActionEvent e) {
				if (getText().equals("SELECT")) {
					if (linkSeeker != null && currentMetaObject != linkSeeker) {
						linkSeeker.createDependencyRule((Gene)currentMetaObject);
						Editors.display(linkSeeker);
						linkSeeker = null;
						setText("DEPEND");
					}	else {} // deny
				}
				else {
					Statics.setMessage("Choose gene to link to and press SELECT in profile");
					Statics.MP.getRightMenu().getDropdown().setSelectedIndex(4);
					if (!Statics.MP.getRightMenu().getDropdown().getSelectedItem().equals("Genes")) throw new IllegalStateException();
					Statics.MP.getRightMenu().showSelectionPanels();
					linkSeeker = (Gene) currentMetaObject;
					setText("SELECT");
				}
			}
		};
		private JSlider minSlider = new JSlider();
		private JSlider maxSlider = new JSlider();
		private JSlider valSlider = new JSlider();
		private JPanel labelsPanel = new JPanel();
		private JPanel slidersPanel = new JPanel();
		private boolean updateable = true;
		private GenePane() {
			super();
			labelsPanel.setLayout(new BorderLayout());
			labelsPanel.add(new Calc.PlainLabel("MIN"), BorderLayout.WEST);
			labelsPanel.add(new Calc.PlainLabel("VAL"), BorderLayout.CENTER);
			labelsPanel.add(new Calc.PlainLabel("MAX"), BorderLayout.EAST);
			slidersPanel.setLayout(new BorderLayout());
			minSlider.setOrientation(SwingConstants.VERTICAL);
			valSlider.setOrientation(SwingConstants.VERTICAL);
			maxSlider.setOrientation(SwingConstants.VERTICAL);
			minSlider.setUI(new Calc.StoppingSliderUI(minSlider));
			valSlider.setUI(new Calc.StoppingSliderUI(valSlider));
			maxSlider.setUI(new Calc.StoppingSliderUI(maxSlider));
			slidersPanel.add(minSlider, BorderLayout.WEST);
			slidersPanel.add(valSlider, BorderLayout.CENTER);
			slidersPanel.add(maxSlider, BorderLayout.EAST);
			minSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if (!updateable) {return;}
					final int max = maxSlider.getValue();
					if(minSlider.getValue() > max) {
						minSlider.setValue(max);
						((Calc.StoppingSliderUI)minSlider.getUI()).stopAt((Calc.StoppingSliderUI)maxSlider.getUI());
					}
					((Gene) currentMetaObject).setMinval(minSlider.getValue());
					Statics.MP.getTheSpace().repaint();
				}
			});
			valSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					final int max = maxSlider.getValue();
					final int min = minSlider.getValue();
					if(valSlider.getValue() > max) {
						valSlider.setValue(max);
						((Calc.StoppingSliderUI)valSlider.getUI()).stopAt((Calc.StoppingSliderUI)maxSlider.getUI());
					}
					else if(valSlider.getValue() < min) {
						valSlider.setValue(min);
						((Calc.StoppingSliderUI)valSlider.getUI()).stopAt((Calc.StoppingSliderUI)minSlider.getUI());
					}
					((Gene) currentMetaObject).setVal(valSlider.getValue());
					Statics.MP.getTheSpace().repaint();
				}
			});
			maxSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if (!updateable) {return;}
					final int min = minSlider.getValue();
					if(maxSlider.getValue() < min) {
						maxSlider.setValue(min);
						((Calc.StoppingSliderUI)maxSlider.getUI()).stopAt((Calc.StoppingSliderUI)minSlider.getUI());
					}
					((Gene) currentMetaObject).setMaxval(maxSlider.getValue());
					Statics.MP.getTheSpace().repaint();
				}
			});
			this.add(dependButton);
			this.add(labelsPanel);
			this.add(slidersPanel);
		}
		@Override
		protected void display(MetaObject mo) {
			Gene gene = (Gene) mo;
			super.display(gene);
			setSlidersMinMax(gene.getVal(), gene.getMinval(), gene.getMaxval(), gene.getAbsMin(), gene.getAbsMax());
		}
		private void setSlidersMinMax(int val, int min, int max, int absmin, int absmax) {
			updateable = false;
			minSlider.setMinimum(absmin);   minSlider.setMaximum(absmax);
			valSlider.setMinimum(absmin);   valSlider.setMaximum(absmax);
			maxSlider.setMinimum(absmin);   maxSlider.setMaximum(absmax);
			minSlider.setValue(min);		maxSlider.setValue(max);
			valSlider.setValue(val);
			updateable = true;
		}
	}
	

	@SuppressWarnings("serial")
	private static final class LinkedGenePane extends EditorPane {
		protected String desc() {return "LINKEDGENE";}
		
		private final NumberBox multBox = new NumberBox(this, new Calc.ParameterCommand<Double>() {
			public void doit(Double i) {((Gene)currentMetaObject).dependency.setMultFactor(i);}
		});
		private final NumberBox addBox = new NumberBox(this, new Calc.ParameterCommand<Double>() {
			public void doit(Double i) {
				((Gene)currentMetaObject).dependency.setAddConstant((int)Math.round(i));
				Statics.MP.getTheSpace().repaint();
			}
		});
		private final JCheckBox wrapBox = new JCheckBox("Wrap?");
		private final JPanel multPanel = new JPanel(new BorderLayout());
		private final JPanel addPanel = new JPanel(new BorderLayout());
		private final JPanel wrapPanel = new JPanel(new BorderLayout());
		
		private LinkedGenePane() {
			super();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			wrapBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					((Gene)currentMetaObject).dependency.setWrap(wrapBox.isSelected());
				}
			});
			multPanel.add(new Calc.PlainLabel("*"), BorderLayout.NORTH);
			multPanel.add(multBox, BorderLayout.SOUTH);
			addPanel.add(new Calc.PlainLabel("+"), BorderLayout.NORTH);
			addPanel.add(addBox, BorderLayout.SOUTH);
			wrapPanel.add(wrapBox, BorderLayout.SOUTH);
			this.add(addPanel);   this.add(multPanel);   this.add(wrapPanel);
			multPanel.setMaximumSize(multPanel.getPreferredSize());
			addPanel.setMaximumSize(addPanel.getPreferredSize());
			wrapPanel.setMaximumSize(wrapPanel.getPreferredSize());
		}
		@Override
		protected void display(MetaObject mo) {
			super.display(mo);
			multBox.setText(((Gene)mo).dependency.getMultFactor());
			addBox.setText(((Gene)mo).dependency.getAddConstant());
			wrapBox.setSelected(((Gene)mo).dependency.getWrapEdges());
		}
	}
	
	@SuppressWarnings("serial")
	private static final class CompositePane extends EditorPane {
		private final JScrollPane scrollpane = new JScrollPane();
		private final JPanel selectedObjects = new JPanel();
		@Override
		protected String desc() {return "COMPOSITE";}

		private CompositePane() {
			super();
			selectedObjects.setLayout(new BoxLayout(selectedObjects, BoxLayout.Y_AXIS));
			scrollpane.getViewport().add(selectedObjects);
			scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			this.add(scrollpane);
		}
		@Override
		protected void display(MetaObject mo) {
			super.display(mo);
			selectedObjects.removeAll();
			for (MetaObject member : getMembers(mo)) {
				selectedObjects.add(member.getSPanel());
			}
			revalidate();
			repaint();
		}
		private Iterable<MetaObject> getMembers(MetaObject mo) {
			ArrayList<MetaObject> members = new ArrayList<MetaObject>();
			if (mo instanceof MetaPoint) {
				for (Variator v : ((MetaPoint) mo).getVariators()) {members.add(v);}
			}
			if (mo instanceof Variator) {
				Variator v = (Variator)mo;
				for (Gene g : v.getGenes()) {members.add(g);}
				members.add(v.getAnchorPoint());
				members.add(v.getDependentPoint());
			}
			else if (mo instanceof MetaShape) {
				for (MetaPoint p : ((MetaShape) mo).getPoints()) {members.add(p);}
			}
			else if (mo instanceof MetaStructure) {
				for (Gene g : ((MetaStructure) mo).getGenes()) {members.add(g);}
				for (MetaShape s : ((MetaStructure) mo).getShapes()) {members.add(s);}
			}
			return members;
		}
	}
	
	@SuppressWarnings("serial")
	private static final class PointPane extends EditorPane {
		@Override
		protected String desc() {return "POINT";}
		
		private final NumberBox xBox = new NumberBox(this, new Calc.ParameterCommand<Double>() {
			public void doit(Double i) {((MetaPoint)currentMetaObject).setX(i);}
		});
		private final NumberBox yBox = new NumberBox(this, new Calc.ParameterCommand<Double>() {
			public void doit(Double i) {((MetaPoint)currentMetaObject).setY(i);}
		});
		private final JPanel xPanel = new JPanel(new BorderLayout());
		private final JPanel yPanel = new JPanel(new BorderLayout());

		private PointPane() {
			super();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			xPanel.add(new Calc.PlainLabel("X"), BorderLayout.NORTH);
			xPanel.add(xBox, BorderLayout.SOUTH);
			yPanel.add(new Calc.PlainLabel("Y"), BorderLayout.NORTH);
			yPanel.add(yBox, BorderLayout.SOUTH);
			this.add(xPanel);   this.add(yPanel);
			xPanel.setMaximumSize(xPanel.getPreferredSize());
			yPanel.setMaximumSize(yPanel.getPreferredSize());
		}
		@Override
		protected void display(MetaObject mo) {
			super.display(mo);
			xBox.setText(((MetaPoint)mo).getX());
			yBox.setText(((MetaPoint)mo).getY());
		}
	}
	
	@SuppressWarnings("serial")
	private static final class AllEditorPane extends JPanel {
		private AllEditorPane(EditorPane... panes) {
			super(new CardLayout());
			for (EditorPane pane : panes) {this.add(pane, pane.desc());}
		}
		private void display(EditorPane pane) {
			CardLayout cl = (CardLayout) (this.getLayout());
			cl.show(this, pane.desc());
		}
	}
	
	@SuppressWarnings("serial")
	private static final class NumberBox extends JPanel {
		private final JTextField box = new JTextField("0");
		public NumberBox(EditorPane ep, final Calc.ParameterCommand<Double> act) {
			box.getDocument().addDocumentListener(new DocumentListener() {
				public void removeUpdate(DocumentEvent arg0) {doRefresh(act);}
				public void insertUpdate(DocumentEvent arg0) {doRefresh(act);}
				public void changedUpdate(DocumentEvent arg0) {doRefresh(act);}
			});
			this.setLayout(new GridLayout(1,3));
			this.setMaximumSize(new Dimension(this.getMaximumSize().width, ep.getMaximumSize().height*2));
			this.add(new JButton("-") {
				{addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {increment(-1);}
				});}
			});
			this.add(box);
			this.add(new JButton("+") {
				{addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {increment(1);}
				});}
			});
		}
		private void doRefresh(Calc.ParameterCommand<Double> act) {
			try {act.doit(Double.parseDouble(box.getText())); Statics.MP.getTheSpace().repaint();}
			catch (Exception e) {}
		}
		public void setText(double i) {box.setText(String.valueOf(i));}
		private void increment(int i) {
			try{
				double v = i + Double.parseDouble(box.getText());
				this.setText(v);
			}	catch (Exception e) {}
		}
	}
}
