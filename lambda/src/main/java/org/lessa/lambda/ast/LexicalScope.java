package org.lessa.lambda.ast;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class LexicalScope {

   static final LexicalScope EMPTY_SCOPE;

   static {
      EMPTY_SCOPE = new LexicalScope(Collections.emptyMap(), Collections.emptyMap());
   }

   private final Map<String, AstNode> definitions;
   private final Map<String, AstNode> variables;

   LexicalScope() {
      this.definitions = new HashMap<>();
      this.variables = new HashMap<>();
   }

   LexicalScope(final Map<String, AstNode> definitions, final Map<String, AstNode> variables) {
      this.definitions = definitions;
      this.variables = variables;
   }

   Map<String, AstNode> definitions() {
      return definitions;
   }

   Map<String, AstNode> variables() {
      return variables;
   }

   LexicalScope copy() {
      return new LexicalScope(new HashMap<>(definitions), new HashMap<>(variables));
   }
}