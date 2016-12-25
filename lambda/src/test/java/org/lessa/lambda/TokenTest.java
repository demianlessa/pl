package org.lessa.lambda;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TokenTest {

   @Test
   public void given_distinct_tokens_all_are_added_to_map() {
      
      final Map<Token, Integer> map = new HashMap<>();
      map.put(new Token(TokenClass.CLOSING_PARENS, ")"), 1);
      map.put(new Token(TokenClass.DEF, "def"), 2);
      map.put(new Token(TokenClass.DOT, "dot"), 3);
      map.put(new Token(TokenClass.EQUALS, "="), 4);
      map.put(new Token(TokenClass.LAMBDA, "λ"), 5);
      map.put(new Token(TokenClass.NAME, "name1"), 6);
      map.put(new Token(TokenClass.NAME, "name2"), 7);
      map.put(new Token(TokenClass.OPENING_PARENS, "("), 8);

      Assert.assertEquals(map.size(), 8);
      Assert.assertEquals(map.get(new Token(TokenClass.NAME, "name1")), new Integer(6));
      Assert.assertEquals(map.get(new Token(TokenClass.NAME, "name2")), new Integer(7));
   }

   @Test(dataProvider = "tokenPairs")
   public void given_two_tokens_equals_should_return_expected(Token first, Token second,
         boolean expectedEqual) {
      Assert.assertEquals(first.equals(second), expectedEqual);
   }

   @DataProvider(name = "tokenPairs")
   private Object[][] createValidProgram() {
      return new Object[][] {
         {
            new Token(TokenClass.CLOSING_PARENS, ")"),
            new Token(TokenClass.CLOSING_PARENS, ")"),
            true },
         { new Token(TokenClass.DEF, "def"), new Token(TokenClass.DEF, "def"), true },
         { new Token(TokenClass.DOT, "."), new Token(TokenClass.DOT, "."), true },
         { new Token(TokenClass.EQUALS, "="), new Token(TokenClass.EQUALS, "="), true },
         { new Token(TokenClass.LAMBDA, "λ"), new Token(TokenClass.LAMBDA, "λ"), true },
         { new Token(TokenClass.NAME, "name"), new Token(TokenClass.NAME, "name"), true },
         { new Token(TokenClass.NAME, "name1"), new Token(TokenClass.NAME, "name2"), false },
         {
            new Token(TokenClass.OPENING_PARENS, "("),
            new Token(TokenClass.OPENING_PARENS, "("),
            true }, };
   }
}
