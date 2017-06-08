package edu.nyu.ai.loa;
/**
*
* @author Nitin
* Enum which represents the pieces on the board
* Each piece has actions field which has all the 
* available actions for that piece
*/
import static edu.nyu.ai.loa.Side.BLACK;
import static edu.nyu.ai.loa.Side.WHITE;

import java.util.ArrayList;

public enum Piece {
	
	BP(BLACK), WP(WHITE),  EMP(null);
	public ArrayList<int[]> action = new ArrayList<int[]>();
	private Side side;
	Piece(Side side) {
        this.side = side;
    }
	Side side(){
		return side;
	}

}
