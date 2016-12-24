package org.lessa.turing;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MachineTest {

   // ----------------------------------------------------------------------
   // Negative cases
   // ----------------------------------------------------------------------

   @Test
   public void given_no_arguments_builder_fails_validate() {
      try {
         new MachineBuilder().build();
         Assert.assertFalse(true, "Builder should fail validation!");
      }
      catch (final MachineBuilderException mbe) {
         final Map<MachinePart, List<String>> violations = mbe.violations();
         Assert.assertEquals(violations.size(), 6);
      }
   }

   @Test
   public void given_blank_input_symbol_builder_fails_validate() {
      try {
         new MachineBuilder().withAlphabetSymbols('0')
               .withInputSymbols(Machine.DEFAULT_BLANK_SYMBOL)
               .build();
         Assert.assertFalse(true, "Builder should fail validation!");
      }
      catch (final MachineBuilderException mbe) {
         final Map<MachinePart, List<String>> violations = mbe.violations();
         Assert.assertEquals(violations.size(), 5);
      }
   }

   @Test
   public void given_input_symbol_not_in_alphabet_builder_fails_validate() {
      try {
         new MachineBuilder().withAlphabetSymbols('0')
               .withInputSymbols('1')
               .build();
         Assert.assertFalse(true, "Builder should fail validation!");
      }
      catch (final MachineBuilderException mbe) {
         final Map<MachinePart, List<String>> violations = mbe.violations();
         Assert.assertEquals(violations.size(), 5);
      }
   }

   @Test
   public void given_empty_states_initial_state_and_final_states_builder_fails_validate() {
      try {
         new MachineBuilder().withAlphabetSymbols('0', '1')
               .withInputSymbols('0')
               .withStates()
               .withInitialState("")
               .withFinalStates()
               .build();
         Assert.assertFalse(true, "Builder should fail validation!");
      }
      catch (final MachineBuilderException mbe) {
         System.out.println(mbe.toString());
         final Map<MachinePart, List<String>> violations = mbe.violations();
         Assert.assertEquals(violations.size(), 4);
      }
   }

   @Test
   public void given_empty_states_and_null_initial_state_and_final_states_builder_fails_validate() {
      try {
         new MachineBuilder().withAlphabetSymbols('0', '1')
               .withInputSymbols('0')
               .withStates()
               .build();
         Assert.assertFalse(true, "Builder should fail validation!");
      }
      catch (final MachineBuilderException mbe) {
         System.out.println(mbe.toString());
         final Map<MachinePart, List<String>> violations = mbe.violations();
         Assert.assertEquals(violations.size(), 4);
      }
   }

   @Test
   public void given_invalid_initial_and_final_states_builder_fails_validate() {
      try {
         new MachineBuilder().withAlphabetSymbols('0', null, '1')
               .withInputSymbols('0', null)
               .withStates("S0", "S1", null, "S2")
               .withFinalStates("F0", null, "F1")
               .build();
         Assert.assertFalse(true, "Builder should fail validation!");
      }
      catch (final MachineBuilderException mbe) {
         System.out.println(mbe.toString());
         final Map<MachinePart, List<String>> violations = mbe.violations();
         Assert.assertEquals(violations.size(), 3);
      }
   }

   // ----------------------------------------------------------------------
   // Positive cases
   // ----------------------------------------------------------------------

   /**
    * Single tape machine, no input, simulates a constant function that outputs
    * the string '0⊔1⊔' on the tape. The final tape state is always '▷0⊔1⊔'.
    */
   @Test
   public void given_single_tape_machine_with_no_input_returns_0b1b() {

      final Character zero = '0';
      final Character one = '1';

      final String Q0 = "q0";
      final String Q1 = "q1";
      final String Q2 = "q2";
      final String Q3 = "q3";
      final String Q4 = "q4";
      final String H = "HALT";

      final Character[] startSymbol = new Character[] { Machine.DEFAULT_START_SYMBOL };
      final Character[] blankSymbol = new Character[] { Machine.DEFAULT_BLANK_SYMBOL };
      final Character[] oneSymbol = new Character[] { one };
      final Character[] zeroSymbol = new Character[] { zero };

      final Move[] moveRight = new Move[] { Move.RIGHT };

      final TransitionFunction program = new TransitionFunctionBuilder(1)
            .withTranstion(Q0, startSymbol, startSymbol, moveRight, Q1)
            .withTranstion(Q1, blankSymbol, zeroSymbol, moveRight, Q2)
            .withTranstion(Q2, blankSymbol, blankSymbol, moveRight, Q3)
            .withTranstion(Q3, blankSymbol, oneSymbol, moveRight, Q4)
            .withTranstion(Q4, blankSymbol, blankSymbol, moveRight, H)
            .build();

      new MachineBuilder().withAlphabetSymbols(zero, one)
            .withInputSymbols(zero, one)
            .withStates(Q0, Q1, Q2, Q3, Q4, H)
            .withInitialState(Q0)
            .withFinalStates(H)
            .withSubscriber(new LoggingEventHandler())
            .withTransitionFunction(program)
            .build()
            .run();
   }
}