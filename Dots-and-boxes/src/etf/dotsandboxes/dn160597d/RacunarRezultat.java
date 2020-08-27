package etf.dotsandboxes.dn160597d;
import java.util.ArrayList;

public class RacunarRezultat {

	private ArrayList<Linija> moves;
	private int value[];
	private Linija bestMove;
	
	public RacunarRezultat(ArrayList<Linija> moves, int[] value, Linija bestMove) {
		this.moves = moves;
		this.value = value;
		this.bestMove = bestMove;
	}
	
	
	public RacunarRezultat() {
	}


	public ArrayList<Linija> getMoves() {
		return moves;
	}
	public void setMoves(ArrayList<Linija> moves) {
		this.moves = moves;
	}
	public int[] getValue() {
		return value;
	}
	public void setValue(int[] value) {
		this.value = value;
	}
	public Linija getBestMove() {
		return bestMove;
	}
	public void setBestMove(Linija bestMove) {
		this.bestMove = bestMove;
	}
	
	
	
	
}
