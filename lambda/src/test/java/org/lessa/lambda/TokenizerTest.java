package org.lessa.lambda;

import java.io.IOException;
import java.io.StringReader;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TokenizerTest {

   @Test(dataProvider = "expressions")
   public void given_valid_lambda_expression_tokenizer_returns_correct_number_of_tokens(
         String expression, int expectedTokens) throws IOException {

      final Tokenizer tokenizer = new Tokenizer(new StringReader(expression));

      int index = 0;
      while (index < expectedTokens) {
         Assert.assertTrue(tokenizer.hasNext());
         tokenizer.next();
         index++;
      }

      Assert.assertFalse(tokenizer.hasNext());
   }

   @DataProvider(name = "expressions")
   private Object[][] createValidLambdaExpressions() {
      return new Object[][] {
         { "my tokens", 2 },
         { "λ.my tokens", 4 },
         { "((λfunc.λarg(func arg) arg) boing)", 15 },
         { "((λfunc1.λarg2ζ(func1 arg2ζ) arg) boing)", 15 } };
   }
}
