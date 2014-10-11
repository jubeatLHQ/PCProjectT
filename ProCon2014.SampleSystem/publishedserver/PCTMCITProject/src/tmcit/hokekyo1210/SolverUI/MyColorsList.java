package tmcit.hokekyo1210.SolverUI;

import java.awt.Color;
import java.util.List;

public class MyColorsList {

	private static int uniid = 0;

	private int id;
	private List<Color> list;
	private DirectionJ dir;

	public MyColorsList(List<Color> list,DirectionJ dir){
		this.id = uniid;
		this.list = list;
		this.dir = dir;
		uniid++;
	}

	public List<Color> getList(){
		return list;
	}

	public int getId(){
		return id;
	}

	public DirectionJ getDir(){
		return dir;
	}

	enum DirectionJ{UP,DOWN,LEFT,RIGHT}

}
