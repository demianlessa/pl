package org.lessa.lambda;

import org.lessa.lambda.ast.AstNode;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for bound variable operation.
 */
public class BoundVariableTest {

   // ----------------------------------------------------------------------
   // Positive cases
   // ----------------------------------------------------------------------

   @Test(dataProvider = "Expression")
   public void given_valid_expression_bound_variable_test_passes(String expression,
         String variableName, boolean isBound) throws ParserException {
      final AstNode node = new Parser().parse(expression);
      Assert.assertEquals(node.isBound(variableName), isBound);
   }

   @DataProvider(name = "Expression")
   private Object[][] createExpression() {
      return new Object[][] {
         { "(λconvict.convict fugitive)", "convict", true },
         { "(λconvict.convict fugitive)", "fugitive", false },
         { "(λprison.prison λconvict.convict)", "convict", true },
         { "(λprison.prison λconvict.convict)", "prison", true },
         { "(prison λconvict.convict)", "convict", true },
         { "(prison λconvict.convict)", "prison", false },
         { "λprisoner.(number6 prisoner)", "prisoner", true },
         { "λprisoner.(number6 prisoner)", "number6", false },
         { "λprisoner.(prisoner number6)", "prisoner", true },
         { "λprisoner.(prisoner number6)", "number6", false },
         { "truant", "truant", false },
         { "(λprisoner.prisoner escaper)", "prisoner", true },
         { "(λprisoner.prisoner escaper)", "escaper", false },
         { "(escaper λjailor.jailor)", "escaper", false },
         { "(escaper λjailor.jailor)", "jailor", true },
         { "λprison.(prison fugitive)", "prison", true },
         { "λprison.(prison fugitive)", "fugitive", false },
         { "λshort.λsharp.λshock.fugitive", "fugitive", false },
         { "λshort.λsharp.λshock.fugitive", "short", true },
         { "λshort.λsharp.λshock.fugitive", "sharp", true },
         { "λshort.λsharp.λshock.fugitive", "shock", true },
         { "def fugitive = λf.f λshort.λsharp.λshock.fugitive", "fugitive", false },
         { "def fugitive = λf.f λshort.λsharp.λshock.fugitive", "short", true },
         { "def fugitive = λf.f λshort.λsharp.λshock.fugitive", "sharp", true },
         { "def fugitive = λf.f λshort.λsharp.λshock.fugitive", "shock", true },
         { "def fugitive = λf.f program", "fugitive", false },
         { "def fugitive = λf.f program", "f", false }, };
   }
}