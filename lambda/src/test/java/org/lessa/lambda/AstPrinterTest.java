package org.lessa.lambda;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.lessa.lambda.ast.AstVisitorFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for printing ASTs using standard textual format.
 */
public class AstPrinterTest {

   // ----------------------------------------------------------------------
   // Positive cases
   // ----------------------------------------------------------------------

   @Test(dataProvider = "ValidProgram")
   public void given_valid_program_prints_as_expected(String program)
         throws ParserException, UnsupportedEncodingException {
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      new Parser().parse(program).accept(new AstVisitorFactory().createPrintVisitor(out, false));
      Assert.assertEquals(program, out.toString(AstVisitorFactory.DEFAULT_CHARSET));
   }

   @DataProvider(name = "ValidProgram")
   private Object[][] createValidProgram() {
      return new Object[][] {
         { "name" },
         { "λx.x" },
         { "(x y)" },
         { "def apply = λfunc.λarg.(func arg)\n(apply apply)" },
         { "def identity = λx.x\ndef apply = λfunc.λarg.(func arg)" } };
   }
}
