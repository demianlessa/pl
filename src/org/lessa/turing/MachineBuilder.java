package org.lessa.turing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.lessa.turing.Event.EventHandler;

public class MachineBuilder {

	private Set<Character> alphabetSymbols;
	private Character blankSymbol;
	private Set<String> finalStates;
	private String initialState;
	private Set<Character> inputSymbols;
	private Tape inputTape;
	private int moreTapes;
	private Character startSymbol;
	private Set<String> states;
	private List<EventHandler> subscribers;
	private TransitionFunction transitionFunction;

	public MachineBuilder() {
		blankSymbol = Machine.DEFAULT_BLANK_SYMBOL;
		startSymbol = Machine.DEFAULT_START_SYMBOL;
		subscribers = new ArrayList<>();
	}

	public final Machine build() {

		// the blank and start symbols must always exist in the alphabet
		alphabetSymbols.add(blankSymbol);
		alphabetSymbols.add(startSymbol);

		validate();

		final List<Tape> tapes = new ArrayList<>();

		// first tape is the input
		tapes.add(inputTape == null ? new Tape(blankSymbol, startSymbol) : inputTape);

		// additional empty tapes
		for (int i = 0; i < moreTapes; i++) {
			tapes.add(new Tape(blankSymbol, startSymbol));
		}

		return new MachineImpl(Collections.unmodifiableSet(alphabetSymbols), blankSymbol,
		      Collections.unmodifiableSet(finalStates), initialState, Collections.unmodifiableSet(inputSymbols),
		      startSymbol, Collections.unmodifiableSet(states), Collections.unmodifiableList(subscribers),
		      Collections.unmodifiableList(tapes), transitionFunction);
	}

	public final MachineBuilder withAlphabetSymbols(final Character... alphabetSymbols) {
		this.alphabetSymbols = Arrays.stream(alphabetSymbols).collect(Collectors.toSet());
		return this;
	}

	public final MachineBuilder withBlankSymbol(final char symbol) {
		this.blankSymbol = symbol;
		return this;
	}

	public final MachineBuilder withFinalStates(final String... finalStates) {
		this.finalStates = Arrays.stream(finalStates).collect(Collectors.toSet());
		return this;
	}

	public final MachineBuilder withInitialState(final String initialState) {
		this.initialState = initialState;
		return this;
	}

	public final MachineBuilder withInputSymbols(final Character... inputSymbols) {
		this.inputSymbols = Arrays.stream(inputSymbols).collect(Collectors.toSet());
		return this;
	}

	public final MachineBuilder withInputTape(final Tape inputTape) {
		this.inputTape = inputTape;
		return this;
	}

	public final MachineBuilder withMoreTapes(final int moreTapes) {
		this.moreTapes = moreTapes;
		return this;
	}

	public final MachineBuilder withStartSymbol(final char symbol) {
		this.startSymbol = symbol;
		return this;
	}

	public final MachineBuilder withStates(final String... states) {
		this.states = Arrays.stream(states).collect(Collectors.toSet());
		return this;
	}

	public final MachineBuilder withSubscriber(final EventHandler subscriber) {
		this.subscribers.add(subscriber);
		return this;
	}

	public final MachineBuilder withTransitionFunction(final TransitionFunction transitionFunction) {
		this.transitionFunction = transitionFunction;
		return this;
	}

	private void validate() {
		// NOTE: any validation errors lead to an immediate invalid argument
		// exception

		// alphabetSymbols: not null, not empty
		if (alphabetSymbols == null || alphabetSymbols.isEmpty()) {
			throw new IllegalArgumentException("The alphabet symbols must be a non-null, non empty array of characters.");
		}

		// blankSymbol: not null
		if (blankSymbol == null) {
			throw new IllegalArgumentException("The blank symbol must be a non-null alphabet symbol.");
		}

		// blankSymbol: contained in alphabetSymbols
		if (!alphabetSymbols.contains(blankSymbol)) {
			throw new IllegalArgumentException("The blank symbol must belong to the set of alphabet symbols.");
		}

		// inputSymbols: not null
		if (inputSymbols == null) {
			throw new IllegalArgumentException("The input symbols must be a non-null set of alphabet symbols.");
		}

		// inputSymbols: contained in alphabetSymbols
		if (!alphabetSymbols.containsAll(inputSymbols)) {
			throw new IllegalArgumentException("The input symbols must belong to the set of alphabet symbols.");
		}

		// states: not null, not empty
		if (states == null || states.isEmpty()) {
			throw new IllegalArgumentException("The states set must be a non-null, non empty array of states.");
		}

		// initialState: not null
		if (transitionFunction == null) {
			throw new IllegalArgumentException("The initial state must be a non-null state.");
		}

		// states: contains initial state
		if (!states.contains(initialState)) {
			throw new IllegalArgumentException("The initial state must belong to the states set.");
		}

		// finalStates: not null, not empty
		if (finalStates == null || finalStates.isEmpty()) {
			throw new IllegalArgumentException("The final states set must be a non-null, non empty array of states.");
		}

		// states: contains final states
		if (!states.containsAll(finalStates)) {
			throw new IllegalArgumentException("The final states must belong to the states set.");
		}

		// transitionFunction: not null
		if (transitionFunction == null) {
			throw new IllegalArgumentException("The transition map must be a non-null map.");
		}
	}

	private static class MachineImpl implements Machine {

		MachineImpl(Set<Character> alphabetSymbols, Character blankSymbol, Set<String> finalStates, String initialState,
		      Set<Character> inputSymbols, Character startSymbol, Set<String> states, List<EventHandler> subscribers,
		      List<Tape> tapes, TransitionFunction transitionFunction) {
			this.alphabetSymbols = alphabetSymbols;
			this.blankSymbol = blankSymbol;
			this.finalStates = finalStates;
			this.initialState = initialState;
			this.inputSymbols = inputSymbols;
			this.startSymbol = startSymbol;
			this.states = states;
			this.subscribers = subscribers;
			this.tapes = tapes;
			this.transitionFunction = transitionFunction;
		}

		private final Set<Character> alphabetSymbols;
		private final Character blankSymbol;
		private final Set<String> finalStates;
		private final String initialState;
		private final Set<Character> inputSymbols;
		private final Set<String> states;
		private final Character startSymbol;
		private final List<EventHandler> subscribers;
		private final List<Tape> tapes;
		private final TransitionFunction transitionFunction;

		@Override
		public Set<Character> alphabetSymbols() {
			return alphabetSymbols;
		}

		@Override
		public Character blankSymbol() {
			return blankSymbol;
		}

		@Override
		public Set<String> finalStates() {
			return finalStates;
		}

		@Override
		public String initialState() {
			return initialState;
		}

		@Override
		public Set<Character> inputSymbols() {
			return inputSymbols;
		}

		@Override
		public void run() {

			int stateId = 0;

			String state = initialState();
			List<Character> tapeHeadSymbols = tapes.stream().map(tape -> tape.read()).collect(Collectors.toList());

			onStateChanged(stateId, state, tapeHeadSymbols);

			while (!finalStates().contains(state)) {
				final Transition transition = transitionFunction().apply(state, tapeHeadSymbols);
				if (transition == null) {
					onDiverged(stateId, state, tapeHeadSymbols);
					System.exit(-1);
				}
				onTransition(stateId, state, transition);
				IntStream.range(0, tapes.size()).forEach(ix -> tapes.get(ix).write(transition.outputs().get(ix)));
				IntStream.range(0, tapes.size()).forEach(ix -> tapes.get(ix).move(transition.moves().get(ix)));
				state = transition.state();
				if (!states.contains(state)) {
					throw new IllegalArgumentException(String.format("Unrecognized machine state '%s'.", state));
				}
				tapeHeadSymbols = tapes.stream().map(tape -> tape.read()).collect(Collectors.toList());
				onStateChanged(++stateId, state, tapeHeadSymbols);
			}
			
			onHalted(stateId, state, tapeHeadSymbols);
		}

		private void onTransition(final int stateId, final String state, final Transition transition) {
			for (final EventHandler eh : subscribers) {
				eh.handle(new Event.OnTransition() {

					@Override
					public Machine machine() {
						return MachineImpl.this;
					}

					@Override
					public String state() {
						return state;
					}

					@Override
					public long stateId() {
						return stateId;
					}

					@Override
					public Transition transition() {
						return transition;
					}

				});
			}
		}

		private void onDiverged(int stateId, String state, List<Character> tapeHeadSymbols) {
			for (final EventHandler eh : subscribers) {
				eh.handle(new Event.OnDiverged() {

					@Override
					public Machine machine() {
						return MachineImpl.this;
					}

					@Override
					public String state() {
						return state;
					}

					@Override
					public long stateId() {
						return stateId;
					}

					@Override
					public List<Character> symbols() {
						return tapeHeadSymbols;
					}
				});
			}
		}

		private void onHalted(int stateId, String state, List<Character> tapeHeadSymbols) {
			for (final EventHandler eh : subscribers) {
				eh.handle(new Event.OnHalted() {

					@Override
					public Machine machine() {
						return MachineImpl.this;
					}

					@Override
					public String state() {
						return state;
					}

					@Override
					public long stateId() {
						return stateId;
					}

					@Override
					public List<Character> symbols() {
						return tapeHeadSymbols;
					}
				});
			}
		}

		private void onStateChanged(int stateId, String state, List<Character> tapeHeadSymbols) {
			for (final EventHandler eh : subscribers) {
				eh.handle(new Event.OnStateChanged() {

					@Override
					public Machine machine() {
						return MachineImpl.this;
					}

					@Override
					public String state() {
						return state;
					}

					@Override
					public long stateId() {
						return stateId;
					}

					@Override
					public List<Character> symbols() {
						return tapeHeadSymbols;
					}
				});
			}
		}

		@Override
		public Character startSymbol() {
			return startSymbol;
		}

		@Override
		public Set<String> states() {
			return states;
		}

		@Override
		public List<Tape> tapes() {
			return tapes;
		}

		@Override
		public TransitionFunction transitionFunction() {
			return transitionFunction;
		}
	}
}