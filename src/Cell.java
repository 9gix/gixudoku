
public class Cell {
	private char letter;
	private boolean editable;
	private boolean conflict;
	
	public Cell(){
		this.letter = ' ';
		editable = true;
	}
	
	public Cell(char letter){
		setText(letter);
		editable = false;
	}
	
	public char getLetter(){
		return this.letter;
	}
	
	public boolean isEditable(){
		return editable;
	}
	
	public void setText(char letter){
		this.letter = letter;
	}
	
	public void setStatus(boolean conflict){
		this.conflict = conflict;
	}
	public boolean isConflict(){
		return conflict;
	}
	public String toString(){
		return letter+"";
	}
}
