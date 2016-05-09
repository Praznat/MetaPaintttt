package main;


public class AbstractCommands {
	public static class combinedCommand implements Command {
		private Command[] commands;
		public combinedCommand(Command... C) {commands = C;}
		public void doit() {for (Command C : commands) {C.doit();}}
	}
	public static class doStaticCommand implements Command {
		public void doit() {Statics.doCommand();}
	}
	public static class waitForNumPoints implements Command {
		private int numPoints;
		private Command nextCommand;
		public waitForNumPoints(int N, Command C) {numPoints = N; nextCommand = C;}
		public void doit() {
			if(Statics.MP.getTheSpace().getSelectedPoints().size() == numPoints) {nextCommand.doit();}
		}
	}

	public static class waitForDoneClick implements Command {
		private Command nextCommand;
		public waitForDoneClick(Command C) {nextCommand = C;}
		public void doit() {
			if(Statics.MP.getBottomMenu().beenClicked()) {nextCommand.doit();}
		}
	}

	public static class waitForClick implements Command {
		private Command nextCommand;
		public waitForClick(Command C) {nextCommand = C;}
		public void doit() {nextCommand.doit();}
	}

	public static void selectPoints(Command nextCommand, int N) {
		if (N <= 0) {selectPoints(nextCommand); return;}
		Statics.TheActionState.setState(ActionState.CHOOSEPOINT);
		Statics.setCommand(new waitForNumPoints(N, nextCommand));
	}
	public static void selectPoints(Command nextCommand) {
		Statics.setButton("DONE", new doStaticCommand());
		Statics.TheActionState.setState(ActionState.CHOOSEPOINT);
		Statics.setCommand(new waitForDoneClick(nextCommand));
	}
	public static void setVariance(Command nextCommand) {
		Statics.setButton("DONE", new doStaticCommand());
		Statics.TheActionState.setState(ActionState.SETVARIATION);
		Statics.setCommand(new waitForDoneClick(nextCommand));
	}
	public static void waitForDone(Command nextCommand) {
		Statics.setButton("DONE", new doStaticCommand());
		Statics.setCommand(new waitForDoneClick(nextCommand));
	}
	
	
}
