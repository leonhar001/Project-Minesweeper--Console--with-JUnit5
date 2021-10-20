package leonhar001.ms.view;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import leonhar001.ms.exception.ExitException;
import leonhar001.ms.exception.ExplosionException;
import leonhar001.ms.model.Board;

public class ConsoleBoard {
	private Board board;
	private Scanner input = new Scanner(System.in);
	
	public ConsoleBoard(Board board) {
		this.board = board;
		
		execGame();
	}

	private void execGame() {
		try {
			
			boolean keepGoing = true;
			while(keepGoing) {
				gameCicle();
				System.out.println("Another Game? (Y/n)");
				String ans = input.nextLine();
				if("n".equalsIgnoreCase(ans)) {
					keepGoing = false;
				} else {
					board.restart();
				}
			}
		} catch (ExitException e) {
			System.out.println("XOXO!");
		} finally {
			input.close();
		}
	}

	private void gameCicle() {
		try {
			while(!board.resolvedField()) {
				System.out.println(board);
				
				String commands = getCommands("Enter (x,y): ");
				
				Iterator<Integer> xy = Arrays.stream(commands.split(","))
					.map(s -> Integer.parseInt(s.trim()))
					.iterator();
				
				commands = getCommands("1 - Open or 2 - (Un)Mark");
				
				if("1".equals(commands)) {
					board.open(xy.next(), xy.next());
				} else if("2".equals(commands)) {
					board.flag(xy.next(), xy.next());
				}
			}
			
			System.out.println(board);
			System.out.println("You win!");
		} catch (ExplosionException e) {
			System.out.println(board);
			System.out.println("You loose!");
		}
	}
	
	private String getCommands(String txt) {
		System.out.print(txt);
		String commands = input.nextLine();
		
		if("exit".equalsIgnoreCase(commands)) {
			throw new ExitException();
		} 
		return commands;
	}
	
}
