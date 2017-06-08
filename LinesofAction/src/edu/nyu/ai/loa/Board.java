package edu.nyu.ai.loa;
import java.util.ArrayList;
import java.util.Observable;
import static edu.nyu.ai.loa.Piece.*;
/**
*
* @author Nitin
* This class maintains and modifies backend
* implementation of LOA board
*/
public class Board extends Observable{
	
	public Piece[][] state;
    public Side currPlayer;
    private int rows = 5;
    private int columns = 5;
    
    /** The standard initial configuration for Lines of Action. */
    public static final Piece[][] INITIAL_BOARD = {
	        { EMP, BP,  BP,  BP,  EMP, },
	        { WP,  EMP, EMP, EMP, WP,  },
	        { WP,  EMP, EMP, EMP, WP,  },
	        { WP,  EMP, EMP, EMP, WP,  },
	        { EMP, BP,  BP,  BP,  EMP, }
	    };
    public static final Piece[][] INITIAL_BOARD1 = {
	        { EMP, BP,  BP,  BP,  BP,  EMP, },
	        { WP,  EMP, EMP, EMP, EMP, WP,  },
	        { WP,  EMP, EMP, EMP, EMP, WP,  },
	        { WP,  EMP, EMP, EMP, EMP, WP,  },
	        { WP,  EMP, EMP, EMP, EMP, WP,  },
	        { EMP, BP,  BP,  BP,  BP,  EMP, }
	    };
	
	public Board(Side player) {
        state = INITIAL_BOARD;
        currPlayer = player;
    }
	/**
	 * 
	 * @param x: X coordinate of piece
	 * @param y: Y coordinate of piece
	 * @return all the valid locations where
	 * piece can move
	 */
	public ArrayList<int[]> getValidMoves(int x,int y){	
		int row = x;
		int column = y;
		int rowSum = rowSum(row,column);
        int colSum = colSum(row,column);
        int dg1Sum = dg1Sum(row,column);
        int dg2Sum = dg2Sum(row,column);        
        ArrayList<int[]> action = new ArrayList<int[]>();
        int move[] = new int[2]; 
        if(checkValidMove(row,column,row+rowSum,column)){
            move[0] = row+rowSum;
            move[1] = column;
            action.add(move);
        }
        move = new int[2];
        if(checkValidMove(row,column,row-rowSum,column)){
        	move[0] = row-rowSum;
            move[1] = column;
            action.add(move);
        }
        move = new int[2];
        if(checkValidMove(row,column,row,column+colSum)){
            move[0] = row;
            move[1] = column+colSum;
            action.add(move);
        }
        move = new int[2];
        if(checkValidMove(row,column,row,column-colSum)){
            move[0] = row;
            move[1] = column-colSum;
            action.add(move);
        }
        move = new int[2];
        if(checkValidMove(row,column,row+dg1Sum,column+dg1Sum)){
            move[0] = row+dg1Sum;
            move[1] = column+dg1Sum;
            action.add(move);
        }
        move = new int[2];
        if(checkValidMove(row,column,row-dg1Sum,column-dg1Sum)){
            move[0] = row-dg1Sum;
            move[1] = column-dg1Sum;
            action.add(move);
        }
        move = new int[2];
        if(checkValidMove(row,column,row-dg2Sum,column+dg2Sum)){
            move[0] = row-dg2Sum;
            move[1] = column+dg2Sum;
            action.add(move);
        }
        move = new int[2];
        if(checkValidMove(row,column,row+dg2Sum,column-dg2Sum)){
            move[0] = row+dg2Sum;
            move[1] = column-dg2Sum;
            action.add(move);
        }
        return action;
	}
	/**
	 * Check if the proposed move is valid
	 * @param currRow: current row of piece
	 * @param currColumn: current column of piece
	 * @param row: proposed row of piece
	 * @param column: proposed column of piece
	 * @return boolean value true if valid move otherwise flase
	 */
	public boolean checkValidMove(int currRow, int currColumn, int row,int column){
		int colSum= colSum(currRow,currColumn);
        int rowSum= rowSum(currRow,currColumn);
        int dg1Sum = dg1Sum(currRow,currColumn);
        int dg2Sum = dg2Sum(currRow,currColumn);
        if(row<0 || row>=rows || column <0 || column>=columns || currRow <0 ||  currColumn<0){
            return false;
        }
        /*
         * checks for same color piece at the location
         */
        if(!(state[currRow][currColumn] == Piece.EMP) && state[currRow][currColumn] == state[row][column]){
            return false;
        }
        /*
         * checks for opposite color piece at the location
         */
        if(row == currRow)
        {        	
        	int right = currColumn + colSum;
            int left = currColumn - colSum;
            if(column == left){
                for(int i=1;i<colSum;i++){
                    if(!(state[currRow][currColumn-i] == Piece.EMP)){
                        if(state[currRow][currColumn] != state[row][currColumn-i]){
                            return false;
                        }
                    }
                }
                return true;
            }
            else if(column == right){
                for(int i=1;i<colSum;i++){
                    if(!(state[currRow][currColumn+i] == Piece.EMP)){
                        if(state[currRow][currColumn] != state[row][currColumn+i]){
                            return false;
                        }
                    }
                }
                return true;
            }
            else{
                return false;
            }
        }
        else if(column == currColumn)
        {
        	int up = currRow - rowSum;
        	int down = currRow + rowSum;
            if(row == down)
            {           
                for(int i=1;i<rowSum;i++){
                    if(!(state[currRow+i][currColumn] == Piece.EMP)){
                        if(state[currRow][currColumn] != state[currRow+i][currColumn]){
                        	
                            return false;
                        }
                    }
                }
                return true;
            }
            else if(row == up){            	
                for(int i=1;i<rowSum;i++){                	
                    if(!(state[currRow-i][currColumn] == Piece.EMP)){                    	
                        if(state[currRow][currColumn] != state[currRow-i][currColumn]){
                            return false;
                        }
                    }
                }
                return true;
            }
            else{
                return false;
            }
        }
        else 
        {
            if(((row == currRow + dg1Sum) && (column == currColumn + dg1Sum))){
                for(int i=1;i<dg1Sum;i++){
                    if(!(state[currRow+i][currColumn+i] == Piece.EMP)){
                        if(state[currRow+i][currColumn+i] != state[currRow][currColumn]){
                            return false;
                        }
                    }
                }
                return true;
            }
            else if((row == currRow - dg1Sum) && (column == currColumn - dg1Sum)){
                for(int i=1;i<dg1Sum;i++){
                    if(!(state[currRow-i][currColumn-i] == Piece.EMP)){
                        if(state[currRow-i][currColumn-i] != state[currRow][currColumn]){
                            return false;
                        }
                    }
                }
                return true;
            }

            if((row == currRow + dg2Sum) && (column == currColumn - dg2Sum)){
                for(int i=1;i<dg2Sum;i++){
                    if(!(state[currRow+i][currColumn-i] == Piece.EMP)){
                        if(state[currRow][currColumn] != state[currRow+i][currColumn-i]){
                            return false;
                        }
                    }
                }
                return true;
            }
            else if((row == currRow - dg2Sum) && (column == currColumn +dg2Sum)){
                for(int i=1;i<dg2Sum;i++){
                    if(!(state[currRow-i][currColumn+i] == Piece.EMP)){
                        if(state[currRow][currColumn] != state[currRow-i][currColumn+i]){
                            return false;
                        }
                    }
                }
                return true;
            }
        }

        return true;
	}
	/**
	 * 
	 * @param currRow: row of piece
	 * @param currColumn: column of piece
	 * @return total pieces in row
	 */
	public int rowSum(int currRow, int currColumn){
        int rowSum=0;
        for(int i = 0 ;i<rows;i++){        	
        	if (state[i][currColumn] == Piece.BP || state[i][currColumn] == Piece.WP){
        		rowSum = rowSum+ 1;	
        	}
        }
        return rowSum;
    }
	/**
	 * 
	 * @param currRow: row of piece
	 * @param currColumn: column of piece
	 * @return total pieces in column
	 */
    public int colSum(int currRow,int currColumn){
        int colSum=0;
        for(int i = 0 ;i<rows;i++){
        	if (state[currRow][i] == Piece.BP || state[currRow][i] == Piece.WP){
        		colSum = colSum+ 1;
        	} 
        }
        return colSum;
    }
    /**
	 * 
	 * @param currRow: row of piece
	 * @param currColumn: column of piece
	 * @return total pieces in left diagonal
	 */
    public int dg1Sum(int currRow,int currColumn){
        int dg1Sum = 1;
        for(int i=1;i<rows;i++){
            if(currRow-i>=0 && currColumn - i >= 0) {
            	if (state[currRow - i][currColumn - i] == Piece.BP || state[currRow - i][currColumn - i] == Piece.WP){
            		dg1Sum = dg1Sum + 1;
            	}
            }
            if(currRow+i<rows && currColumn +i < rows) {
            	if (state[currRow + i][currColumn + i] == Piece.BP || state[currRow + i][currColumn + i] == Piece.WP){
            		dg1Sum = dg1Sum + 1;
            	}
            }
        }
        return dg1Sum;
    }
    /**
	 * 
	 * @param currRow: row of piece
	 * @param currColumn: column of piece
	 * @return total pieces in right diagonal
	 */
    public int dg2Sum(int currRow,int currColumn){
        int dg2Sum = 1;
        for(int i=1;i<rows;i++){
        	if(currRow-i>=0 && currColumn+i < rows) {
            	if (state[currRow - i][currColumn + i] == Piece.BP || state[currRow - i][currColumn + i] == Piece.WP){
            		dg2Sum = dg2Sum + 1;
            	}
            }
            if(currRow+i<rows && currColumn-i >=0) {
            	if (state[currRow + i][currColumn - i] == Piece.BP || state[currRow + i][currColumn - i] == Piece.WP){
            		dg2Sum = dg2Sum + 1;
            	}
            }
        }
        return dg2Sum;
    }
    /**
     * 
     * @param currRow: current row of piece
     * @param currColumn: current column of piece
     * @param row: row of piece after move
     * @param column: column of piece after move
     * Notifies the UI of the changes done
     */
    public void makeMove(int currRow, int currColumn, int row,int column){
    	state[currRow][currColumn].action = getValidMoves(currRow,currColumn);
    	for(int[] arr : state[currRow][currColumn].action){
    		if (arr[0] == row && arr[1] == column){
    			if(currPlayer == Side.BLACK && state[currRow][currColumn] == Piece.BP){
    				state[row][column] = Piece.BP;
    				state[currRow][currColumn] = Piece.EMP;
    				setChanged();
            		notifyObservers(Side.BLACK);
    			}
    			if(currPlayer == Side.WHITE && state[currRow][currColumn] == Piece.WP){
    				state[row][column] = Piece.WP;
    				state[currRow][currColumn] = Piece.EMP;
    				setChanged();
            		notifyObservers(Side.WHITE);
    			}
    		}
    	}
    }
    /**
     * 
     * @param pieces: connected set of pieces
     * @param piece: piece to be checked for connection
     * @return true if all the piece is connected to other pieces
     */
    private static boolean isConnected(ArrayList<int[]> pieces,int[] piece) {
        for (int[] target : pieces) {
            int xd = Math.abs(piece[0] - target[0])
                    , yd = Math.abs(piece[1] - target[1]);
            if (xd <= 1 && yd <= 1) {
                return true;
            }
        }
        return false;
    }
    /**
     * 
     * @param player: AI or human
     * @return coordinates of all the pieces of given player
     */
    ArrayList<int[]> getCoordinates(Side player) {
        ArrayList<int[]> result = new ArrayList<int[]>();
        int y = 0;
        for (Piece[] row : state) {
            int x = 0;
            for (Piece piece: row) {
                if (piece.side() == player && piece.side() != null) {
                    int[] temp = new int[2];
                    temp[0] = x; temp[1] = y;
                    result.add(temp);
                }
                x++;
            }
            y++;
        }
        return result;
    }
    /**
     * 
     * @param side: for which win condition is to be checked
     * @return true iff the game is won by a side
     */
    public boolean gameOver(Side side) {
    	if(side == Side.BLACK){
    		return piecesContiguous(Side.BLACK);
    	}
    	else{
    		return piecesContiguous(Side.WHITE);
    	}
         
    }
    /**
     * 
     * @param player: AI or human
     * @return true if all pieces of the given player are conneted
     */
    boolean piecesContiguous(Side player) {
        ArrayList<int[]> remaining = getCoordinates(player);
        if (remaining.size() <= 1) {
            return true;
        }
        ArrayList<int[]> grouped = new ArrayList<int[]>();
        grouped.add(remaining.get(0)); remaining.remove(0);
        boolean connected, allAway;
        while (remaining.size() != 0) {
            allAway = true;
            for (int[] piece : remaining) {
                connected = isConnected(grouped, piece);
                if (connected) {
                    grouped.add(piece); remaining.remove(piece);
                    allAway = false;
                    break;
                }
            }
            if (allAway) {
                return false;
            }
        }
        return true;
    }
}