package il.org.spartan.spartanizer.research.patterns.characteristics;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.patterns.common.*;

public final class MethodEmpty extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return ¢.getBody() != null && ¢.getBody().statements().isEmpty();
  }
}