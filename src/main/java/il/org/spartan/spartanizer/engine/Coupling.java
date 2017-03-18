package il.org.spartan.spartanizer.engine;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

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
