package GUI;

import java.awt.event.*;

import javax.swing.JMenuItem;

import main.*;

@SuppressWarnings("serial")
public class AMenuItem extends JMenuItem {

	public AMenuItem(String s) {super(s);}
	
	

	public static AMenuItem newMenuItem(String s, final Command A) {
		AMenuItem AMI = new AMenuItem(s);
		AMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {A.doit();}
		});
		return AMI;
	}
	public static AMenuItem newMenuItem(String s, final Command A, final int nextActionState) {
		AMenuItem AMI = new AMenuItem(s);
		AMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Statics.setCommand(A);
				if (nextActionState == ActionState.DOACTION) {A.doit();}
				else {Statics.TheActionState.setState(nextActionState);}
			}
		});
		return AMI;
	}
}
