package org.lessa.lambda.ast;

import java.util.List;

/**
 * An AST node representing an entire lambda calculus program.
 * 
 * <pre>
 * program ::= definition* expression?
 * </pre>
 */
public interface Program extends AstNode {

   public List<Definition> definitions();

   public Expression expression();
}