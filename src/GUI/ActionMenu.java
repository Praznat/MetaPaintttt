package GUI;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import main.*;
import variators.*;

@SuppressWarnings("serial")
public class ActionMenu extends JPanel implements ActionListener {
	private final JButton SaveButton = new JButton("Save");
	private final JButton LoadButton = new JButton("Load");
	private final JButton RandomizeButton = new JButton("Randomize");
	private final JButton FunButton = new JButton("Fun Time");
	private final JFileChooser fileChooser = new JFileChooser();
	
	public ActionMenu() {
		super();
		JButton[] buttons = {SaveButton, LoadButton, RandomizeButton, FunButton};
		for (JButton B : buttons) {
			add(B);
			B.addActionListener(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Statics.MP.getTheSpace().restore();

		if (e.getActionCommand().equals("Save")) {
			int returnVal = fileChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String fileName = fileChooser.getSelectedFile().toString();
				IOManager.saveFile(fileName.endsWith(".maf") ? fileName : (fileName + ".maf"));
			}
		}
		else if (e.getActionCommand().equals("Load")) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				IOManager.loadFile(file.toString());
				Statics.MP.getTheSpace().orderAllTheDamnPointsCorrectly();
			}
		}
		else if (e.getActionCommand().equals("Randomize")) {
			for(Object G : Statics.getAll(Gene.class)) {((Gene)G).randomizeGenotype();}
			Statics.MP.getTheSpace().comeAllToPhenoPoint();
			Statics.MP.getTheSpace().comeAllToPhenoPoint();
			Statics.MP.getTheSpace().restore();
		}
		else if (e.getActionCommand().equals("Fun Time")) {
			try {
				Statics.MP.getTheSpace().setDrawBuildersOK(false);
				for (int i = 0; i < 30; i++) {
					for(Object G : Statics.getAll(Gene.class)) {((Gene)G).randomizeGenotype();}
					Statics.MP.getTheSpace().forcePaint();
					Thread.sleep(100);
				}
				Statics.MP.getTheSpace().setDrawBuildersOK(true);
			}
			catch (Exception error) {}
		}
	}
}
