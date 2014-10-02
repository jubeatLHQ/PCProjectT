import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
	
	public Main(){
		
		String dir = getStringByConsole();
		String dir2 = getStringByConsole();
		BufferedImage image;
		BufferedImage image2;
		try{
			image = ImageIO.read(new File(dir));
			image2 = ImageIO.read(new File(dir2));
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		double rate = getSimulate(image,image2);
		System.out.println(rate);
		main(new String[]{});
		
	}
	
	///C:\Users\proken\Desktop\Procon\testsample\picture\Koala\Koala0.png
	
	private double getSimulate(BufferedImage image, BufferedImage image2) {
		List<Color> test = getLineColor(image,image.getHeight()-1,Direction.X);
		List<Color> test2 = getLineColor(image2,0,Direction.X);
		int sum = 0;
		for(int i = 0;i<test.size();i++){
			Color c1 = test.get(i);
			Color c2 = test2.get(i);
			int r = Math.abs(c1.getRed()-c2.getRed());
			int g = Math.abs(c1.getGreen()-c2.getGreen());
			int b = Math.abs(c1.getBlue()-c2.getBlue());
			sum+=r+g+b;
		}
		double rate = sum/test.size()*1.0;
		rate = 765.0-rate;
		rate = rate/765.0*100.0;
		return rate;
	}
	
	enum Direction{X,Y};
	
	private List<Color> getLineColor(BufferedImage image, int pointat,Direction dir) {
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

	private Color getColorByRGB(int rgb){
		int r = rgb>>16&0xff;
		int g = rgb>>8&0xff;
		int b = rgb&0xff;
		return new Color(r,g,b);
	}

	public String getStringByConsole(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			return br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

}
