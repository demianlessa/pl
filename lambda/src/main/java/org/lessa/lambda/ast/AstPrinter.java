package org.lessa.lambda.ast;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Prints the original lambda calculus program to the {@link PrintStream}
 * specified in the constructor.
 */
class AstPrinter implements AstVisitor {

   private final PrintStream out;

   AstPrinter(final OutputStream out, String charset) throws UnsupportedEncodingException {
      this.out = new PrintStream(out, true, charset);
   }

   @Override
   public void visit(Application node) {
      out.print("(");
      node.leftExpression().accept(this);
      out.print(" ");
      node.rightExpression().accept(this);
      out.print(")");
   }

   @Override
   public void visit(Definition node) {
      out.print("def ");
      node.name().accept(this);
      out.print(" = ");
      node.expression().accept(this);
   }

   @Override
   public void visit(Function node) {
      out.print("λ");
      node.name().accept(this);
      out.print(".");
      node.body().accept(this);
      out.print("");
   }

   @Override
   public void visit(Name node) {
      out.print(node.name());
   }

   @Override
   public void visit(Program node) {
      for (int ix = 0; ix < node.definitions().size(); ix++) {
         final Definition def = node.definitions().get(ix);
         def.accept(this);
         if (ix < node.definitions().size() - 1) {
            out.println();
         }
      }
      if (node.expression() != null) {
         if (!node.definitions().isEmpty()) {
            out.println();
         }
         node.expression().accept(this);
      }
   }
}