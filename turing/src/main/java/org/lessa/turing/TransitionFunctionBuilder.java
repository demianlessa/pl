package org.lessa.turing;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitionFunctionBuilder {

   private static class Key {

      private final String state;

      private final Character[] symbols;

      public Key(String state, Character[] symbols) {
         this.state = state;
         this.symbols = symbols;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         }
         if (obj == null) {
            return false;
         }
         if (getClass() != obj.getClass()) {
            return false;
         }
         final Key other = (Key) obj;
         if (state == null) {
            if (other.state != null) {
               return false;
            }
         }
         else if (!state.equals(other.state)) {
            return false;
         }
         if (!Arrays.equals(symbols, other.symbols)) {
            return false;
         }
         return true;
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + (state == null ? 0 : state.hashCode());
         result = prime * result + Arrays.hashCode(symbols);
         return result;
      }

      @Override
      public String toString() {
         return "(" + state + "," + Arrays.toString(symbols) + ")";
      }
   }

   private static final String ERR_ARRAY_SIZE_MISMATCH = "The '%s' array must have the same number of symbols as tapes. Found %d but expected %d.";
   private final int numTapes;
   private final Map<Key, Transition> transitionMap;

   /**
    * Creates a builder with the specified number of work+output tapes and an
    * empty transition map.
    *
    * @param numTapes
    *           number of work+output tapes
    */
   public TransitionFunctionBuilder(int numTapes) {
      this.numTapes = numTapes;
      this.transitionMap = new HashMap<>();
   }

   /**
    * Uses the parameters passed to the builder in order to create and return a
    * new {@link TransitionFunction} object.
    *
    * @return transition function
    */
   public TransitionFunction build() {
      return (state, symbols) -> {
         return transitionMap.get(buildKey(state, symbols.toArray(new Character[] {})));
      };
   }

   public TransitionFunctionBuilder withTranstion(String currentState,
         Character[] currentTapeSymbols, Character[] symbolsToWrite, Move[] tapeMoves,
         String newState) {

      if (currentState == null || currentState.length() == 0) {
         throw new IllegalArgumentException(
               "The 'currentState' argument must be a non-empty string.");
      }

      if (currentTapeSymbols == null || currentTapeSymbols.length != numTapes) {
         throw new IllegalArgumentException(
               String.format(ERR_ARRAY_SIZE_MISMATCH, "currentTapeSymbols",
                     currentTapeSymbols == null ? 0 : currentTapeSymbols.length, numTapes));
      }

      if (symbolsToWrite == null || symbolsToWrite.length != numTapes) {
         throw new IllegalArgumentException(String.format(ERR_ARRAY_SIZE_MISMATCH, "symbolsToWrite",
               symbolsToWrite == null ? 0 : symbolsToWrite.length, numTapes));
      }

      if (tapeMoves == null || tapeMoves.length != numTapes) {
         throw new IllegalArgumentException(String.format(ERR_ARRAY_SIZE_MISMATCH, "tapeMoves",
               tapeMoves == null ? 0 : tapeMoves.length, numTapes));
      }

      if (newState == null || newState.length() == 0) {
         throw new IllegalArgumentException("The 'newState' argument must be a non-empty string.");
      }

      transitionMap.put(buildKey(currentState, currentTapeSymbols),
            buildTransition(symbolsToWrite, tapeMoves, newState));

      return this;
   }

   private Key buildKey(final String currentState, final Character[] currentTapeSymbols) {
      return new Key(currentState, currentTapeSymbols);
   }

   private Transition buildTransition(final Character[] symbolsToWrite, final Move[] tapeMoves,
         final String state) {

      final List<Move> moves = Collections.unmodifiableList(Arrays.asList(tapeMoves));
      final List<Character> symbols = Collections.unmodifiableList(Arrays.asList(symbolsToWrite));

      return new Transition() {

         @Override
         public List<Move> moves() {
            return moves;
         }

         @Override
         public List<Character> outputs() {
            return symbols;
         }

         @Override
         public String state() {
            return state;
         }
      };
   }
}