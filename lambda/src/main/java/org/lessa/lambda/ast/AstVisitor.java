package org.lessa.lambda.ast;

public interface AstVisitor {

   void visit(Application node);

   void visit(Definition node);

   void visit(Function node);

   void visit(Name node);

   void visit(Program node);
}