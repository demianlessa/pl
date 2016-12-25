package org.lessa.lambda.ast;

/**
 * An AST node representing a lambda calculus function:
 * 
 * <pre>
 * function ::= 'λ' name '.' expression
 * </pre>
 */
public interface Function extends Expression {

   public Expression body();

   public Name name();
}