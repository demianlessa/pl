package org.lessa.lambda;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public final class Tokenizer implements ListIterator<Token> {

   private static final int CLOSING_PARENS = ')';
   private static final int DOT = '.';
   private static final int EQUALS = '=';
   private static final int LAMBDA = 'λ';
   private static final int OPENING_PARENS = '(';

   private static final Token TK_CLOSING_PARENS = new Token(TokenClass.CLOSING_PARENS, ")");
   private static final Token TK_DEF = new Token(TokenClass.DEF, "def");
   private static final Token TK_DOT = new Token(TokenClass.DOT, ".");
   private static final Token TK_EQUALS = new Token(TokenClass.EQUALS, "=");
   private static final Token TK_LAMBDA = new Token(TokenClass.LAMBDA, "λ");
   private static final Token TK_OPENING_PARENS = new Token(TokenClass.OPENING_PARENS, "(");

   private int tokenIndex;
   private final List<Token> tokens;

   public Tokenizer(final Reader reader) throws IOException {
      this.tokenIndex = 0;
      this.tokens = tokenize(readFrom(reader));
   }

   public Tokenizer(final String expression) throws IOException {
      this(new StringReader(expression));
   }

   @Override
   public void add(Token token) {
      tokens.add(tokenIndex, token);
   }

   @Override
   public boolean hasNext() {
      return tokens.size() > 0 && tokenIndex < tokens.size();
   }

   @Override
   public boolean hasPrevious() {
      return tokens.size() > 0 && tokenIndex > 0;
   }

   @Override
   public Token next() {
      return tokens.get(tokenIndex++);
   }

   @Override
   public int nextIndex() {
      return tokenIndex + 1;
   }

   @Override
   public Token previous() {
      return tokens.get(tokenIndex--);
   }

   @Override
   public int previousIndex() {
      return tokenIndex - 1;
   }

   @Override
   public void remove() {
      tokens.remove(tokenIndex);
   }

   @Override
   public void set(Token token) {
      tokens.set(tokenIndex, token);
   }

   private Token createSymbolToken(final String token) {
      if (token.equals(TK_DOT.token())) {
         return TK_DOT;
      }
      if (token.equals(TK_EQUALS.token())) {
         return TK_EQUALS;
      }
      if (token.equals(TK_LAMBDA.token())) {
         return TK_LAMBDA;
      }
      if (token.equals(TK_OPENING_PARENS.token())) {
         return TK_OPENING_PARENS;
      }
      if (token.equals(TK_CLOSING_PARENS.token())) {
         return TK_CLOSING_PARENS;
      }
      throw new IllegalArgumentException(String.format("Character is not supported: '%s'.", token));
   }

   private boolean isSupportedSymbol(final int codePoint) {
      return codePoint == DOT || codePoint == EQUALS || codePoint == LAMBDA
            || codePoint == OPENING_PARENS || codePoint == CLOSING_PARENS;
   }

   private StringBuffer readFrom(Reader reader) throws IOException {

      final StringBuffer buffer = new StringBuffer();
      final CharBuffer charBuffer = CharBuffer.allocate(1024);
      while (reader.read(charBuffer) != -1) {
         charBuffer.flip();
         buffer.append(charBuffer);
         charBuffer.clear();
      }
      reader.close();

      return buffer;
   }

   private List<Token> tokenize(StringBuffer buffer) {

      final List<Token> result = new ArrayList<>();

      int tokenStart = -1;
      for (int index = 0; index < buffer.length(); index++) {

         // whitespace character
         if (Character.isWhitespace(buffer.codePointAt(index))) {
            // marks the end of a token
            if (tokenStart != -1) {
               final String token = buffer.substring(tokenStart, index);
               result.add(new Token(TK_DEF.token()
                     .equals(token) ? TokenClass.DEF : TokenClass.NAME, token));
               tokenStart = -1;
            }
         }

         // supported symbol
         else if (isSupportedSymbol(buffer.codePointAt(index))) {
            // marks the end of a token
            if (tokenStart != -1) {
               final String token = buffer.substring(tokenStart, index);
               result.add(new Token(TK_DEF.token()
                     .equals(token) ? TokenClass.DEF : TokenClass.NAME, token));
               tokenStart = -1;
            }
            // the symbol is a token of its own
            result.add(createSymbolToken(buffer.substring(index, index + 1)));
         }

         // alphabetic character
         else if (Character.isJavaIdentifierPart(buffer.codePointAt(index))) {
            // marks the start of a token
            if (tokenStart == -1) {
               if (!Character.isJavaIdentifierStart(buffer.codePointAt(index))
                     || !Character.isLetter(buffer.codePointAt(index))) {
                  throw new IllegalArgumentException("Every name must start with a letter.");
               }
               tokenStart = index;
            }
         }

         else {
            throw new IllegalArgumentException(String.format("Character is not supported: '%s'.",
                  new String(Character.toChars(buffer.codePointAt(index)))));
         }
      }

      // unterminated token
      if (tokenStart != -1) {
         final String token = buffer.substring(tokenStart, buffer.length());
         result.add(new Token(TK_DEF.token()
               .equals(token) ? TokenClass.DEF : TokenClass.NAME, token));
         tokenStart = -1;
      }

      return result;
   }
}