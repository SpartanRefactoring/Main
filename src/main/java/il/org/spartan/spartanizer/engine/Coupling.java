package il.org.spartan.spartanizer.engine;

import org.eclipse.jdt.core.dom.*;

public enum Coupling {
  IFF, IMPLIED, INDEPENDENT,;
  @SuppressWarnings("unused") public static Inner of(final ASTNode next) {
    return λ -> null;
  }

  @FunctionalInterface
  interface Inner {
    Coupling withRespectTo(ASTNode to);
  }
}
