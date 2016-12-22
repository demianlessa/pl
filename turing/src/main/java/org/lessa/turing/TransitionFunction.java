package org.lessa.turing;

import java.util.List;

public interface TransitionFunction {
	/**
	 * By convention, the symbol at index 0 is the current symbol under the input
	 * tape's head, the symbol at position |symbols|-1 is the current symbol
	 * under the output tape's head, and all other symbols are under the head of
	 * the respective work tape.
	 * 
	 * @param state
	 *           current machine state
	 * @param symbols
	 *           a vertical slice of symbols under all tape heads
	 * @return
	 */
	Transition apply(String state, List<Character> symbols);
}
