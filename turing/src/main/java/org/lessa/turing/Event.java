package org.lessa.turing;

import java.util.List;

public class Event {

	public interface MachineEvent {

		Machine machine();

		String state();

		long stateId();
	}

	public interface OnDiverged extends MachineEvent {
		List<Character> symbols();
	}

	public interface OnHalted extends MachineEvent {
		List<Character> symbols();
	}

	public interface OnStateChanged extends MachineEvent {
		List<Character> symbols();
	}

	public interface OnTransition extends MachineEvent {
		Transition transition();
	}

	public interface EventHandler {

		void handle(OnDiverged event);

		void handle(OnHalted event);

		void handle(OnStateChanged event);

		void handle(OnTransition event);
	}
}