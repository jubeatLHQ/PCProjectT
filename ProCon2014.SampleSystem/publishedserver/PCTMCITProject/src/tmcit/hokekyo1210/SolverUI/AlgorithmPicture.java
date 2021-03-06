package tmcit.hokekyo1210.SolverUI;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import tmcit.hokekyo1210.SolverUI.MyColorsList.DirectionJ;
import tmcit.hokekyo1210.SolverUI.UI.SubFrame;

public class AlgorithmPicture {

	private List<BufferedImage> images;
	private List<MyBufferedImage> images2 = new ArrayList<MyBufferedImage>();
	private int row;
	private int column;
	private int width;
	private int height;
	private long order = 0;

	public double prate = 96.5;
	public double rate2 = 0.0;
	public boolean isSP = false;

	private SubFrame subframe;

	public AlgorithmPicture(Problem problem, SubFrame subframe) {
		this.images = problem.cutImages;
		this.row = problem.row;
		this.column = problem.column;
		this.width = problem.width;
		this.height = problem.height;
		this.subframe = subframe;

		for(int i = 0;i<images.size();i++){
			images2.add(new MyBufferedImage(images.get(i),i));
		}
	}

	public void setRate1(double rate){
		prate = rate;
	}
	public void setRate2(double rate){
		rate2 = rate;
	}

	private List<MyBufferedImage> fuls;
	private List<MyBufferedImage> furs;
	private List<MyBufferedImage> fdls;
	private List<MyBufferedImage> fdrs;
	private HashMap<Integer,List<Integer>> ulrate;
	private HashMap<Integer,List<Integer>> urrate;
	private HashMap<Integer,List<Integer>> dlrate;
	private HashMap<Integer,List<Integer>> drrate;
	private List<Integer> uls;
	private List<Integer> urs;
	private List<Integer> dls;
	private List<Integer> drs;

	public int pieces;
	public int ulpieces;
	public int urpieces;
	public int dlpieces;
	public int drpieces;

	public void start() throws Exception{

		long started = System.currentTimeMillis();

		int count = 1;

		ulrate = new HashMap<Integer,List<Integer>>();
		urrate = new HashMap<Integer,List<Integer>>();
		dlrate = new HashMap<Integer,List<Integer>>();
		drrate = new HashMap<Integer,List<Integer>>();
		uls = new ArrayList<Integer>();
		urs = new ArrayList<Integer>();
		dls = new ArrayList<Integer>();
		drs = new ArrayList<Integer>();

		ulpieces = 0;
		urpieces = 0;
		dlpieces = 0;
		drpieces = 0;
		for(MyBufferedImage image:images2){

			int upratemax = 0;
			int downratemax = 0;
			int rightratemax = 0;
			int leftratemax = 0;

			for(int i = 0;i<images2.size();i++){
				MyBufferedImage compi = images2.get(i);
				int r = 0;
				if(!image.equals(compi)){
					r = rate(image.up,compi.down,false);
					if(r>upratemax){
						upratemax = r;
					}
					r = rate(image.down,compi.up,false);
					if(r>downratemax){
						downratemax = r;
					}
					r = rate(image.right,compi.left,false);
					if(r>rightratemax){
						rightratemax = r;
					}
					r = rate(image.left,compi.right,false);
					if(r>leftratemax){
						leftratemax = r;
					}

				}
			}
			int ul = Math.abs(rightratemax+downratemax-upratemax-leftratemax);
			int ur = Math.abs(leftratemax+downratemax-upratemax-rightratemax);
			int dl = Math.abs(upratemax+rightratemax-downratemax-leftratemax);
			int dr = Math.abs(leftratemax+upratemax-downratemax-rightratemax);
			if(upratemax!=100&&leftratemax!=100){
				if(ulrate.get(ul)==null){
					ulrate.put(ul, new ArrayList<Integer>());
				}
				ulrate.get(ul).add(count);
				uls.add(ul);
				ulpieces++;
				///System.out.println(ul+","+count);
			}
			if(upratemax!=100&&rightratemax!=100){
				if(urrate.get(ur)==null){
					urrate.put(ur, new ArrayList<Integer>());
				}
				urrate.get(ur).add(count);
				urs.add(ur);
				urpieces++;
			}
			if(downratemax!=100&&leftratemax!=100){
				if(dlrate.get(dl)==null){
					dlrate.put(dl, new ArrayList<Integer>());
				}
				dlrate.get(dl).add(count);
				dls.add(dl);
				dlpieces++;
			}
			if(downratemax!=100&&rightratemax!=100){
				if(drrate.get(dr)==null){
					drrate.put(dr, new ArrayList<Integer>());
				}
				drrate.get(dr).add(count);
				drs.add(dr);
				drpieces++;
			}
			///System.out.println(count+"/"+upratemax+"."+id1+"/"+downratemax+"."+id2+"/"+rightratemax+"."+id3+"/"+leftratemax+"."+id4);
			count++;
		}
		pieces = ulpieces*urpieces*dlpieces*drpieces;
		System.out.println(ulpieces+" pieces");
		System.out.println(urpieces+" pieces");
		System.out.println(dlpieces+" pieces");
		System.out.println(drpieces+" pieces");

		if(pieces==0){
			throw new Exception();
		}
		ulpieces++;urpieces++;dlpieces++;drpieces++;
		pieces = ulpieces*urpieces*dlpieces*drpieces;
		System.out.println(pieces+" pieces_time "+(System.currentTimeMillis()-started)+" ms");
	}

	public void start2(){
		long started = System.currentTimeMillis();

		fuls = new ArrayList<MyBufferedImage>();
		furs = new ArrayList<MyBufferedImage>();
		fdls = new ArrayList<MyBufferedImage>();
		fdrs = new ArrayList<MyBufferedImage>();

		Collections.sort(uls);
		Collections.sort(urs);
		Collections.sort(dls);
		Collections.sort(drs);
		int before = -1;
		for(int i = uls.size()-1;i!=-1;i--){
			if(uls.get(i)==before){continue;}
			before = uls.get(i);
			List<Integer> array = ulrate.get(uls.get(i));
			for(int k:array){
				///System.out.println("ul,"+uls.get(i)+","+k);
				fuls.add(images2.get(k-1));
			}
		}
		before = -1;
		for(int i = urs.size()-1;i!=-1;i--){
			if(urs.get(i)==before){continue;}
			before = urs.get(i);
			List<Integer> array = urrate.get(urs.get(i));
			for(int k:array){
				///System.out.println("ur,"+urs.get(i)+","+k);
				furs.add(images2.get(k-1));
			}
		}
		before = -1;
		for(int i = dls.size()-1;i!=-1;i--){
			if(dls.get(i)==before){continue;}
			before = dls.get(i);
			List<Integer> array = dlrate.get(dls.get(i));
			for(int k:array){
				///System.out.println("dl,"+dls.get(i)+","+k);
				fdls.add(images2.get(k-1));
			}
		}
		before = -1;
		for(int i = drs.size()-1;i!=-1;i--){
			if(drs.get(i)==before){continue;}
			before = drs.get(i);
			List<Integer> array = drrate.get(drs.get(i));
			for(int k:array){
				///System.out.println("dr,"+drs.get(i)+","+k);
				fdrs.add(images2.get(k-1));
			}
		}

		fuls.add(null);
		furs.add(null);
		fdls.add(null);
		fdrs.add(null);

		List<MyPuzzle> finishedPuzzles = new ArrayList<MyPuzzle>();
		int count = 0;
		for(MyBufferedImage imgul:fuls){
			for(MyBufferedImage imgur:furs){
				for(MyBufferedImage imgdl:fdls){
					for(MyBufferedImage imgdr:fdrs){
						MyPuzzle puzzle = makePuzzle(imgul,imgur,imgdl,imgdr);
						finishedPuzzles.add(puzzle);
						count++;
						if(count%100==0){
							double d = (count*1.0)/(pieces*1.0);
							System.out.println((d*100.0)+"% search "+String.valueOf(order));
						}
					}
				}
			}
		}

		subframe.setProblem(width,height,row,column,images2);

		PriorityArrayList<MyPuzzle,Double> pal = new PriorityArrayList<MyPuzzle,Double>();
		for(MyPuzzle puzzle:finishedPuzzles){
			///System.out.println(puzzle.getAllRate());
			pal.addValue(puzzle.getAllRate(), puzzle);
		}
		List<MyPuzzle> sortedPuzzles = new ArrayList<MyPuzzle>();
		for(int i = 0;i<pal.size();i++){
			for(MyPuzzle puzzle:pal.getSortedObjects(i)){
				sortedPuzzles.add(puzzle);
			}
		}
		subframe.setPuzzles(sortedPuzzles);

		System.out.println(sortedPuzzles.get(0).getAllRate()+"% matched"+" order "+String.valueOf(order)+" "+(System.currentTimeMillis()-started)+" ms");
	}


	private MyPuzzle makePuzzle(MyBufferedImage imgul, MyBufferedImage imgur, MyBufferedImage imgdl, MyBufferedImage imgdr) {
		MyPuzzle newPuzzle = new MyPuzzle();
		int[][] puzzle = new int[row][column];
		if((imgul!=null)&&(imgul==imgur||imgul==imgdl||imgul==imgdr)){
			newPuzzle.setPuzzle(puzzle);
			return newPuzzle;
		}
		if((imgur!=null)&&(imgur==imgdl||imgur==imgdr)){
			newPuzzle.setPuzzle(puzzle);
			return newPuzzle;
		}
		if((imgdl!=null)&&(imgdl==imgdr)){
			newPuzzle.setPuzzle(puzzle);
			return newPuzzle;
		}



		for(int i = 0;i<row;i++){
			for(int u = 0;u<column;u++){
				puzzle[i][u] = -1;
			}
		}
		List<MyBufferedImage> stacks = new ArrayList<MyBufferedImage>();
		for(MyBufferedImage i:images2){
			if(i!=imgul&&i!=imgur&&i!=imgdl&&i!=imgdr){
				stacks.add(i);
			}
		}

		LinkedList<Pair> array = new LinkedList<Pair>();

		if(imgul!=null){
			puzzle[0][0] = imgul.getIndex();
			array.offer(new Pair(0,0));
		}
		if(imgur!=null){
			puzzle[row-1][0] = imgur.getIndex();
			array.offer(new Pair(row-1,0));
		}
		if(imgdl!=null){
			puzzle[0][column-1] = imgdl.getIndex();
			array.offer(new Pair(0,column-1));
		}
		if(imgdr!=null){
			puzzle[row-1][column-1] = imgdr.getIndex();
			array.offer(new Pair(row-1,column-1));
		}


		while(!array.isEmpty()){

			Pair p = array.poll();
			List<Pair> around = aroundPair(p);
			for(Pair newP:around){
				if(puzzle[newP.first][newP.second]!=-1){continue;}
				List<Pair> around2 = aroundPair(newP);
				List<Pair> match = new ArrayList<Pair>();
				for(Pair aP:around2){
					if(puzzle[aP.first][aP.second]!=-1){match.add(aP);}
				}
				if(match.size()==0){continue;}
				MyBufferedImage mostMatch = searchPiece(newP,match,stacks,puzzle,newPuzzle);

				if(mostMatch!=null){
					puzzle[newP.first][newP.second]=mostMatch.getIndex();
					array.offer(newP);
				}
			}
		}
		newPuzzle.setPuzzle(puzzle);
		return newPuzzle;
	}

	public MyBufferedImage searchPiece(Pair newP,List<Pair> match,List<MyBufferedImage> stacks,int[][] puzzle,MyPuzzle mypuzzle){

		MyBufferedImage mostMatch = null;
		double mostrate = 0.0;
		int mindex = -1;
		for(int i = 0;i<stacks.size();i++){
			MyBufferedImage source = stacks.get(i);
			double sum = 0.0;
			for(Pair around:match){
				order++;
				int index = puzzle[around.first][around.second];
				MyBufferedImage around1image = images2.get(index);
				DirectionJ dir = getDirection(newP,around);
				String key = getKey(source.getMyColors(dir).getId(),around1image.getMyColors(reverseDir(dir)).getId());
				sum+=memo.get(key);
			}
			sum/=match.size()*1.0;
			if(sum>mostrate){
				mostMatch = source;
				mostrate = sum;
				mindex = i;
			}
		}
		if(isSP&&mostrate<rate2){///def94.0
			mostMatch = null;
		}else{
			mypuzzle.allRate+=mostrate;
			stacks.remove(mindex);
		}
		return mostMatch;
	}

	public DirectionJ reverseDir(DirectionJ dir){
		if(dir==DirectionJ.UP){
			return DirectionJ.DOWN;
		}else if(dir==DirectionJ.DOWN){
			return DirectionJ.UP;
		}else if(dir==DirectionJ.RIGHT){
			return DirectionJ.LEFT;
		}else{
			return DirectionJ.RIGHT;
		}
	}

	public DirectionJ getDirection(Pair source,Pair p2){
		DirectionJ returnDir = null;
		int first = source.first;
		int second = source.second;
		int first2 = p2.first;
		int second2 = p2.second;
		int ff = first2-first;
		int fs = second2-second;
		if(ff==1){
			returnDir = DirectionJ.RIGHT;
		}else if(ff==-1){
			returnDir = DirectionJ.LEFT;
		}else if(fs==-1){
			returnDir = DirectionJ.UP;
		}else if(fs==1){
			returnDir = DirectionJ.DOWN;
		}
		return returnDir;
	}

	private int max(int i,int u){
		if(i>u){
			return i;
		}
		return u;
	}
	private int min(int i,int u){
		if(u>i){
			return i;
		}
		return u;
	}

	public List<Pair> aroundPair(Pair source){
		List<Pair> pairs = new ArrayList<Pair>();
		int first = source.first-1;
		int second = source.second;
		if(first!=-1&&first<row&&second!=-1&&second<column){
			Pair p = new Pair(first,second);
			pairs.add(p);
		}
		first = source.first;
		second = source.second-1;
		if(first!=-1&&first<row&&second!=-1&&second<column){
			Pair p = new Pair(first,second);
			pairs.add(p);
		}
		first = source.first+1;
		second = source.second;
		if(first!=-1&&first<row&&second!=-1&&second<column){
			Pair p = new Pair(first,second);
			pairs.add(p);
		}
		first = source.first;
		second = source.second+1;
		if(first!=-1&&first<row&&second!=-1&&second<column){
			Pair p = new Pair(first,second);
			pairs.add(p);
		}
		return pairs;
	}
	private HashMap<String,Double> memo = new HashMap<String,Double>();

	private int rate(MyColorsList color1,MyColorsList color2,boolean gradation){
		List<Color> c1s = color1.getList();
		List<Color> c2s = color2.getList();
		int sum = 0;
		double rate;
		if(memo.get(getKey(color1.getId(),color2.getId()))==null){
			for(int i = 0;i<c1s.size();i++){
				Color c1 = c1s.get(i);
				Color c2;
				if(gradation){
					c2 = getBetterColor(c2s,i,c1);
				}else{
					c2 = c2s.get(i);
				}
				int r = Math.abs(c1.getRed()-c2.getRed());
				int g = Math.abs(c1.getGreen()-c2.getGreen());
				int b = Math.abs(c1.getBlue()-c2.getBlue());
				sum+=r+g+b;
			}
			rate = sum/c1s.size()*1.0;
			rate = 765.0-rate;
			rate = rate/765.0*100.0;
			memo.put(getKey(color1.getId(),color2.getId()), rate);
			memo.put(getKey(color2.getId(),color1.getId()), rate);
		}else{
			rate = memo.get(getKey(color1.getId(),color2.getId()));
		}
		if(rate>=prate){
			rate = 100;
		}
		int frate = (int) rate;
		return frate;
	}

	private String getKey(int id,int id2){
		return String.valueOf(id)+","+String.valueOf(id2);
	}

	private Color getBetterColor(List<Color> list,int index,Color source){
		Color newColor = null;
		List<Color> colors = new ArrayList<Color>();
		if(index!=0){
			colors.add(list.get(index-1));
		}
		if(index!=list.size()-1){
			colors.add(list.get(index));
		}
		colors.add(list.get(index));
		int dis = 10000;
		int r,g,b,sum;
		for(Color c:colors){
			r = Math.abs(source.getRed()-c.getRed());
			g = Math.abs(source.getGreen()-c.getGreen());
			b = Math.abs(source.getBlue()-c.getBlue());
			sum = r+g+b;
			if(sum<dis){
				dis = sum;
				newColor = c;
			}
		}

		return newColor;
	}
}
