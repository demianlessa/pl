package org.lessa.lambda.ast;

/**
 * An AST node representing a lambda calculus syntactic element.
 */
public interface AstNode {

   void accept(AstVisitor visitor);

   boolean isBound(String variableName);
}