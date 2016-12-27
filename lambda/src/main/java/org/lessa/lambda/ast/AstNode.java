package org.lessa.lambda.ast;

/**
 * An AST node representing a lambda calculus syntactic element.
 */
public interface AstNode {

   /**
    * Accepts a visitor and exposes it to the structure of the AST tree.
    * 
    * @param visitor
    *           visitor to accept
    */
   void accept(AstVisitor visitor);

   /**
    * Indicates whether the specified name is bound in the current AST node.
    * 
    * @param name
    *           name to verify
    * @return true if the name is bound in this node
    */
   boolean isBound(String name);
}