package org.lessa.turing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MachineBuilderException extends RuntimeException {

   private static final long serialVersionUID = 1645355424736698527L;
   private final Map<MachinePart, List<String>> partToViolation;

   public MachineBuilderException(final String message) {
      super(message);
      partToViolation = new HashMap<>();
   }

   public void addViolation(final MachinePart part, final String message) {
      if (!partToViolation.containsKey(part)) {
         partToViolation.put(part, new ArrayList<>());
      }
      partToViolation.get(part).add(message);
   }

   public boolean hasViolations() {
      return !partToViolation.isEmpty();
   }

   @Override
   public String toString() {
      return String.format("%s Violations:%n{%n%s}", getMessage(), partToViolation.entrySet()
            .stream()
            .map(e -> String.format("  %s: [%s]%n", e.getKey().name(), e.getValue().stream()
                  .map(s -> String.format("\"%s\"", s)).collect(Collectors.joining(", "))))
            .collect(Collectors.joining("")));
   }

   public Map<MachinePart, List<String>> violations() {
      return Collections.unmodifiableMap(partToViolation);
   }
}
