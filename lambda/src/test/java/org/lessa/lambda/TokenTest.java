package org.lessa.lambda;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TokenTest {

   private static final Token TK_CLOSING_PARENS = new Token(TokenClass.CLOSING_PARENS, ")");
   private static final Token TK_DEF = new Token(TokenClass.DEF, "def");
   private static final Token TK_DOT = new Token(TokenClass.DOT, ".");
   private static final Token TK_EQUALS = new Token(TokenClass.EQUALS, "=");
   private static final Token TK_LAMBDA = new Token(TokenClass.LAMBDA, "λ");
   private static final Token TK_NAME1 = new Token(TokenClass.NAME, "name1");
   private static final Token TK_NAME2 = new Token(TokenClass.NAME, "name2");
   private static final Token TK_OPENING_PARENS = new Token(TokenClass.OPENING_PARENS, "(");

   @Test
   public void given_distinct_tokens_all_are_added_to_map() {

      final Map<Token, Integer> map = new HashMap<>();

      map.put(TK_CLOSING_PARENS, 1);
      map.put(TK_DEF, 2);
      map.put(TK_DOT, 3);
      map.put(TK_EQUALS, 4);
      map.put(TK_LAMBDA, 5);
      map.put(TK_NAME1, 6);
      map.put(TK_NAME2, 7);
      map.put(TK_OPENING_PARENS, 8);

      Assert.assertEquals(map.size(), 8);
      Assert.assertEquals(map.get(TK_CLOSING_PARENS), new Integer(1));
      Assert.assertEquals(map.get(TK_DEF), new Integer(2));
      Assert.assertEquals(map.get(TK_DOT), new Integer(3));
      Assert.assertEquals(map.get(TK_EQUALS), new Integer(4));
      Assert.assertEquals(map.get(TK_LAMBDA), new Integer(5));
      Assert.assertEquals(map.get(new Token(TokenClass.NAME, "name1")), new Integer(6));
      Assert.assertEquals(map.get(new Token(TokenClass.NAME, "name2")), new Integer(7));
      Assert.assertEquals(map.get(TK_OPENING_PARENS), new Integer(8));
   }

   @Test(dataProvider = "tokenPairs")
   public void given_two_tokens_equals_should_return_expected(Object first, Object second,
         boolean expectedEqual) {
      Assert.assertEquals(first.equals(second), expectedEqual);
   }

   @DataProvider(name = "tokenPairs")
   private Object[][] createValidProgram() {
      return new Object[][] {
         { TK_CLOSING_PARENS, TK_CLOSING_PARENS, true },
         { TK_DEF, TK_DEF, true },
         { TK_DOT, TK_DOT, true },
         { TK_EQUALS, TK_EQUALS, true },
         { TK_LAMBDA, TK_LAMBDA, true },
         { TK_NAME1, TK_NAME1, true },
         { TK_NAME2, TK_NAME2, true },
         { TK_NAME1, TK_NAME2, false },
         { TK_OPENING_PARENS, TK_OPENING_PARENS, true },
         { TK_DEF, null, false },
         { TK_DEF, new Token(TokenClass.DEF, "not_def"), false },
         { TK_DEF, new Token(TokenClass.DEF, null), false },
         { TK_DEF, new Token(TokenClass.NAME, "def"), false },
         { TK_DEF, "def", false }, };
   }
}
