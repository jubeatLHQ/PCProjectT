package tmcit.hokekyo1210.SolverUI;

public class MyPuzzle {

	private int[][] puzzle;
	private double allRate;


	public MyPuzzle(int[][] puzzle,double allRate){
		this.puzzle = puzzle;
		this.allRate = allRate;
	}

	public double getAllRate(){
		return allRate;
	}

	public int[][] getPuzzle(){
		return puzzle;
	}

}
