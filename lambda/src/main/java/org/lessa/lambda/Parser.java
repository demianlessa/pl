package org.lessa.lambda;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.lessa.lambda.ast.Application;
import org.lessa.lambda.ast.AstNodeFactory;
import org.lessa.lambda.ast.Definition;
import org.lessa.lambda.ast.Expression;
import org.lessa.lambda.ast.Function;
import org.lessa.lambda.ast.Name;
import org.lessa.lambda.ast.Program;

public class Parser {

   private final AstNodeFactory factory;

   public Parser() {
      this.factory = new AstNodeFactory();
   }

   /**
    * Grammar:
    *
    * <pre>
    * program     ::= definition* expression
    * definition  ::= 'def' name '=' expression
    * expression  ::= name | function | application
    * name        ::= &lt;non-blank character sequence&gt;
    * function    ::= 'λ' name '.' expression
    * application ::= '(' expression expression ')'
    * </pre>
    *
    * @param reader
    *           token reader
    * @return lambda calculus program
    * @throws ParserException
    *            if the {@link Reader} object has an invalid token stream or if
    *            parsing fails
    */
   public Program parse(final Reader reader) throws ParserException {

      try {
         final Tokenizer tokenizer = new Tokenizer(reader);
         final Program program = program(tokenizer);

         if (tokenizer.hasNext()) {
            throw new TokenizerException(
                  "Expected end of stream but additional tokens were found after program.");
         }

         return program;
      }
      catch (final TokenizerException te) {
         throw new ParserException("Invalid token stream.", te);
      }
   }

   // ----------------------------------------------------------------------
   // Supporting types
   // ----------------------------------------------------------------------

   public Program parse(final String... lines) throws ParserException {
      return parse(new StringReader(Arrays.stream(lines).collect(Collectors.joining("\n"))));
   }

   private Application application(ListIterator<Token> iterator) throws TokenizerException {
      final Expression lhs = expression(iterator);
      final Expression rhs = expression(iterator);
      requireNextToken(iterator, TokenClass.CLOSING_PARENS);
      return factory.createApplication(lhs, rhs);
   }

   private Definition definition(ListIterator<Token> iterator) throws TokenizerException {
      requireNextToken(iterator, TokenClass.DEF);
      final Token nameToken = requireNextToken(iterator, TokenClass.NAME);
      requireNextToken(iterator, TokenClass.EQUALS);
      return factory.createDefinition(name(nameToken), expression(iterator));
   }

   private Expression expression(ListIterator<Token> iterator) throws TokenizerException {

      final Token token = requireNextToken(iterator);

      if (token.tokenClass() == TokenClass.NAME) {
         return name(token);
      }
      else if (token.tokenClass() == TokenClass.LAMBDA) {
         return function(iterator);
      }
      else if (token.tokenClass() == TokenClass.OPENING_PARENS) {
         return application(iterator);
      }

      throw new TokenizerException(
            String.format("Expected name, function, or application but found token '%s' instead.",
                  token.token()));
   }

   private Function function(ListIterator<Token> iterator) throws TokenizerException {
      final Token nameToken = requireNextToken(iterator, TokenClass.NAME);
      requireNextToken(iterator, TokenClass.DOT);
      return factory.createFunction(name(nameToken), expression(iterator));
   }

   private Name name(Token token) {
      return factory.createName(token.token());
   }

   private Program program(ListIterator<Token> iterator)
         throws TokenizerException, ParserException {

      final List<Definition> definitions = new ArrayList<>();

      // a program might have a list of definitions
      while (iterator.hasNext()) {

         // consume one token
         final Token next = iterator.next();

         // put back one token
         iterator.previous();

         if (next.tokenClass() == TokenClass.DEF) {
            definitions.add(definition(iterator));
         }
         else {
            break;
         }
      }

      // a program must have an expression-- consume one token
      requireNextToken(iterator);

      // put back one token
      iterator.previous();

      try {
         return factory.createProgram(definitions, expression(iterator));
      }
      catch (final IllegalArgumentException iae) {
         throw new ParserException("Invalid program.", iae);
      }
   }

   private Token requireNextToken(final ListIterator<Token> iterator) throws TokenizerException {
      if (!iterator.hasNext()) {
         throw new TokenizerException("Expected a token but reached the end of stream instead.");
      }
      return iterator.next();
   }

   private Token requireNextToken(final ListIterator<Token> iterator, final TokenClass tokenClass)
         throws TokenizerException {

      if (!iterator.hasNext()) {
         throw new TokenizerException(String.format(
               "Expected a token of class '%s' but reached the end of stream instead.",
               tokenClass.name()));
      }

      final Token token = iterator.next();
      if (tokenClass != token.tokenClass()) {
         throw new TokenizerException(
               String.format("Expected token class '%s' but found token '%s' instead.",
                     tokenClass.name(), token.token()));
      }

      return token;
   }
}