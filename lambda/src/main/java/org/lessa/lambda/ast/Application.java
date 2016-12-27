package org.lessa.lambda.ast;

/**
 * <p>
 * An AST node representing a lambda calculus application.
 * </p>
 *
 * <pre>
 * application ::= '(' expression expression ')'
 * </pre>
 * 
 * <p>
 * In an application, the left expression within the parenthesis is the function
 * expression whereas the right expression is the argument expression.
 * </p>
 */
public interface Application extends Expression {

   /**
    * The argument expression of this application, corresponding to the right
    * expression within the parenthesis.
    * 
    * @return argument expression (rightmost expression)
    */
   Expression argument();

   /**
    * The function expression of this application, corresponding to the left
    * expression within the parenthesis.
    * 
    * @return function expression (leftmost expression)
    */
   Expression function();
}