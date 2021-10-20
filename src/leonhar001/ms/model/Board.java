package leonhar001.ms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import leonhar001.ms.exception.ExplosionException;


public class Board {
	
	private int rows;
	private int columns;
	private int mines;
	
	private final List<BField> fields = new ArrayList<>();

	public Board(int rows, int columns, int mines) {
		this.rows = rows;
		this.columns = columns;
		this.mines = mines;
		
		generateFields();
		associateNeighbors();
		plantMines();
	}
	
	public void open(int row, int column) {
		try {
			fields.stream()
				.filter(f->f.getRow() == row && f.getColumn() == column)
				.findFirst()
				.ifPresent(c -> c.openField());
		} catch (ExplosionException e) {
			fields.forEach(f->f.setOpenned(true));
			throw e;
		}
		
	}
	public void flag(int row, int column) {
		fields.stream()
			.filter(f->f.getRow() == row && f.getColumn() == column)
			.findFirst()
			.ifPresent(c -> c.alternateFlag());
	}

	private void generateFields() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				fields.add(new BField(r,c));
			}
		}
	}
	
	private void associateNeighbors() {
		for (BField f1 : fields) {
			for (BField f2 : fields) {
				f1.addNeighbor(f2);
			}
		}
	}

	private void plantMines() {
		long toPlantMines = 0;
		
		Predicate<BField> mined = n -> n.isMined();
		
		do {
			int random = (int)(Math.random() * fields.size());
			fields.get(random).mine();
			toPlantMines = fields.stream().filter(mined).count();
		} while (toPlantMines<mines);
	}
	
	public boolean resolvedField() {
		return fields.stream().allMatch(f -> f.resolvedField());
	}
	
	public void restart() {
		fields.stream().forEach(f->f.restart());
		plantMines();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---");
		for (int c = 0; c < columns; c++) {
			sb.append("(");
			sb.append(c);
			sb.append(")");
		}
			sb.append("\n");
			
		int i=0;
		for (int  r = 0; r < rows; r++) {
			sb.append("(");
			sb.append(r);
			sb.append(")");
			for (int c = 0; c < columns; c++) {
				sb.append(" ");
				sb.append(fields.get(i));
				sb.append(" ");
				i++;
			}
			sb.append("\n");
		}
 		return sb.toString();
	}
}
