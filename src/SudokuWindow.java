import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


public class SudokuWindow {
	private static String[] FILELIST = { "EASY", "NORMAL", "HARD", "TEST" };
	private static String[] menuItem = {"Easy","Normal","Hard","Test","Undo", 
		"Redo","Hide Flag","Give Up" };
	Sudoku sudoku;
	JTextField[][] txtGrid;
	
	// Constructor
	public SudokuWindow(){
		sudoku = new Sudoku();
		JFrame frmSudoku = new JFrame();
		frmSudoku.setLayout(new BorderLayout());
		frmSudoku.setBounds(400, 100, 400, 500);
		
		// Resize Font Size on Window Resize
		frmSudoku.addComponentListener(new ComponentListener(){
			public void componentResized(ComponentEvent evt) {
				Component c = (Component)evt.getSource();
				Dimension newSize = c.getSize();
				for (int i = 0; i < 9; i++){
					for (int j = 0; j < 9; j++){
						txtGrid[i][j].setFont(new Font("Lucida Sans Typewriter",Font.BOLD,(int) ((newSize.getWidth()+newSize.getHeight())/25)));
					}
				}
			}
			@Override public void componentHidden(ComponentEvent e) {}
			@Override public void componentMoved(ComponentEvent e) {}
			@Override public void componentShown(ComponentEvent e) {}
		});
		
		// pnlSudokuBoard
		JPanel pnlBoard = buildBoardPanel();
//		SudokuPanel pnlBoard = new SudokuPanel();
		// pnlControl
		JPanel pnlControl = buildControlPanel();
		
		frmSudoku.add(pnlBoard,BorderLayout.CENTER);
		frmSudoku.add(pnlControl,BorderLayout.SOUTH);
		frmSudoku.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSudoku.setVisible(true);
	}

	// create Sudoku Panel on Center
	private JPanel buildBoardPanel() {
		JPanel pnlBoard = new JPanel(new GridLayout(9,9));
		pnlBoard.setBorder(new LineBorder(Color.BLACK,1));
		txtGrid = new JTextField[9][9];
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				txtGrid[i][j] = new JTextField();
				txtGrid[i][j].setHorizontalAlignment(JTextField.CENTER);
				txtGrid[i][j].setFont(new Font("Times",Font.BOLD,36));
				txtGrid[i][j].setEditable(false);

				final int iTemp = i;
				final int jTemp = j;
				txtGrid[i][j].addKeyListener(new KeyListener(){
					@Override
					public void keyPressed(KeyEvent e) {}
					
					@Override
					public void keyReleased(KeyEvent e) {
						if (!sudoku.isEnded()){
							sudoku.setLetter(iTemp, jTemp, e.getKeyChar());
							refresh();
							if (sudoku.isEnded()){
								endGame();
							}
						}	
					}
					@Override
					public void keyTyped(KeyEvent e) {}
				});
				pnlBoard.add(txtGrid[i][j]);
			}
		}
		return pnlBoard;
	}
	
	// Disable all TextField on end Game
	private void endGame(){
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				txtGrid[i][j].setEditable(false);
			}
		}
		JOptionPane.showMessageDialog(null, "Congratulation You Win", "Win",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	// Create the South Control Panel
	private JPanel buildControlPanel() {
		JPanel pnlControl = new JPanel(new GridLayout(2, menuItem.length/2, 5, 5));
		pnlControl.setBorder(new TitledBorder("Menu Items"));
		for (int i = 0; i < menuItem.length; i++) {
			JButton btn = new JButton(menuItem[i]);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String command = e.getActionCommand();
					for (int i = 0; i < menuItem.length; i++) {
						if (command.equals(menuItem[i])) {
							doOption(i);
							break;
						}
					}
				}
			});
			pnlControl.add(btn);
		}
		return pnlControl;
	}
	
	private void doOption(int i) {
		switch (i){
		// choose Difficulty
		case 0: case 1: case 2: case 3:
			sudoku.newGame(FILELIST[i]);
			sudoku.DEBUG = i == 3;
			break;
		// undo
		case 4:
			sudoku.undo();
			break;
		// redo
		case 5:
			sudoku.redo();
			break;
		// helper
		case 6:
			sudoku.setHelper(!sudoku.flag);
			break;
		case 7:
			sudoku.solution();
			break;
		}
		this.refresh();
		System.out.print(sudoku.DEBUG?sudoku:"");
	}

	private void refresh() {
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				Cell cell = sudoku.getCell(i, j);
				char letter = cell.getLetter();
				boolean editable = cell.isEditable();
				boolean conflict = cell.isConflict();
				txtGrid[i][j].setText(null);
				txtGrid[i][j].setEditable(editable);
				txtGrid[i][j].setForeground(editable?Color.BLACK:new Color(0,50,200));				
				txtGrid[i][j].setText(""+letter);
				int x = Sudoku.convertToRegion(i);
				int y = Sudoku.convertToRegion(j);
				txtGrid[i][j].setBackground(conflict?Color.RED:letter==' '?new Color(220,220,190):(x + y) % 2==0?Color.LIGHT_GRAY:new Color(150,150,150));		
			}
		}
	}
}
