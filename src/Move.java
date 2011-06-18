public class Move {
	public final int x;
	public final int y;
	public final char prevLetter;
	public final char currLetter;
	public Move(int x, int y, char prevLetter, char currLetter) {
		this.x = x;
		this.y = y;
		this.prevLetter = prevLetter;
		this.currLetter = currLetter;
	}
}
