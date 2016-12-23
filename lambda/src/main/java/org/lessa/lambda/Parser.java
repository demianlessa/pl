package org.lessa.lambda;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class Parser {

   public static class Application extends Expression {

      private final Expression leftExpression;
      private final Expression rightExpression;

      public Application(final Expression leftExpression, final Expression rightExpression) {
         this.leftExpression = leftExpression;
         this.rightExpression = rightExpression;
      }

      @Override
      public final void accept(final LambdaVisitor visitor) {
         visitor.visit(this);
      }

      public Expression leftExpression() {
         return leftExpression;
      }

      public Expression rightExpression() {
         return rightExpression;
      }
   }

   public static class Definition implements Node {

      private final Expression expression;
      private final Name name;

      public Definition(final Name name, final Expression expression) {
         this.name = name;
         this.expression = expression;
      }

      @Override
      public final void accept(final LambdaVisitor visitor) {
         visitor.visit(this);
      }

      public Expression expression() {
         return expression;
      }

      public Name name() {
         return name;
      }
   }

   public abstract static class Expression implements Node {
   }

   public static class Function extends Expression {

      private final Expression body;
      private final Name name;

      public Function(final Name name, final Expression body) {
         this.name = name;
         this.body = body;
      }

      @Override
      public final void accept(final LambdaVisitor visitor) {
         visitor.visit(this);
      }

      public Expression body() {
         return body;
      }

      public Name name() {
         return name;
      }
   }

   public static class InvalidTokenStream extends RuntimeException {

      private static final long serialVersionUID = -4041232798425895600L;

      public InvalidTokenStream(String message) {
         super(message);
      }
   }

   public interface LambdaVisitor {

      public void visit(Application node);

      public void visit(Definition node);

      public void visit(Function node);

      public void visit(Name node);

      public void visit(Program node);
   }

   public static class Name extends Expression {

      private final String name;

      public Name(final String name) {
         this.name = name;
      }

      @Override
      public final void accept(final LambdaVisitor visitor) {
         visitor.visit(this);
      }

      public String name() {
         return name;
      }
   }

   public interface Node {
      void accept(LambdaVisitor visitor);
   }

   public static class Program implements Node {

      private final List<Definition> definitions;
      private final Expression expression;

      public Program(final List<Definition> definitions, final Expression expression) {
         this.definitions = definitions;
         this.expression = expression;
      }

      @Override
      public final void accept(final LambdaVisitor visitor) {
         visitor.visit(this);
      }

      public List<Definition> definitions() {
         return definitions;
      }

      public Expression expression() {
         return expression;
      }
   }

   /**
    * Grammar:
    *
    * <pre>
    * program     ::= defList* expression?
    * defList     ::= 'def' name '=' expression
    * expression  ::= name | function | application
    * name        ::= &lt;non-blank character sequence&gt;
    * function    ::= 'λ' name '.' expression
    * application ::= '(' expression expression ')'
    * </pre>
    *
    * @param reader
    *           token reader
    * @return lambda calculus program
    * @throws IOException
    *            if the tokenizer fails to read tokens from the reader
    */
   public Program parse(final Reader reader) throws IOException {

      final Tokenizer tokenizer = new Tokenizer(reader);
      final Program program = program(tokenizer);

      if (tokenizer.hasNext()) {
         throw new InvalidTokenStream(
               "Expected end of stream but additional tokens found after program.");
      }

      return program;
   }

   // ----------------------------------------------------------------------
   // Supporting types
   // ----------------------------------------------------------------------

   public Program parse(final String... lines) throws IOException {
      return parse(new StringReader(Arrays.stream(lines)
            .collect(Collectors.joining("\n"))));
   }

   private Application application(ListIterator<Token> iterator) {

      final Expression lhs = expression(iterator);
      final Expression rhs = expression(iterator);
      requireNextToken(iterator, TokenClass.CLOSING_PARENS);

      return new Application(lhs, rhs);
   }

   private Definition definition(ListIterator<Token> iterator) {

      requireNextToken(iterator, TokenClass.DEF);
      final Token nameToken = requireNextToken(iterator, TokenClass.NAME);
      requireNextToken(iterator, TokenClass.EQUALS);

      return new Definition(name(nameToken), expression(iterator));
   }

   private Expression expression(ListIterator<Token> iterator) {

      final Token token = iterator.next();

      if (token.tokenClass() == TokenClass.NAME) {
         return name(token);
      }
      else if (token.tokenClass() == TokenClass.LAMBDA) {
         return function(iterator);
      }
      else if (token.tokenClass() == TokenClass.OPENING_PARENS) {
         return application(iterator);
      }

      throw new InvalidTokenStream(
            String.format("Expected a name, function, or application but found token '%s' instead.",
                  token.token()));
   }

   private Function function(ListIterator<Token> iterator) {

      final Token nameToken = requireNextToken(iterator, TokenClass.NAME);
      requireNextToken(iterator, TokenClass.DOT);

      return new Function(name(nameToken), expression(iterator));
   }

   private boolean isNextToken(final ListIterator<Token> iterator, final TokenClass tokenClass) {

      if (!iterator.hasNext()) {
         throw new InvalidTokenStream(String.format(
               "Unexpected end of stream: expected a token of class '%s'.", tokenClass.name()));
      }

      final Token token = iterator.next();
      final boolean result = token.tokenClass() == tokenClass;
      iterator.previous();

      return result;
   }

   private Name name(Token token) {
      return new Name(token.token());
   }

   private Program program(ListIterator<Token> iterator) {

      final List<Definition> definitions = new ArrayList<>();

      while (iterator.hasNext() && isNextToken(iterator, TokenClass.DEF)) {
         definitions.add(definition(iterator));
      }

      return new Program(definitions, iterator.hasNext() ? expression(iterator) : null);
   }

   private Token requireNextToken(final ListIterator<Token> iterator, final TokenClass tokenClass) {

      if (!iterator.hasNext()) {
         throw new InvalidTokenStream(String.format(
               "Unexpected end of stream: expected a token of class '%s'.", tokenClass.name()));
      }

      final Token token = iterator.next();
      if (tokenClass != token.tokenClass()) {
         throw new InvalidTokenStream(
               String.format("Expected token class '%s' but found token '%s' instead.",
                     tokenClass.name(), token.token()));
      }

      return token;
   }
}