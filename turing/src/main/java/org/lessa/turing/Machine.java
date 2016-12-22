package org.lessa.turing;

import java.util.List;
import java.util.Set;

public interface Machine {

	final Character DEFAULT_BLANK_SYMBOL = '⊔';
	final Character DEFAULT_START_SYMBOL = '▷';

	/**
	 * Finite non-empty set of symbols, including the special blank symbol. In
	 * the formal definition, this is denoted by the capital Greek letter Γ
	 * ("gamma").
	 * 
	 * @return immutable set of valid machine states
	 */
	Set<Character> alphabetSymbols();

	/**
	 * Special blank symbol. In the formal definition, this is denoted by the
	 * symbol '⊔'.
	 * 
	 * @return blank symbol
	 */
	Character blankSymbol();

	/**
	 * Set of final or accepting states. The initial tape contents is said to be
	 * accepted by the machine if it eventually halts in a state from this set.
	 * In the formal definition, this is denoted by the capital letter F.
	 * 
	 * @return immutable set of final machine states
	 */
	Set<String> finalStates();

	/**
	 * Special tape start symbol. In the formal definition, this is denoted by a
	 * the symbol '▷'.
	 * 
	 * @return tape start symbol
	 */
	Character startSymbol();

	/**
	 * Initial state of the machine prior to performing any transitions. In the
	 * formal definition, this is denoted by the lower-case letter q with a zero
	 * subscript ("q_0").
	 * 
	 * @return initial machine state
	 */
	String initialState();

	/**
	 * Set of symbols that can occur in the input tape, excluding the special
	 * blank symbol. In the formal definition, this is denoted by the capital
	 * Greek letter Σ ("sigma").
	 * 
	 * @return immutable set of valid input symbols
	 */
	Set<Character> inputSymbols();

	/**
	 * Runs the machine. If the machine diverges, it emits an
	 * {@link Event.OnDiverged} event and if it halts with one of the final
	 * states, it emits an {@link Event.OnHalted} event.
	 */
	void run();

	/**
	 * Finite non-empty set of states, each of which is a valid internal machine
	 * state. In the formal definition, this is denoted by the capital letter Q.
	 * 
	 * @return set of valid machine states
	 */
	Set<String> states();

	/**
	 * Finite non-empty list of tapes. There will always exist at least one tape.
	 * The input tape is always at the first list index and the output tape at
	 * the last list index. If the machine has a single tape, then it will be
	 * both the input and output tape.
	 * 
	 * @return immutable list of tapes
	 */
	List<Tape> tapes();

	/**
	 * Partial function called transition function. Takes a non-final state and
	 * the symbols on the input and work tapes, and returns the machine's next
	 * state and the symbols to write on the work and output tapes. In the formal
	 * definition, this is denoted by the lower-case Greek letter δ ("delta").
	 * 
	 * @return
	 */
	TransitionFunction transitionFunction();
}