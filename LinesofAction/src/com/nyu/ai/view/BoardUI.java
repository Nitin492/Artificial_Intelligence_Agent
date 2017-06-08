package com.nyu.ai.view;
/**
*
* @author Nitin
*/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import edu.nyu.ai.loa.*;
/***
 * Board UI to display the board and piece also
 * attaches action listeners to buttons 
 */
public class BoardUI implements Observer{
	
	public Side side;
	public Side opponent;
    public String level;
    public int size;
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JLabel resultLabel = new JLabel("Result", SwingConstants.CENTER);
    private JTextArea analysisLabel = new JTextArea("Analyis");
    private JPanel LOABoard;
    private  JButton[][] squares = new JButton[6][6];
    private  JButton AIButton = new JButton("Play AI");
    public Board board;
    private ActionClass actionEvent;
    
	
	public BoardUI(Side c,String l,int s) {
		this.side = c;
		if(c == Side.BLACK){
			this.opponent = Side.WHITE;
		}else{
			this.opponent = Side.BLACK;
		}
		this.level = l;
		this.size = s;
		board = new Board(side);
		actionEvent = new ActionClass(board,squares,side);
    	initComponents();
        placePiecesOnBoard(board,squares,size);
        
    }
	/**
	 * This method is called from within the constructor to initialize the form.
	 * This function initializes all the UI components
	 * i.e all the panel and squares of LOA board
	 */
	private void initComponents(){
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		resultLabel.setOpaque(true);
		analysisLabel.setLineWrap(true);
		analysisLabel.setBackground(Color.LIGHT_GRAY);
		Font customFont = new Font("Serif", Font.BOLD+Font.ITALIC, 24);
		resultLabel.setFont(customFont);
		gui.add(resultLabel, BorderLayout.PAGE_START);
		gui.add(analysisLabel, BorderLayout.PAGE_END);
		LOABoard = new JPanel(new GridLayout(0, size)){
            /**
             * Override for the preferred size to return the largest it can, in
             * a square shape.  Must (must, must) be added to a GridBagLayout
             * as the only component.
             */
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize = null;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension(
                            (int)d.getWidth(),(int)d.getHeight());
                } else if (c!=null &&
                        c.getWidth()>d.getWidth() &&
                        c.getHeight()>d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                int s = (w>h ? h : w);
                return new Dimension(s,s);
            }
        };
        LOABoard.setBorder(new CompoundBorder(new EmptyBorder(3,3,3,3),new LineBorder(Color.BLACK)));
        LOABoard.setBackground(Color.BLACK);
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(Color.BLACK);
        boardConstrain.add(LOABoard);
        gui.add(LOABoard);
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
            	JButton p = new JButton();
                p.setMargin(buttonMargin);
                
                ImageIcon icon = new ImageIcon(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB));
                p.setIcon(icon);
                if ((j % 2 == 1 && i % 2 == 1)|| (j % 2 == 0 && i % 2 == 0)) {
                    p.setBackground(Color.BLACK);
                } else {
                    p.setBackground(Color.BLACK);

                }
                squares[i][j] = p;
                squares[i][j].addActionListener(actionEvent);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                      LOABoard.add(squares[i][j]);
                }
            }	
	}
	/**
	 * 
	 * @param board: backend implementation of LOA board
	 * @param squares: Jbuttons for UI
	 * @param size: size of the board
	 * places all the checkers on the board
	 */
	
	public static void placePiecesOnBoard(Board board,JButton[][] squares,int size) {
    	for(int i = 0;i<size;i++){
    		for(int j = 0;j<size;j++){
    			squares[i][j].setBackground(Color.BLACK);
    			if(board.state[i][j].equals(Piece.BP)){
    				Checker checker = new Checker();
    		    	squares[i][j].setIcon(checker);
    			}
    			if(board.state[i][j].equals(Piece.WP)){
    				Checker checker = new Checker(Color.BLUE);
    		    	squares[i][j].setIcon(checker);
    			}
    			if(board.state[i][j].equals(Piece.EMP)){
    				ImageIcon icon = new ImageIcon(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB));
    		    	squares[i][j].setIcon(icon);
    			}
    		} 
        }
    }
	public final JComponent getGui() {
        return gui;
    }
	/**
	 * 
	 * @param c: color chosen
	 * @param l: level chosen
	 * @param s: size chosen
	 * creates the window and runs the AI if human chooses white
	 */
	public static void createPage(String c,String l,String s) {
		Side side;
	    String level = l;
	    int size;
	    if (c == "White"){
    		side = Side.WHITE;
    	}
    	else{
    		side = Side.BLACK;
    	}
    	if (s == "5X5"){
    		size = 5;
    	}
    	else{
    		size = 6;
    	}
    	BoardUI page = new BoardUI(side,level,size);
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BoardUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BoardUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BoardUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BoardUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	
            	JFrame f = new JFrame("Lines of Action");
                f.add(page.getGui());
                f.setSize(550, 550);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);
                f.pack();
                f.setVisible(true); 
                Game game = new Game(page);
                page.AIButton.setVisible(true);
                page.resultLabel.setVisible(false);
                /**
                 * It listens to actions of a hidden button
                 * which call the AI whenever human makes a
                 * move
                 */
                page.AIButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	String analysis = game.play();
                    	page.analysisLabel.setText(analysis);
                    }
                });
                if(side == Side.WHITE){
                	page.AIButton.doClick();
                }
                else{
                	page.resultLabel.setVisible(true);
                	page.resultLabel.setText("Your Turn");
                }
                	
            }
        });
        
    }
	/**
	 * Part of observer-observable architecture
	 * updates the display after every move and call AI on 
	 * AI's turn 
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		//System.out.println("Update called");
		resultLabel.setVisible(true);
		resultLabel.setText("Your Turn");
		BoardUI.placePiecesOnBoard(board,squares,5);
		if (board.gameOver(side)){
			//System.out.println(board.currPlayer);
			//BoardUI.placePiecesOnBoard(board,squares,5);
			System.out.println("Human wins");
			resultLabel.setVisible(true);
			resultLabel.setText("You Won");
			resultLabel.setBackground(Color.green);
		}
		else if (board.gameOver(opponent)){
			//System.out.println(board.currPlayer);
			resultLabel.setVisible(true);
			
			System.out.println("AI wins");
			resultLabel.setText("You Lost");
			resultLabel.setBackground(Color.red);
		}
		else{
			
			AIButton.setVisible(false);
			if (side == (Side)arg){
				AIButton.doClick();
			}
		}
		
	}

}
