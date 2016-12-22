package org.lessa.lambda;

import org.lessa.lambda.Parser.Application;
import org.lessa.lambda.Parser.Definition;
import org.lessa.lambda.Parser.Function;
import org.lessa.lambda.Parser.LambdaVisitor;
import org.lessa.lambda.Parser.Name;
import org.lessa.lambda.Parser.Program;

public class PrintVisitors {

   public static class LambdaPrinter implements LambdaVisitor {

      @Override
      public void visit(Application node) {
         System.out.print("(");
         node.leftExpression()
               .accept(this);
         System.out.print(" ");
         node.rightExpression()
               .accept(this);
         System.out.print(")");
      }

      @Override
      public void visit(Definition node) {
         System.out.print("def ");
         node.name()
               .accept(this);
         System.out.print(" = ");
         node.expression()
               .accept(this);
         System.out.println();
      }

      @Override
      public void visit(Function node) {
         System.out.print("λ");
         node.name()
               .accept(this);
         System.out.print(".");
         node.body()
               .accept(this);
         System.out.print("");
      }

      @Override
      public void visit(Name node) {
         System.out.print(node.name());
      }

      @Override
      public void visit(Program node) {
         for (final Definition def : node.definitions()) {
            def.accept(this);
         }
         if (node.expression() != null) {
            if (!node.definitions()
                  .isEmpty()) {
               System.out.println();
            }
            node.expression()
                  .accept(this);
            System.out.println();
         }
      }
   }

   public static class PrettyPrinter implements LambdaVisitor {

      @Override
      public void visit(Application node) {
         System.out.print("Application(LeftExp(");
         node.leftExpression()
               .accept(this);
         System.out.print("), RightExp(");
         node.rightExpression()
               .accept(this);
         System.out.print("))");
      }

      @Override
      public void visit(Definition node) {
         System.out.print("Definition(Name(");
         node.name()
               .accept(this);
         System.out.print("), Expression(");
         node.expression()
               .accept(this);
         System.out.println("))");
      }

      @Override
      public void visit(Function node) {
         System.out.print("Function(Name(");
         node.name()
               .accept(this);
         System.out.print("), Body(");
         node.body()
               .accept(this);
         System.out.print("))");
      }

      @Override
      public void visit(Name node) {
         System.out.print(node.name());
      }

      @Override
      public void visit(Program node) {
         for (final Definition def : node.definitions()) {
            def.accept(this);
         }
         if (node.expression() != null) {
            if (!node.definitions()
                  .isEmpty()) {
               System.out.println();
            }
            node.expression()
                  .accept(this);
         }
         System.out.println();
      }
   }
}
