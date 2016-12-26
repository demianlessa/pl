package org.lessa.lambda.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AstNodeFactory {

   private static final class ApplicationImpl extends BaseNode implements Application {

      private final Expression left;
      private final Expression right;

      public ApplicationImpl(final Expression left, final Expression right) {
         this.left = left;
         this.right = right;
      }

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
   }

   private abstract static class BaseNode implements AstNode {

      private LexicalScope lexicalScope;

      BaseNode() {
         lexicalScope = LexicalScope.EMPTY_SCOPE;
      }

      LexicalScope scope() {
         return lexicalScope;
      }

      void setScope(final LexicalScope scope) {
         lexicalScope = scope;
      }
   }

   private static final class DefinitionImpl extends BaseNode implements Definition {

      private final List<Name> bindings;
      private final Expression expression;
      private final Name name;

      DefinitionImpl(final Name name, final Expression expression) {
         this.bindings = new ArrayList<>();
         this.name = name;
         this.expression = expression;
      }

      @Override
      public final void accept(final AstVisitor visitor) {
         visitor.visit(this);
      }

      @Override
      public void addBinding(final Name name) {
         bindings.add(name);
      }

      @Override
      public Expression expression() {
         return expression;
      }

      @Override
      public final boolean isBound(final String variableName) {
         return false;
         // return expression.isBound(variableName);
      }

      @Override
      public Name name() {
         return name;
      }
   }

   private static final class FunctionImpl extends BaseNode implements Function, BindingNode {

      private final List<Name> bindings;
      private final Expression body;
      private final Name name;

      FunctionImpl(final Name name, final Expression body) {
         this.bindings = new ArrayList<>();
         this.name = name;
         this.body = body;
      }

      @Override
      public final void accept(final AstVisitor visitor) {
         visitor.visit(this);
      }

      @Override
      public void addBinding(final Name name) {
         bindings.add(name);
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
   }

   private static final class NameImpl extends BaseNode implements Name {

      private final String name;
      private BindingNode boundTo;

      NameImpl(final String name) {
         this.name = name;
      }

      @Override
      public final void accept(final AstVisitor visitor) {
         visitor.visit(this);
      }

      @Override
      public BindingNode boundTo() {
         return boundTo;
      }

      @Override
      public final boolean isBound(final String variableName) {
         return false;
      }

      @Override
      public String name() {
         return name;
      }

      void setBoundTo(final BindingNode node) {
         boundTo = node;
         if (node != null) {
            node.addBinding(this);
         }
      }
   }

   private static final class ProgramImpl extends BaseNode implements Program {

      private final List<Definition> definitions;
      private final Expression expression;

      ProgramImpl(final List<Definition> definitions, final Expression expression) {
         this.definitions = definitions;
         this.expression = expression;
      }

      @Override
      public final void accept(final AstVisitor visitor) {
         visitor.visit(this);
      }

      @Override
      public List<Definition> definitions() {
         return definitions;
      }

      @Override
      public Expression expression() {
         return expression;
      }

      @Override
      public final boolean isBound(final String variableName) {
         return expression != null && expression.isBound(variableName);
         // return definitions.stream().anyMatch(d -> d.isBound(variableName))
         // || expression != null && expression.isBound(variableName);
      }
   }

   public Application createApplication(final Expression left, final Expression right) {
      return new ApplicationImpl(left, right);
   }

   public Definition createDefinition(final Name name, final Expression expression) {
      return new DefinitionImpl(name, expression);
   }

   public Function createFunction(final Name name, final Expression body) {
      return new FunctionImpl(name, body);
   }

   public Name createName(final String name) {
      return new NameImpl(name);
   }

   public Program createProgram(final List<Definition> definitions, final Expression expression) {

      final List<AstNode> toRename = new ArrayList<>();
      final Stack<LexicalScope> scopes = new Stack<>();

      final Program program = new ProgramImpl(definitions, expression);
      program.accept(new AstVisitor() {

         @Override
         public void visit(final Application node) {

            // copy the top lexical scope
            final LexicalScope scope = scopes.peek().copy();

            // push onto the stack before drilling down
            scopes.push(scope);

            // drill down on left
            node.leftExpression().accept(this);

            // drill down on right
            node.rightExpression().accept(this);

            // set the node's scope
            ((BaseNode) node).setScope(scopes.pop());
         }

         @Override
         public void visit(final Definition node) {

            // throw an exception on a duplicate definition name
            if (scopes.peek().definitions().containsKey(node.name().name())) {
               throw new IllegalArgumentException(
                     String.format("Duplicate definition for '%s'.", node.name().name()));
            }

            // push an empty scope onto the stack before drilling down
            scopes.push(new LexicalScope());

            // drill down
            node.expression().accept(this);

            // set the node's scope
            ((BaseNode) node).setScope(scopes.pop());
         }

         @Override
         public void visit(final Function node) {

            // copy the top lexical scope
            final LexicalScope scope = scopes.peek().copy();

            // record the name if it conflicts with an in-scope name
            if (scope.variables().containsKey(node.name().name())) {
               toRename.add(scope.variables().get(node.name().name()));
            }

            // add the name of the function
            scope.variables().put(node.name().name(), node);

            // push onto the stack before drilling down
            scopes.push(scope);

            // drill down
            node.body().accept(this);

            // set the node's scope
            ((BaseNode) node).setScope(scopes.pop());
         }

         @Override
         public void visit(final Name node) {
            // set the node's scope
            ((BaseNode) node).setScope(scopes.peek().copy());

            // try to find a definition with the specified name
            final AstNode resolvedDef = scopes.peek().definitions().get(node.name());

            // try to find a function with the specified name
            final AstNode resolvedVar = scopes.peek().variables().get(node.name());

            // bound to the closest function or definition with this name
            ((NameImpl) node)
                  .setBoundTo((BindingNode) (resolvedVar != null ? resolvedVar : resolvedDef));
         }

         @Override
         public void visit(final Program node) {

            // copy the top lexical scope
            final LexicalScope scope = new LexicalScope();

            // push onto the stack before drilling down
            scopes.push(scope);

            // build independent scopes for each definition
            for (int ix = 0; ix < node.definitions().size(); ix++) {

               // retrieve the next in-order definition
               final Definition def = node.definitions().get(ix);

               // drill down to update the scope
               def.accept(this);

               // add this definition's name to the program's scope
               scope.definitions().put(def.name().name(), def);
            }

            // drill down
            if (node.expression() != null) {
               node.expression().accept(this);
            }

            // set the node's scope
            ((BaseNode) node).setScope(scopes.pop());

            // α-reduction: rename duplicate function names
            if (toRename.size() > 0) {
               // TODO: generate unique names-- collect set of names?
               System.err.printf("Must rename %d functions.\n", toRename.size());
            }
         }
      });
      return program;
   }
}