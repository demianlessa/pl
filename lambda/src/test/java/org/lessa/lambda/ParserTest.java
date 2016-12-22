package org.lessa.lambda;

import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ParserTest {

   private static Parser.LambdaVisitor createPrinter(boolean asLambda) {
      return asLambda ? new PrintVisitors.LambdaPrinter() : new PrintVisitors.PrettyPrinter();
   }

   @Test(dataProvider = "expressions")
   public void given_valid_lambda_program_parsing_terminates(String... expressions)
         throws IOException {
      final Parser.LambdaVisitor pv = createPrinter(true);
      new Parser().parse(expressions)
            .accept(pv);
   }

   @DataProvider(name = "expressions")
   private Object[][] createValidLambdaExpressions() {
      return new Object[][] {
         {
            new String[] {
               "def identity = λx.x",
               "def self_apply = λs.(s s)",
               "def apply = λfunc.λarg.(func arg)",
               "def select_first = λfirst.λsecond.first",
               "def select_second = λfirst.λsecond.second",
               "def make_pair = λfirst.λsecond.λfunc.((func first) second)" } },
         { "λfirst.λsecond.first" },
         { "λf.λa.(f a)" },
         { "(λx.x λa.λb.b)" },
         { "(λx.x λx.x)" },
         { "λS.(S S)" },
         { "(λx.x λS.(S S))" },
         { "(λS.(S S) λx.x)" },
         { "(λS.(S S) λS.(S S))" },
         { "λfunc.λarg.(func arg)" },
         { "((λfunc.λarg.(func arg) λx.x) λS.(S S))" } };
   }
}