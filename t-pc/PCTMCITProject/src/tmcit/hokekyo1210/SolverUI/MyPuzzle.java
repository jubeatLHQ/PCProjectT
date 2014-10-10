package tmcit.hokekyo1210.SolverUI;

public class MyPuzzle {

	private int[][] puzzle;
	public double allRate;


	public MyPuzzle(int[][] puzzle,double allRate){
		this.puzzle = puzzle;
		this.allRate = allRate;
	}
	public MyPuzzle(){
		this.allRate = 0.0;
	}

	public double getAllRate(){
		return allRate;
	}

	public int[][] getPuzzle(){
		return puzzle;
	}
	
	public void setPuzzle(int[][] puzzle){
		this.puzzle = puzzle;
	}

}
