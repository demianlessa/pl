package org.lessa.lambda.ast;

public interface AstVisitor {

   public void visit(Application node);

   public void visit(Definition node);

   public void visit(Function node);

   public void visit(Name node);

   public void visit(Program node);
}