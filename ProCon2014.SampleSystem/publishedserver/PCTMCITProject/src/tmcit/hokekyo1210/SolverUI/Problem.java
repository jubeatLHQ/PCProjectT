package tmcit.hokekyo1210.SolverUI;

import java.awt.image.BufferedImage;
import java.util.List;

public class Problem {

	public String name;
	public int row;
	public int column;
	public int width;
	public int height;
	public int cCost;
	public int mCost;
	public int cMax;
	public List<BufferedImage> cutImages;

	public Problem(String name,int row,int column,int width,int height,int cCost,int mCost,int cMax,List<BufferedImage> images){
		this.name = name;
		this.row = row;
		this.column = column;
		this.width = width;
		this.height = height;
		this.cCost = cCost;
		this.mCost = mCost;
		this.cMax = cMax;
		this.cutImages = images;
	}

}
