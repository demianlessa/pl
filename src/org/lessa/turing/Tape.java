package org.lessa.turing;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Tape {

	private final Character blankSymbol;
	private int currentIndex;
	private int minUsedIndex;
	private int maxUsedIndex;
	private Map<Integer, Character> tapeSymbols;

	public Tape(Character blankSymbol, Character startSymbol) {
		this.blankSymbol = blankSymbol;
		this.currentIndex = 0;
		this.minUsedIndex = 0;
		this.maxUsedIndex = 0;
		this.tapeSymbols = new HashMap<>();
		this.tapeSymbols.put(currentIndex, startSymbol);
	}

	public Tape(Character blankSymbol, Character startSymbol, Character... contents) {
		this(blankSymbol, startSymbol);
		for (int i = 1; i <= contents.length; i++) {
			this.tapeSymbols.put(i, contents[i]);
		}
	}

	public void move(Move direction) {
		currentIndex += direction == Move.RIGHT ? 1 : direction == Move.LEFT ? -1 : 0;
		if (currentIndex < 0) {
			throw new IllegalStateException("Error: tried to move beyond the start of the tape.");
		}
	}

	public Character read() {
		final Character symbol = tapeSymbols.get(currentIndex);
		return symbol == null ? blankSymbol : symbol;
	}

	public void write(Character symbol) {
		tapeSymbols.put(currentIndex, symbol);
		if (currentIndex > maxUsedIndex) {
			maxUsedIndex = currentIndex;
		}
		if (currentIndex < minUsedIndex) {
			minUsedIndex = currentIndex;
		}
	}

	@Override
	public String toString() {
		final char[] cells = new char[maxUsedIndex - minUsedIndex + 1];
		IntStream.range(minUsedIndex, maxUsedIndex + 1).forEach(ix -> cells[ix] = tapeSymbols.get(ix));
		return new String(cells);
	}
}
