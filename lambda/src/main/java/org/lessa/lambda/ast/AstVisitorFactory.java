package org.lessa.lambda.ast;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class AstVisitorFactory {

   public static final String UTF16 = "UTF-16";
   public static final String UTF8 = "UTF-8";
   public static final String DEFAULT_CHARSET = UTF8;

   public AstVisitor createPrintVisitor(final OutputStream out, final boolean isFunctional)
         throws UnsupportedEncodingException {
      return createPrintVisitor(out, isFunctional, DEFAULT_CHARSET);
   }

   public AstVisitor createPrintVisitor(final OutputStream out, final boolean isFunctional,
         final String encoding) throws UnsupportedEncodingException {
      return isFunctional ? new AstFunctionalPrinterVisitor(out, encoding)
            : new AstPrinterVisitor(out, encoding);
   }
}