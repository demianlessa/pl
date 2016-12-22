package org.lessa.turing;

import java.util.List;

public interface Transition {

   List<Move> moves();

   List<Character> outputs();

   String state();
}
