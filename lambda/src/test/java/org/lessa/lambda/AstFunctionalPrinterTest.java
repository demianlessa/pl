package org.lessa.lambda;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.lessa.lambda.ast.AstVisitorFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for printing ASTs using functional notation.
 */
public class AstFunctionalPrinterTest {

   // ----------------------------------------------------------------------
   // Positive cases
   // ----------------------------------------------------------------------

   @Test(dataProvider = "ValidProgram")
   public void given_valid_program_prints_as_expected(String program, String expected)
         throws ParserException, UnsupportedEncodingException {
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      new Parser().parse(program).accept(new AstVisitorFactory().createPrintVisitor(out, true));
      Assert.assertEquals(out.toString(AstVisitorFactory.DEFAULT_CHARSET), expected);
   }

   @DataProvider(name = "ValidProgram")
   private Object[][] createValidProgram() {
      return new Object[][] {
         { "name", "Name(name)" },
         { "λx.λy.z", "Function(Name(x), Function(Name(y), Name(z)))" },
         { "(λy.z w)", "Application(Function(Name(y), Name(z)), Name(w))" },
         {
            "def apply = λfunc.λarg.(func arg)\n(apply apply)",
            "DefinitionList(Definition(Name(apply), Function(Name(func), "
                  + "Function(Name(arg), Application(Name(func), Name(arg))))))\n"
                  + "Application(Name(apply), Name(apply))" },
         {
            "def identity = λx.x\ndef apply = λfunc.λarg.(func arg)\nidentity",
            "DefinitionList(Definition(Name(identity), Function(Name(x), Name(x))), "
                  + "Definition(Name(apply), Function(Name(func), Function(Name(arg), "
                  + "Application(Name(func), Name(arg))))))\nName(identity)" } };
   }
}
