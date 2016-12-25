package org.lessa.lambda;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for basic tokenizer operation.
 */
public class TokenizerTest {

   // ----------------------------------------------------------------------
   // Negative cases
   // ----------------------------------------------------------------------

   @Test(dataProvider = "InvalidTokenStreams")
   public void given_invalid_token_stream_tokenizer_throws_exception(String expression)
         throws IOException {

      try {
         final Tokenizer tokenizer = new Tokenizer(expression);
         while (tokenizer.hasNext()) {
            tokenizer.next();
         }
         Assert.assertFalse(true, "Should throw an exception on a bad expression.");
      }
      catch (final TokenizerException te) {

      }
   }

   @DataProvider(name = "InvalidTokenStreams")
   private Object[][] createInvalidTokenStream() {
      return new Object[][] { { "<" }, { "12" }, { "_" } };
   }

   // ----------------------------------------------------------------------
   // Positive cases
   // ----------------------------------------------------------------------

   @Test(dataProvider = "ValidTokenStreams")
   public void given_valid_token_stream_tokenizer_returns_correct_number_of_tokens(
         String expression, int expectedTokens) throws TokenizerException {

      final Tokenizer tokenizer = new Tokenizer(expression);

      int index = 0;
      while (index < expectedTokens) {
         Assert.assertTrue(tokenizer.hasNext());
         tokenizer.next();
         index++;
      }

      Assert.assertFalse(tokenizer.hasNext());
   }

   @DataProvider(name = "ValidTokenStreams")
   private Object[][] createValidTokenStreams() {
      return new Object[][] {
         { "tokens", 1 },
         { "my tokens", 2 },
         { "def name = λ.my", 6 },
         { "def . name = λ.my", 7 },
         { "name = λ.my def", 6 },
         { "λ.my tokens", 4 },
         { "((λfunc.λarg(func arg) arg) boing)", 15 },
         { "((λfunc1.λarg2ζ(func1 arg2ζ) arg) boing)", 15 } };
   }
}
