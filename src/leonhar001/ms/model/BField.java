package leonhar001.ms.model;

import java.util.ArrayList;
import java.util.List;

import leonhar001.ms.exception.ExplosionException;

public class BField {
	
	/*All possible field's status*/
	private boolean openned;
	private boolean mined;
	private boolean flagged;
	
	/*Fied address*/
	private final int row; 
	private final int column;
	
	/*List of field's neighbors*/
	private List<BField> neighbors = new ArrayList<>();
	
	
	BField(int row, int colums){
		this.row = row;
		this.column = colums;
	}
	
	boolean addNeighbor(BField neighbor) {
		/*Verify neighbor's position (diagonal or beside)*/
		boolean differentRow = row != neighbor.row;
		boolean differentColumn = column != neighbor.column;
		boolean diagonal = differentRow && differentColumn;
		
		int deltaRow = Math.abs(row - neighbor.row);
		int deltaColumn = Math.abs(column - neighbor.column);
		int deltaGeneral = deltaRow + deltaColumn;
		
		if(deltaGeneral == 1 && !diagonal) {
			neighbors.add(neighbor);
			return true;
		} else if(deltaGeneral == 2 && diagonal) {
			neighbors.add(neighbor);
			return  true;
		} else {
			return false;
		}
	}
	void alternateFlag() {
		if(!openned) {
			flagged = !flagged;
		}
	}
	
	boolean openField() {
		if(!openned && !flagged) {
			openned = true;
			
			if(mined) {
				throw new ExplosionException();
			}
			
			if(safeNeighborhood()) {
				neighbors.forEach(n -> n.openField()); /*Recursive*/
			}
			
			return true;
		}
		return false;
	}
	
	boolean safeNeighborhood() {
		return neighbors.stream().noneMatch(n -> n.mined);
	}
	
	boolean isFlagged() {
		return flagged;
	}
	
	public void setOpenned(boolean openned) {
		this.openned = openned;
	}

	boolean isOpenned() {
		return openned;
	}
	
	boolean isClosed() {
		return !isOpenned();
	}
	
	boolean isMined() {
		return mined;
	}
	
	int getRow() {
		return row;
	}
	
	int getColumn() {
		return column;
	}
	
	void mine() {
		mined = true;
	}
	
	boolean resolvedField() {
		boolean uncovered = openned && !mined;
		boolean protect = mined && flagged;
		
		return uncovered || protect;
	}
	
	long minesAroundNeighborhood() {
		return neighbors.stream().filter(n -> n.mined).count();
	}
	
	void restart() {
		openned  = false;
		mined = false;
		flagged = false;
	}
	
	public String toString() {
		if(flagged) {
			return "x";
		} else if(openned && mined) {
			return "*";
		} else if(openned && minesAroundNeighborhood()>0) {
			return Long.toString(minesAroundNeighborhood());
		} else if(openned) {
			return " ";
		} else {
			return "?";
		}
	}
	
}
