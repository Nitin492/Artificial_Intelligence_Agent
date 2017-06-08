package edu.nyu.ai.loa;
import com.nyu.ai.view.BoardUI;
/**
 * 
 * @author Nitin
 * Acts as an interface between UI and backend implementation 
 */

public class Game {
	
	private Side sidep;
    private String analysis;
	private Board board;
	private int size;
	private int depth;
	public static int count;
    /** The white player. */
    public AIPlayer wp;
    /** The black player. */
    public AIPlayer bp;
	public Game(BoardUI page){
		this.board = page.board;
		sidep = page.side;
		board.currPlayer = sidep;
		this.size = page.size;
		if(page.level.equals("Easy")){
			depth = 5;
		}
		if(page.level.equals("Medium")){
			depth = 7;
		}
		if(page.level.equals("Hard")){
			depth = 9;
		}
		board.addObserver(page);
		if (sidep == Side.BLACK){
			wp = new AIPlayer(Side.WHITE,size,depth);
		}
		else{
			bp = new AIPlayer(Side.BLACK,size,depth);
		}
	}
	/**
	 * calls Makemove on the basis on player side 
	 * @return
	 */
	public String play() {
		if (sidep == Side.BLACK){	
			analysis = wp.makeMove(board);
			System.out.println(analysis);
		}
		else{
			analysis = bp.makeMove(board);
			System.out.println(analysis);
		}
		return analysis;
	}
	
}
