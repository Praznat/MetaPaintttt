package main;

public class ActionState {
	public static final int DEFAULT = 0;
	public static final int NEWPOINT = 1;
	public static final int NEWVARIATOR = 2;
	public static final int NEWSHAPE = 3;
	public static final int CHOOSEPOINT = 4;
	public static final int CHOOSEVARIATOR = 5;
	public static final int CHOOSESHAPE = 6;
	public static final int CHOOSESTRUCTURE = 6;
	public static final int CHOOSEGENE = 6;
	public static final int DOACTION = 9;
	public static final int SETVARIATION = 10;
	
	private int state;

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public boolean isPoint() {return (state == NEWPOINT);}
	public boolean isVariator() {return (state == NEWVARIATOR);}
	public boolean isShape() {return (state == NEWSHAPE);}
}
