package tmcit.hokekyo1210.SolverUI.UI;

import java.awt.image.BufferedImage;

import javax.swing.JLabel;

public class ILabel extends JLabel{
	
	private static final long serialVersionUID = -970381564795032079L;
	
	private int number;
	private BufferedImage image;
	
	public ILabel(int number,BufferedImage image){
		this.number = number;
		this.image = image;
	}
	
	public int getNum(){
		return number;
	}
	
	public BufferedImage getImage(){
		return image;
	}

}
