package org.lessa.lambda.ast;

/**
 * An AST node representing a named expression definition:
 * 
 * <pre>
 * definition ::= 'def' name '=' expression
 * </pre>
 */
public interface Definition extends AstNode {

   public Expression expression();

   public Name name();
}