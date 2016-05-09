package GUI;

import java.awt.event.*;
import javax.swing.*;

import main.*;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel implements ActionListener {
	private JLabel label;
	private JButton button;
	private Command buttonAction;
	private boolean clicked;
	
	public StatusPanel() {
		super();
		label = new JLabel();
		button = new JButton();
		add(label);
		add(button);
		button.setVisible(false);
		button.addActionListener(this);
	}
	
	public void setLabel(String S) {button.setVisible(false);   label.setText(S);}
	public void setButton(String S, Command C) {button.setText(S);   buttonAction = C;   button.setVisible(true);}
	public boolean beenClicked() {return clicked;}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Calc.p("clicked");
		clicked = true;
		buttonAction.doit();
		clicked = false;
	}
}
