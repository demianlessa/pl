package org.lessa.lambda.ast;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Prints a functional (parenthesized) representation of the lambda calculus
 * program to the {@link PrintStream} specified in the constructor.
 */
class AstFunctionalPrinter implements AstVisitor {

   private final PrintStream out;

   AstFunctionalPrinter(final OutputStream out, String encoding)
         throws UnsupportedEncodingException {
      this.out = new PrintStream(out, true, encoding);
   }

   @Override
   public void visit(Application node) {
      out.print("Application(");
      node.leftExpression().accept(this);
      out.print(", ");
      node.rightExpression().accept(this);
      out.print(")");
   }

   @Override
   public void visit(Definition node) {
      out.print("Definition(");
      node.name().accept(this);
      out.print(", ");
      node.expression().accept(this);
      out.print(")");
   }

   @Override
   public void visit(Function node) {
      out.print("Function(");
      node.name().accept(this);
      out.print(", ");
      node.body().accept(this);
      out.print(")");
   }

   @Override
   public void visit(Name node) {
      out.print("Name(");
      out.print(node.name());
      out.print(")");
   }

   @Override
   public void visit(Program node) {
      if (!node.definitions().isEmpty()) {
         out.print("DefinitionList(");
         for (int ix = 0; ix < node.definitions().size(); ix++) {
            final Definition def = node.definitions().get(ix);
            def.accept(this);
            if (ix < node.definitions().size() - 1) {
               out.print(", ");
            }
         }
         out.print(")");
      }
      if (node.expression() != null) {
         if (!node.definitions().isEmpty()) {
            out.println();
         }
         node.expression().accept(this);
      }
   }
}