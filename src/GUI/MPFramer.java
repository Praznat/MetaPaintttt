package GUI;
import java.awt.Container;

import javax.swing.*;
import javax.swing.border.*;

import main.*;


public class MPFramer {
	private JFrame TheFrame;
	private JPanel TheSpace, TopMenu, BottomMenu, LeftMenu, RightMenu;
	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	private static final int SPACEWIDTH = 300;
	private static final int SPACEHEIGHT = 300;
	private static final int TMHEIGHT = 50;
	private static final int BMHEIGHT = 30;
	private static final int LMWIDTH = 150;
	private static final int RMWIDTH = 150;
	
	public MPFramer() {
		TheFrame = new JFrame("METAPAINTER");
		TheFrame.setVisible(true);
		TheFrame.setSize(WIDTH,HEIGHT);
		FrameCP().setSize(WIDTH,HEIGHT);
		FrameCP().setLayout(null);
		
		
		Border defaultborder = new BevelBorder(1);
		TheSpace = new Space(SPACEWIDTH, SPACEHEIGHT);
		TopMenu = new ActionMenu();
		BottomMenu = new StatusPanel();
		LeftMenu = new JPanel();
		RightMenu = new SelectionPanel();
		JPanel[] panels = {TheSpace, TopMenu, BottomMenu, LeftMenu, RightMenu};
		for (JPanel p : panels) {
			FrameCP().add(p);
			p.setBorder(defaultborder);
		}
		TopMenu.setBounds(0, 0, FrameCP().getWidth(), TMHEIGHT);
		BottomMenu.setBounds(0, FrameCP().getHeight() - BMHEIGHT, FrameCP().getWidth(), BMHEIGHT);
		Calc.p(""+BottomMenu.getY());
		LeftMenu.setBounds(0, TMHEIGHT, LMWIDTH, FrameCP().getHeight() - TMHEIGHT - BMHEIGHT);
		RightMenu.setBounds(FrameCP().getWidth() - RMWIDTH, TMHEIGHT, RMWIDTH, FrameCP().getHeight() - TMHEIGHT - BMHEIGHT);
		TheSpace.setBounds(LMWIDTH, TMHEIGHT, FrameCP().getWidth() - LMWIDTH - RMWIDTH, FrameCP().getHeight() - TMHEIGHT - BMHEIGHT);
		LeftMenu.setLayout(new BoxLayout(LeftMenu, BoxLayout.Y_AXIS));
		Editors.attachTo(LeftMenu);
	}
	
	public int spaceHypotenuseSize() {return (int) Math.sqrt(SPACEWIDTH * SPACEWIDTH + SPACEHEIGHT * SPACEHEIGHT);}

	public Space getTheSpace() {return (Space)TheSpace;}
	public StatusPanel getBottomMenu() {return (StatusPanel) BottomMenu;}
	public ActionMenu getTopMenu() {return (ActionMenu) TopMenu;}
	public SelectionPanel getRightMenu() {return (SelectionPanel) RightMenu;}
	public JFrame getFrame() {return TheFrame;}
	private Container FrameCP() {return TheFrame.getContentPane();}
}
