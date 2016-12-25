package org.lessa.lambda.ast;

import java.util.List;

public class AstNodeFactory {

   public Application createApplication(final Expression left, final Expression right) {
      return new Application() {
         @Override
         public final void accept(final AstVisitor visitor) {
            visitor.visit(this);
         }

         @Override
         public final boolean isBound(final String variableName) {
            return left.isBound(variableName) || right.isBound(variableName);
         }

         @Override
         public Expression leftExpression() {
            return left;
         }

         @Override
         public Expression rightExpression() {
            return right;
         }
      };
   }

   public Definition createDefinition(final Name name, final Expression expression) {
      return new Definition() {

         @Override
         public final void accept(final AstVisitor visitor) {
            visitor.visit(this);
         }

         @Override
         public Expression expression() {
            return expression;
         }

         @Override
         public final boolean isBound(final String variableName) {
            return expression.isBound(variableName);
         }

         @Override
         public Name name() {
            return name;
         }
      };
   }

   public Function createFunction(final Name name, final Expression body) {
      return new Function() {
         @Override
         public final void accept(final AstVisitor visitor) {
            visitor.visit(this);
         }

         @Override
         public Expression body() {
            return body;
         }

         @Override
         public final boolean isBound(final String variableName) {
            return name.name().equals(variableName) || body.isBound(variableName);
         }

         @Override
         public Name name() {
            return name;
         }
      };
   }

   public Name createName(final String name) {
      return new Name() {
         @Override
         public final void accept(final AstVisitor visitor) {
            visitor.visit(this);
         }

         @Override
         public final boolean isBound(final String variableName) {
            return false;
         }

         @Override
         public String name() {
            return name;
         }
      };
   }

   public Program createProgram(final List<Definition> definitions, final Expression expression) {
      return new Program() {
         @Override
         public final void accept(final AstVisitor visitor) {
            visitor.visit(this);
         }

         @Override
         public List<Definition> definitions() {
            return definitions;
         }

         @Override
         public final boolean isBound(final String variableName) {
            return definitions.stream().anyMatch(d -> d.isBound(variableName))
                  || (expression != null && expression.isBound(variableName));
         }

         @Override
         public Expression expression() {
            return expression;
         }
      };
   }
}
