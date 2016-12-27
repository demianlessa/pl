package org.lessa.lambda.ast;

import java.util.List;

/**
 * <p>
 * An AST node representing an entire lambda calculus program.
 * </p>
 *
 * <pre>
 * program ::= definition* expression
 * </pre>
 */
public interface Program extends AstNode {

   /**
    * List of helper functions defined by the program, introduced to simplify
    * this program's expression.
    * 
    * @return possibly empty list of definitions introduced by this program
    */
   List<Definition> definitions();

   /**
    * Actual lambda calculus expression to calculate.
    * 
    * @return expression object representing this program's expression
    */
   Expression expression();
}