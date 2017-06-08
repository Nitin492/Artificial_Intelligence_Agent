package com.nyu.ai.view;
/**
*
* @author Nitin
*/
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import com.nyu.ai.view.BoardUI;
import edu.nyu.ai.loa.Board;
import edu.nyu.ai.loa.Piece;
import edu.nyu.ai.loa.Side;
/**
 *Controls all the actions of the 
 *buttons on the LOA board
 */
public class ActionClass implements ActionListener {
	
	private Side side;
	private int coord[] = new int[2];
	private int x,y;
	private int clickcount = 0;
	private Board board = new Board(side);
	private  JButton[][] squares = new JButton[6][6];
	public ActionClass(Board board,JButton[][] squares,Side side) {
		// TODO Auto-generated constructor stub
		this.squares = squares;
		this.board = board;
		this.side = side;
	}
	/**
	 * Shows all the valid moves for a piece 
	 * and makes the specified move
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(clickcount == 0){
			coord = getcoordinates(e);
			x = coord[0];
			y = coord[1];
			showValidMoves();
			clickcount++;
		}
		else{
			clickcount = 0;			
			coord = getcoordinates(e);
			System.out.println("clicks:"+clickcount);
			BoardUI.placePiecesOnBoard(board,squares,5);
			board.makeMove(x,y,coord[0],coord[1]);
		}	
	}
	/**
	 * 
	 * @param e
	 * @return coordinates of the button which makes the call
	 */
	private int[] getcoordinates(ActionEvent e){
		int coord[] = new int[2];
		for (int i = 0; i < 5; i++) {
			  for (int j = 0; j < 5; j++) {
			    if( squares[i][j] == e.getSource() ) { 
			      coord[0]=i;
			      coord[1]=j;
			    }
			  }
			}
		return coord;
	}
	/**
	 * displays all the valid moves available
	 */
	private void showValidMoves(){
			int[] arr;
			board.state[x][y].action = board.getValidMoves(x,y);
			System.out.println(("moves"));
			for(int i = 0;i<board.state[x][y].action.size();i++ ){
				if(board.state[x][y] == Piece.BP || board.state[x][y] == Piece.WP){
					arr = board.state[x][y].action.get(i);
					System.out.println("move:"+arr[0]+","+arr[1]);
			    	squares[arr[0]][arr[1]].setBackground(Color.cyan);
				}
			}	
	}
}
