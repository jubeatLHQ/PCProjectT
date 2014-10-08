package tmcit.hokekyo1210.SolverUI.UI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import tmcit.hokekyo1210.SolverUI.AlgorithmPicture;
import tmcit.hokekyo1210.SolverUI.HttpUtil;

public class MainFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 164320872137330976L;

	private String title;
	private SubFrame subframe;

	public MainFrame(String title,SubFrame subframe){
		this.title = title;
		this.subframe = subframe;
		try {
			initUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private JLayeredPane pane;
	private JPanel mainPanel;
	private List<BufferedImage> images = new ArrayList<BufferedImage>();
	private List<List<ILabel>> imageLabels = new ArrayList<List<ILabel>>();
	private List<String> options = new ArrayList<String>();
	private int width;
	private int height;

	private void initUI() throws Exception{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(title);
		this.setSize(803, 450);
		this.setResizable(false);

		mainPanel = new JPanel(null);
		mainPanel.setBounds(0, 0, 400, 450);
		mainPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

		ILabel imageLabel = new ILabel(-1,null);
		List<ILabel> newArray = new ArrayList<ILabel>();
		newArray.add(imageLabel);

		imageLabels.add(newArray);
		mainPanel.add(imageLabel);

		this.setTransferHandler(new DropFileHandler(this));

		this.add(mainPanel);

		JMenuBar menubar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		JMenuItem menuitem1 = new JMenuItem("Problem");
		JMenuItem menuitem2 = new JMenuItem("Open");
		JMenuItem menuitem3 = new JMenuItem("Exit");
		menuitem1.addActionListener(this);
		menuitem2.addActionListener(this);
		menuitem3.addActionListener(this);
		menu1.add(menuitem1);
		menu1.add(menuitem2);
		menu1.add(menuitem3);
		menubar.add(menu1);

		this.setJMenuBar(menubar);
		this.addKeyListener(new MyKeyListener(new ILabelController(imageLabels)));
	}

	public void sendImage(String dir) throws Exception{
		File file = new File(dir);
		ILabel label;
		for(List<ILabel> ll:imageLabels){
			for(ILabel l:ll){
				mainPanel.remove(l);
			}
		}
		images.clear();
		imageLabels.clear();
		options.clear();
		mainPanel.repaint();


		BufferedImage image = null;

		if(file.getName().indexOf(".ppm")!=-1){
			ImageUtil iu = new ImageUtil(file.getAbsolutePath());
			BufferedImage bi = iu.getBuffImg();
			if(bi==null){return;}
			options = iu.getOptions();
			image = bi;
		}else{
			image = ImageIO.read(file);
		}


		width = image.getWidth();
		height = image.getHeight();
		int row = 1;
		int column = 1;
		int cnum = 0;
		int cost1 = 0;
		int cost2 = 0;
		if(options.size()==3){
			String[] strs = options.get(0).split(" ");
			row = Integer.parseInt(strs[0]);
			column = Integer.parseInt(strs[1]);
			cnum = Integer.parseInt(options.get(1));
			String[] strs2 = options.get(2).split(" ");
			cost1 = Integer.parseInt(strs2[0]);
			cost2 = Integer.parseInt(strs2[1]);
		}

		int splitwidth = width/row;
		int splitheight = height/column;

		images = imagesCut(row,column,image);
		int count = 0;
		for(int i = 0;i<column;i++){
			List<ILabel> newArray = new ArrayList<ILabel>();
			for(int u = 0;u<row;u++){
				label = getImageLabel(count+1,images.get(count));
				paint(label);
				label.setLocation(splitwidth*u+2, splitheight*i+2);
				label.setVisible(true);
				mainPanel.add(label);
				newArray.add(label);
				count++;
			}
			imageLabels.add(newArray);
		}

		AlgorithmPicture picSolver = new AlgorithmPicture(images,row,column,image.getWidth(),image.getHeight(),subframe);
		try{
			picSolver.start();
		}catch(Exception e){
			System.out.println("error");
		}

		mainPanel.setBounds(0, 0, image.getWidth()+3, image.getHeight()+3);
		this.setSize(image.getWidth()+12, image.getHeight()+50);
	}

	private void paint(ILabel label){
		BufferedImage bi = label.getImage();
		int num = label.getNum();
		if(bi==null) return;
		Graphics2D g = bi.createGraphics();
		g.setColor(Color.black);
		g.drawString(String.valueOf(num), 3, 11);
		g.setColor(Color.white);
		g.drawString(String.valueOf(num), 2, 10);
	}

	private ILabel getImageLabel(int num,BufferedImage image){
		ILabel imageLabel = new ILabel(num,image);
		ImageIcon icon = new ImageIcon(image);
		imageLabel.setIcon(icon);
		imageLabel.setSize(icon.getIconWidth(), icon.getIconHeight());
		return imageLabel;
	}

	private List<BufferedImage> imagesCut(int row,int column, BufferedImage bi){
		List<BufferedImage> list = new ArrayList<BufferedImage>();
		int spx = bi.getWidth()/row;
		int spy = bi.getHeight()/column;
		for(int i = 0;i<column;i++){
			for(int u = 0;u<row;u++){
				list.add(bi.getSubimage(spx*u, spy*i, spx, spy));
			}
		}
		return list;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() instanceof JMenuItem){
			JMenuItem menuitem = (JMenuItem) event.getSource();
			String text = menuitem.getText();
			if(text.equalsIgnoreCase("Open")){
				openFiler();
			}else if(text.equalsIgnoreCase("Exit")){
				System.exit(0);
			}else if(text.equalsIgnoreCase("Problem")){
				String value = JOptionPane.showInputDialog(this, "Problem Number");
				if(value != null){
					value = "prob"+value;
					value += ".ppm";
					try {
						HttpUtil.getProblemFile(value);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void openFiler() {
		JFileChooser filechooser = new JFileChooser();
		int selected = filechooser.showOpenDialog(this);

		if(selected==JFileChooser.APPROVE_OPTION){
			File file = filechooser.getSelectedFile();
			if(DropFileHandler.isImage(file)){
				try {
					sendImage(file.getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


}
