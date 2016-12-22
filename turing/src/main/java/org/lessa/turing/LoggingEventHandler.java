package org.lessa.turing;

import java.util.Arrays;
import java.util.List;

import org.lessa.turing.Event.OnDiverged;
import org.lessa.turing.Event.OnHalted;
import org.lessa.turing.Event.OnStateChanged;
import org.lessa.turing.Event.OnTransition;

public final class LoggingEventHandler implements Event.EventHandler {
   @Override
   public void handle(OnDiverged event) {
      System.err.println("Machine diverged: no transition found for current machine state.");
   }

   @Override
   public void handle(OnHalted event) {
      System.out.printf("Machine halted with the following contents on its output tape: %s\n",
            event.machine()
                  .tapes()
                  .get(event.machine()
                        .tapes()
                        .size() - 1));
   }

   @Override
   public void handle(OnStateChanged event) {
      logInternalState(event.stateId(), event.state(), event.symbols());
   }

   @Override
   public void handle(OnTransition event) {
      logTransition(event.stateId(), event.transition());
   }

   private void logInternalState(long stateId, String state, List<Character> tapeHeadSymbols) {
      final String logEntry = String.format(
            "{\"stateId\": %d, \"state\": \"%s\", \"tapeHeadSymbols\": %s}", stateId, state,
            Arrays.toString(tapeHeadSymbols.toArray()));
      System.out.println(logEntry);
   }

   private void logTransition(long stateId, Transition transition) {
      final String logEntry = String.format(
            "{\"stateId\": %d, \"transition\": {\"nextState\": \"%s\", \"writeToTapes\": %s, \"moves\": %s}}",
            stateId, transition.state(), Arrays.toString(transition.outputs()
                  .toArray()),
            Arrays.toString(transition.moves()
                  .toArray()));
      System.out.println(logEntry);
   }
}