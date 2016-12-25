package org.lessa.lambda.ast;

/**
 * An AST node representing a name:
 * 
 * <pre>
 * name ::= &lt;non-blank character sequence&gt;
 * </pre>
 */
public interface Name extends Expression {
   public String name();
}