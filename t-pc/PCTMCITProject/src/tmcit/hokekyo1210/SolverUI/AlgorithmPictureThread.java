package tmcit.hokekyo1210.SolverUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tmcit.hokekyo1210.SolverUI.MyColorsList.DirectionJ;
import tmcit.hokekyo1210.SolverUI.UI.OptionFrame;
import tmcit.hokekyo1210.SolverUI.UI.SubFrame;
import tmcit.hokekyo1210.SolverUI.Util.CancelableRunnable;
import tmcit.hokekyo1210.SolverUI.Util.Pair;
import tmcit.hokekyo1210.SolverUI.Util.PriorityArrayList;

public class AlgorithmPictureThread {

	private int threadAmount = 4;

	private List<MyPuzzle> puzzles = new ArrayList<MyPuzzle>();
	private Problem problem;
	private SubFrame subframe;
	private OptionFrame optionframe;

	private double rate;
	private boolean isSPMode;

	private HashMap<String,Double> rateMemo;

	public AlgorithmPictureThread(List<MyPuzzle> puzzles,Problem problem,SubFrame subframe,OptionFrame optionframe,double rate,boolean isSP,HashMap<String,Double> memo,int threadAmount){
		this.puzzles = puzzles;
		this.problem = problem;
		this.subframe = subframe;
		this.optionframe = optionframe;
		this.rate = rate;
		this.isSPMode = isSP;
		this.rateMemo = memo;
		this.threadAmount = threadAmount;
	}

	private boolean isRunning = false;

	public boolean isRunning(){
		return isRunning;
	}

	List<CancelableRunnable> runningTask;

	public void cancelAllTask(){
		for(CancelableRunnable run:runningTask){
			run.isCanceled(true);
		}
	}

	public void start(){
		final long started = System.currentTimeMillis();

		isRunning = true;
		optionframe.b1.setEnabled(false);

		runningTask = new ArrayList<CancelableRunnable>();

		ExecutorService pool = Executors.newFixedThreadPool(threadAmount);

		Runnable mergeTask = new Runnable() {
			@Override
			public void run(){

				subframe.setProblem(problem);

				PriorityArrayList<MyPuzzle,Double> pal = new PriorityArrayList<MyPuzzle,Double>();
				for(MyPuzzle puzzle:puzzles){
					///System.out.println(puzzle.getAllRate());
					pal.addValue(puzzle.getAllRate(), puzzle);
				}
				List<MyPuzzle> sortedPuzzles = new ArrayList<MyPuzzle>();
				for(MyPuzzle puzzle:pal.getSortedObjects()){
					sortedPuzzles.add(puzzle);
				}
				subframe.setPuzzles(sortedPuzzles);
				System.out.println(sortedPuzzles.get(0).getAllRate()+"% matched"+" order "/*+String.valueOf(order)+" "*/+(System.currentTimeMillis()-started)+" ms");

				isRunning = false;
				optionframe.b1.setEnabled(true);
			}
		};

		final CyclicBarrier cyclicBarrier = new CyclicBarrier(threadAmount, mergeTask);

		for(int i = 0;i<threadAmount;i++){
			int startat = i*puzzles.size()/threadAmount;
			if(startat!=0){
				startat++;
			}
			if(startat>=puzzles.size()){
				break;
			}
			int endat = (i+1)*puzzles.size()/threadAmount;
			if(i==threadAmount-1){
				endat = puzzles.size()-1;
			}
			final List<MyPuzzle> splitPuzzle = new ArrayList<MyPuzzle>();
			for(int u = startat;u<=endat;u++){
				splitPuzzle.add(puzzles.get(u));
			}

			final int row = problem.row;
			final int column = problem.column;
			final double rate2 = rate;
			final boolean isSP = isSPMode;
			final HashMap<String,Double> memo = rateMemo;
			final int id = i;

			System.out.println("[Thread"+id+1+"] "+splitPuzzle.size()+" ps");

			CancelableRunnable calcTask = new CancelableRunnable() {

				public boolean isCanceled = false;
				private int order = 0;

				public void isCanceled(boolean isCanceled){
					this.isCanceled = isCanceled;
				}

				@Override
				public void run() {
					for(MyPuzzle puzzle:splitPuzzle){
						solve(puzzle);
						if(isCanceled){
							break;
						}
					}
					if(!isCanceled){
						System.out.println("[Thread"+id+1+"] completed "+order+" order "+(System.currentTimeMillis()-started)+" ms");
					}else{
						System.out.println("[Thread"+id+1+"] canceled "+order+" order "+(System.currentTimeMillis()-started)+" ms");
					}
					try {
						cyclicBarrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				}

				public void solve(MyPuzzle newPuzzle){
					if(newPuzzle.getQueue()==null){
						return;
					}
					LinkedList<Pair> array = newPuzzle.getQueue();
					List<MyBufferedImage> stacks = newPuzzle.getStacks();

					int[][] puzzle = newPuzzle.getPuzzle();
					MyBufferedImage[][] puzzleImage = newPuzzle.getPuzzleImage();

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
							MyBufferedImage mostMatch = searchPiece(newP,match,stacks,puzzle,puzzleImage,newPuzzle);

							if(mostMatch!=null){
								puzzle[newP.first][newP.second]=mostMatch.getIndex();
								puzzleImage[newP.first][newP.second]=mostMatch;
								array.offer(newP);
							}
						}
					}
				}

				public MyBufferedImage searchPiece(Pair newP,List<Pair> match,List<MyBufferedImage> stacks,int[][] puzzle,MyBufferedImage[][] puzzleImage,MyPuzzle mypuzzle){
					MyBufferedImage mostMatch = null;
					double mostrate = 0.0;
					double mostrate2 = 0.0;
					int mindex = -1;
					for(int i = 0;i<stacks.size();i++){
						MyBufferedImage source = stacks.get(i);
						double sum = 0.0;
						for(Pair around:match){
							order++;
							MyBufferedImage around1image = puzzleImage[around.first][around.second];
							DirectionJ dir = getDirection(newP,around);
							String key = getKey(source.getMyColors(dir).getId(),around1image.getMyColors(reverseDir(dir)).getId());
							sum+=memo.get(key);
						}

						if(sum/match.size()*1.0>mostrate){
							mostMatch = source;
							mostrate = sum/match.size()*1.0;
							mostrate2 = sum/4*1.0;
							mindex = i;
						}
					}
					if(isSP&&mostrate<rate2){///def94.0
						mostMatch = null;
					}else{
						mypuzzle.allRate+=mostrate2;
						stacks.remove(mindex);
					}
					return mostMatch;
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

				private String getKey(int id,int id2){
					return String.valueOf(id)+","+String.valueOf(id2);
				}


			};
			runningTask.add(calcTask);
			pool.execute(calcTask);

		}


	}
}
