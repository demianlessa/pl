package org.lessa.lambda.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Factory for AST nodes, encapsulating the implementation of node logic.
 */
public class AstNodeFactory {

   /**
    * Creates a new application '(left right)' from a pair of expressions.
    * 
    * @param left
    *           the left expression in the application
    * @param right
    *           the right expression in the application
    * @return the application node
    */
   public Application createApplication(final Expression left, final Expression right) {
      return new ApplicationImpl(left, right);
   }

   /**
    * Creates a new definition 'def myVal = expression' from a name and
    * expression. Note that, by design, a program will not accept two
    * definitions with the same name.
    * 
    * @param name
    *           the name of the definition
    * @param expression
    *           the expression represented by the name
    * @return the definition node
    */
   public Definition createDefinition(final Name name, final Expression expression) {
      return new DefinitionImpl(name, expression);
   }

   /**
    * Creates a new function 'λmyFunc.body' from a variable and body. Any number
    * of functions with the same bound variable are acceptable in an expression.
    * 
    * @param variable
    *           the name object representing the function's bound variable
    * @param body
    *           the body expression of the function
    * @return the function node
    */
   public Function createFunction(final Name variable, final Expression body) {
      return new FunctionImpl(variable, body);
   }

   /**
    * Creates a new name node from the specified string.
    * 
    * @param value
    *           the string value of the name
    * @return the name node
    */
   public Name createName(final String value) {
      return new NameImpl(value);
   }

   /**
    * Creates a new program 'def* expression' from a list of definitions and an
    * expression.
    * 
    * @param definitions
    *           the list of definitions
    * @param expression
    *           the expression
    * @return the program node
    */
   public Program createProgram(final List<Definition> definitions, final Expression expression) {
      final Program program = new ProgramImpl(definitions, expression);
      program.accept(new AstLexicalScopeVisitor());
      return program;
   }

   // ------------------------------------------------------------------------------------------
   // Private implementations
   // ------------------------------------------------------------------------------------------

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
      public Expression function() {
         return left;
      }

      @Override
      public Expression argument() {
         return right;
      }
   }

   private final class AstLexicalScopeVisitor implements AstVisitor {

      private final Stack<LexicalScope> scopes;
      private final List<AstNode> toRename;

      private AstLexicalScopeVisitor() {
         toRename = new ArrayList<>();
         scopes = new Stack<>();
      }

      @Override
      public void visit(final Application node) {

         // copy the top lexical scope
         final LexicalScope scope = scopes.peek().copy();

         // push onto the stack before drilling down
         scopes.push(scope);

         // drill down on left
         node.function().accept(this);

         // drill down on right
         node.argument().accept(this);

         // set the node's scope
         ((BaseNode) node).setScope(scopes.pop());
      }

      @Override
      public void visit(final Definition node) {

         // throw an exception on a duplicate definition name
         if (scopes.peek().definitions().containsKey(node.name().value())) {
            throw new IllegalArgumentException(
                  String.format("Duplicate definition for '%s'.", node.name().value()));
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
         if (scope.variables().containsKey(node.boundVariable().value())) {
            toRename.add(scope.variables().get(node.boundVariable().value()));
         }

         // add the variable name of the function
         scope.variables().put(node.boundVariable().value(), node);

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
         final AstNode resolvedDef = scopes.peek().definitions().get(node.value());

         // try to find a function with the specified variable name
         final AstNode resolvedVar = scopes.peek().variables().get(node.value());

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
            scope.definitions().put(def.name().value(), def);
         }

         // drill down
         if (node.expression() != null) {
            node.expression().accept(this);
         }

         // set the node's scope
         ((BaseNode) node).setScope(scopes.pop());

         // α-conversion: rename duplicate function variable names
         if (toRename.size() > 0) {
            // TODO: generate unique names-- collect set of names?
            System.err.printf("Must rename %d functions.\n", toRename.size());
         }
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
      private final Name variable;

      FunctionImpl(final Name variable, final Expression body) {
         this.bindings = new ArrayList<>();
         this.variable = variable;
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
         return variable.value().equals(variableName) || body.isBound(variableName);
      }

      @Override
      public Name boundVariable() {
         return variable;
      }
   }

   private static final class NameImpl extends BaseNode implements Name {

      private BindingNode boundTo;
      private final String value;

      NameImpl(final String value) {
         this.value = value;
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
      public String value() {
         return value;
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
}