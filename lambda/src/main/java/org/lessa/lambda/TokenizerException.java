package org.lessa.lambda;

public class TokenizerException extends Exception {

   private static final long serialVersionUID = 9218081727444036573L;

   public TokenizerException(String message) {
      super(message);
   }

   public TokenizerException(String message, Throwable cause) {
      super(message, cause);
   }
}
