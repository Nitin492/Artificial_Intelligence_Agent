package edu.nyu.ai.loa;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.ArrayList;
/**
*
* @author Nitin
* The AI player class, It makes moves and runs 
* Alpha beta algorithm as AI
*/
public class AIPlayer  {
	private int size;
	private boolean flag = true;
	private Piece human;
	private Piece ai;
	private int currdepth;
	private int depthLimit;
	private int minUtility;
	private int maxUtility;
	private int totalnodes;
	private int maxValEval;
	private int minValEval;
	private int maxValPrune;
	private int minValPrune;
	private Board localboard;
	public String analysis;
	public Piece[][] laststate;
	public AIPlayer(Side side,int size,int depth) {
		this.size = size;
		this.depthLimit = depth;
		if(side == Side.BLACK){
			human = Piece.WP;
			ai = Piece.BP;			
		}else{
			human = Piece.BP;
			ai = Piece.WP;
		}
		this.currdepth = 0;
		this.minUtility = -100;
		this.maxUtility = +100;
		this.totalnodes = 0;
		this.maxValEval = 0;
		this.minValEval = 0;
		this.maxValPrune = 0;
		this.minValPrune = 0;
		this.laststate = new Piece[size][size];
		this.localboard = new Board(ai.side());
	}
	/**
	 * 
	 * @param board
	 * saves board locally
	 */
	private void saveState(Board board) {
		for(int i=0;i<size;i++){
        	System.arraycopy(board.state[i], 0, laststate[i], 0, size);
        }
    }
	/**
	 * 
	 * @param board
	 * returns local board
	 */
	private void getState(Board board) {
		for(int i=0;i<size;i++){
        	System.arraycopy(laststate[i], 0, board.state[i], 0, size);
        }
    }
	/**
	 * 
	 * @param board
	 * @return Analysis of the alpha beta algorithm
	 * calls the Alpha beta algorithm and decides on
	 * the best move possible to win game for AI
	 * and makes that move
	 */
	String makeMove(Board board) {
		board.currPlayer = ai.side();
        saveState(board);
        long starttime = System.currentTimeMillis();
        int moves[] = alphaBetaSearch(board.state);
        long endTime = System.currentTimeMillis();
        getState(board);
        int row = moves[0];
        int column = moves[1];
        int newRow = moves[2];
        int newColumn = moves[3];
        if(newRow>=0){
        	System.out.println("Moved from "+row +","+column+"to "+ newRow +", "+newColumn);
            board.makeMove(row,column,newRow,newColumn);
        }
        analysis ="Time Taken: "+(endTime - starttime)/1000+"sec Depth: "+currdepth
                +", Nodes: "+ totalnodes
                +", MaxVal Eval: "+maxValEval
                +", MinVal Eval: "+minValEval
                +", MaxVal Prune: "+maxValPrune
                +", MinVal Prune: "+minValPrune;
        System.out.println(analysis);
        board.currPlayer = human.side();
        return analysis;    
	}
	/**
	 * 
	 * @param state: current board
	 * @return array of moves and v 
	 * calls alphabeta algorithm
	 */
	int[] alphaBetaSearch(Piece state[][]){
	    int moves[] = new int[5];
	    moves = alphaBeta(state,minUtility,maxUtility,0,moves);
	    return moves;
	}
	/**
	 *  Alpha beta algorithm
	 * @param state: board
	 * @param a: alpha i.e. minimum utility
	 * @param b: beta i.e. maximum utility
	 * @param depth
	 * @param moves: dummy array to store and forward moves made
	 * @return: best move if found
	 */  
    int[] alphaBeta(Piece state[][],int a,int b,int depth,int moves[]){
        Piece localState[][] = new Piece[size][size];
        boolean found = false;
        for(int i=0;i<size;i++){
        	System.arraycopy(state[i], 0, localState[i], 0, size);
        }
        ArrayList<int[]> actions = new ArrayList<>();
        actions.addAll(getAllValidAction(localState,ai));       
        int v = -100000;        
        for(int i=0;i<actions.size();i++){
            int row = actions.get(i)[0];
            int column = actions.get(i)[1];
            int newRow = actions.get(i)[2];
            int newColumn = actions.get(i)[3];            
            v = max(v,minValue(result(localState,row,column,newRow,newColumn),a,b,depth+1,moves)[4]);
            this.totalnodes++;            
            if(v>=b){
                found = true;
                moves[0] = row;
                moves[1] = column;
                moves[2]=newRow;
                moves[3]=newColumn;
                moves[4] = v;              
                this.maxValPrune++;
                return moves;
            }
            a = max(a,v);
        }
        if(!found){
        	if(flag){
        		moves = actions.get(actions.size()-6);
        		System.out.println("flag:"+flag);
        		flag = false;
	            this.maxValPrune++;
        	}else{
        		moves = actions.get(actions.size()-2);
        		System.out.println("flag:"+flag);
	            this.maxValPrune++;
        	}
        }
        return moves;
    }
    /**
     * 
     * @param state
     * @param a
     * @param b
     * @param depth
     * @param moves
     * @return 
     */
    int[] maxValue(Piece state[][],int a,int b,int depth,int moves[]){
        if(depth>this.currdepth){
            this.currdepth= depth;
        }
        Piece localState[][] = new Piece[size][size];
        for(int i=0;i<size;i++){
        	System.arraycopy(state[i], 0, localState[i], 0, size);
        }
    	if(terminalTest(localState,human)){
            moves[4]= minUtility;
            return moves;
        }
        if(terminalTest(localState,ai)){
            moves[4] = maxUtility;
            return moves;
        }       
        if(depth>=depthLimit){
            moves[4] = (int)evaluationFunction(localState);         
            this.maxValEval++;
            return moves;
        }
        ArrayList<int[]> actions = new ArrayList<>();
        actions.addAll(getAllValidAction(localState,ai));
        int v = -100000;
        for(int i=0;i<actions.size();i++){
            int row = actions.get(i)[0];
            int column = actions.get(i)[1];
            int newRow = actions.get(i)[2];
            int newColumn = actions.get(i)[3];         
            v = max(v,minValue(result(localState,row,column,newRow,newColumn),a,b,depth+1,moves)[4]);            
            this.totalnodes++;
            if(v>=b){
                moves[4] = v;
                this.maxValPrune++;           
                return moves;
            }
            a = max(a,v);           
        }
        return moves;
    }

    int[] minValue(Piece state[][],int a,int b,int depth,int moves[]){    	
        if(depth>this.currdepth){
            this.currdepth= depth;
        }
        Piece localState[][] = new Piece[size][size];
        for(int i=0;i<size;i++){
        	System.arraycopy(state[i], 0, localState[i], 0, size);
        }
    	if(terminalTest(localState,human)){
            moves[4]= minUtility;
            return moves;
        }
        if(terminalTest(localState,ai)){
            moves[4] = maxUtility;
            return moves;
        }
        
        if(depth>= depthLimit){
            moves[4] = (int)evaluationFunction(localState);
            this.minValEval++;
            return moves;
        }
        ArrayList<int[]> actions = new ArrayList<>();
        actions.addAll(getAllValidAction(localState,human));
        int v = 100000;
        for(int i=0;i<actions.size();i++){
            int row = actions.get(i)[0];
            int column = actions.get(i)[1];
            int newRow = actions.get(i)[2];
            int newColumn = actions.get(i)[3];
            v = min(v,maxValue(result(localState,row,column,newRow,newColumn),a,b,depth+1,moves)[4]);
            this.totalnodes++;
            if(v<=a){
                this.minValPrune++;
                moves[4] = v;
                return moves;
            }
            b = min(b,v);
        }
        return moves;
    }
    /**
     * 
     * @param state
     * @return evalution value for the current state
     */
    private double evaluationFunction(Piece[][] state) {
    	localboard = new Board(ai.side());
    	for(int i=0;i<size;i++){
        	System.arraycopy(state[i], 0, localboard.state[i], 0, size);
        }
        ArrayList<int[]> aipieces = new ArrayList<>();	        
        ArrayList<int[]> humanpieces = new ArrayList<>();
        if (ai == Piece.BP){
        	aipieces.addAll(localboard.getCoordinates(Side.BLACK));
        	humanpieces.addAll(localboard.getCoordinates(Side.WHITE));
        }
        if (ai == Piece.WP){
        	aipieces.addAll(localboard.getCoordinates(Side.WHITE));
        	humanpieces.addAll(localboard.getCoordinates(Side.BLACK));
        }
        double aiDist = distanceBetweenPieces(aipieces);
        double humDist = distanceBetweenPieces(humanpieces);
        return humDist-aiDist;
    }
    /**
     * 
     * @param pieces
     * @return cumulative distance in pieces
     */
    private double distanceBetweenPieces(ArrayList<int[]> pieces){
        double totalSum = 0;
        for(int i = 0; i < pieces.size();i++){
            for(int j = 0; j < i;j++){
                totalSum += eucledianDistance(pieces.get(i), pieces.get(j));
            }
        }
        return totalSum;
    }
    /**
     * 
     * @param one
     * @param two
     * @return eucledian distance between piece
     */
    private double eucledianDistance(int[] piece1, int[] piece2){
        int xSq = (int) Math.pow(piece1[1]-piece2[1],2);
        int YSq = (int) Math.pow(piece1[0]-piece2[0],2);
        return Math.sqrt(xSq+YSq);
    }
    /**
     * 
     * @param state
     * @param row: move from row
     * @param column: move from column
     * @param newRow: move to row
     * @param newColumn: move from column
     * @return resulting state after making move
     */
   private Piece[][] result(Piece state[][],int row,int column, int newRow,int newColumn){
        Piece result[][] = new Piece[size][size];
        for(int i=0;i<size;i++){
        	System.arraycopy(state[i], 0, result[i], 0, size);
        }
        if(result[row][column] == human){
            result[newRow][newColumn] = human;
        }
        if(result[row][column] == ai){
            result[newRow][newColumn] = ai;
        }
        result[row][column] = Piece.EMP;
        return result;
    }
   /**
    * 
    * @param state
    * @param player
    * @return array of all the valid moves for given player
    */
    ArrayList<int[]> getAllValidAction(Piece state[][],Piece player){
    	localboard = new Board(ai.side());
    	for(int i=0;i<size;i++){
        	System.arraycopy(state[i], 0, localboard.state[i], 0, size);
        }
        ArrayList<int[]> action = new ArrayList<>();
        ArrayList<int[]>pieces = new ArrayList<>();
        if (player == Piece.BP){
        	pieces.addAll(localboard.getCoordinates(Side.BLACK));
        }
        if (player == Piece.WP){
        	pieces.addAll(localboard.getCoordinates(Side.WHITE));
        }
        for(int i=0;i<pieces.size();i++){
            ArrayList<int[]> paction = new ArrayList<>();
            int row = pieces.get(i)[1];
            int col = pieces.get(i)[0];
            paction.addAll(localboard.getValidMoves(row,col));
            for(int j=0;j<paction.size();j++){
                int move[] = new int[4];
                move[0] = row;
                move[1] = col;
                if(paction.get(j)[0]<0){
                    continue;
                }
                move[2] = paction.get(j)[0];
                move[3] = paction.get(j)[1];
                action.add(move);
            }
        }
        return action;
    }
    /**
     * 
     * @param state
     * @param player
     * @return true if terminal condition is acieved
     */
    boolean terminalTest(Piece state[][], Piece player){
    	localboard = new Board(ai.side());
    	for(int i=0;i<size;i++){
        	System.arraycopy(state[i], 0, localboard.state[i], 0, size);
        }
    	if (player == Piece.BP){
    		if(localboard.piecesContiguous(Side.BLACK)){
	            return true;
	        }
        }
        if (player == Piece.WP){
        	if(localboard.piecesContiguous(Side.WHITE)){
	            return true;
	        }
        }
        return false;
    }
}
