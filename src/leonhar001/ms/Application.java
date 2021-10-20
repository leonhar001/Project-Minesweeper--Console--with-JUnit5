package leonhar001.ms;

import leonhar001.ms.model.Board;
import leonhar001.ms.view.ConsoleBoard;

public class Application {

	public static void main(String[] args) {
		Board board = new Board(6, 6, 6);
		new ConsoleBoard(board);
	}

}