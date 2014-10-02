package tmcit.hokekyo1210.SolverUI;

import tmcit.hokekyo1210.SolverUI.UI.MainFrame;
import tmcit.hokekyo1210.SolverUI.UI.SubFrame;

public class Main {

	private MainFrame frame;
	private SubFrame frame2;

	public Main(){
		launchUI();
		return;
	}

	private void launchUI() {
		frame2 = new SubFrame("");
		frame = new MainFrame("2014PCUI",frame2);
		return;
	}



	public static void main(String[] args) {
		new Main();
	}

}
