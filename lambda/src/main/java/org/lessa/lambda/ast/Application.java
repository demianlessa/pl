package org.lessa.lambda.ast;

/**
 * An AST node representing a lambda calculus application:
 * 
 * <pre>
 * application ::= '(' expression expression ')'
 * </pre>
 */
public interface Application extends Expression {

   public Expression leftExpression();

   public Expression rightExpression();
}