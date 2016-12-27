package org.lessa.lambda.ast;

/**
 * <p>
 * An AST node representing a name.
 * </p>
 *
 * <pre>
 * name ::= &lt;non-blank character sequence&gt;
 * </pre>
 */
public interface Name extends Expression {

   /**
    * Node to which this name is bound. This can be a {@link Definition} or a
    * {@link Function} reference. If <code>null</code>, this means that the name
    * is not bound, i.e., represents a free variable.
    * 
    * @return node to which this name is bound
    */
   BindingNode boundTo();

   /**
    * String value of this name.
    * 
    * @return string representation of this name
    */
   String value();
}