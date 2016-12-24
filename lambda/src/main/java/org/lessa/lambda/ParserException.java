package org.lessa.lambda;

public class ParserException extends Exception {

   private static final long serialVersionUID = -8800947145386917529L;

   public ParserException(String message) {
      super(message);
   }

   public ParserException(String message, Throwable cause) {
      super(message, cause);
   }
}
