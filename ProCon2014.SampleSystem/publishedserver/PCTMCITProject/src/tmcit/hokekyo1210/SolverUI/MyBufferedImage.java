package tmcit.hokekyo1210.SolverUI;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import tmcit.hokekyo1210.SolverUI.MyColorsList.DirectionJ;

public class MyBufferedImage{

	private BufferedImage image;
	private int index;
	public MyColorsList up;
	public MyColorsList down;
	public MyColorsList right;
	public MyColorsList left;

	public MyBufferedImage(BufferedImage image,int index){
		this.image = image;
		this.index = index;
		up = new MyColorsList(getLineColor(image,0,Direction.X),DirectionJ.UP);
		down = new MyColorsList(getLineColor(image,image.getHeight()-1,Direction.X),DirectionJ.DOWN);
		right = new MyColorsList(getLineColor(image,image.getWidth()-1,Direction.Y),DirectionJ.RIGHT);
		left = new MyColorsList(getLineColor(image,0,Direction.Y),DirectionJ.LEFT);
	}

	public BufferedImage getImage(){
		return image;
	}

	public int getIndex(){
		return index;
	}

	public MyColorsList getMyColors(DirectionJ dir){
		if(dir==DirectionJ.UP){
			return up;
		}else if(dir==DirectionJ.DOWN){
			return down;
		}else if(dir==DirectionJ.RIGHT){
			return right;
		}else{
			return left;
		}
	}


	enum Direction{X,Y};

	private static List<Color> getLineColor(BufferedImage image, int pointat,Direction dir) {
		List<Color> line = new ArrayList<Color>();
		if(dir==Direction.X){
			for(int i = 0;i<image.getWidth();i++){
				line.add(getColorByRGB(image.getRGB(i,pointat)));
			}
		}else{
			for(int i = 0;i<image.getHeight();i++){
				line.add(getColorByRGB(image.getRGB(pointat, i)));
			}
		}
		return line;
	}

	private static Color getColorByRGB(int rgb){
		int r = rgb>>16&0xff;
		int g = rgb>>8&0xff;
		int b = rgb&0xff;
		return new Color(r,g,b);
	}
}
