package org.lessa.lambda.ast;

/**
 * <p>
 * An AST node representing a lambda calculus function.
 * </p>
 *
 * <pre>
 * function ::= 'λ' name '.' expression
 * </pre>
 */
public interface Function extends Expression, BindingNode {

   /**
    * The expression representing the body of this function.
    * 
    * @return expression object representing the body
    */
   Expression body();

   /**
    * The bound variable introduced by this function.
    * 
    * @return name object representing the bound variable
    */
   Name boundVariable();
}