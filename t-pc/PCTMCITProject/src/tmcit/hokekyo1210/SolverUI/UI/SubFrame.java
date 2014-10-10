package tmcit.hokekyo1210.SolverUI.UI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.hokekyo1210.SolverUI.MyBufferedImage;

public class SubFrame extends JFrame implements KeyListener{

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
	private List<JLabel> labels = new ArrayList<JLabel>();

	private void initUI() {

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle(title);
		this.setSize(803, 450);
		this.setResizable(false);
		
		this.addKeyListener(this);

		mainPanel = new JPanel(null);
		mainPanel.setBounds(0, 0, 0, 0);
		this.add(mainPanel);
	}

	private int row;
	private int column;
	private List<MyBufferedImage> images;

	public void setProblem(int width,int height,int row,int column,List<MyBufferedImage> images){
		for(JLabel label:labels){
			mainPanel.remove(label);
		}
		labels.clear();
		this.row = row;
		this.column = column;
		this.images = images;

		mainPanel.setSize(width, height);
		this.setSize(width+10, height+20);
		this.setVisible(true);

		int partw = width/row;
		int parth = height/column;

		for(int y = 0;y<column;y++){
			for(int x = 0;x<row;x++){
				JLabel label= new JLabel();
				label.setBounds(x*partw, y*parth, partw, parth);
				mainPanel.add(label);
				labels.add(label);
			}
		}
	}
	
	private int all = 0;
	private int now = 0;

	public void setPart(int x,int y,int index){
		if(index==-1){return;}
		labels.get(x+y*row).setIcon(new ImageIcon(images.get(index).getImage()));
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		char c = event.getKeyChar();
		if(c=='a'){
			System.out.println("a");
		}else if(c=='d'){
			System.out.println("d");
		}else if(c=='w'){
		}else if(c=='s'){
		}else if(c=='\n'){
			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	

}
