package org.lessa.lambda.ast;

public interface BindingNode extends AstNode {
   void addBinding(Name name);
}