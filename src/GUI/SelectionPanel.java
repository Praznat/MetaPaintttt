package GUI;

import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

import main.*;
import points.MetaPoint;
import shapes.MetaShape;
import variators.*;

@SuppressWarnings("serial")
public class SelectionPanel extends JPanel implements ActionListener {
	private final JComboBox dropdown = new JComboBox(new String[] {"Points", "Variators", "Shapes", "Structures", "Genes"});
	private final JButton createButton = new JButton("Spawn");
	private final JPanel topPanel = new JPanel();
	private final JPanel selectedObjects = new JPanel();
	private final JScrollPane scrollpane = new JScrollPane();
	
	public SelectionPanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		topPanel.add(dropdown);
		topPanel.add(createButton);
		topPanel.setLayout(new GridLayout(0, 1));
		topPanel.setMaximumSize(topPanel.getPreferredSize());
		this.add(topPanel);
		dropdown.addActionListener(this);
		createButton.addActionListener(this);
		selectedObjects.setLayout(new BoxLayout(selectedObjects, BoxLayout.Y_AXIS));
		scrollpane.getViewport().add(selectedObjects);
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollpane);
	}
	
	public JComboBox getDropdown() {return dropdown;}
	
	public void showSelectionPanels() {
		selectedObjects.removeAll();
		String selection = (String) dropdown.getSelectedItem();
		Calc.MetaSet<MetaObject> m = null;
		if (selection.equals("Points")) {
			m = Statics.getAll(MetaPoint.class);
		}
		else if (selection.equals("Variators")) {
			m = Statics.getAll(Variator.class);
		}
		else if (selection.equals("Shapes")) {
			m = Statics.getAll(MetaShape.class);
		}
		else if (selection.equals("Structures")) {
			m = Statics.getAll(MetaStructure.class);
		}
		else if (selection.equals("Genes")) {
			m = Statics.getAll(Gene.class);
		}
		for (MetaObject o : m) {selectedObjects.add(o.getSPanel());}
		revalidate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String selection = (String) dropdown.getSelectedItem();
		Statics.MP.getTheSpace().restore();
		if (e.getActionCommand().equals("Spawn")) {
			if (selection.equals("Points")) {
				Statics.TheActionState.setState(ActionState.NEWPOINT);
			}
			else if (selection.equals("Variators")) {
				if (Statics.MP.getTheSpace().getAllMetas(MetaPoint.class).getMembers().size() < 1) {return;}
				Statics.TheActionState.setState(ActionState.NEWVARIATOR); SpaceVariator.createNew().doit();
			}
			else if (selection.equals("Shapes")) {
				if (Statics.MP.getTheSpace().getAllMetas(MetaPoint.class).getMembers().size() < 1) {return;}
				Statics.TheActionState.setState(ActionState.NEWSHAPE); Popupper.metaShapeChoice();
			}
			else if (selection.equals("Structures")) {
				if (Statics.MP.getTheSpace().getAllMetas(MetaShape.class).getMembers().size() < 1) {return;}
				Statics.TheActionState.setState(ActionState.CHOOSESHAPE);
				AbstractCommands.waitForDone(MetaStructure.FACTORY);
			}
		}
		else {	// just change in dropdown
			showSelectionPanels();
		}
	}
	
}
