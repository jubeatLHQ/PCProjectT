package tmcit.hokekyo1210.SolverUI;

import tmcit.hokekyo1210.SolverUI.UI.MainFrame;
import tmcit.hokekyo1210.SolverUI.UI.SubFrame;

public class Main {

	public static final String TARGET_HOST_POST = "http://localhost/SubmitAnswer";
	public static final String TARGET_HOST_GET = "http://localhost/problem";
	public static final String teamToken = "1";
	public static final String problemID = "00";

	public static final String tmpDir = System.getProperty("user.home")+"/temp";

	public static MainFrame frame;
	public static SubFrame frame2;


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
