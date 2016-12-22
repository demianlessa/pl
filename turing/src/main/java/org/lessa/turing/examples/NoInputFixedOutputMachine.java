package org.lessa.turing.examples;

import org.lessa.turing.MachineBuilder;
import org.lessa.turing.Move;
import org.lessa.turing.TransitionFunction;
import org.lessa.turing.TransitionFunctionBuilder;
import org.lessa.turing.Machine;

/**
 * Single tape machine, no input, simulates a constant function that outputs the
 * string '0⊔1⊔' on the tape. The final tape state is always '▷0⊔1⊔'.
 */
public class NoInputFixedOutputMachine {

	public static void main(String[] args) {

		final Character ZERO = '0';
		final Character ONE = '1';

		final String Q0 = "q0";
		final String Q1 = "q1";
		final String Q2 = "q2";
		final String Q3 = "q3";
		final String Q4 = "q4";
		final String H = "HALT";

		final Character[] startSymbol = new Character[] { Machine.DEFAULT_START_SYMBOL };
		final Character[] blankSymbol = new Character[] { Machine.DEFAULT_BLANK_SYMBOL };
		final Character[] oneSymbol = new Character[] { ONE };
		final Character[] zeroSymbol = new Character[] { ZERO };

		final Move[] moveRight = new Move[] { Move.RIGHT };

		final TransitionFunction program = new TransitionFunctionBuilder(1)
		      .withTranstion(Q0, startSymbol, startSymbol, moveRight, Q1)
		      .withTranstion(Q1, blankSymbol, zeroSymbol, 	moveRight, Q2)
		      .withTranstion(Q2, blankSymbol, blankSymbol, moveRight, Q3)
		      .withTranstion(Q3, blankSymbol, oneSymbol, 	moveRight, Q4)
		      .withTranstion(Q4, blankSymbol, blankSymbol, moveRight, H)
		      .build();

		new MachineBuilder()
				.withAlphabetSymbols(ZERO, ONE)
				.withInputSymbols(ZERO, ONE)
				.withStates(Q0, Q1, Q2, Q3, Q4, H)
		      .withInitialState(Q0)
		      .withFinalStates(H)
		      .withSubscriber(new LoggingEventHandler())
		      .withTransitionFunction(program)
		      .build()
		      .run();
	}
}