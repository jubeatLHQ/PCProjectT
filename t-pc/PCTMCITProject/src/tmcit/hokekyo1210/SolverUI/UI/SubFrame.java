package tmcit.hokekyo1210.SolverUI.UI;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SubFrame extends JFrame{

	private String title;

	public SubFrame(String title){
		this.title = title;
		try {
			initUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

	private JPanel mainPanel;

	private void initUI() {

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle(title);
		this.setSize(803, 450);
		this.setResizable(false);

		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 0, 0);
		this.add(mainPanel);
	}

	public void setProblem(int width,int height){
		mainPanel.setSize(width, height);
		this.setSize(width+10, height+10);
		this.setVisible(true);
	}

}
