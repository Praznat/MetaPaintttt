package GUI;

import javax.swing.*;

import shapes.*;
import variators.*;
import main.*;

public class Popupper {

	private static PopupFactory factory = PopupFactory.getSharedInstance();
	
	public static void newPopup(JPanel panel) {
		JFrame frame = Statics.MP.getFrame();
		factory.getPopup(frame, panel, (frame.getWidth() - panel.getWidth()) / 2, (frame.getHeight() - panel.getHeight()) / 2);
	}

	public static void variatorChoice() {
		JPopupMenu pum = new JPopupMenu("Choose Variator Type");
		AMenuItem menuItem1 = AMenuItem.newMenuItem("Horizontal", SpaceVariator.createNew());//, ActionState.CHOOSEPOINT);
		AMenuItem menuItem2 = AMenuItem.newMenuItem("Vertical", SpaceVariator.createNew());
		AMenuItem menuItem3 = AMenuItem.newMenuItem("Distance", SpaceVariator.createNew());
		AMenuItem menuItem4 = AMenuItem.newMenuItem("Angle", SpaceVariator.createNew());
		pum.add(menuItem1);pum.add(menuItem2);pum.add(menuItem3);pum.add(menuItem4);

		pum.show(Statics.MP.getRightMenu(), Statics.MP.getRightMenu().getComponent(0).getX(), 0);
		Statics.TheActionState.setState(ActionState.CHOOSEVARIATOR);
	}
	public static void metaShapeChoice() {
		JPopupMenu pum = new JPopupMenu("Choose Shape Type");
		AMenuItem menuItem1 = AMenuItem.newMenuItem("Polygon", MetaPolygon.createNew());
		AMenuItem menuItem2 = AMenuItem.newMenuItem("Circle", MetaCircle.createNew());
		AMenuItem menuItem3 = AMenuItem.newMenuItem("Ellipse", MetaEllipse.createNew());
		AMenuItem menuItem4 = AMenuItem.newMenuItem("Quadratic", MetaCurve.createNew());
		AMenuItem menuItem5 = AMenuItem.newMenuItem("Dirty Bezier", MetaCurve.createNew());
		AMenuItem menuItem6 = AMenuItem.newMenuItem("Roundoff", MetaBridge.createNew());
		pum.add(menuItem1);pum.add(menuItem2);pum.add(menuItem3);pum.add(menuItem4);pum.add(menuItem5);pum.add(menuItem6);

		pum.show(Statics.MP.getRightMenu(), Statics.MP.getRightMenu().getComponent(0).getX(), 0);
		Statics.TheActionState.setState(ActionState.CHOOSESHAPE);
	}
	

}
