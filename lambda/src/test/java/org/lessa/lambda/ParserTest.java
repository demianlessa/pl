package org.lessa.lambda;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for basic parser operation.
 */
public class ParserTest {

   // ----------------------------------------------------------------------
   // Negative cases
   // ----------------------------------------------------------------------

   @Test(dataProvider = "InvalidProgram")
   public void given_invalid_program_parsing_throws_exception(String program) {
      try {
         new Parser().parse(program);
         Assert.assertFalse(true,
               String.format("Should throw an exception on invalid program '%s'.", program));
      }
      catch (final ParserException pe) {
      }
   }

   @DataProvider(name = "InvalidProgram")
   private Object[][] createInvalidProgram() {
      return new Object[][] {
         { "λfirst." },
         { "λfirst(f a)" },
         { "λ(f a)" },
         { "λ.(f a)" },
         { "λx.x foo" },
         { ")foo bar(" },
         { "def λx.x = (f a)" },
         { "(f a b)" },
         { "(f a" } };
   }

   // ----------------------------------------------------------------------
   // Positive cases
   // ----------------------------------------------------------------------

   @Test(dataProvider = "ValidProgram")
   public void given_valid_program_parsing_terminates(String... expressions)
         throws ParserException {
      new Parser().parse(expressions);
   }

   @DataProvider(name = "ValidProgram")
   private Object[][] createValidProgram() {
      return new Object[][] {
         { "name" },
         { "λfirst.λsecond.first" },
         { "λf.λa.(f a)" },
         { "(λx.x λa.λb.b)" },
         { "(λx.x λx.x)" },
         { "λS.(S S)" },
         { "(λx.x λS.(S S))" },
         { "(λS.(S S) λx.x)" },
         { "(λS.(S S) λS.(S S))" },
         { "λfunc.λarg.(func arg)" },
         { "((λfunc.λarg.(func arg) λx.x) λS.(S S))" },
         {
            new String[] {
               "def identity = λx.x",
               "def self_apply = λs.(s s)",
               "def apply = λfunc.λarg.(func arg)",
               "def select_first = λfirst.λsecond.first",
               "def select_second = λfirst.λsecond.second",
               "def make_pair = λfirst.λsecond.λfunc.((func first) second)" } }
         };
   }
}