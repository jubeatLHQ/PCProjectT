package tmcit.hokekyo1210.SolverUI.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tmcit.hokekyo1210.SolverUI.AlgorithmPicture;
import tmcit.hokekyo1210.SolverUI.Main;
import tmcit.hokekyo1210.SolverUI.Problem;

public class OptionFrame extends JFrame implements ActionListener, ChangeListener{

	private MainFrame main;
	private SubFrame subframe;
	private OptionFrame thisf;

	public static int width = 160;
	public static int height = 440;

	private Color bg = new Color(237, 235, 223, 255);

	public OptionFrame(String title,MainFrame frame,SubFrame subframe){

		this.main = frame;
		this.subframe = subframe;
		this.thisf = this;

		main.optionframe = thisf;

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle(title);
		this.setSize(width,height);
		this.setUndecorated(true);
		this.setResizable(false);
		this.setBackground(bg);
		setLookAndFeel();

		launchPanel();

		moveDefault();
		this.setVisible(true);
	}

	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void moveDefault(){
		this.setLocation(main.getX()+main.getWidth(), main.getY());
	}

	private static final int defValue1 = 115;
	private static final int defValue2 = 40;

	private AlgorithmPicture picSolver;

	public void setProblem(Problem problem){
		subframe.setVisible(false);
		if(problem==null){
			border.setTitle("");
			rowLabel.setText("0");
			columnLabel.setText("0");
			sizeLabel.setText("0*0");
			cCostLabel.setText("0");
			mCostLabel.setText("0");
			cMaxLabel.setText("0");
			rate1box.setEnabled(false);
			rate2box.setEnabled(false);
			slider.setValue(0);
			slider2.setValue(0);
			slider.setEnabled(false);
			slider2.setEnabled(false);
			b1.setEnabled(false);
			combo.setEnabled(false);

			picSolver = null;
		}else{

			if(Main.threads!=null&&Main.threads.isRunning()){return;}

			border.setTitle(problem.name);
			rowLabel.setText(String.valueOf(problem.row));
			columnLabel.setText(String.valueOf(problem.column));
			sizeLabel.setText(String.valueOf(problem.width)+"*"+String.valueOf(problem.height));
			cCostLabel.setText(String.valueOf(problem.cCost));
			mCostLabel.setText(String.valueOf(problem.mCost));
			cMaxLabel.setText(String.valueOf(problem.cMax));
			rate1box.setEnabled(true);
			rate1box.setSelected(true);
			rate2box.setEnabled(true);
			rate2box.setSelected(false);
			slider.setValue(defValue1);
			slider.setEnabled(rate1box.isSelected());
			slider2.setValue(defValue2);
			rate1Label.setText("0.0%");
			rate2Label.setText("0.0%");
			piecesLabel.setText("0_0");
			piecesLabel1.setText("0_0");
			piecesLabel2.setText("0 ps");
			combo.setEnabled(true);

			reloadPanel();

			picSolver = new AlgorithmPicture(problem,subframe);
			reloadProblem();
		}
		moveDefault();
	}

	public void reloadProblem(){
		if(picSolver==null){return;}
		try {
			double rate1 = 85.0+(slider.getValue()/10.0);
			picSolver.start(rate1);
			if(Main.threads!=null&&Main.threads.isRunning()){
			}else{
				b1.setEnabled(true);
			}
		} catch (Exception e) {
			b1.setEnabled(false);
		}
		reloadPanel();

	}

	public void reloadPanel(){
		if(picSolver==null){return;}
		double rate1 = 85.0+(slider.getValue()/10.0);
		double rate2 = 90.0+(slider2.getValue()/10.0);
		rate1Label.setText(String.valueOf(rate1)+"%");
		rate2Label.setText(String.valueOf(rate2)+"%");
		piecesLabel.setText(picSolver.ulpieces+"_"+picSolver.urpieces);
		piecesLabel1.setText(picSolver.dlpieces+"_"+picSolver.drpieces);
		piecesLabel2.setText(picSolver.pieces+" ps");
	}

	private TitledBorder border;
	private JLabel rowLabel;
	private JLabel columnLabel;
	private JLabel sizeLabel;
	private JLabel cCostLabel;
	private JLabel mCostLabel;
	private JLabel cMaxLabel;
	private JSlider slider;
	private JSlider slider2;
	private JCheckBox rate1box;
	private JCheckBox rate2box;
	private JLabel rate1Label;
	private JLabel rate2Label;
	private JLabel piecesLabel;
	private JLabel piecesLabel1;
	private JLabel piecesLabel2;
	public JButton b1;
	public JComboBox<Integer> combo;

	private void launchPanel() {
		JPanel mainPanel = new JPanel(null);
		mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		mainPanel.setBackground(bg);

		JPanel p1 = new JPanel(null);
		p1.setBackground(bg);
		border = new TitledBorder(new LineBorder(Color.DARK_GRAY,2 ,false),"",TitledBorder.CENTER,TitledBorder.TOP);
		border.setTitleFont(new Font("Century", Font.ITALIC, 16));
		p1.setBorder(border);
		p1.setBounds(2, 2, width-4, height-4);

		JLabel tag1 = new JLabel("row:");
		JLabel tag2 = new JLabel("column:");
		JLabel tag3 = new JLabel("size:");
		JLabel tag4 = new JLabel("cost1:");
		JLabel tag5 = new JLabel("cost2:");
		JLabel tag6 = new JLabel("limit:");
		rowLabel = new JLabel("0");
		columnLabel = new JLabel("0");
		sizeLabel = new JLabel("0*0");
		cCostLabel = new JLabel("0");
		mCostLabel = new JLabel("0");
		cMaxLabel = new JLabel("0");

		tag1.setBounds(5, 10, 60, 30);
		tag2.setBounds(5, 40, 60, 30);
		tag3.setBounds(5, 70, 60, 30);
		tag4.setBounds(5, 100, 60, 30);
		tag5.setBounds(5, 130, 60, 30);
		tag6.setBounds(5, 160, 60, 30);
		rowLabel.setBounds(23, 10, 125, 30);
		columnLabel.setBounds(23, 40, 125, 30);
		sizeLabel.setBounds(23, 70, 125, 30);
		cCostLabel.setBounds(23, 100, 125, 30);
		mCostLabel.setBounds(23, 130, 125, 30);
		cMaxLabel.setBounds(23, 160, 125, 30);

		tag1.setVerticalAlignment(JLabel.CENTER);
		tag2.setVerticalAlignment(JLabel.CENTER);
		tag3.setVerticalAlignment(JLabel.CENTER);
		tag4.setVerticalAlignment(JLabel.CENTER);
		tag5.setVerticalAlignment(JLabel.CENTER);
		tag6.setVerticalAlignment(JLabel.CENTER);
		rowLabel.setHorizontalAlignment(JLabel.RIGHT);
		columnLabel.setHorizontalAlignment(JLabel.RIGHT);
		sizeLabel.setHorizontalAlignment(JLabel.RIGHT);
		cCostLabel.setHorizontalAlignment(JLabel.RIGHT);
		mCostLabel.setHorizontalAlignment(JLabel.RIGHT);
		cMaxLabel.setHorizontalAlignment(JLabel.RIGHT);


		tag1.setFont(new Font("Arial", Font.BOLD, 14));
		tag2.setFont(new Font("Arial", Font.BOLD, 14));
		tag3.setFont(new Font("Arial", Font.BOLD, 14));
		tag4.setFont(new Font("Arial", Font.BOLD, 14));
		tag5.setFont(new Font("Arial", Font.BOLD, 14));
		tag6.setFont(new Font("Arial", Font.BOLD, 14));
		rowLabel.setFont(new Font("Arial", Font.BOLD, 14));
		columnLabel.setFont(new Font("Arial", Font.BOLD, 14));
		sizeLabel.setFont(new Font("Arial", Font.BOLD, 14));
		cCostLabel.setFont(new Font("Arial", Font.BOLD, 14));
		mCostLabel.setFont(new Font("Arial", Font.BOLD, 14));
		cMaxLabel.setFont(new Font("Arial", Font.BOLD, 14));

		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		separator.setBounds(7, 195,width-19,5);

		slider = new JSlider(JSlider.VERTICAL,0,150,0);
		slider.setBounds(10, 202, 30, 170);
		slider.setBackground(bg);
		slider.setEnabled(false);
		slider2 = new JSlider(JSlider.VERTICAL,0,100,0);
		slider2.setBounds(120, 202, 30, 170);
		slider2.setBackground(bg);
		slider2.setEnabled(false);
		slider.addChangeListener(this);
		slider2.addChangeListener(this);

		rate1box = new JCheckBox();
		rate2box = new JCheckBox();
		rate1box.setBackground(bg);
		rate2box.setBackground(bg);
		rate1box.setBounds(40, 207, 20, 20);
		rate2box.setBounds(40, 251, 20, 20);
		rate1box.setEnabled(false);
		rate2box.setEnabled(false);
		rate1box.addActionListener(this);
		rate2box.addActionListener(this);

		JLabel tag7 = new JLabel("rate1");
		JLabel tag8 = new JLabel("rate2");
		JLabel tag9 = new JLabel("puzzle");
		rate1Label = new JLabel("0.0%");
		rate2Label = new JLabel("0.0%");
		piecesLabel = new JLabel("0_0");
		piecesLabel1 = new JLabel("0_0");
		piecesLabel2 = new JLabel("0 ps");
		tag7.setHorizontalAlignment(JLabel.CENTER);
		tag8.setHorizontalAlignment(JLabel.CENTER);
		tag9.setHorizontalAlignment(JLabel.CENTER);
		rate1Label.setHorizontalAlignment(JLabel.CENTER);
		rate2Label.setHorizontalAlignment(JLabel.CENTER);
		piecesLabel.setHorizontalAlignment(JLabel.CENTER);
		piecesLabel1.setHorizontalAlignment(JLabel.CENTER);
		piecesLabel2.setHorizontalAlignment(JLabel.RIGHT);
		tag7.setFont(new Font("Arial", Font.BOLD, 14));
		tag8.setFont(new Font("Arial", Font.BOLD, 14));
		tag9.setFont(new Font("Arial", Font.BOLD, 14));
		rate1Label.setFont(new Font("Arial", Font.PLAIN, 14));
		rate2Label.setFont(new Font("Arial", Font.PLAIN, 14));
		piecesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		piecesLabel1.setFont(new Font("Arial", Font.PLAIN, 14));
		piecesLabel2.setFont(new Font("Arial", Font.PLAIN, 14));

		tag7.setBounds(40, 205, 75, 20);
		tag8.setBounds(40, 250, 75, 20);
		tag9.setBounds(40, 295, 75, 20);
		rate1Label.setBounds(40, 225, 75, 20);
		rate2Label.setBounds(40, 270, 75, 20);
		piecesLabel.setBounds(40, 315, 75, 20);
		piecesLabel1.setBounds(40, 335, 75, 20);
		piecesLabel2.setBounds(40, 355, 75, 20);

		JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
		separator2.setBounds(7, 375,width-19,5);
		JSeparator separator3 = new JSeparator(SwingConstants.VERTICAL);
		separator3.setBounds(width/2, 380,5,50);

		Integer[] combodata = {1,2,3,4,5,6,7,8};
		combo = new JComboBox<Integer>(combodata);
		combo.setBackground(bg);
		combo.setBounds(9, 378, 28, 17);
		combo.setEnabled(false);
		combo.setSelectedItem(Integer.valueOf(4));

		JCheckBox box2 = new JCheckBox();
		JCheckBox box3 = new JCheckBox();
		box2.setBackground(bg);
		box3.setBackground(bg);
		box2.setBounds(5,395, 20,17);
		box3.setBounds(5,412, 20,17);
		box2.setEnabled(false);
		box3.setEnabled(false);


		b1 = new JButton("run");
		b1.setBackground(bg);
		b1.setBounds(83,378, 67, 52);
		b1.setEnabled(false);
		b1.addActionListener(this);

		p1.add(tag1);
		p1.add(tag2);
		p1.add(tag3);
		p1.add(tag4);
		p1.add(tag5);
		p1.add(tag6);
		p1.add(rowLabel);
		p1.add(columnLabel);
		p1.add(sizeLabel);
		p1.add(cCostLabel);
		p1.add(mCostLabel);
		p1.add(cMaxLabel);
		p1.add(separator);
		p1.add(slider);
		p1.add(slider2);
		p1.add(rate1box);
		p1.add(rate2box);
		p1.add(tag7);
		p1.add(tag8);
		p1.add(tag9);
		p1.add(rate1Label);
		p1.add(rate2Label);
		p1.add(piecesLabel);
		p1.add(piecesLabel1);
		p1.add(piecesLabel2);
		p1.add(separator2);
		p1.add(separator3);
		p1.add(combo);
		p1.add(box2);
		p1.add(box3);
		p1.add(b1);

		mainPanel.add(p1);

		this.add(mainPanel);

		setProblem(null);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource()==rate1box){
			slider.setEnabled(rate1box.isSelected());
			reloadPanel();
		}else if(event.getSource()==rate2box){
			slider2.setEnabled(rate2box.isSelected());
			reloadPanel();
		}else if(event.getSource()==b1){
			double rate2 = 90.0+(slider2.getValue()/10.0);
			picSolver.start2(rate2,rate2box.isSelected(),(Integer)combo.getSelectedItem(),this);
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		if(event.getSource()==slider){
			reloadPanel();
			if(!slider.getValueIsAdjusting()){
				reloadProblem();
			}
		}else if(event.getSource()==slider2){
			reloadPanel();
		}
	}


}
