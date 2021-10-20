package leonhar001.ms.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import leonhar001.ms.exception.ExplosionException;
import leonhar001.ms.model.BField;

public class BFieldTest {

	private BField bField;

	@BeforeEach
	void startBField() {
		bField = new BField(3,3);
	}
	
	/* EXAMPLE
	 * (...)(...)(...)(...)(...)
	 * (...)(2,2)(2,3)(2,3)(...)
	 * (...)(3,2)(3,3)(3,4)(...)
	 * (...)(4,2)(4,3)(4,4)(...)
	 * (...)(...)(...)(...)(...)
	 * */
	
	@Test
	void neighborTestDist1Left() {
		BField neighbor =  new BField(3,2);
		boolean result = bField.addNeighbor(neighbor);
		assertTrue(result);
	}
	
	@Test
	void neighborTestDist1Right() {
		BField neighbor =  new BField(3,4);
		boolean result = bField.addNeighbor(neighbor);
		assertTrue(result);
	}
	
	@Test
	void neighborTestDist1Up() {
		BField neighbor =  new BField(2,3);
		boolean result = bField.addNeighbor(neighbor);
		assertTrue(result);
	}
	
	@Test
	void neighborTestDist1Down() {
		BField neighbor =  new BField(4,3);
		boolean result = bField.addNeighbor(neighbor);
		assertTrue(result);
	}
	
	@Test
	void neighborTestDist2DiagonalUR() {
		BField neighbor =  new BField(2,3);
		boolean result = bField.addNeighbor(neighbor);
		assertTrue(result);
	}
	
	@Test
	void neighborTestDist2DiagonalUL() {
		BField neighbor =  new BField(2,2);
		boolean result = bField.addNeighbor(neighbor);
		assertTrue(result);
	}
	
	@Test
	void neighborTestDist2DiagonalDR() {
		BField neighbor =  new BField(4,4);
		boolean result = bField.addNeighbor(neighbor);
		assertTrue(result);
	}
	
	@Test
	void neighborTestDist2DiagonalDL() {
		BField neighbor =  new BField(4,2);
		boolean result = bField.addNeighbor(neighbor);
		assertTrue(result);
	}
	
	@Test
	void neighborTestNotReal() {
		BField neighbor =  new BField(5,4);
		boolean result = bField.addNeighbor(neighbor);
		assertFalse(result);
	}
	
	@Test
	void testDefaultConditionFlag() {
		assertFalse(bField.isFlagged());
	}
	
	@Test
	void testAlternateFlag() {
		bField.alternateFlag();
		assertTrue(bField.isFlagged());
	}
	
	@Test
	void testAlternateFlagTwoCalls() {
		bField.alternateFlag();
		bField.alternateFlag();
		assertFalse(bField.isFlagged());
	}
	
	@Test
	void openFieldNotMinedNotFlagged() {
		assertTrue(bField.openField());
	}
	
	@Test
	void openFieldNotMinedButFlagged() {
		bField.alternateFlag();
		assertFalse(bField.openField());
	}
	
	@Test
	void openFieldMinedButFlagged() {
		bField.mine();
		bField.alternateFlag();
		assertFalse(bField.openField());
	}
	
	@Test
	void openFieldMinedNotFlagged() {
		bField.mine();
		assertThrows(ExplosionException.class, () -> bField.openField());
	}
	
	@Test
	void openWithNeighbors1() {
		BField field22 = new BField(2,2);
		
		BField field11 = new BField(1,1);
		field22.addNeighbor(field11);
		
		bField.addNeighbor(field22);
		
		bField.openField();
		
		assertTrue(field22.isOpenned() && field11.isOpenned());
	}
	
	@Test
	void openWithNeighbors2() {
		BField field22 = new BField(2,2);
		BField field11 = new BField(1,1);
		BField field12 = new BField(1,2);
		field12.mine();
		
		field22.addNeighbor(field12);
		field22.addNeighbor(field11);
		bField.addNeighbor(field22);
		
		bField.openField();
		
		assertTrue(field22.isOpenned() && field11.isClosed());
	}
	
	@Test
	void isFieldResolvedProtect() {
		bField.mine();
		bField.alternateFlag();
		
		assertTrue(bField.resolvedField());
	}
	
	@Test
	void isFieldResolvedUncovered() {
		bField.openField();
		
		assertTrue(bField.resolvedField());
	}
	
	@Test
	void isFieldResolvedOnlyMined() {
		bField.mine();
		
		assertFalse(bField.resolvedField());
	}
	
	@Test
	void isFieldResolvedOnlyFlagged() {
		bField.alternateFlag();
		
		assertFalse(bField.resolvedField());
	}
	
	@Test
	void minesAroundCount1() {
		BField neighbour22 = new BField(2,2);
		neighbour22.mine();
		bField.addNeighbor(neighbour22);
		
		assertEquals(1, bField.minesAroundNeighborhood());
	}
	
	@Test
	void minesAroundCount2() {
		BField neighbour22 = new BField(2,2);
		BField neighbour43 = new BField(4,3);
		neighbour22.mine();
		neighbour43.mine();
		bField.addNeighbor(neighbour22);
		bField.addNeighbor(neighbour43);
		
		assertEquals(2, bField.minesAroundNeighborhood());
	}
	
	@Test
	void restartMinesTest() {
		bField.mine();
		bField.alternateFlag();
		bField.openField();
		
		bField.restart();
		assertFalse(bField.isOpenned() && bField.isFlagged() && bField.isMined());
	}
	
	@Test
	void toStringFlagged() {
		bField.alternateFlag();
		
		assertEquals("x", bField.toString());
	}
	
	@Test
	void toStringWithBomb() {
		bField.openField();
		bField.mine();
		
		assertEquals("*", bField.toString());
	}
	
	@Test
	void toStringOpennedWith1BombOnNeighborhood() {
		bField.openField();
		BField neighbour22 = new BField(2,2);
		neighbour22.mine();
		bField.addNeighbor(neighbour22);
		
		assertEquals("1", bField.toString());
	}

	@Test
	void toStringOpennedWith2BombOnNeighborhood() {
		bField.openField();
		
		BField neighbour22 = new BField(2,2);
		BField neighbour32 = new BField(3,2);
		
		neighbour22.mine();
		neighbour32.mine();
		
		bField.addNeighbor(neighbour22);
		bField.addNeighbor(neighbour32);
		
		assertEquals("2", bField.toString());
	}
	
	@Test
	void toStringOnlyOpenned() {
		bField.openField();
		
		assertEquals(" ", bField.toString());
	}
	
	@Test
	void toStringNothing() {
		assertEquals("?", bField.toString());
	}
}
