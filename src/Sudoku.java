import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Sudoku {
	private Cell[][] grid = new Cell[9][9];
	private Stack<Move> undoHistory;
	private Stack<Move> redoHistory;
	private String answer;
	private boolean completed = false;
	boolean flag = true;
	boolean DEBUG = true;

	public Sudoku() {

	}

	public boolean setLetter(int row, int col, char letter) {
		boolean is1to9 = (int) letter >= 49	&& (int) letter <= 57;
		if (grid[row][col].isEditable() && (!conflict(row, col, letter) || flag) && is1to9) {
			char prevLetter = grid[row][col].getLetter();
			grid[row][col].setText(letter);
			undoHistory.push(new Move(row, col, prevLetter, grid[row][col]
					.getLetter()));
		} else if ((int) letter == 8 || letter == 127
				&& grid[row][col].isEditable()) {
			grid[row][col].setText(' ');
		}
		if (!markConflict() && isFilled()) {
			completed = true;
		}
		return false;
	}

	public boolean markConflict() {
		// clear previous conflict
		clearPreviousConflict();
		boolean conflict = false;
		
		// check conflict for each cell with the others cell
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				
				ArrayList<Cell> cellList;
				// check repetition on the row
				cellList = hasRepetition(getAllColumnCell(row));
				conflict = setConflictStatus(conflict, cellList);
				
				// check repetition on the column
				cellList = hasRepetition(getAllRowCell(col));
				conflict = setConflictStatus(conflict, cellList);
				
				// check repetition on the region
				cellList = hasRepetition(getAllRegionCell(convertToRegion(row), convertToRegion(col)));
				conflict = setConflictStatus(conflict, cellList);
			}
		}
		return conflict;
	}

	private void clearPreviousConflict() {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				Cell cell = grid[row][col];
				cell.setStatus(false);
			}
		}
	}

	// set all conflicting cell status to conflict 
	private boolean setConflictStatus(boolean conflict, ArrayList<Cell> cellList) {
		for (Cell cell : cellList) {
			cell.setStatus(true);
			conflict = true;
		}
		return conflict;
	}

	
	private boolean conflict(int row, int col, char letter) {
		int x = convertToRegion(row);
		int y = convertToRegion(col);
		ArrayList<Cell> rowNum = getAllColumnCell(row);
		ArrayList<Cell> colNum = getAllRowCell(col);
		ArrayList<Cell> regNum = getAllRegionCell(x, y);
		boolean conflict = compare(letter, rowNum) || compare(letter, colNum)
				|| compare(letter, regNum);
		if (DEBUG) {
			System.out.println("Row : " + rowNum);
			System.out.println("Col : " + colNum);
			System.out.println("Reg : " + regNum);
			System.out.println(conflict ? "CONFLICT" : "SAFE");
		}
		return conflict;
	}

	public static int convertToRegion(int var) {
		return var < 3 ? 0 : var < 6 ? 3 : 6;
	}

	private boolean compare(char letter, ArrayList<Cell> regNum) {
		for (Cell r : regNum) {
			if (r.getLetter() == letter) {
				return true;
			}
		}
		return false;
	}

	public void newGame(String file) {
		String[] questionAnswer = getQuestionAnswer(file);
		makeBoard(questionAnswer[0]);
		answer = questionAnswer[1];

		undoHistory = new Stack<Move>();
		redoHistory = new Stack<Move>();
		completed = false;
	}

	private String[] getQuestionAnswer(String file) {
		ArrayList<String> questionList = new ArrayList<String>();
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			String line;
			while ((line = input.readLine()) != null) {
				questionList.add(line);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int rndQuestion = (int) (Math.random() * questionList.size());
		String[] questionAnswer = questionList.get(rndQuestion).split(" ");
		return questionAnswer;
	}

	public void makeBoard(String str) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				char chr = str.charAt(i * 9 + j);
				if (chr == '0') {
					grid[i][j] = new Cell();
				} else {
					grid[i][j] = new Cell(chr);
				}
			}
		}
	}

	public boolean isEnded() {
		return completed;
	}

	private boolean isFilled() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (grid[i][j].getLetter() == ' ') {
					return false;
				}
			}
		}
		return true;
	}

	private ArrayList<Cell> getAllRegionCell(int x, int y) {

		ArrayList<Cell> regNumbers = new ArrayList<Cell>();
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				regNumbers.add(grid[row + x][col + y]);
			}
		}
		return regNumbers;
	}

	private ArrayList<Cell> getAllColumnCell(int row) {
		ArrayList<Cell> chr = new ArrayList<Cell>();
		for (int col = 0; col < 9; col++) {
			chr.add(grid[row][col]);
		}
		return chr;
	}

	private ArrayList<Cell> getAllRowCell(int col) {
		ArrayList<Cell> colNumbers = new ArrayList<Cell>();
		for (int row = 0; row < 9; row++) {
			colNumbers.add(grid[row][col]);
		}
		return colNumbers;
	}

	public static ArrayList<Cell> hasRepetition(ArrayList<Cell> arr) {
		ArrayList<Cell> conflictLetters = new ArrayList<Cell>();
		for (Cell numToFind : arr) {
			int occurence = 0;
			for (int i = 0; i < arr.size(); i++) {
				if (arr.get(i).getLetter() != ' ') {
					if (arr.get(i).getLetter() == numToFind.getLetter()) {
						occurence++;
						if (occurence > 1) {
							conflictLetters.add(numToFind);
						}
					}
				}
			}
		}

		return conflictLetters;
	}

	public boolean undo() {
		if (!undoHistory.isEmpty()) {
			Move move = undoHistory.pop();
			redoHistory.push(move);
			grid[move.x][move.y].setText(move.prevLetter);
			return true;
		}
		return false;
	}

	public boolean redo() {
		if (!redoHistory.isEmpty()) {
			Move move = redoHistory.pop();
			undoHistory.push(move);
			grid[move.x][move.y].setText(move.currLetter);
			return true;
		}
		return false;
	}

	public Cell getCell(int x, int y) {
		return grid[x][y];
	}

	public void setHelper(boolean hint) {
		this.flag = hint;
	}

	public char getLetter(int x, int y) {
		return getCell(x, y).getLetter();
	}

	public void solution() {
		makeBoard(answer);
	}

	public String toString() {
		String str = "";
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				str = str.concat("" + grid[i][j].getLetter());
			}
			str = str.concat("\n");
		}
		return str;
	}
}
