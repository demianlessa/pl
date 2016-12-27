package org.lessa.lambda.ast;

/**
 * An AST node to which variables are bound.
 */
interface BindingNode extends AstNode {

   /**
    * Associated a bound name/variable to this node.
    * 
    * @param name
    *           name/variable bound to this node
    */
   void addBinding(Name name);
}