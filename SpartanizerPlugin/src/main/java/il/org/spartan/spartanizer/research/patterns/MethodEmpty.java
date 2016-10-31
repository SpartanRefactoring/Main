package il.org.spartan.spartanizer.research.patterns;

import org.eclipse.jdt.core.dom.*;

public final class MethodEmpty extends JavadocMarkerNanoPattern<MethodDeclaration> {
  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return ¢.getBody() != null && ¢.getBody().statements().isEmpty();
  }
}