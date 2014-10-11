package tmcit.hokekyo1210.SolverUI;

import java.util.LinkedList;
import java.util.List;

import tmcit.hokekyo1210.SolverUI.Util.Pair;

public class MyPuzzle {

	private int[][] puzzle;
	private MyBufferedImage[][] puzzleImage;
	public double allRate;
	private List<MyBufferedImage> stacks;
	private LinkedList<Pair> queue;

	public MyPuzzle(){
		this.allRate = 0.0;
	}

	public double getAllRate(){
		return allRate;
	}

	public int[][] getPuzzle(){
		return puzzle;
	}

	public List<MyBufferedImage> getStacks(){
		return stacks;
	}

	public LinkedList<Pair> getQueue(){
		return queue;
	}

	public MyBufferedImage[][] getPuzzleImage(){
		return puzzleImage;
	}

	public void setPuzzle(int[][] puzzle){
		this.puzzle = puzzle;
	}

	public void setStacks(List<MyBufferedImage> stacks){
		this.stacks = stacks;
	}

	public void setQueue(LinkedList<Pair> queue){
		this.queue = queue;
	}

	public void setPuzzleImage(MyBufferedImage[][] puzzleImage){
		this.puzzleImage = puzzleImage;
	}

}
